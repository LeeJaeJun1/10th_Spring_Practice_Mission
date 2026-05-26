package umc.domain.member.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberResDTO {

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GetInfo {
		String name;
		String profileUrl;
		String email;
		String phoneNumber;
		Boolean isPhoneVerified;
		Integer point;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	// 회원가입시 memberID와 생성시간
	public static class JoinResultDTO {
		Long memberId;
		LocalDateTime createdAt;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class HomeResponseDTO {
		String regionName;
		Integer totalPoints;
		MissionProgressDTO missionProgress;
		MissionListDTO missionList;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MissionProgressDTO {
		Integer completeCount;
		Integer goalCount;
		Integer rewardPoints;
	}

	// 선택된 지역에서 도전이 가능한 미션 목록이기 때문에 그냥 합쳐서 하는게 낫다고 판단. 중복되서 사용되는 일이 없다고 생각
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MissionListDTO {
		List<MissionDetailDTO> missionList;
		Boolean hasNext;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MissionDetailDTO {
		Long missionId;
		String storeName;
		String category;
		String missionContent;
		Integer point;
		LocalTime deadline;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class LoginResultDTO {
		String accessToken;
	}
}
