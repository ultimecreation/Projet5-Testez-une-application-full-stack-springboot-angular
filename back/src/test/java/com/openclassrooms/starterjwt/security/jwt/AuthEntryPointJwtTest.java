package com.openclassrooms.starterjwt.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthEntryPointJwtTest {

    private ByteArrayOutputStream byteArrayOutputStream;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private AuthenticationException authException;

    @Mock
    private HttpServletResponse httpServletResponse;

    @InjectMocks
    private AuthEntryPointJwt authEntryPointJwt;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        byteArrayOutputStream = new ByteArrayOutputStream();
    }

    @Test
    void testCommence() throws IOException, ServletException {
        when(httpServletRequest.getServletPath()).thenReturn("/api/test");
        when(authException.getMessage()).thenReturn("Unauthorized error");
        when(httpServletResponse.getOutputStream()).thenReturn(new MockServletOutputStream(byteArrayOutputStream));

        authEntryPointJwt.commence(httpServletRequest, httpServletResponse, authException);
        String body = byteArrayOutputStream.toString("UTF-8");

        assertThat(body).isEqualTo(
                "{\"path\":\"/api/test\",\"error\":\"Unauthorized\",\"message\":\"Unauthorized error\",\"status\":401}");
    }

    private static class MockServletOutputStream extends ServletOutputStream {
        private final OutputStream outputStream;

        public MockServletOutputStream(OutputStream outputStream) {
            this.outputStream = outputStream;
        }

        @Override
        public void write(int b) throws IOException {
            outputStream.write(b);
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener listener) {

        }
    }
}
