package com.bancosimplificado.bancosimplificado.domain.Service;

import com.bancosimplificado.bancosimplificado.domain.repositories.UserRepository;
import com.bancosimplificado.bancosimplificado.domain.user.User;
import com.bancosimplificado.bancosimplificado.domain.user.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void validateTransaction(User sender, BigDecimal amount) throws Exception {
        if (sender.getUserType() == UserType.MERCHANT) {
            throw new Exception("Usuário do tipo lojista não está autorizado a realizar transação");
        }
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new Exception("Saldo insuficiente");
        }
    }

    public User findUserById(Long id) throws Exception {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new Exception("Usuário não encontrado"));
    }
    public void saveUser(User user){
        this.userRepository.save(user);
    }
}