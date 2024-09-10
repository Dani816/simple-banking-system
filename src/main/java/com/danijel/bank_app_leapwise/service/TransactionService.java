package com.danijel.bank_app_leapwise.service;

import com.danijel.bank_app_leapwise.model.command.TransactionCommand;
import com.danijel.bank_app_leapwise.model.dto.TransactionDTO;
import com.danijel.bank_app_leapwise.model.entity.Account;
import com.danijel.bank_app_leapwise.model.entity.Transaction;
import com.danijel.bank_app_leapwise.repository.AccountRepository;
import com.danijel.bank_app_leapwise.repository.TransactionRepository;
import com.danijel.bank_app_leapwise.util.EmailBodyTemplate;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository,
                              ModelMapper modelMapper,
                              EmailService emailService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
    }

    public Long saveTransaction(TransactionCommand command) {
        Transaction transaction = mapTransactionCommandToEntity(command);
        Account sender = getAccountById(transaction.getSenderAccountId());
        Account receiver = getAccountById(transaction.getReceiverAccountId());

        validateTransaction(sender, transaction.getAmount());

        double oldAmount = sender.getBalance();
        saveNewAccountBalances(sender, receiver, transaction.getAmount());

        Transaction savedTransaction = transactionRepository.save(transaction);

        sendSuccessEmail(savedTransaction, transaction.getAmount(), sender.getBalance(), oldAmount );

        return savedTransaction.getId();
    }

    private Transaction mapTransactionCommandToEntity(TransactionCommand command) {
        Transaction transaction = new Transaction();
        modelMapper.map(command, transaction);
        transaction.setTimeStamp(ZonedDateTime.now());
        return transaction;
    }

    private Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> {
                    sendFailureEmail();
                    return new RuntimeException("Account not found with ID: " + accountId);
                });
    }

    private void validateTransaction(Account sender, double amount) {
        if (sender.getBalance() < amount) {
            sendFailureEmail();
            throw new RuntimeException("Sender's account balance is insufficient to complete the transaction.");
        }
    }

    private void saveNewAccountBalances(Account sender, Account receiver, double amount) {
        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);
        accountRepository.saveAll(List.of(sender, receiver));
    }

    private void sendSuccessEmail(Transaction transaction, double amount, double oldBalance, double newBalance) {
        String emailBody = EmailBodyTemplate.createSuccessEmailBody(transaction.getId(), amount, oldBalance, newBalance);
        emailService.sendSimpleEmail("New transaction notice", emailBody);
    }

    private void sendFailureEmail() {
        String emailBody = EmailBodyTemplate.createFailureEmailBody();
        emailService.sendSimpleEmail("Failed transaction notice", emailBody);
    }

    public List<TransactionDTO> getTransactionHistory(Long customerId, String filterName, String filterValue) {
        Specification<Transaction> spec = getTransactionSpecification(customerId, filterName, filterValue);
        List<Transaction> transactions = transactionRepository.findAll(spec);
        return TransactionDTO.fromEntityList(transactions);
    }

    private Specification<Transaction> getTransactionSpecification(Long customerId, String filterName, String filterValue) {
        List<Long> customerAccountIds = accountRepository.findByCustomerId(customerId)
                .stream()
                .map(Account::getId)
                .toList();

        Specification<Transaction> spec = Specification.where((root, query, cb) ->
                cb.or(
                        root.get("senderAccountId").in(customerAccountIds),
                        root.get("receiverAccountId").in(customerAccountIds)
                )
        );

        if (filterName != null && filterValue != null) {
            spec = spec.and((root, query, cb) -> switch (filterName) {
                case "amount" -> cb.equal(root.get("amount"), Double.parseDouble(filterValue));
                case "message" -> cb.like(cb.lower(root.get("message")), "%" + filterValue.toLowerCase() + "%");
                case "timeStamp" -> {
                    ZonedDateTime dateTime = ZonedDateTime.parse(filterValue);
                    yield cb.between(root.get("timeStamp"),
                            dateTime.withHour(0).withMinute(0).withSecond(0),
                            dateTime.withHour(23).withMinute(59).withSecond(59));
                }
                default -> null;
            });
        }
        return spec;
    }
}
