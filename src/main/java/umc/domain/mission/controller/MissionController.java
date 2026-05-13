package umc.domain.mission.controller;

import java.util.List;

import org.springframework.data.domain.Page;
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
import umc.domain.mission.dto.MissionReqDTO;
import umc.domain.mission.dto.MissionResDTO;
import umc.domain.mission.entity.Mission;
import umc.domain.mission.enums.MissionStatus;
import umc.domain.mission.exception.code.MissionSuccessCode;
import umc.domain.mission.service.MissionCommandService;
import umc.domain.mission.service.MissionQueryService;
import umc.global.apiPayload.ApiResponse;
import umc.global.apiPayload.code.BaseSuccessCode;
import umc.global.apiPayload.code.GeneralSuccessCode;
import umc.global.dto.CursorResponseDTO;
import umc.global.dto.PageResponseDTO;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor

public class MissionController {

	private final MissionQueryService missionService;
	private final MissionCommandService missionCommandService;

	// 미션 목록 조회 (진행중, 완료)
	@GetMapping("/members/{memberId}/missions")
	@Operation(summary = "상태별 미션 조회")
	public ApiResponse<PageResponseDTO<MissionResDTO.MissionPreviewDTO>> getMyMissions(
		@PathVariable(name = "memberId") Long memberId,
		@RequestParam(name = "status") MissionStatus status,
		@RequestParam(defaultValue = "0") Integer page) {

		PageResponseDTO<MissionResDTO.MissionPreviewDTO> result = missionService.getMissions(memberId, status, page);

		return ApiResponse.onSuccess(MissionSuccessCode.MISSION_FOUND, result);
	}


	// 미션 성공 누르기 (도전/완료)
	@PostMapping("/missions/{missionId}")
	@Operation(summary = "미션 성공 누르기")
	public ApiResponse<Long> completeMission(@PathVariable(name = "missionId") Long missionId) {
		return ApiResponse.onSuccess(GeneralSuccessCode.OK, null);
	}

	// 가게 미션 생성
	@PostMapping("/stores/{storeId}/missions")
	@Operation(summary = "가게 미션 생성")
	public ApiResponse<Void> createMission(
		@PathVariable Long storeId,
		@RequestBody @Valid MissionReqDTO.CreateMissionDTO req)  {

		BaseSuccessCode code = MissionSuccessCode.STORE_MISSION_CREATED;
		return ApiResponse.onSuccess(code, missionCommandService.createMission(storeId, req));
	}


	// 가게 내 미션들 조회
	@GetMapping("/stores/{storeId}/missions")
	@Operation(summary = "가게 내 미션 조회")
	public ApiResponse<CursorResponseDTO<MissionResDTO.GetMission>> getMission(
		@PathVariable Long storeId,
		@RequestParam Integer pageSize,
		@RequestParam String cursor,
		@RequestParam String query
	) {
		BaseSuccessCode code = MissionSuccessCode.MISSION_FOUND;
		return ApiResponse.onSuccess(code, missionService.getStoreMissions(storeId, pageSize, cursor, query));
	}
}
