package com.openclassrooms.starterjwt.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.PersistenceException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

public class SessionServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private SessionService sessionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create() {
        Session session = Session.builder()
                .id(1L)
                .name("session1")
                .description("session1 desc")
                .date(Date.from(Instant.now()))
                .createdAt(LocalDateTime.parse("2024-12-01T12:00:00"))
                .updatedAt(LocalDateTime.parse("2024-12-01T12:00:00"))
                .build();

        when(sessionRepository.save(session)).thenReturn(session);

        Session createdSession = sessionService.create(session);

        assertThat(createdSession).isEqualTo(session);
        verify(sessionRepository).save(session);

    }

    @Test
    void delete() {
        sessionService.delete(1L);

        verify(sessionRepository).deleteById(1L);
    }

    @Test
    void findAll() {
        Session session1 = Session.builder()
                .id(1L)
                .name("session1")
                .description("session1 desc")
                .date(Date.from(Instant.now()))
                .createdAt(LocalDateTime.parse("2024-12-01T12:00:00"))
                .updatedAt(LocalDateTime.parse("2024-12-01T12:00:00"))
                .build();
        Session session2 = Session.builder()
                .id(1L)
                .name("session2")
                .description("session2 desc")
                .date(Date.from(Instant.now()))
                .createdAt(LocalDateTime.parse("2024-12-01T12:00:00"))
                .updatedAt(LocalDateTime.parse("2024-12-01T12:00:00"))
                .build();
        List<Session> sessionsList = new ArrayList<>();
        sessionsList.add(session1);
        sessionsList.add(session2);

        when(sessionRepository.findAll()).thenReturn(sessionsList);
        List<Session> foundSessions = sessionService.findAll();

        assertThat(foundSessions).isEqualTo(sessionsList);
        verify(sessionRepository).findAll();
    }

    @Test
    void getById() {
        Long id = 1L;
        Session session = Session.builder()
                .id(id)
                .name("session1")
                .description("session1 desc")
                .date(Date.from(Instant.now()))
                .createdAt(LocalDateTime.parse("2024-12-01T12:00:00"))
                .updatedAt(LocalDateTime.parse("2024-12-01T12:00:00"))
                .build();

        when(sessionRepository.findById(id)).thenReturn(Optional.of(session));

        Session sessionFound = sessionService.getById(id);

        assertThat(sessionFound).isEqualTo(session);
        verify(sessionRepository).findById(id);
    }

    @Test
    void update() {
        Long id = 1L;

        Session sessionUpdate = Session.builder()
                // .id(id)
                .name("session1 updated")
                .description("session1 desc updated")
                .date(Date.from(Instant.now()))
                .createdAt(LocalDateTime.parse("2024-12-01T12:00:00"))
                .updatedAt(LocalDateTime.parse("2024-12-01T12:00:00"))
                .build();
        sessionUpdate.setId(id);

        sessionService.update(id, sessionUpdate);

        verify(sessionRepository).save(sessionUpdate);
    }

    @Test
    void participate() {
        Long id = 1L;
        Long userId = 1L;

        Session session = Session.builder()
                .id(id)
                .name("session1")
                .description("session1 desc")
                .date(Date.from(Instant.now()))
                .users(new ArrayList<>())
                .createdAt(LocalDateTime.parse("2024-12-01T12:00:00"))
                .updatedAt(LocalDateTime.parse("2024-12-01T12:00:00"))
                .build();

        User user = User.builder()
                .id(userId)
                .email("test@mail.com")
                .lastName("lastname")
                .firstName("firstname")
                .password("123456")
                .build();

        when(sessionRepository.findById(id)).thenReturn(Optional.of(session));
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        sessionService.participate(id, userId);

        verify(sessionRepository).findById(id);
        verify(sessionRepository).save(session);
        verify(userRepository).findById(id);

    }

    @Test
    void participate_fails_whenUserAndSessionAreNull() {
        Long id = null;
        Long userId = null;

        assertThatThrownBy(() -> sessionService.participate(id, userId)).isInstanceOf(NotFoundException.class);

        verify(sessionRepository).findById(id);
        verify(userRepository).findById(id);
    }

    @Test
    void participate_fails_whenUserIsAlreadyParticipating() {
        Long id = 1L;
        Long userId = 1L;

        User user = User.builder()
                .id(userId)
                .email("test@mail.com")
                .lastName("lastname")
                .firstName("firstname")
                .password("123456")
                .build();

        List<User> users = new ArrayList<>();
        users.add(user);

        Session session = Session.builder()
                .id(id)
                .name("session1")
                .description("session1 desc")
                .date(Date.from(Instant.now()))
                .users(users)
                .createdAt(LocalDateTime.parse("2024-12-01T12:00:00"))
                .updatedAt(LocalDateTime.parse("2024-12-01T12:00:00"))
                .build();

        when(sessionRepository.findById(id)).thenReturn(Optional.of(session));
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> sessionService.participate(id, userId)).isInstanceOf(BadRequestException.class);

        verify(sessionRepository).findById(id);
        verify(userRepository).findById(id);

    }

    @Test
    void noLongerParticipate_succeed_whenUserIsNotAlreadyParticipating() {
        Long id = 1L;
        Long userId = 1L;

        User user = User.builder()
                .id(userId)
                .email("test@mail.com")
                .lastName("lastname")
                .firstName("firstname")
                .password("123456")
                .build();

        List<User> users = new ArrayList<>();
        users.add(user);

        Session session = Session.builder()
                .id(id)
                .name("session1")
                .description("session1 desc")
                .date(Date.from(Instant.now()))
                .users(users)
                .createdAt(LocalDateTime.parse("2024-12-01T12:00:00"))
                .updatedAt(LocalDateTime.parse("2024-12-01T12:00:00"))
                .build();

        when(sessionRepository.findById(id)).thenReturn(Optional.of(session));
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        sessionService.noLongerParticipate(id, userId);

        verify(sessionRepository).findById(id);

    }

    @Test
    void noLongerParticipate_fails_whenUserIsAlreadyParticipating() {
        Long id = 1L;
        Long userId = 1L;

        User user = User.builder()
                .id(userId)
                .email("test@mail.com")
                .lastName("lastname")
                .firstName("firstname")
                .password("123456")
                .build();

        List<User> users = new ArrayList<>();

        Session session = Session.builder()
                .id(id)
                .name("session1")
                .description("session1 desc")
                .date(Date.from(Instant.now()))
                .users(users)
                .createdAt(LocalDateTime.parse("2024-12-01T12:00:00"))
                .updatedAt(LocalDateTime.parse("2024-12-01T12:00:00"))
                .build();

        when(sessionRepository.findById(id)).thenReturn(Optional.of(session));
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> sessionService.noLongerParticipate(id, userId))
                .isInstanceOf(BadRequestException.class);

        verify(sessionRepository).findById(id);

    }

    @Test
    void noLongerParticipate_fails_whenSessionIsNull() {
        Long id = null;
        Long userId = 1L;

        assertThatThrownBy(() -> sessionService.noLongerParticipate(id, userId)).isInstanceOf(NotFoundException.class);

        verify(sessionRepository).findById(id);
    }
}
