package com.repit.repository;

import com.example.yourpackage.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByOauthIdAndOauthProvider(String oauthId, String oauthProvider);
    Optional<Member> findByEmail(String email);
    // You can add more custom queries if needed
}