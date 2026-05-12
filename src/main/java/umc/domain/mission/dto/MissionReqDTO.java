package umc.domain.mission.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class MissionReqDTO {

	// 가게 미션 생성
	@Getter
	public static class CreateMissionDTO {
		@NotBlank(message = "조건은 빈칸일 수 없습니다.")
		private String missionContent;
		@NotNull(message = "미션 성공 포인트는 필수입니다.")
		private Integer missionPoint;
		@NotNull(message = "마감기한은 필수입니다.")
		private LocalDate missionDeadline;
	}
}
