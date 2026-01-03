package com.gitfactory.blogapi.repository;

import com.gitfactory.blogapi.entity.User;
import com.gitfactory.blogapi.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaAuditing  // 이 줄 추가! ✨
@DisplayName("UserRepository 테스트")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 초기화
        userRepository.deleteAll();

        testUser = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .role(UserRole.USER)
                .build();
    }

    @Test
    @DisplayName("사용자 저장 성공")
    void saveUser() {
        // when
        User savedUser = userRepository.save(testUser);

        // then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("testuser");
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(savedUser.getRole()).isEqualTo(UserRole.USER);
        assertThat(savedUser.getCreatedAt()).isNotNull();
        assertThat(savedUser.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("username으로 사용자 조회 성공")
    void findByUsername() {
        // given
        userRepository.save(testUser);

        // when
        Optional<User> foundUser = userRepository.findByUsername("testuser");

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("존재하지 않는 username 조회 시 empty 반환")
    void findByUsernameNotFound() {
        // when
        Optional<User> foundUser = userRepository.findByUsername("nonexistent");

        // then
        assertThat(foundUser).isEmpty();
    }

    @Test
    @DisplayName("email로 사용자 조회 성공")
    void findByEmail() {
        // given
        userRepository.save(testUser);

        // when
        Optional<User> foundUser = userRepository.findByEmail("test@example.com");

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("username 존재 여부 확인 - 존재함")
    void existsByUsernameTrue() {
        // given
        userRepository.save(testUser);

        // when
        boolean exists = userRepository.existsByUsername("testuser");

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("username 존재 여부 확인 - 존재하지 않음")
    void existsByUsernameFalse() {
        // when
        boolean exists = userRepository.existsByUsername("nonexistent");

        // then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("email 존재 여부 확인 - 존재함")
    void existsByEmailTrue() {
        // given
        userRepository.save(testUser);

        // when
        boolean exists = userRepository.existsByEmail("test@example.com");

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("email 존재 여부 확인 - 존재하지 않음")
    void existsByEmailFalse() {
        // when
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        // then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("role로 사용자 목록 조회")
    void findByRole() {
        // given
        userRepository.save(testUser);

        User adminUser = User.builder()
                .username("admin")
                .email("admin@example.com")
                .password("admin123")
                .role(UserRole.ADMIN)
                .build();
        userRepository.save(adminUser);

        // when
        List<User> users = userRepository.findByRole(UserRole.USER);
        List<User> admins = userRepository.findByRole(UserRole.ADMIN);

        // then
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getRole()).isEqualTo(UserRole.USER);
        assertThat(admins).hasSize(1);
        assertThat(admins.get(0).getRole()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    @DisplayName("username 검색 기능")
    void findByUsernameContaining() {
        // given
        userRepository.save(testUser);

        User anotherUser = User.builder()
                .username("testadmin")
                .email("testadmin@example.com")
                .password("password123")
                .role(UserRole.ADMIN)
                .build();
        userRepository.save(anotherUser);

        // when
        List<User> users = userRepository.findByUsernameContaining("test");

        // then
        assertThat(users).hasSize(2);
        assertThat(users).extracting("username")
                .containsExactlyInAnyOrder("testuser", "testadmin");
    }

    @Test
    @DisplayName("사용자 정보 업데이트")
    void updateUser() {
        // given
        User savedUser = userRepository.save(testUser);
        Long userId = savedUser.getId();

        // when
        savedUser.updateEmail("newemail@example.com");
        savedUser.updatePassword("newpassword");
        userRepository.save(savedUser);

        // then
        User updatedUser = userRepository.findById(userId).orElseThrow();
        assertThat(updatedUser.getEmail()).isEqualTo("newemail@example.com");
        assertThat(updatedUser.getPassword()).isEqualTo("newpassword");
    }

    @Test
    @DisplayName("사용자 삭제")
    void deleteUser() {
        // given
        User savedUser = userRepository.save(testUser);
        Long userId = savedUser.getId();

        // when
        userRepository.deleteById(userId);

        // then
        Optional<User> deletedUser = userRepository.findById(userId);
        assertThat(deletedUser).isEmpty();
    }
}