package com.openclassrooms.starterjwt.security.services;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserDetailsImplTest {

    private UserDetailsImpl userDetailsImpl;

    @BeforeEach
    void setUp() {
        userDetailsImpl = generateUserDetailsImpl(1);

    }

    @Test
    void getAuthorities_returnsNotNull_whenUserDetailsIsNotEmpty() {
        assertThat(userDetailsImpl.getAuthorities()).isNotNull();
    }

    @Test
    void isAccountNonExpired_returnsTrue_whenUserDetailsIsNotEmpty() {
        assertThat(userDetailsImpl.isAccountNonExpired()).isTrue();
    }

    @Test
    void isAccountNonLocked_returnsTrue_whenUserDetailsIsNotEmpty() {
        assertThat(userDetailsImpl.isAccountNonLocked()).isTrue();
    }

    @Test
    void isCredentialsNonExpired_returnsTrue_whenUserDetailsIsNotEmpty() {
        assertThat(userDetailsImpl.isCredentialsNonExpired()).isTrue();
    }

    @Test
    void isEnabled_returnsTrue_whenUserDetailsIsNotEmpty() {
        assertThat(userDetailsImpl.isEnabled()).isTrue();
    }

    @Test
    void equals_returnsObjectEquality_whenCurrentInstanceIsOkAndIdIsTheSame() {
        UserDetailsImpl o = generateUserDetailsImpl(1);
        assertThat(userDetailsImpl).isEqualTo(o);
    }

    @Test
    void equals_returnsTrue_whenCurrentInstanceIsTheSame() {
        assertThat(userDetailsImpl.equals(userDetailsImpl)).isTrue();
    }

    @Test
    void equals_returnsFalse_whenCurrentInstanceIsNull() {
        UserDetailsImpl o = null;
        assertThat(userDetailsImpl.equals(o)).isFalse();
    }

    @Test
    void equals_returnsFalse_whenCurrentInstanceIsNullAndNotSameInstance() {
        String o = "";
        assertThat(userDetailsImpl.equals(o)).isFalse();
    }

    private UserDetailsImpl generateUserDetailsImpl(int id) {
        return new UserDetailsImpl(Long.valueOf(id), "username" + id, "firstname" + id, "lastname", true, "123456");
    }

}
