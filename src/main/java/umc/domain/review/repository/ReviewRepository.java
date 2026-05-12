package umc.domain.review.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import umc.domain.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	// 1. ID순 정렬
	// 커서가 없을 때
	Slice<Review> findAllByMember_IdOrderByIdDesc(Long memberId, Pageable pageable);
	// 커서가 있을 때
	Slice<Review> findAllByMember_IdAndIdLessThanOrderByIdDesc(Long memberId, Long id, Pageable pageable);

	// 2. 별점순 정렬 (별점 내림차순, ID 내림차순)
	@Query("SELECT r FROM Review r JOIN FETCH r.store s WHERE r.member.id = :memberId " +
		"AND (:star IS NULL OR r.star < :star OR (r.star = :star AND r.id < :id)) " +
		"ORDER BY r.star DESC, r.id DESC")
	Slice<Review> findAllByMemberAndScoreCursor(@Param("memberId") Long memberId,
		@Param("star")  Integer star,
		@Param("id") Long id,
		Pageable pageable);
}
