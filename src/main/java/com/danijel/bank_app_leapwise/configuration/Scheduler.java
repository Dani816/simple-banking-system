package com.danijel.bank_app_leapwise.configuration;

import com.danijel.bank_app_leapwise.model.entity.Account;
import com.danijel.bank_app_leapwise.repository.AccountRepository;
import com.danijel.bank_app_leapwise.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@EnableScheduling
@Configuration
public class Scheduler {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public Scheduler(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }


    @Scheduled(cron = "* 1 * * * *")
    @Transactional
    public void calculatePastMonthTurnover() {
        log.info("[SCHEDULED] starting calculatePastMonthTurnover");
        ZonedDateTime startDate = ZonedDateTime.now().minusMonths(1).withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);

        List<Account> accounts = accountRepository.findAll();

        for (Account account : accounts) {
            Double turnover = transactionRepository.calculateTurnoverForAccount(account.getId(), startDate);

            account.setPastMonthTurnover(turnover.longValue());
            accountRepository.save(account);
        }
        log.info("[SCHEDULED] finished calculatePastMonthTurnover");
    }
}