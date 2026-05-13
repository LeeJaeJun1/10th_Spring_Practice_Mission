package umc.domain.mission.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MissionResDTO {

	// 홈화면
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class HomeViewDTO {
		Integer userPoint;
		Integer missionCount;
		List<MissionPreviewDTO> missions; // 추천 미션
		Boolean hasNext; // 다음 페이지 있는지
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MissionPreviewDTO {
		Long missionId;
		Long storeId;
		String storeName;
		String missionContent;
		Integer missionPoint;
		LocalTime missionDeadline;
	}

	// 미션목록 조회
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MissionListDTO {
		List<MissionDetailDTO> missions;
		Boolean hasNext;
	}


	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MissionDetailDTO {
		Long memberMissionId;
		Long missionId;
		String storeName;
		Integer point;
		String missionContent;
		String status; // 진행중, 진행완료
	}


	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GetMission {
		Long missionId;
		Long storeId;
		String missionContent;
		Integer missionPoint;
		LocalTime missionDeadline;
	}

	@Builder
	public record Pagination<T> (
		List<T> data,
		Boolean hasNext,
		String nextCursor,
		Integer pageSize
	) {}
}
