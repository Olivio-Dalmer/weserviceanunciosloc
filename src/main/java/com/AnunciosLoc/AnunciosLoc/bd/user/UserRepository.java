package com.AnunciosLoc.AnunciosLoc.bd.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findById(String email);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);
}
