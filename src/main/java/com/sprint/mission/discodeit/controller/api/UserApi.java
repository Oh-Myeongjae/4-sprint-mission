package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User")
public interface UserApi {

  @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
      encoding = @Encoding(name = "userCreateRequest", contentType = MediaType.APPLICATION_JSON_VALUE)))
  @Operation(summary = "User 등록")
  @ApiResponse(responseCode = "201", description = "User가 성공적으로 생성됨")
  @ApiResponse(responseCode = "400", description = "같은 email 또는 username를 사용하는 User가 이미 존재함",
      content = @Content(examples = @ExampleObject(value = "User with email {email} already exists")))
  public ResponseEntity<User> create(
      @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
      @Schema(description = "User 프로필 이미지") @RequestPart(value = "profile", required = false) MultipartFile profile
  );

  @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
      encoding = @Encoding(name = "userUpdateRequest", contentType = MediaType.APPLICATION_JSON_VALUE)))
  @Operation(summary = "User 정보 수정")
  @ApiResponse(responseCode = "200", description = "User 정보가 성공적으로 수정됨")
  @ApiResponse(responseCode = "400", description = "같은 email 또는 username를 사용하는 User가 이미 존재함",
      content = @Content(examples = @ExampleObject(value = "user with email {newEmail} already exists")))
  @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음",
      content = @Content(examples = @ExampleObject(value = "User with id {userId} not found")))
  @Parameter(name = "userId", description = "수정할 User ID")
  public ResponseEntity<User> update(
      @PathVariable("userId") UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
      @Schema(description = "수정할 User 프로필 이미지") @RequestPart(value = "profile", required = false) MultipartFile profile
  );

  @Operation(summary = "User 삭제")
  @ApiResponse(responseCode = "204", description = "User가 성공적으로 삭제됨")
  @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음",
      content = @Content(examples = @ExampleObject(value = "User with id {id} not found")))
  @Parameter(name = "userId", description = "삭제할 User ID")
  public ResponseEntity<Void> delete(@PathVariable("userId") UUID userId);

  @Operation(summary = "전체 User 목록 조회")
  @ApiResponse(responseCode = "200", description = "User 목록 조회 성공")
  public ResponseEntity<List<UserDto>> findAll();

  @Operation(summary = "User 온라인 상태 업데이트")
  @ApiResponse(responseCode = "200", description = "User 온라인 상태가 성공적으로 업데이트됨")
  @ApiResponse(responseCode = "404", description = "해당 User의 UserStatus를 찾을 수 없음",
      content = @Content(examples = @ExampleObject(value = "UserStatus with userId {userId} not found")))
  @Parameter(name = "userId", description = "상태를 변경할 User ID")
  public ResponseEntity<UserStatus> updateUserStatusByUserId(@PathVariable("userId") UUID userId,
      @RequestBody UserStatusUpdateRequest request);
}
