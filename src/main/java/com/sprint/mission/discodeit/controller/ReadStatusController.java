package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ReadStatusApi;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/readStatuses")
public class ReadStatusController implements ReadStatusApi {

  private final ReadStatusService readStatusService;
  private final ReadStatusMapper readStatusMapper;

  @PostMapping
  public ResponseEntity<ReadStatusDto> create(@RequestBody ReadStatusCreateRequest request) {
    ReadStatus createdReadStatus = readStatusService.create(request);
    ReadStatusDto readStatusDto = readStatusMapper.toDto(createdReadStatus);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(readStatusDto);
  }

  @PatchMapping(path = "{readStatusId}")
  public ResponseEntity<ReadStatusDto> update(@PathVariable("readStatusId") UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest request) {
    ReadStatus updatedReadStatus = readStatusService.update(readStatusId, request);
    ReadStatusDto readStatusDto = readStatusMapper.toDto(updatedReadStatus);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatusDto);
  }

  @GetMapping
  public ResponseEntity<List<ReadStatusDto>> findAllByUserId(@RequestParam("userId") UUID userId) {
    List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
    List<ReadStatusDto> readStatusDtos = readStatuses.stream().map(readStatusMapper::toDto).toList();

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatusDtos);
  }
}
