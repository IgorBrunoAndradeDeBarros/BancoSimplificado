package com.bancosimplificado.bancosimplificado.domain.repositories;

import com.bancosimplificado.bancosimplificado.domain.Transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
