package com.sprint.mission.discodeit.config.security;

import com.sprint.mission.discodeit.dto.data.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserOnlineChecker {
  private final SessionRegistry sessionRegistry;

  public UserDto withOnlineStatus(UserDto dto) {
    boolean online = sessionRegistry.getAllPrincipals().stream()
        .filter(principal -> principal instanceof DiscodeitUserDetails)
        .map(principal -> (DiscodeitUserDetails) principal)
        .anyMatch(userDetails -> userDetails.getUserDto().id().equals(dto.id()));

    return new UserDto(
        dto.id(),
        dto.username(),
        dto.email(),
        dto.profile(),
        online
    );
  }
}
