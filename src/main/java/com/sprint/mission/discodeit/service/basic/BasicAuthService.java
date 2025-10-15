package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.config.security.UserOnlineChecker;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.InvalidCredentialsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
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
  private final UserOnlineChecker userOnlineChecker;

  @Transactional(readOnly = true)
  @Override
  public UserDto login(LoginRequest loginRequest) {
    log.debug("로그인 시도: username={}", loginRequest.username());
    
    String username = loginRequest.username();
    String password = loginRequest.password();

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> UserNotFoundException.withUsername(username));

    if (!user.getPassword().equals(password)) {
      throw InvalidCredentialsException.wrongPassword();
    }

    log.info("로그인 성공: userId={}, username={}", user.getId(), username);
    UserDto userDto = userMapper.toDto(user);
    return userOnlineChecker.withOnlineStatus(userDto);
  }

  @Transactional
  @PreAuthorize("hasRole('ADMIN')")
  @Override
  public UserDto updateRole(RoleUpdateRequest request) {
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> UserNotFoundException.withId(request.userId()));

    user.updateRole(request.newRole());
    UserDto userDto = userMapper.toDto(user);
    return userOnlineChecker.withOnlineStatus(userDto);
  }
}
