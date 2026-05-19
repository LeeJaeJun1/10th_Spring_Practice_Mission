package umc.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.domain.member.entity.mapping.MemberFood;

public interface MemberFoodRepository extends JpaRepository<MemberFood, Long> {
}
