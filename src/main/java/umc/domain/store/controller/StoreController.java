package umc.domain.store.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import umc.domain.review.converter.ReviewConverter;
import umc.domain.review.dto.ReviewReqDTO;
import umc.domain.review.dto.ReviewResDTO;
import umc.domain.review.entity.Review;
import umc.domain.review.exception.code.ReviewSuccessCode;
import umc.domain.review.service.ReviewCommandService;
import umc.domain.store.dto.StoreReqDTO;
import umc.domain.store.service.StoreService;
import umc.global.apiPayload.ApiResponse;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreController {

	private final StoreService storeService;
	private final ReviewCommandService reviewCommandService;

	// 리뷰작성
	@PostMapping("/{storeId}/reviews")
	@Operation(summary = "리뷰 작성")
	public ApiResponse<ReviewResDTO.CreateReviewResultDTO> createReview(
		@RequestParam(name = "memberId") Long memberId,
		@PathVariable(name = "storeId") Long storeId,
		@RequestBody @Valid ReviewReqDTO.ReviewCreateDTO request) {

		Review review = reviewCommandService.createReview(memberId, storeId, request);

		return ApiResponse.onSuccess(ReviewSuccessCode.REVIEW_CREATED, ReviewConverter.toReviewResultDTO(review));
	}
}
