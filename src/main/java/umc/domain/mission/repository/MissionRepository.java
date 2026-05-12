package umc.domain.mission.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import umc.domain.mission.entity.Mission;

public interface MissionRepository extends JpaRepository<Mission, Long> {
	// 특정 지역(regionName)의 미션들을 가게, 카테고리 정보와 함께 페이징 조회
	@Query(value = "SELECT m FROM Mission m " +
		"JOIN FETCH m.store s " +
		"JOIN FETCH s.region r " +
		"JOIN FETCH s.foodCategory c " +
		"WHERE r.name = :regionName AND m.isActive = true" +
		" AND NOT EXISTS (SELECT 1 FROM MemberMission mm WHERE mm.mission = m AND mm.member.id = :memberId)",
		countQuery = "SELECT count(m) FROM Mission m WHERE m.store.region.name = :regionName AND m.isActive = true " +
			"AND NOT EXISTS (SELECT 1 FROM MemberMission mm WHERE mm.mission = m AND mm.member.id = :memberId)")
	Page<Mission> findAvailableMissionsByRegion(
		@Param("memberId") Long memberId,
		@Param("regionName") String regionName, Pageable pageable);

	Page<Mission> findAllByStore_Id(Long storeId, Pageable pageable);

	Slice<Mission> findMissionsByStore_IdAndIdLessThanOrderByIdDesc(Long storeId, Long idCursor, Pageable pageable);
	Slice<Mission> findMissionsByStore_IdOrderByIdDesc(Long storeId, Pageable pageable);
}