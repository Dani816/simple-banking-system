package com.danijel.bank_app_leapwise.repository;

import com.danijel.bank_app_leapwise.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    @Query(value = "SELECT COALESCE(SUM(CASE WHEN sender_account_id = :accountId THEN -amount " +
            "WHEN receiver_account_id = :accountId THEN amount ELSE 0 END), 0) " +
            "FROM transaction " +
            "WHERE (sender_account_id = :accountId OR receiver_account_id = :accountId) " +
            "AND time_stamp >= :startDate", nativeQuery = true)
    Double calculateTurnoverForAccount(@Param("accountId") Long accountId, @Param("startDate") ZonedDateTime startDate);
}
