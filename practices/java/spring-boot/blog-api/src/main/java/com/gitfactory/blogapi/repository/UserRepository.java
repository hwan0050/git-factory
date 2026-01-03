package com.gitfactory.blogapi.repository;

import com.gitfactory.blogapi.entity.User;
import com.gitfactory.blogapi.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // username으로 사용자 조회
    Optional<User> findByUsername(String username);

    // email로 사용자 조회
    Optional<User> findByEmail(String email);

    // username 존재 여부 확인
    boolean existsByUsername(String username);

    // email 존재 여부 확인
    boolean existsByEmail(String email);

    // role로 사용자 목록 조회
    List<User> findByRole(UserRole role);

    // username에 특정 문자열 포함된 사용자 조회
    List<User> findByUsernameContaining(String keyword);
}