package com.bancosimplificado.bancosimplificado.Controller;

import com.bancosimplificado.bancosimplificado.Dto.TransactionDto;
import com.bancosimplificado.bancosimplificado.Service.TransactionService;
import com.bancosimplificado.bancosimplificado.domain.Transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/transactions")
@Tag(name = "Transactions", description = "Operações de transferência entre usuários")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    @Operation(summary = "Criar transação", description = "Realiza uma transferência entre usuários")
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDto transaction) throws Exception {
        Transaction newTransaction = this.transactionService.createTransaction(transaction);
        return new ResponseEntity<>(newTransaction, HttpStatus.OK);
    }
}
