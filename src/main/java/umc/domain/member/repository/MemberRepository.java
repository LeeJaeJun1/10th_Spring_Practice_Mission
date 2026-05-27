package umc.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import umc.domain.member.entity.Member;
import umc.domain.member.enums.SocialType;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);

	boolean existsByEmail(String email);

	Optional<Member> findBySocialTypeAndSocialUid(SocialType providerId, String socialUid);
}
