package com.danijel.bank_app_leapwise.service;

import com.danijel.bank_app_leapwise.model.command.TransactionCommand;
import com.danijel.bank_app_leapwise.model.dto.TransactionDTO;
import com.danijel.bank_app_leapwise.model.entity.Account;
import com.danijel.bank_app_leapwise.model.entity.Transaction;
import com.danijel.bank_app_leapwise.repository.AccountRepository;
import com.danijel.bank_app_leapwise.repository.TransactionRepository;
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

        Transaction transaction = new Transaction();

        modelMapper.map(command, transaction);
        transaction.setTimeStamp(ZonedDateTime.now());

        var sender = accountRepository.findById(transaction.getSenderAccountId());
        var receiver = accountRepository.findById(transaction.getReceiverAccountId());

        if (sender.isEmpty() || receiver.isEmpty()) {
            throw new RuntimeException("Sender: " + sender.isEmpty() + "Receiver: " + receiver.isEmpty());
        }

        if (sender.get().getBalance() < transaction.getAmount()) {
            throw new RuntimeException("Sender's account balance is insufficient to complete the transaction.");
        }

        sender.get().setBalance(sender.get().getBalance() - transaction.getAmount());
        receiver.ifPresent(acc -> acc.setBalance(acc.getBalance() + transaction.getAmount()));

        List<Account> accounts = List.of(sender.get(), receiver.get());
        accountRepository.saveAll(accounts);

        Transaction savedTransaction = transactionRepository.save(transaction);

        emailService.sendSimpleEmail("New transactions notice", "hehed asasd");

        return savedTransaction.getId();
    }

    public List<TransactionDTO> getTransactionHistory(Long customerId, String filterName, String filterValue) {
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

        List<Transaction> transactions = transactionRepository.findAll(spec);
        return TransactionDTO.fromEntityList(transactions);
    }
}
