package com.openclassrooms.starterjwt.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void authenticateUser() {

        User user = User.builder()
                .email("test@mail.com")
                .lastName("lastname")
                .firstName("firstname")
                .password("123456")
                .build();
        userDetails = new UserDetailsImpl(1L, "test@gmail.com", "firstname", "lastname", true, "123456");
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(user.getEmail());
        loginRequest.setPassword(user.getPassword());

        String generatedJwt = "generatedJwt";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.setContext(securityContext);

        when(jwtUtils.generateJwtToken(authentication)).thenReturn(generatedJwt);
        when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(Optional.of(user));

        ResponseEntity<?> response = authController.authenticateUser(loginRequest);
        JwtResponse jwtResponse = (JwtResponse) response.getBody();

        ResponseEntity<?> expectedResponse = ResponseEntity.ok(jwtResponse);

        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    void registerUser() {

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@mail.com");
        signupRequest.setLastName("lastname");
        signupRequest.setFirstName("firstname");
        signupRequest.setPassword("123456");

        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");

        @SuppressWarnings("unchecked")
        ResponseEntity<MessageResponse> response = (ResponseEntity<MessageResponse>) authController
                .registerUser(signupRequest);

        assertThat(response.getBody().getMessage()).isEqualTo("User registered successfully!");
    }

    @Test
    void registerUser_fails_whenEmailAlreadyExists() {

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@mail.com");

        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        @SuppressWarnings("unchecked")
        ResponseEntity<MessageResponse> response = (ResponseEntity<MessageResponse>) authController
                .registerUser(signupRequest);

        assertThat(response.getBody().getMessage()).isEqualTo("Error: Email is already taken!");
    }
}
