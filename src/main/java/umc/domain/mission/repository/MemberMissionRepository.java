package umc.domain.mission.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import umc.domain.mission.entity.mapping.MemberMission;
import umc.domain.mission.enums.MissionStatus;

public interface MemberMissionRepository extends JpaRepository<MemberMission, Long> {

	@Query(value = "SELECT mm FROM MemberMission mm " +
		"JOIN FETCH mm.mission m " +
		"JOIN FETCH m.store s " +
		"WHERE mm.member.id = :memberId AND mm.status = :status",
		countQuery = "SELECT count(mm) FROM MemberMission mm " +
			"WHERE mm.member.id = :memberId AND mm.status = :status")
	Page<MemberMission> findMyMissions(
		@Param("memberId") Long memberId,
		@Param("status") MissionStatus status,
		Pageable pageable
	);

	// 홈화면 프로그레스 바
	@Query("SELECT COUNT(mm) FROM MemberMission mm WHERE mm.member.id = :memberId AND mm.status = :status")
	Integer countCompletedMissions(
		@Param("memberId") Long memberId,
		@Param("status") MissionStatus status
	);
}
