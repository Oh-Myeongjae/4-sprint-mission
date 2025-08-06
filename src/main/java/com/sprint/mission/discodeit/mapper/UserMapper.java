package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = { BinaryContentMapper.class })
public interface UserMapper {

  @Mapping(source = "status", target = "online", qualifiedByName = "mapperIsOnline")
  UserDto toDto(User user);

  @Named("mapperIsOnline")
  default boolean isOnline(UserStatus userStatus) {
    return userStatus.isOnline();
  }
}
