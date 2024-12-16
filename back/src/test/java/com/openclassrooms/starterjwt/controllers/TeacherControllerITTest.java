package com.openclassrooms.starterjwt.controllers;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public class TeacherControllerITTest {

    private LocalDateTime nowDateTime = LocalDateTime.now();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeacherRepository teacherRepository;

    @BeforeEach
    public void init() {
        teacherRepository.deleteAll();
    }

    @AfterAll
    public void cleanup() {
        teacherRepository.deleteAll();
    }

    @Test
    @WithMockUser
    void findById() throws Exception {
        Teacher teacher = createTeacher();
        teacherRepository.save(teacher);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/{id}", teacher.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @WithMockUser
    void findById_fails_whenIdDoNotExists() throws Exception {
        Teacher teacher = createTeacher();
        teacherRepository.save(teacher);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/99999999999999999"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    @WithMockUser
    void findById_fails_whenWhenBadRequestIsSent() throws Exception {
        Teacher teacher = createTeacher();
        teacherRepository.save(teacher);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/1test"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @WithMockUser
    void findAll() throws Exception {
        Teacher teacher = createTeacher();
        teacherRepository.save(teacher);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("Firstname"));

    }

    private Teacher createTeacher() {
        Teacher teacher = Teacher.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .createdAt(nowDateTime)
                .updatedAt(nowDateTime).build();
        return teacher;
    }
}
