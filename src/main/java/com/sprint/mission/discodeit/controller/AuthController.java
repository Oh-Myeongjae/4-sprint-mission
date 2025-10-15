package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.config.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

  private final AuthService authService;

//  @PostMapping(path = "login")
  public ResponseEntity<UserDto> login(@RequestBody @Valid LoginRequest loginRequest) {
    log.info("로그인 요청: username={}", loginRequest.username());
    UserDto user = authService.login(loginRequest);
    log.debug("로그인 응답: {}", user);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(user);
  }

  @GetMapping("csrf-token")
  public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken, HttpServletResponse response) {
    // 명시적으로 토큰을 초기화
    String tokenValue = csrfToken.getToken();

    log.debug("CSRF 토큰 요청: {}", tokenValue);

    // 쿠키에 담아 내려주기
    ResponseCookie cookie = ResponseCookie.from("XSRF-TOKEN", tokenValue)
        .httpOnly(false) // JS에서 읽을 수 있도록 false
        .secure(true)    // HTTPS 환경에서 true 권장
        .sameSite("Strict") // SPA라면 Lax 또는 Strict 권장
        .path("/")
        .build();

    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).build(); // 203
  }

  @GetMapping("/me")
  public ResponseEntity<UserDto> me(@AuthenticationPrincipal DiscodeitUserDetails principal) {
    return ResponseEntity.ok(principal.getUserDto());
  }

  @PutMapping("/role")
  public ResponseEntity<UserDto> updateRole(@RequestBody @Valid RoleUpdateRequest request) {
    log.info("권한 수정 요청: userId={}, newRole={}", request.userId(), request.newRole());
    UserDto updatedUser = authService.updateRole(request);
    return ResponseEntity.ok(updatedUser);
  }

}
