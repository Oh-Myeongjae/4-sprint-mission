package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.auth.jwt.JwtRegistry;
import com.sprint.mission.discodeit.auth.jwt.JwtTokenProvider;
import com.sprint.mission.discodeit.dto.data.JwtDto;
import com.sprint.mission.discodeit.dto.data.JwtInformation;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.SessionManager;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final SessionManager sessionManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final JwtRegistry jwtRegistry;

  @PreAuthorize("hasRole('ADMIN')")
  @Transactional
  @Override
  public UserDto updateRole(RoleUpdateRequest request) {
    return updateRoleInternal(request);
  }

  @Transactional
  @Override
  public UserDto updateRoleInternal(RoleUpdateRequest request) {
    UUID userId = request.userId();
    User user = userRepository.findById(userId)
        .orElseThrow(() -> UserNotFoundException.withId(userId));

    Role newRole = request.newRole();
    user.updateRole(newRole);

    sessionManager.invalidateSessionsByUserId(userId);

    return userMapper.toDto(user);
  }

  @Override
  public JwtDto reissueToken(HttpServletRequest request, HttpServletResponse response) {
    String refreshToken = extractRefreshTokenFromCookies(request);

    if (refreshToken == null || refreshToken.isBlank()) {
      throw new DiscodeitException(ErrorCode.MISSING_TOKEN);
    }

    if (!jwtTokenProvider.validateToken(refreshToken)) {
      throw new DiscodeitException(ErrorCode.INVALID_TOKEN);
    }

    String userEmail = jwtTokenProvider.getSubject(refreshToken);
    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> UserNotFoundException.withEmail(userEmail));

    Map<String, Object> claims = new HashMap<>();
    claims.put("username", user.getUsername());
    claims.put("roles", user.getRole());

    UserDto userDto = userMapper.toDto(user);
    String newAccessToken = jwtTokenProvider.generateAccessToken(claims,user.getEmail());
    String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());

    JwtInformation newJwtInfo = new JwtInformation(userDto, newAccessToken, newRefreshToken);

    JwtInformation jwtInformation = jwtRegistry.rotateJwtInformation(refreshToken, newJwtInfo);

    jwtTokenProvider.addRefreshCookie(response, newRefreshToken);

    return new JwtDto(jwtInformation.userDto(),jwtInformation.accessToken());
  }

  private String extractRefreshTokenFromCookies(HttpServletRequest request) {
    if (request.getCookies() == null) return null;
    for (Cookie cookie : request.getCookies()) {
      if ("REFRESH_TOKEN".equals(cookie.getName())) {
        return cookie.getValue();
      }
    }
    return null;
  }
}
