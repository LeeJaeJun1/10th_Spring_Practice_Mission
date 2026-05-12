package umc.domain.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class ReviewReqDTO {
	@Getter
	public static class ReviewCreateDTO {
		@NotBlank(message = "리뷰 내용은 필수 입력 항목입니다.")
		String reviewContent;
		@NotNull(message = "별점은 필수 입력 항목입니다.") @Min(value = 1) @Max(5)
		Integer star;
		String photoUrl;
	}

	@Getter
	public static class MyReviewDTO {
		String cursor;
		Integer pageSize;
		String sortType;
	}
}
