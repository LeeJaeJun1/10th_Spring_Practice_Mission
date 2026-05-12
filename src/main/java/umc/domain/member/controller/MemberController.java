package umc.domain.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import umc.domain.member.dto.MemberReqDTO;
import umc.domain.member.dto.MemberResDTO;
import umc.domain.member.exception.code.MemberSuccessCode;
import umc.domain.member.service.MemberQueryService;
import umc.global.apiPayload.ApiResponse;
import umc.global.apiPayload.code.BaseSuccessCode;
import umc.global.apiPayload.code.GeneralSuccessCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")

public class MemberController {

	private final MemberQueryService memberQueryService;

	// 마이페이지
	@GetMapping("/{memberId}/mypage")
	@Operation(summary = "마이페이지 조회")
	public ApiResponse<MemberResDTO.GetInfo> getInfo(
		@PathVariable(name = "memberId") Long memberId
	) {
		MemberResDTO.GetInfo getInfo = memberQueryService.getInfo(memberId);

		return ApiResponse.onSuccess(MemberSuccessCode.OK, getInfo);
	}


	// 회원가입
	@PostMapping("/signup")
	@Operation(summary = "회원가입")
	public ApiResponse<String> join(@RequestBody MemberReqDTO.JoinDTO request) {
		BaseSuccessCode code = MemberSuccessCode.MEMBER_JOINED;
		return ApiResponse.onSuccess(GeneralSuccessCode.OK, null);
	}


	// 홈화면
	@GetMapping("/{memberId}/home")
	@Operation(summary = "홈화면 조회")
	public ApiResponse<MemberResDTO.HomeResponseDTO> getHomeInfo(
		@PathVariable(name = "memberId") Long memberId,
		@RequestParam(name = "regionName") String regionName,
		@RequestParam(name = "page", defaultValue = "0") Integer page) {

		MemberResDTO.HomeResponseDTO result = memberQueryService.getHomeData(memberId, regionName, page);

		return ApiResponse.onSuccess(MemberSuccessCode.OK, result);
	}
}
