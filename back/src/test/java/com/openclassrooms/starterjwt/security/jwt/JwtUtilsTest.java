package com.openclassrooms.starterjwt.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtilsTest {

    private String jwtSecret = "jwtSecret";
    private int timeBeforeExpiration = 6_000_000;
    private UserDetailsImpl userDetailsImpl;
    @Mock
    Authentication authentication;

    @InjectMocks
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", timeBeforeExpiration);

        userDetailsImpl = generateUserDetailsImpl(1);
        when(authentication.getPrincipal()).thenReturn(userDetailsImpl);
    }

    @Test
    void generateJwtToken() {
        UserDetailsImpl userDetailsImpl = generateUserDetailsImpl(1);
        when(authentication.getPrincipal()).thenReturn(userDetailsImpl);

        String jwts = jwtUtils.generateJwtToken(authentication);

        assertThat(jwts).isNotNull();
    }

    @Test
    void getUserNameFromJwtToken() {

        String jwts = jwtUtils.generateJwtToken(authentication);
        String usernameFromJwtToken = jwtUtils.getUserNameFromJwtToken(jwts);

        assertThat(usernameFromJwtToken).isNotNull();
        assertThat(usernameFromJwtToken).isEqualTo(userDetailsImpl.getUsername());
    }

    @Test
    void validateJwtToken() {

        String jwts = jwtUtils.generateJwtToken(authentication);
        Boolean isValid = jwtUtils.validateJwtToken(jwts);

        assertThat(isValid).isTrue();
    }

    @Test
    void validateJwtToken_returnsFalse_whenWrongSignatureIsSet() {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        String token = Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + timeBeforeExpiration))
                .signWith(SignatureAlgorithm.HS512, "wrongSecret")
                .compact();

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertThat(isValid).isFalse();

    }

    @Test
    void validateJwtToken_returnsFalse_whenMalformedTokenIsSet() {

        boolean isValid = jwtUtils.validateJwtToken("malformed.Secret.token");

        assertThat(isValid).isFalse();

    }

    @Test
    void validateJwtToken_returnsFalse_whenExpiredTokenIsSet() {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        String token = Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() - 1000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertThat(isValid).isFalse();

    }

    @Test
    void validateJwtToken_returnsFalse_whenIllegalArgumentIsSet() {
        boolean isValid = jwtUtils.validateJwtToken(null);

        assertThat(isValid).isFalse();

    }

    private UserDetailsImpl generateUserDetailsImpl(int id) {
        return new UserDetailsImpl(Long.valueOf(id), "username" + id, "firstname" + id, "lastname", true, "123456");
    }
}
