package umc.global.security.dto;

import umc.domain.member.enums.SocialType;

public interface OAuthDTO {
	SocialType getSocialType();
	String getSocialUid();
	String getSocialEmail();
	String getName();
}
