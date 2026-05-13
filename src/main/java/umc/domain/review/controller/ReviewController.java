package umc.domain.review.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import umc.domain.mission.dto.MissionResDTO;
import umc.domain.review.dto.ReviewReqDTO;
import umc.domain.review.dto.ReviewResDTO;
import umc.domain.review.exception.code.ReviewSuccessCode;
import umc.domain.review.service.ReviewQueryService;
import umc.global.apiPayload.ApiResponse;
import umc.global.apiPayload.code.BaseSuccessCode;
import umc.global.dto.CursorResponseDTO;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewQueryService reviewQueryService;

	@GetMapping("/members/{memberId}/reviews")
	@Operation(summary = "리뷰 조회")
	public ApiResponse<CursorResponseDTO<ReviewResDTO.ReviewPreviewDTO>> getMyReviews(
		@PathVariable(name = "memberId") Long memberId,
		@RequestParam(name = "cursor", defaultValue = "-1") String cursor,
		@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
		@RequestParam(name = "sortType", defaultValue = "id") String sortType
	) {

		BaseSuccessCode code = ReviewSuccessCode.REVIEW_FOUND;

		return ApiResponse.onSuccess(code,  reviewQueryService.getMyReviews(memberId, cursor, pageSize, sortType));
	}

}
