package umc.domain.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {

	MALE("남자"),
	FEMALE("여자"),
	NONE("선택안함");

	private final String label;
}
