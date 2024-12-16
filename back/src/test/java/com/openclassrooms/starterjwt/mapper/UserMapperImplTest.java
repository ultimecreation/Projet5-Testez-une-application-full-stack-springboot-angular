package com.openclassrooms.starterjwt.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;

public class UserMapperImplTest {
    private LocalDateTime nowDateTime = LocalDateTime.now();
    @InjectMocks
    private UserMapperImpl userMapperImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void toEntity_returnsNull_whenDtoIsNull() {
        UserDto userDto = null;
        User user = userMapperImpl.toEntity(userDto);

        assertThat(user).isNull();
    }

    @Test
    void toEntity_returnsUser_whenDtoIsNotNull() {
        UserDto userDto = generateUserDtoWithId(1);
        User user = userMapperImpl.toEntity(userDto);

        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
    }

    @Test
    void toDto_returnsNull_whenUserEntityIsNull() {
        User user = null;
        UserDto userDto = userMapperImpl.toDto(user);

        assertThat(userDto).isNull();
    }

    @Test
    void toDto_returnsUserDto_whenUserEntityIsNotNull() {
        User user = generateUserWithId(1);
        UserDto userDto = userMapperImpl.toDto(user);

        assertThat(userDto).isNotNull();
        assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void toEntity_returnsNull_whenDtoListIsNull() {
        List<UserDto> userDtoList = null;
        List<User> userList = userMapperImpl.toEntity(userDtoList);

        assertThat(userList).isNull();
    }

    @Test
    void toEntity_returnsUserList_whenDtoListIsNotNull() {
        UserDto userDto1 = generateUserDtoWithId(1);
        UserDto userDto2 = generateUserDtoWithId(2);
        List<UserDto> userDtoList = new ArrayList<>();
        userDtoList.add(userDto1);
        userDtoList.add(userDto2);

        List<User> userList = userMapperImpl.toEntity(userDtoList);

        assertThat(userList).isNotNull();
        assertThat(userList).hasSize(2);
    }

    @Test
    void toDto_returnsNull_whenEntityListIsNull() {
        List<User> userList = null;
        List<UserDto> userDtoList = userMapperImpl.toDto(userList);

        assertThat(userDtoList).isNull();
    }

    @Test
    void toDto_returnsUserDtoList_whenUserListIsNotNull() {
        User user1 = generateUserWithId(1);
        User user2 = generateUserWithId(2);
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        List<UserDto> userDtoList = userMapperImpl.toDto(userList);

        assertThat(userDtoList).isNotNull();
        assertThat(userDtoList).hasSize(2);
    }

    private User generateUserWithId(int id) {
        User user = User.builder()
                .id(Long.valueOf(id))
                .email("mail" + id + "@mail.com")
                .firstName("firstname " + id)
                .lastName("lastname " + id)
                .password("123456")
                .admin(true)
                .createdAt(nowDateTime)
                .updatedAt(nowDateTime)
                .build();
        return user;
    }

    private UserDto generateUserDtoWithId(int id) {
        UserDto userDto = new UserDto(
                Long.valueOf(id),
                "mail" + id + "@mail.com",
                "lastname " + id,
                "firstname " + id,
                true,
                "123456",
                nowDateTime,
                nowDateTime);
        return userDto;
    }
}
