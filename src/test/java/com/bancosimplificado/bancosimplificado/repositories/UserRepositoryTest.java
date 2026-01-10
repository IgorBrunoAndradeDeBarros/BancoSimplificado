package com.bancosimplificado.bancosimplificado.repositories;

import com.bancosimplificado.bancosimplificado.Dto.UserDto;
import com.bancosimplificado.bancosimplificado.domain.user.User;
import com.bancosimplificado.bancosimplificado.domain.user.UserType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should get User successfully from DB")
    void findUserByDocumentCase1() {
        String document = "99999999901";
        UserDto data = new UserDto("Fernanda", "Teste", document, new BigDecimal(10), "test@gmail.com", "44444", UserType.COMMON);
        this.createUser(data);

        Optional<User> result = this.userRepository.findUserByDocument(document);

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should not get User from DB when user not exists")
    void findUserByDocumentCase2() {
        String document = "99999999901";

        Optional<User> result = this.userRepository.findUserByDocument(document);

        assertThat(result.isEmpty()).isTrue();
    }

    private User createUser(UserDto data){
        User newUser = new User(data);
        this.entityManager.persist(newUser);
        return newUser;
    }
}
