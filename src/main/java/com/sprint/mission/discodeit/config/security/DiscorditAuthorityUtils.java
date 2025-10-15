package com.sprint.mission.discodeit.config.security;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class DiscorditAuthorityUtils {

  @Value("${role.admin.address}")
  private String adminMailAddress;

  private final List<String> ADMIN_ROLES_STRING = List.of("ADMIN","CHANNEL_MANAGER", "USER");
  private final List<String> CHANNEL_MANAGER_ROLES_STRING = List.of("CHANNEL_MANAGER", "USER");
  private final List<String> USER_ROLES_STRING = List.of("USER");

  public List<String> createRoles(String email) {
    if (email.equals(adminMailAddress)) {
      return ADMIN_ROLES_STRING;
    }
    return USER_ROLES_STRING;
  }

  public List<GrantedAuthority> createAuthorities(List<String> roles) {
  return roles.stream()
      .map(role -> new SimpleGrantedAuthority("ROLE_"+role))
      .collect(Collectors.toList());
  }
}
