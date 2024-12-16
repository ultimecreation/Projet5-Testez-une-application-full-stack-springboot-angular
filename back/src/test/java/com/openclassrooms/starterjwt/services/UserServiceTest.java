package com.openclassrooms.starterjwt.services;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void shouldFindTheStudentById() throws Exception {
        Long id = 1L;
        User user = User.builder()
                .email("test@mail.com")
                .lastName("lastname")
                .firstName("firstname")
                .password("123456")
                .build();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User returnedUser = userService.findById(id);
        assertThat(returnedUser).isEqualTo(user);
        verify(userRepository).findById(id);
    }

    @Test
    void shouldDeleteTheUser() {
        Long id = 1L;

        userService.delete(id);
        verify(userRepository).deleteById(id);
    }
}
