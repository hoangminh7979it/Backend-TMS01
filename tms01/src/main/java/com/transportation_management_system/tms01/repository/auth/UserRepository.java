package com.transportation_management_system.tms01.repository.auth;

import com.transportation_management_system.tms01.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndIsDeleteFalse(String username);

    Optional<User> findByUserIdAndIsDeleteFalse(Long userId);

    List<User> findAllByIsDeleteFalse();

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
