package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateDto;
import com.sprint.mission.discodeit.dto.PublicChannelCreateDto;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelResponseDto create(ChannelType type, String name, String description);
    ChannelResponseDto createPublicChannel(PublicChannelCreateDto publicChannelCreateDto);
    ChannelResponseDto createPrivateChannel(PrivateChannelCreateDto publicChannelCreateDto);
    ChannelResponseDto find(UUID channelId);
    List<ChannelResponseDto> findAllByUserId(UUID userId);
    ChannelResponseDto update(ChannelUpdateRequestDto channelUpdateRequestDto);
    void delete(UUID channelId);
}
