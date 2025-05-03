package com.jhlab.gigsync.domain.user.repository;

import com.jhlab.gigsync.domain.user.dto.UserResponseDto;
import com.jhlab.gigsync.domain.user.entity.User;
import com.jhlab.gigsync.domain.user.type.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.role = :role")
    Page<User> findUsers(@Param("role") UserRole role, Pageable pageable);
}
