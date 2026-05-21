package umc.domain.member.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import umc.domain.member.enums.Gender;
import umc.domain.mission.enums.Address;

public class MemberReqDTO {

	// 마이페이지
	public record GetInfo(
		Long id
	) {}

	// 회원가입
	@Getter
	public static class JoinDTO {
		List<TermDTO> terms; // 약관 동의 목록
		@NotBlank(message = "이름은 필수입니다")
		String name;
		Gender gender;
		@Past
		LocalDate birth;
		Address address;
		@NotBlank(message = "이메일은 필수입니다") @Email(message = "이메일 형식이 아닙니다")
		String email;
		@NotBlank(message = "비밀번호는 필수입니다")
		String password;
		List<Long> favoriteFoods; // 선택한 선호 음식
	}

	@Getter
	public static class TermDTO {
		Long termId;
		Boolean isAgree;
	}
}
