package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.config.security.DiscodeitUserDetailsService;
import com.sprint.mission.discodeit.config.security.JsonAccessDeniedHandler;
import com.sprint.mission.discodeit.config.security.JsonAuthenticationEntryPoint;
import com.sprint.mission.discodeit.config.security.LoginFailureHandler;
import com.sprint.mission.discodeit.config.security.LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

  private final SpaCsrfTokenRequestHandler SpaCsrfTokenRequestHandler;

  private final LoginSuccessHandler loginSuccessHandler;
  private final LoginFailureHandler loginFailureHandler;

  private final JsonAuthenticationEntryPoint jsonAuthenticationEntryPoint;
  private final JsonAccessDeniedHandler jsonAccessDeniedHandler;
  private final SessionRegistry sessionRegistry;

  private final DiscodeitUserDetailsService DiscodeitUserDetailsService;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .csrfTokenRequestHandler(SpaCsrfTokenRequestHandler)
        )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/",
                "/api/auth/csrf-token",
                "/api/auth/register",
                "/api/auth/login",
                "/api/auth/logout",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/actuator/**"
            ).permitAll()

            .anyRequest().authenticated()
        )
        .sessionManagement(management -> management
            .sessionConcurrency(concurrency -> concurrency
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .sessionRegistry(sessionRegistry)
            )
        )
        .formLogin(login -> login
            .loginProcessingUrl("/api/auth/login")
            .successHandler(loginSuccessHandler)
            .failureHandler(loginFailureHandler)
        )
        .rememberMe(rememberMe -> rememberMe
            .key("my-remember-key")
            .rememberMeParameter("remember-me")
            .tokenValiditySeconds(7 * 24 * 60 * 60)
            .userDetailsService(DiscodeitUserDetailsService)
        )
        .logout(logout -> logout
            .logoutUrl("/api/auth/logout")
            .logoutSuccessHandler(
                new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT))
            .deleteCookies("JSESSIONID", "remember-me")
        )
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint(jsonAuthenticationEntryPoint)
            .accessDeniedHandler(jsonAccessDeniedHandler)
        )
    ;
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  WebSecurityCustomizer webSecurityCustomizer() {

    return (web -> web.ignoring()
        .requestMatchers("/favicon.ico")
        // 정적 리소스 (CSS, JavaScript, 이미지 등)
        .requestMatchers(
            "/static/**",
            "/assets/**",
            "/index.html",
            "/index-*.js",
            "/index-*.css"
        ));
  }
}
