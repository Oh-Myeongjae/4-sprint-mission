package com.sprint.mission.discodeit.config.security;

import com.sprint.mission.discodeit.dto.data.UserDto;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@RequiredArgsConstructor
public class DiscodeitUserDetails implements UserDetails {
  private final UserDto userDto;
  private final String password;
  private final DiscorditAuthorityUtils discorditAuthorityUtils;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<String> roles = discorditAuthorityUtils.createRoles(userDto.email());

    return discorditAuthorityUtils.createAuthorities(roles);
  }

  @Override
  public String getUsername() {
    return userDto.username();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof DiscodeitUserDetails that)) return false;
    return this.userDto.id().equals(that.userDto.id());
  }

  @Override
  public int hashCode() {
    return userDto.id().hashCode();
  }
}

