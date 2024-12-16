package com.openclassrooms.starterjwt.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;

public class TeacherMapperImplTest {

    private LocalDateTime nowDateTime = LocalDateTime.now();

    @InjectMocks
    TeacherMapperImpl teacherMapperImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void toEntity_returnsNull_whenDtoIsNull() {
        TeacherDto teacherDto = null;
        Teacher teacher = teacherMapperImpl.toEntity(teacherDto);

        assertThat(teacher).isNull();
    }

    @Test
    void toEntity_returnsTeacherEntity_whenDtoIsNotNull() {
        TeacherDto teacherDto = generateTeacherDtoWithId(1);
        Teacher teacher = teacherMapperImpl.toEntity(teacherDto);

        assertThat(teacher).isNotNull();
        assertThat(teacher.getFirstName()).isEqualTo(teacherDto.getFirstName());
        assertThat(teacher.getLastName()).isEqualTo(teacherDto.getLastName());
    }

    @Test
    void toEntity_returnsNull_whenDtoListIsNull() {
        List<TeacherDto> teacherDtoList = null;
        List<Teacher> teacherList = teacherMapperImpl.toEntity(teacherDtoList);

        assertThat(teacherList).isNull();
    }

    @Test
    void toEntity_returnsTeacherEntityList_whenDtoListIsNotNull() {
        TeacherDto teacherDto1 = generateTeacherDtoWithId(1);
        TeacherDto teacherDto2 = generateTeacherDtoWithId(2);
        List<TeacherDto> teacherDtoList = new ArrayList<>();
        teacherDtoList.add(teacherDto1);
        teacherDtoList.add(teacherDto2);

        List<Teacher> teacherList = teacherMapperImpl.toEntity(teacherDtoList);

        assertThat(teacherList).isNotNull();
        assertThat(teacherList).hasSize(2);
    }

    @Test
    void toDto_returnsNull_whenTeacherIsNull() {
        Teacher teacher = null;
        TeacherDto teacherDto = teacherMapperImpl.toDto(teacher);

        assertThat(teacherDto).isNull();
    }

    @Test
    void toDto_returnsNull_whenTeacherListIsNull() {
        List<Teacher> teacherList = null;
        List<TeacherDto> teacherDtoList = teacherMapperImpl.toDto(teacherList);

        assertThat(teacherDtoList).isNull();
    }

    @Test
    void toDto_returnsTeacherDtoList_whenEntityListIsNotNull() {
        Teacher teacher1 = generateTeacherWithId(1);
        Teacher teacher2 = generateTeacherWithId(2);
        List<Teacher> teacherList = new ArrayList<>();
        teacherList.add(teacher1);
        teacherList.add(teacher2);

        List<TeacherDto> teacherDtoList = teacherMapperImpl.toDto(teacherList);

        assertThat(teacherDtoList).isNotNull();
        assertThat(teacherDtoList).hasSize(2);
    }

    private Teacher generateTeacherWithId(int id) {
        Teacher teacher = Teacher.builder()
                .id(Long.valueOf(id))
                .firstName("firstname " + id)
                .lastName("lastname " + id)
                .createdAt(nowDateTime)
                .updatedAt(nowDateTime)
                .build();
        return teacher;
    }

    private TeacherDto generateTeacherDtoWithId(int id) {
        TeacherDto teacherDto = new TeacherDto(
                Long.valueOf(id),
                "lastname " + id,
                "firstname " + id,
                nowDateTime,
                nowDateTime);
        return teacherDto;
    }
}
