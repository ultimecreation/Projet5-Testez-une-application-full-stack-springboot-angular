package com.openclassrooms.starterjwt.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;

public class SessionMapperImplTest {
    private LocalDateTime nowDateTime = LocalDateTime.now();
    private Date nowDate = new Date();

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SessionMapperImpl sessionMapperImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void toEntity_returnsNull_whendtoListIsNull() {
        List<SessionDto> dtoList = null;
        List<Session> listSession = sessionMapperImpl.toEntity(dtoList);

        assertThat(listSession).isNull();
    }

    @Test
    void toEntity_returnsEntityList_whendtoListIsNotNull() {

        SessionDto sessionDto1 = generateSessionDtoWithIds(1);
        SessionDto sessionDto2 = generateSessionDtoWithIds(2);

        Session session1 = generateSessionWithIds(1);
        Session session2 = generateSessionWithIds(2);

        List<SessionDto> dtoList = new ArrayList<>();
        dtoList.add(sessionDto1);
        dtoList.add(sessionDto2);

        List<Session> expectedSessions = new ArrayList<>();
        expectedSessions.add(session1);
        expectedSessions.add(session2);

        List<Session> receivedSessions = sessionMapperImpl.toEntity(dtoList);
        assertThat(receivedSessions).isEqualTo(expectedSessions);

    }

    @Test
    void toEntity_returnsNull_whendtoIsNull() {
        SessionDto sessionDto = null;
        Session session = sessionMapperImpl.toEntity(sessionDto);

        assertThat(session).isNull();

    }

    @Test
    void toDto_returnsNull_whenEntityListIsNull() {
        List<Session> sessionList = null;
        List<SessionDto> sessionDtoList = sessionMapperImpl.toDto(sessionList);

        assertThat(sessionDtoList).isNull();
    }

    @Test
    void toDto_returnsNull_whenSessionIsNull() {
        Session session = null;
        SessionDto sessionDto = sessionMapperImpl.toDto(session);

        assertThat(sessionDto).isNull();
    }

    private SessionDto generateSessionDtoWithIds(int id) {
        return new SessionDto(Long.valueOf(id), "session " + id, nowDate, Long.valueOf(id),
                "session " + id + " description", null, nowDateTime.minusDays(id), nowDateTime);
    }

    private Session generateSessionWithIds(int id) {
        List<User> users = new ArrayList<>();
        Teacher teacher = generateTeacherWithId(id);
        return new Session(Long.valueOf(id), "session " + id, nowDate, "session " + id + " description", teacher, users,
                nowDateTime.minusDays(id), nowDateTime);

    }

    private Teacher generateTeacherWithId(int id) {
        Teacher teacher = Teacher.builder().id(Long.valueOf(id)).build();
        return teacher;
    }
}
