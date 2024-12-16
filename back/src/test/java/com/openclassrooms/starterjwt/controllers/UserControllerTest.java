package com.openclassrooms.starterjwt.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    @Mock
    SecurityContext securityContext;

    @Mock
    Authentication authentication;

    @Mock
    UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById() {
        String id = "1";
        UserDto userDto = new UserDto();
        User user = User.builder()
                .email("test@mail.com")
                .lastName("lastname")
                .firstName("firstname")
                .password("123456")
                .build();

        when(userService.findById(Long.valueOf(id))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        ResponseEntity<?> expectedResponse = ResponseEntity.ok(userDto);
        ResponseEntity<?> response = userController.findById(id);

        assertThat(response).isEqualTo(expectedResponse);

        verify(userService).findById(Long.valueOf(id));
    }

    @Test
    void findById_fails_whenUserNotFound() {
        String id = "1";

        when(userService.findById(Long.valueOf(id))).thenReturn(null);

        ResponseEntity<?> expectedResponse = ResponseEntity.notFound().build();
        ResponseEntity<?> response = userController.findById(id);

        assertThat(response).isEqualTo(expectedResponse);

        verify(userService).findById(Long.valueOf(id));
    }

    @Test
    void findById_fails_whenNumberFormatExceptionIsTriggered() {
        String id = "1-test";

        ResponseEntity<?> expectedResponse = ResponseEntity.badRequest().build();
        ResponseEntity<?> response = userController.findById(id);

        assertThat(response).isEqualTo(expectedResponse);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    void save() {

        String id = "1";
        User user = User.builder()
                .email("test@mail.com")
                .lastName("lastname")
                .firstName("firstname")
                .password("123456")
                .build();

        userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), new ArrayList<>());
        when(userService.findById(Long.valueOf(id))).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.setContext(securityContext);

        ResponseEntity<?> expectedResponse = ResponseEntity.ok().build();
        ResponseEntity<?> response = userController.save(id);

        assertThat(response).isEqualTo(expectedResponse);

        verify(userService).findById(Long.valueOf(id));
        verify(userService).delete(Long.valueOf(id));

    }

    @Test
    void save_fails_whenUserDoNotMatchWithUserDetails() {

        String id = "1";
        User user = User.builder()
                .email("test@mail.com")
                .lastName("lastname")
                .firstName("firstname")
                .password("123456")
                .build();

        userDetails = new org.springframework.security.core.userdetails.User("wrongemial@mail.com",
                user.getPassword(), new ArrayList<>());
        when(userService.findById(Long.valueOf(id))).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.setContext(securityContext);

        ResponseEntity<?> expectedResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        ResponseEntity<?> response = userController.save(id);

        assertThat(response).isEqualTo(expectedResponse);

    }

    @Test
    void save_fails_whenUserNotFound() {
        String id = "1";

        when(userService.findById(Long.valueOf(id))).thenReturn(null);

        ResponseEntity<?> expectedResponse = ResponseEntity.notFound().build();
        ResponseEntity<?> response = userController.save(id);

        assertThat(response).isEqualTo(expectedResponse);

        verify(userService).findById(Long.valueOf(id));
    }

    @Test
    void save_fails_whenNumberFormatExceptionIsTriggered() {
        String id = "1-test";

        ResponseEntity<?> expectedResponse = ResponseEntity.badRequest().build();
        ResponseEntity<?> response = userController.save(id);

        assertThat(response).isEqualTo(expectedResponse);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

}
