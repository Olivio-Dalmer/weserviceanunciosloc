package com.AnunciosLoc.AnunciosLoc.bd.userProfile;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AnunciosLoc.AnunciosLoc.bd.user.User;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserAndChave(User user, String chave);
    List    <UserProfile> findByUserId(Long usuer_id);
    List    <UserProfile> findByUser(User user);

}
