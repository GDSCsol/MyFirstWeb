package org.example.security.repository;

import org.example.security.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = "authorities") // Lazy 조회 X Eager 조회 O 권한 정보를 같이 조회
    Optional<User> findOneWithAuthoritiesByName(String username);
}