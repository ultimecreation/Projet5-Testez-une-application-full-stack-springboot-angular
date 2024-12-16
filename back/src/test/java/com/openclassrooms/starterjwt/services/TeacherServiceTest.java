package com.openclassrooms.starterjwt.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        Teacher teacher1 = Teacher.builder().firstName("firstname1").lastName("lastname1").build();
        Teacher teacher2 = Teacher.builder().firstName("firstname2").lastName("lastname2").build();

        List<Teacher> teachersList = new ArrayList<>();
        teachersList.add(teacher1);
        teachersList.add(teacher2);

        when(teacherRepository.findAll()).thenReturn(teachersList);

        List<Teacher> foundTeachers = teacherService.findAll();

        assertThat(teachersList).isEqualTo(foundTeachers);
        verify(teacherRepository).findAll();
    }

    @Test
    void findById() {
        Long id = 1L;
        Teacher teacher = Teacher.builder().firstName("firstname").lastName("lastname").build();

        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));
        Teacher foundTeacher = teacherService.findById(id);

        assertThat(foundTeacher).isEqualTo(teacher);
        verify(teacherRepository).findById(id);
    }

}
