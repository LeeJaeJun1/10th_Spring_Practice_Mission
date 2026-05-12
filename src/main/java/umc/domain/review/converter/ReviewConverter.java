package umc.domain.review.converter;

import org.springframework.data.domain.Slice;

import umc.domain.member.entity.Member;
import umc.domain.review.dto.ReviewReqDTO;
import umc.domain.review.dto.ReviewResDTO;
import umc.domain.review.entity.Review;
import umc.domain.store.entity.Store;

public class ReviewConverter {

	// 클라이언트 요청 저장
	public static Review toReviewEntity(ReviewReqDTO.ReviewCreateDTO request, Member member, Store store) {
		return Review.builder()
			.member(member)
			.store(store)
			.star(request.getStar())
			.content(request.getReviewContent())
			.build();
	}

	// DB 내용 클라이언트에게 돌려줄 응답
	public static ReviewResDTO.CreateReviewResultDTO toReviewResultDTO(Review review) {
		return ReviewResDTO.CreateReviewResultDTO.builder()
			.reviewId(review.getId())
			.createdAt(review.getCreatedAt())
			.build();
	}

	public static ReviewResDTO.ReviewPreviewDTO toReviewPreviewDTO(Review review) {
		return ReviewResDTO.ReviewPreviewDTO.builder()
			.reviewId(review.getId())
			.storeName(review.getStore().getName())
			.star(review.getStar())
			.content(review.getContent())
			.createdAt(review.getCreatedAt())
			.build();
	}

	// 정렬 타입에 따라 다음 커서 문자열을 생성합니다.
	public static String getNextCursor(Slice<Review> reviewSlice, String sortType) {
		if (!reviewSlice.hasNext()) return "-1";

		Review lastReview = reviewSlice.getContent().get(reviewSlice.getContent().size() - 1);
		if ("star".equals(sortType)) {
			return lastReview.getStar() + ":" + lastReview.getId();
		}
		return lastReview.getId().toString() + ":" + lastReview.getId();
	}
}


