package umc.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import umc.global.security.util.CustomAccessDenied;
import umc.global.security.util.CustomEntryPoint;

@EnableWebSecurity // Spring Security 설정 활성화
@Configuration
public class SecurityConfig {

	private final String[] allowUris = {
		// Swagger 허용
		"/swagger-ui/**",
		"/swagger-resources/**",
		"/v3/api-docs/**",
		"/auth/**",
		"/api/v1/signup",
		"/error/**",
	};

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(requests -> requests
				.requestMatchers(allowUris).permitAll() // allowUris에 있는 주소들은 누구나 접근 가능
				.anyRequest().authenticated() // 그 외 모든 요청은 로그인 필요
			)
			.formLogin(form -> form
				.defaultSuccessUrl("/swagger-ui/index.html", true)
				.permitAll()
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
}
