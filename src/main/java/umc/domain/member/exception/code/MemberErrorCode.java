package umc.domain.member.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import umc.global.apiPayload.code.BaseErrorCode;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {

	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,
		"MEMBER404_1",
		"해당 사용자를 찾을 수 없습니다."),

	TERM_NOT_FOUND(HttpStatus.NOT_FOUND, "TERM404_1", "해당 약관을 찾을 수 없습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}
