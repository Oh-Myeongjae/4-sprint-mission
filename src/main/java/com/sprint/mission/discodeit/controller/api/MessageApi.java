package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Message")
public interface MessageApi {

  @Operation(summary = "Message 생성")
  @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
      encoding = @Encoding(name = "messageCreateRequest", contentType = MediaType.APPLICATION_JSON_VALUE)))
  @ApiResponse(responseCode = "201", description = "Message가 성공적으로 생성됨")
  @ApiResponse(responseCode = "404", description = "Channel 또는 User를 찾을 수 없음",
      content = @Content(examples = @ExampleObject(value = "Channel | Author with id {channelId | authorId} not found")))
  public ResponseEntity<Message> create(
      @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @Schema(description = "Message 첨부 파일들") @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  );

  @Operation(summary = "Message 내용 수정")
  @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
      encoding = @Encoding(name = "messageCreateRequest", contentType = MediaType.APPLICATION_JSON_VALUE)))
  @ApiResponse(responseCode = "200", description = "Message가 성공적으로 수정됨")
  @ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음",
      content = @Content(examples = @ExampleObject(value = "Message with id {messageId} not found")))
  @Parameter(name = "messageId", description = "수정할 Message ID")
  public ResponseEntity<Message> update(@PathVariable("messageId") UUID messageId,
      @RequestBody MessageUpdateRequest messageUpdateRequest);

  @Operation(summary = "Message 삭제")
  @ApiResponse(responseCode = "204", description = "Message가 성공적으로 삭제됨")
  @ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음",
      content = @Content(examples = @ExampleObject(value = "Message with id {messageId} not found")))
  @Parameter(name = "messageId", description = "삭제할 Message ID")
  public ResponseEntity<Void> delete(@PathVariable("messageId") UUID messageId);

  @Operation(summary = "Channel의 Message 목록 조회", operationId = "findAllByChannelId")
  @ApiResponse(responseCode = "200", description = "Message 목록 조회 성공")
  @Parameter(name = "channelId", description = "조회할 Channel ID")
  public ResponseEntity<List<Message>> findAllByChannelId(@RequestParam UUID channelId);
}
