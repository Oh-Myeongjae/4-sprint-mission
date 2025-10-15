package com.sprint.mission.discodeit.config.security;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscodeitUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final UserOnlineChecker userOnlineChecker;
  private final DiscorditAuthorityUtils discorditAuthorityUtils;


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> findMember = userRepository.findByEmail(username);
    User user = findMember.orElseThrow(()-> new DiscodeitException(ErrorCode.USER_NOT_FOUND));

    UserDto userDto = userMapper.toDto(user);

    return new DiscodeitUserDetails(userOnlineChecker.withOnlineStatus(userDto), user.getPassword(),discorditAuthorityUtils);
  }
}

