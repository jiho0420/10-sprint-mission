package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.UpdateReadStatusRequestDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readStatuses")
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ReadStatusDto create(@Valid @RequestBody CreateReadStatusRequestDto request){
        return readStatusService.create(request);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{readStatusId}")
    public ResponseEntity<ReadStatusDto> update(@PathVariable UUID readStatusId,
                                                @Valid @RequestBody UpdateReadStatusRequestDto request){
        return ResponseEntity.ok(readStatusService.update(readStatusId, request));
    }

//    // 단건 조회는 요구사항에 없어 주석처리
//    @RequestMapping(method = RequestMethod.GET, value = "/{readStatusId}")
//    public ReadStatusDto find(@PathVariable UUID readStatusId){
//        return readStatusService.find(readStatusId);
//    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatusDto>> findAllByUser(@RequestParam UUID userId){
        return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
    }
}
