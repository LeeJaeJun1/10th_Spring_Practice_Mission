package umc.domain.member.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import umc.domain.member.dto.MemberReqDTO;
import umc.domain.member.dto.MemberResDTO;
import umc.domain.member.entity.Member;
import umc.domain.member.exception.code.MemberSuccessCode;
import umc.domain.member.service.MemberCommandService;
import umc.domain.member.service.MemberQueryService;
import umc.global.apiPayload.ApiResponse;
import umc.global.apiPayload.code.BaseSuccessCode;
import umc.global.apiPayload.code.GeneralSuccessCode;
import umc.global.security.entity.AuthMember;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")

public class MemberController {

	private final MemberQueryService memberQueryService;
	private final MemberCommandService memberCommandService;

	// 마이페이지
	@GetMapping("/users/me")
	@Operation(summary = "마이페이지 조회")
	public ApiResponse<MemberResDTO.GetInfo> getInfo(
		@AuthenticationPrincipal AuthMember member
	) {
		MemberResDTO.GetInfo getInfo = memberQueryService.getInfo(member);

		return ApiResponse.onSuccess(MemberSuccessCode.OK, getInfo);
	}


	// 회원가입
	@PostMapping("/signup")
	@Operation(summary = "회원가입")
	public ApiResponse<MemberResDTO.JoinResultDTO> join(@RequestBody @Valid MemberReqDTO.JoinDTO request) {

		MemberResDTO.JoinResultDTO joins = memberCommandService.joinMember(request);

		BaseSuccessCode code = MemberSuccessCode.MEMBER_JOINED;
		return ApiResponse.onSuccess(code, joins);
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

	// 로그인
	@PostMapping("/login")
	@Operation(summary = "로그인")
	public ApiResponse<MemberResDTO.LoginResultDTO> login(
		@RequestBody @Valid MemberReqDTO.LoginDTO request
	) {
		MemberResDTO.LoginResultDTO result = memberCommandService.login(request);

		return ApiResponse.onSuccess(MemberSuccessCode.OK, result);
	}
}
