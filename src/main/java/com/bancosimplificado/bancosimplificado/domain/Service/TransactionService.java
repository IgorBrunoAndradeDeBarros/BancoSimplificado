package com.bancosimplificado.bancosimplificado.domain.Service;

import com.bancosimplificado.bancosimplificado.domain.Dto.TransactionDto;
import com.bancosimplificado.bancosimplificado.domain.Service.UserService;
import com.bancosimplificado.bancosimplificado.domain.Transaction.Transaction;
import com.bancosimplificado.bancosimplificado.domain.repositories.TransactionRepository;
import com.bancosimplificado.bancosimplificado.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RestTemplate restTemplate;

    public void createTransection(TransactionDto transection) throws Exception {
        User sender = this.userService.findUserById(transection.senderId());
        User receiver = this.userService.findUserById(transection.receiverId());

        userService.validateTransaction(sender, transection.value());

        boolean isAuthorized = authorizeTransaction(sender, transection.value());
        if (!isAuthorized) {
            throw new Exception("transação não autorizada");
        }

        Transaction newtransaction = new Transaction();
        newtransaction.setSender(sender);
        newtransaction.setReceiver(receiver);
        newtransaction.setAmount(transection.value());
        newtransaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transection.value()));
        receiver.setBalance(receiver.getBalance().add(transection.value()));

        this.transactionRepository.save(newtransaction);
        userService.saveUser(sender);
        userService.saveUser(receiver);
    }

    public boolean authorizeTransaction(User sender, BigDecimal value) throws Exception {
        ResponseEntity<Map> authorizationResponse =
                restTemplate.getForEntity(
                        "https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf3d0aef",
                        Map.class
                );

        if (authorizationResponse.getStatusCode() == HttpStatus.OK) {
            String message = (String) authorizationResponse.getBody().get("message");
            return "Autorizado".equalsIgnoreCase(message);
        } else {
            return false;
        }
    }
}