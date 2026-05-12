package umc.domain.mission.converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import umc.domain.mission.dto.MissionReqDTO;
import umc.domain.mission.dto.MissionResDTO;
import umc.domain.mission.entity.Mission;
import umc.domain.mission.entity.mapping.MemberMission;
import umc.domain.store.entity.Store;

@Component
public class MissionConverter {

	// 미션 조회

	public MissionResDTO.MissionDetailDTO toMissionDetailDTO(MemberMission memberMission) {

		Mission mission = memberMission.getMission();
		Store store = mission.getStore();

		return MissionResDTO.MissionDetailDTO.builder()
			.memberMissionId(memberMission.getId())
			.missionId(mission.getId())
			.storeName(store.getName())
			.point(mission.getSuccessPoint())
			.missionContent(mission.getMissionCondition())
			.status(memberMission.getStatus().toString())
			.build();
	}

	public MissionResDTO.MissionListDTO toMissionListDTO(Page<MemberMission> page) {
		List<MissionResDTO.MissionDetailDTO> missions = page.getContent()
			.stream()
			.map(this::toMissionDetailDTO)
			.collect(Collectors.toList());

		return MissionResDTO.MissionListDTO.builder()
			.missions(missions)
			.hasNext(page.hasNext())
			.build();
	}

	// 미션 생성
	public static Mission toMission(Store store, MissionReqDTO.CreateMissionDTO request) {
		return Mission.builder()
			.store(store)
			.missionCondition(request.getMissionContent())
			.successPoint(request.getMissionPoint())
			.deadline(request.getMissionDeadline())
			.isActive(true)
			.build();
	}

	// 미션 조회
	public static MissionResDTO.GetMission toGetMission(Mission mission) {
		return MissionResDTO.GetMission.builder()
			.missionId(mission.getId())
			.storeId(mission.getStore().getId())
			.missionContent(mission.getMissionCondition())
			.missionPoint(mission.getSuccessPoint())
			.missionDeadline(mission.getDeadline())
			.build();
	}

	public static <T> MissionResDTO.Pagination<T> toPagination(
		List<T> data,
		Boolean hasNext,
		String nextCursor,
		Integer pageSize
	) {
		return MissionResDTO.Pagination.<T>builder()
			.data(data)
			.hasNext(hasNext)
			.nextCursor(nextCursor)
			.pageSize(pageSize)
			.build();
	}
}
