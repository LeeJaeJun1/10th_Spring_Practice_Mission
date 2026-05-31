package umc.domain.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialType {

	KAKAO("카카오"),
	NAVER("네이버"),
	GOOGLE("구글"),
	LOCAL("일반 회원");

	private final String label;
}
