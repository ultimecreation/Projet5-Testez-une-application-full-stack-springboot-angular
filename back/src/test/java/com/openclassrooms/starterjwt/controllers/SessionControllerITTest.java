package com.openclassrooms.starterjwt.controllers;

import java.time.LocalDateTime;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public class SessionControllerITTest {

    private String idForBadRequest = "1test";
    private String idForNotFoundRequest = "99999999999999999";
    private LocalDateTime nowDateTime = LocalDateTime.now();
    private Date nowDate = new Date();

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        sessionRepository.deleteAll();
        teacherRepository.deleteAll();
    }

    @AfterAll
    public void cleanup() {
        sessionRepository.deleteAll();
        teacherRepository.deleteAll();
    }

    @Test
    @WithMockUser
    public void findById() throws Exception {
        Session session = getSessionCreatedAndSaveSessionWithTeacher();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/{id}", session.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Session 1"));
    }

    @Test
    @WithMockUser
    public void findById_fails_whenIdNotFound() throws Exception {
        getSessionCreatedAndSaveSessionWithTeacher();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/{id}", idForNotFoundRequest))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    public void findById_fails_whenBadRequestIsSent() throws Exception {
        getSessionCreatedAndSaveSessionWithTeacher();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/{id}", idForBadRequest))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void findAll() throws Exception {
        Session session = getSessionCreatedAndSaveSessionWithTeacher();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(session.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser
    public void create() throws Exception {
        Teacher teacher = createTeacher();
        teacherRepository.save(teacher);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/session")
                .contentType("application/json")
                .content(
                        "{\"name\": \"session 1\", \"date\": \"2024-01-01\", \"teacher_id\": 1, \"users\": null, \"description\": \"my description\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("session 1"));

    }

    @Test
    @WithMockUser
    public void update() throws Exception {
        Teacher teacher = createTeacher();
        teacherRepository.save(teacher);

        Session session = createSessionWithoutTeacherOrUsers();
        session.setTeacher(teacher);
        sessionRepository.save(session);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/session/{id}", session.getId())
                .contentType("application/json")
                .content(
                        "{\"name\": \"session 1 updated\", \"date\": \"2024-01-01\", \"teacher_id\": " + teacher.getId()
                                + ", \"users\": null, \"description\": \"my description\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("session 1 updated"));

    }

    @Test
    @WithMockUser
    public void update_fails_whenBadRequestIsSent() throws Exception {
        Teacher teacher = createTeacher();
        teacherRepository.save(teacher);

        Session session = createSessionWithoutTeacherOrUsers();
        session.setTeacher(teacher);
        sessionRepository.save(session);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/session/{id}", idForBadRequest)
                .contentType("application/json")
                .content(
                        "{\"name\": \"session 1 updated\", \"date\": \"2024-01-01\", \"teacher_id\": " + teacher.getId()
                                + ", \"users\": null, \"description\": \"my description\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @WithMockUser
    public void save() throws Exception {
        Session session = getSessionCreatedAndSaveSessionWithTeacher();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/{id}", session.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void save_fails_whenIdNotFound() throws Exception {
        getSessionCreatedAndSaveSessionWithTeacher();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/{id}", "123456"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    public void save_fails_whenBadRequestIsSent() throws Exception {
        getSessionCreatedAndSaveSessionWithTeacher();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/{id}", idForBadRequest))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void participate() throws Exception {
        Teacher teacher = createTeacher();
        teacherRepository.save(teacher);

        Session session = createSessionWithoutTeacherOrUsers();
        session.setTeacher(teacher);
        sessionRepository.save(session);

        User user = createUser();
        userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/session/{id}/participate/{userId}", session.getId(), user.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @WithMockUser
    public void participate_fails_whenBadRequestIsSent() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/session/{id}/participate/{userId}", idForBadRequest, idForBadRequest))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    private Session getSessionCreatedAndSaveSessionWithTeacher() {
        Teacher teacher = createTeacher();
        teacherRepository.save(teacher);
        Session session = createSessionWithoutTeacherOrUsers();
        session.setTeacher(teacher);
        sessionRepository.save(session);
        return session;
    }

    @Test
    @WithMockUser
    public void noLongerParticipate_fails_whenBadRequestIsSent() throws Exception {
        // Teacher teacher = createTeacher();
        // teacherRepository.save(teacher);

        // Session session = createSessionWithoutTeacherOrUsers();
        // session.setTeacher(teacher);
        // sessionRepository.save(session);

        // User user = createUser();
        // userRepository.save(user);

        // mockMvc.perform(MockMvcRequestBuilders
        // .post("/api/session/{id}/participate/{userId}", session.getId(),
        // user.getId()))
        // .andExpect(MockMvcResultMatchers.status().isOk());
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/session/{id}/participate/{userId}", idForBadRequest, idForBadRequest))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @WithMockUser
    public void noLongerParticipate() throws Exception {
        Teacher teacher = createTeacher();
        teacherRepository.save(teacher);

        Session session = createSessionWithoutTeacherOrUsers();
        session.setTeacher(teacher);
        sessionRepository.save(session);

        User user = createUser();
        userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/session/{id}/participate/{userId}", session.getId(), user.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/session/{id}/participate/{userId}", session.getId(), user.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    private Teacher createTeacher() {
        Teacher teacher = Teacher.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .createdAt(nowDateTime)
                .updatedAt(nowDateTime).build();
        return teacher;
    }

    private Session createSessionWithoutTeacherOrUsers() {
        Session session = Session.builder()
                .name("Session 1")
                .date(nowDate)
                .description("Session 1 description")
                .createdAt(nowDateTime)
                .updatedAt(nowDateTime)
                .build();
        return session;
    }

    private User createUser() {
        User user = User.builder()
                .email("test@mail.com")
                .lastName("lastname")
                .firstName("firstname")
                .password("123456")
                .build();
        return user;
    }

}
