package com.danijel.bank_app_leapwise;

import com.danijel.bank_app_leapwise.model.entity.Account;
import com.danijel.bank_app_leapwise.model.entity.Customer;
import com.danijel.bank_app_leapwise.model.entity.Transaction;
import com.danijel.bank_app_leapwise.repository.CustomerRepository;
import com.danijel.bank_app_leapwise.service.CustomerService;
import com.danijel.bank_app_leapwise.util.CsvGenerator;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class InitData {

    private static final int TOTAL_CUSTOMERS = 10;
    private static final int ACCOUNTS_PER_CUSTOMER = 3;
    private static final int TOTAL_TRANSACTIONS = 100_000;
    private static final int BATCH_SIZE = 10_000;
    private static final double STARTING_BALANCE = 10_000d;
    private static final String TRANSACTION_FILE_NAME = "random_transactions.txt";

    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final Random random = new Random();

    private final List<String> nameList = List.of("Marko", "Luka", "Šimun", "Marija", "Ana", "Mia", "Nina", "Tea");
    private final List<String> lastNameList = List.of("Ivić", "Marić", "Kelava", "Peleš", "Slavić", "Žagar", "Diminić");

    public InitData(CustomerService customerService, CustomerRepository customerRepository) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
    }

    @PostConstruct
    public void initData() {
        System.out.println("Starting data init");
        Instant start = Instant.now();

        List<Customer> customers = createCustomersAndAccounts();
        createFileWithTransactions(customers);

        System.out.println("Time taken: " + Duration.between(start, Instant.now()));
    }

    private List<Customer> createCustomersAndAccounts() {
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < TOTAL_CUSTOMERS; i++) {
            customers.add(createRandomCustomer());
        }
        return customerRepository.saveAll(customers);
    }

    private Customer createRandomCustomer() {
        String name = getRandomElement(nameList);
        String lastName = getRandomElement(lastNameList);
        String fullName = name + " " + lastName;

        Customer customer = new Customer();
        customer.setName(fullName);
        customer.setEmail(fullName.toLowerCase() + "@mock.com");
        customer.setAddress(lastName + " ulica");
        customer.setPhoneNumber("0911525221");
        customer.setAccounts(createAccounts(customer));

        return customer;
    }

    private List<Account> createAccounts(Customer customer) {
        return IntStream.range(0, ACCOUNTS_PER_CUSTOMER)
                .mapToObj(i -> createAccount(customer))
                .collect(Collectors.toList());
    }

    private Account createAccount(Customer customer) {
        Account account = new Account();
        account.setBalance(STARTING_BALANCE);
        account.setAccountNumber(Math.abs(random.nextLong() % 10000000));
        account.setCustomer(customer);
        return account;
    }

    private void createFileWithTransactions(List<Customer> customers) {
        Path filePath = Path.of(TRANSACTION_FILE_NAME);

        try {
            Files.deleteIfExists(filePath);
            Files.write(filePath, Collections.singletonList("id,senderAccountId,receiverAccountId,amount,currencyId,message,timeStamp"), StandardOpenOption.CREATE);

            List<Account> allAccounts = customers.stream()
                    .flatMap(customer -> customer.getAccounts().stream())
                    .collect(Collectors.toList());

            IntStream.range(0, TOTAL_TRANSACTIONS / BATCH_SIZE)
                    .forEach(batch -> processBatch(filePath, allAccounts, batch * BATCH_SIZE));

        } catch (IOException e) {
            throw new RuntimeException("Error creating transaction file", e);
        }
    }

    private void processBatch(Path filePath, List<Account> allAccounts, int batchStart) {
        List<String> transactions = IntStream.range(batchStart, batchStart + BATCH_SIZE)
                .mapToObj(i -> createValidTransactionInCsvFormat(allAccounts))
                .collect(Collectors.toList());

        try {
            Files.write(filePath, transactions, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("Error writing batch to file", e);
        }
    }

    private String createValidTransactionInCsvFormat(List<Account> allAccounts) {
        Collections.shuffle(allAccounts);

        Account sender = allAccounts.stream()
                .filter(account -> account.getBalance() > 0)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Can't find account with positive balance"));

        Account receiver = allAccounts.stream()
                .filter(account -> !account.equals(sender))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Can't find different account for receiver"));

        double amount = Math.round(random.nextDouble() * 10000) / 100.0;

        Transaction transaction = new Transaction();
        transaction.setSenderAccountId(sender.getId());
        transaction.setReceiverAccountId(receiver.getId());
        transaction.setAmount(amount);
        transaction.setMessage("Test123");
        transaction.setTimeStamp(ZonedDateTime.now());

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        return CsvGenerator.generateCsv(transaction);
    }

    private <T> T getRandomElement(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }
}
