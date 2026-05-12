package umc.domain.review.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import umc.domain.mission.converter.MissionConverter;
import umc.domain.mission.dto.MissionResDTO;
import umc.domain.review.converter.ReviewConverter;
import umc.domain.review.dto.ReviewResDTO;
import umc.domain.review.entity.Review;
import umc.domain.review.repository.ReviewRepository;

@Service
@RequiredArgsConstructor
public class ReviewQueryService {

	private final ReviewRepository reviewRepository;

	@Transactional(readOnly = true)
	public MissionResDTO.Pagination<ReviewResDTO.ReviewPreviewDTO> getMyReviews(Long memberId, String cursor, Integer pageSize, String sortType) {

		PageRequest pageRequest = PageRequest.of(0, pageSize);
		Slice<Review> reviewSlice;

		// 1. 정렬 및 커서 처리 로직
		// 별점순일 때는 마지막 별점 : 마지막 ID , 최신순일 때는 마지막 ID : 마지막 ID
		if ("star".equals(sortType)) {
			Integer scoreCursor = null;
			Long idCursor = null;
			if (!"-1".equals(cursor)) {
				String[] parts = cursor.split(":");
				scoreCursor = Integer.parseInt(parts[0]);
				idCursor = Long.parseLong(parts[1]);
			}
			reviewSlice = reviewRepository.findAllByMemberAndScoreCursor(memberId, scoreCursor, idCursor, pageRequest);
		} else {
			if ("-1".equals(cursor)) {
				reviewSlice = reviewRepository.findAllByMember_IdOrderByIdDesc(memberId, pageRequest);
			} else {
				Long idCursor = Long.parseLong(cursor.split(":")[1]);
				reviewSlice = reviewRepository.findAllByMember_IdAndIdLessThanOrderByIdDesc(memberId, idCursor, pageRequest);
			}
		}

		// 2. DTO 변환 및 결과 반환
		List<ReviewResDTO.ReviewPreviewDTO> data = reviewSlice.getContent().stream()
			.map(ReviewConverter::toReviewPreviewDTO).toList();

		return MissionConverter.toPagination(
			data,
			reviewSlice.hasNext(),
			ReviewConverter.getNextCursor(reviewSlice, sortType),
			pageSize
		);
	}
}
