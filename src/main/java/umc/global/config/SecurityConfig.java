package umc.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.webauthn.management.JdbcPublicKeyCredentialUserEntityRepository;
import org.springframework.security.web.webauthn.management.JdbcUserCredentialRepository;

import lombok.RequiredArgsConstructor;
import umc.global.security.filter.JwtAuthFilter;
import umc.global.security.handler.OAuthSuccessHandler;
import umc.global.security.service.CustomOAuthService;
import umc.global.security.service.CustomUserDetailsService;
import umc.global.security.util.CustomAccessDenied;
import umc.global.security.util.CustomEntryPoint;
import umc.global.security.util.JwtUtil;

@EnableWebSecurity // Spring Security 설정 활성화
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtUtil jwtUtil;
	private final CustomUserDetailsService customUserDetailsService;
	private final CustomOAuthService customOAuthService;

	private final String[] allowUris = {
		// Swagger 허용
		"/swagger-ui/**",
		"/swagger-resources/**",
		"/v3/api-docs/**",
		"/auth/**",
		"/api/v1/signup",
		"/api/v1/login",
		"/error/**",
	};

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, OAuthSuccessHandler oAuthSuccessHandler) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(requests -> requests
				.requestMatchers(allowUris).permitAll() // allowUris에 있는 주소들은 누구나 접근 가능
				.anyRequest().authenticated() // 그 외 모든 요청은 로그인 필요
			)
			.formLogin(AbstractHttpConfigurer::disable)
			.sessionManagement(AbstractHttpConfigurer::disable)
			.addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
			.oauth2Login(oauth -> oauth
				// 인증 엔트리 포인트
				.authorizationEndpoint(auth -> auth
					.baseUri("/oauth/authorize")
				)
				// 콜백 주소
				.redirectionEndpoint(redirect -> redirect
					.baseUri("/oauth/callback/**")
				)
				// 인증 완료 후 정보 활용
				.userInfoEndpoint(userInfo -> userInfo
					.userService(customOAuthService)
				)
				// 성공 시 JWT 토큰 발행할 핸들러
				.successHandler(oAuthSuccessHandler())
			)
			.logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login?logout")
				.permitAll()
			)
			// 인증/인가 실패 시 응답 통일 적용
			.exceptionHandling(exception -> exception
				.accessDeniedHandler(customAccessDenied()) // 인가 실패 시 실패응답
				.authenticationEntryPoint(customEntryPoint()) // 인증 실패 시 실패응답
			)

	;
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CustomAccessDenied customAccessDenied() {
		return new CustomAccessDenied();
	}

	@Bean
	public CustomEntryPoint customEntryPoint() {
		return new CustomEntryPoint();
	}

	@Bean
	public JwtAuthFilter jwtAuthFilter() {
		return new JwtAuthFilter(jwtUtil, customUserDetailsService);
	}

	@Bean
	JdbcPublicKeyCredentialUserEntityRepository jdbcPublicKeyCredentialUserEntityRepository(
		JdbcOperations jdbc) {
		return new JdbcPublicKeyCredentialUserEntityRepository(jdbc);
	}

	@Bean
	JdbcUserCredentialRepository jdbcUserCredentialRepository(JdbcOperations jdbc) {
		return new JdbcUserCredentialRepository(jdbc);
	}

	@Bean
	public OAuthSuccessHandler oAuthSuccessHandler() {
		return new OAuthSuccessHandler(jwtUtil);
	}
}