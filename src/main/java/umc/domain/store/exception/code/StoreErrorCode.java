package umc.domain.store.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import umc.global.apiPayload.code.BaseErrorCode;

@Getter
@RequiredArgsConstructor
public enum StoreErrorCode implements BaseErrorCode {
	STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE404_1", "해당 가게를 찾을 수 없습니다."),
	REGION_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE404_2", "해당 지역이 존재하지 않습니다."),
	FOOD_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE404_3", "해당 음식이 존재하지 않습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}
