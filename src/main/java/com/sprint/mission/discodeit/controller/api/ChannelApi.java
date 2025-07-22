package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Channel")
public interface ChannelApi {

  @Operation(summary = "Public Channel 생성")
  @ApiResponse(responseCode = "201", description = "Public Channel이 성공적으로 생성됨")
  public ResponseEntity<Channel> create(
      @RequestBody(required = true) PublicChannelCreateRequest request);

  @Operation(summary = "Private Channel 생성")
  @ApiResponse(responseCode = "201", description = "Private Channel이 성공적으로 생성됨")
  public ResponseEntity<Channel> create(
      @RequestBody(required = true) PrivateChannelCreateRequest request);

  @Operation(summary = "Channel 정보 수정")
  @ApiResponse(responseCode = "200", description = "Channel 정보가 성공적으로 수정됨")
  @ApiResponse(responseCode = "400", description = "Private Channel은 수정할 수 없음",
      content = @Content(examples = @ExampleObject(value = "Private channel cannot be updated")))
  @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음",
      content = @Content(examples = @ExampleObject(value = "Channel with id {channelId} not found")))
  @Parameter(name = "channelId", description = "수정할 Channel ID")
  public ResponseEntity<Channel> update(@PathVariable("channelId") UUID channelId,
      @RequestBody(required = true) PublicChannelUpdateRequest request);

  @Operation(summary = "Channel 삭제")
  @ApiResponse(responseCode = "204", description = "Channel이 성공적으로 삭제됨")
  @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음",
      content = @Content(examples = @ExampleObject(value = "Channel with id {channelId} not found")))
  @Parameter(name = "channelId", description = "삭제할 Channel ID")
  public ResponseEntity<Void> delete(@PathVariable("channelId") UUID channelId);

  @Operation(summary = "User가 참여 중인 Channel 목록 조회", operationId = "findAll_1")
  @ApiResponse(responseCode = "200", description = "Channel 목록 조회 성공")
  @Parameter(name = "userId", description = "조회할 User ID")
  public ResponseEntity<List<ChannelDto>> findAll(@RequestParam UUID userId);
}