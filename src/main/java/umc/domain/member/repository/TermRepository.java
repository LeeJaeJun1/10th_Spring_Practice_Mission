package umc.domain.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import umc.domain.member.entity.Term;
import umc.domain.member.enums.TermStatus;

public interface TermRepository extends JpaRepository<Term, Long> {
	List<Term> findAllByStatus(TermStatus status);
}
