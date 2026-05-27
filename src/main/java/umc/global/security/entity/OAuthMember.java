package umc.global.security.entity;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import umc.domain.member.entity.Member;

@RequiredArgsConstructor
public class OAuthMember implements OAuth2User {

	@Getter
	private final Member member;
	private final Map<String, Object> attributes;

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of();
	}
	@Override
	public String getName() {
		return member.getEmail();
	}

}
