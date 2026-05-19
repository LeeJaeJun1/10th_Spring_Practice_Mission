package umc.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import umc.domain.member.entity.Term;

public interface TermRepository extends JpaRepository<Term, Long> {
}
