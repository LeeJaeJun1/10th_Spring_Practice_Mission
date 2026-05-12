package umc.domain.review.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReviewResDTO {
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CreateReviewResultDTO {
		Long reviewId;
		LocalDateTime createdAt;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ReviewPreviewDTO {
		Long reviewId;
		String storeName;
		Integer star;
		String content;
		LocalDateTime createdAt;
	}

	@Builder
	public record Pagination<T> (
		List<T> data,
		Boolean hasNext,
		String nextCursor,
		Integer pageSize
	) {}
}
