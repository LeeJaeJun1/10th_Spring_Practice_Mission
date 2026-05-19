package umc.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import umc.domain.member.entity.mapping.MemberTerm;

public interface MemberTermRepository extends JpaRepository<MemberTerm, Long> {
}
