package com.openclassrooms.starterjwt.security.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

public class UserDetailsServiceImplTest {
    private LocalDateTime nowDateTime = LocalDateTime.now();
    private User user;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername() {

        user = generateUserWithId(1);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetailsServiceImpl.loadUserByUsername(user.getEmail());

        assertThat(userDetailsImpl).isNotNull();
    }

    @Test
    void loadUserByUsername_throwsException_whenEmailDoNotMatch() {

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsServiceImpl.loadUserByUsername("wrong@mail.com");
        });

        assertThat(exception.getMessage()).isEqualTo("User Not Found with email: wrong@mail.com");
    }

    private User generateUserWithId(int id) {
        User user = User.builder()
                .id(Long.valueOf(id))
                .email("mail" + id + "@mail.com")
                .firstName("firstname " + id)
                .lastName("lastname " + id)
                .password("123456")
                .admin(true)
                .createdAt(nowDateTime)
                .updatedAt(nowDateTime)
                .build();
        return user;
    }
}
