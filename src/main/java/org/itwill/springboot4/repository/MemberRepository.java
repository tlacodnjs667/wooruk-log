package org.itwill.springboot4.repository;

import java.util.Optional;
import org.itwill.springboot4.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @EntityGraph(attributePaths = {"roles"})
    Optional<Member> findByUsername(String username);
}
