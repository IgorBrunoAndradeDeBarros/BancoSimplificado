package com.bancosimplificado.bancosimplificado.Service;

import com.bancosimplificado.bancosimplificado.Dto.UserDto;
import com.bancosimplificado.bancosimplificado.domain.user.User;
import com.bancosimplificado.bancosimplificado.domain.user.UserType;
import com.bancosimplificado.bancosimplificado.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("validateTransaction deve lançar erro para lojista")
    void validateTransactionShouldThrowWhenMerchant() {
        User sender = new User();
        sender.setUserType(UserType.MERCHANT);
        sender.setBalance(new BigDecimal("100"));

        Exception ex = assertThrows(Exception.class,
                () -> userService.validateTransaction(sender, new BigDecimal("10")));

        assertThat(ex.getMessage())
                .isEqualTo("Usuário do tipo Lojista não está autorizado a realizar transação");
    }

    @Test
    @DisplayName("validateTransaction deve lançar erro para saldo insuficiente")
    void validateTransactionShouldThrowWhenBalanceLow() {
        User sender = new User();
        sender.setUserType(UserType.COMMON);
        sender.setBalance(new BigDecimal("5"));

        Exception ex = assertThrows(Exception.class,
                () -> userService.validateTransaction(sender, new BigDecimal("10")));

        assertThat(ex.getMessage()).isEqualTo("Saldo insuficiente");
    }

    @Test
    @DisplayName("findUserById deve retornar usuário quando existe")
    void findUserByIdShouldReturnUser() throws Exception {
        User user = new User();
        user.setId(1L);

        when(userRepository.findUserById(1L)).thenReturn(Optional.of(user));

        User result = userService.findUserById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("findUserById deve lançar exceção quando não existe")
    void findUserByIdShouldThrowWhenNotFound() {
        when(userRepository.findUserById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class,
                () -> userService.findUserById(1L));

        assertThat(ex.getMessage()).isEqualTo("Usuário não encontrado");
    }

    @Test
    @DisplayName("createUser deve salvar e retornar usuário")
    void createUserShouldSaveUser() {
        UserDto dto = new UserDto(
                "Fernanda",
                "Teste",
                "99999999901",
                new BigDecimal("10"),
                "test@gmail.com",
                "44444",
                UserType.COMMON
        );

        // quando o repositório salvar, retorna a mesma instância
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.createUser(dto);

        assertThat(result).isNotNull();
        assertThat(result.getFirstname()).isEqualTo("Fernanda");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("getAllUsers deve delegar para repository.findAll")
    void getAllUsersShouldCallRepository() {
        when(userRepository.findAll()).thenReturn(List.of(new User(), new User()));

        List<User> users = userService.getAllUsers();

        assertThat(users).hasSize(2);
        verify(userRepository, times(1)).findAll();
    }
}
