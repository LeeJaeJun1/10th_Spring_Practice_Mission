package umc.domain.member.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import umc.global.apiPayload.code.BaseErrorCode;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {

	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER404_1", "해당 사용자를 찾을 수 없습니다."),

	EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "MEMBER409_1", "이미 존재하는 이메일입니다."),

	INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "MEMBER400_1", "비밀번호가 일치하지 않습니다"),

	NOT_SUPPORT_SOCIAL_PROVIDER(HttpStatus.BAD_REQUEST, "MEMBER400_X", "지원하지 않는 소셜 로그인 제공자입니다.");
	private final HttpStatus status;
	private final String code;
	private final String message;
}
