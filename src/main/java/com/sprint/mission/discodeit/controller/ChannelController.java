package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.CreatePrivateChannelRequestDto;
import com.sprint.mission.discodeit.dto.CreatePublicChannelRequestDto;
import com.sprint.mission.discodeit.dto.UpdateChannelRequestDto;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping(method = RequestMethod.POST, value = "/public")
        @ResponseStatus(HttpStatus.CREATED)
        public ChannelDto createPublic(@Valid @RequestBody CreatePublicChannelRequestDto request){
            return channelService.createPublic(request);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/private")
        @ResponseStatus(HttpStatus.CREATED)
        public ChannelDto createPrivate(@Valid @RequestBody CreatePrivateChannelRequestDto request){
            return channelService.createPrivate(request);
    }

    // 이미 방어 로직 구현되어있음
    @RequestMapping(method = RequestMethod.PATCH, value = "/{channelId}")
    public ResponseEntity<ChannelDto> update(@PathVariable UUID channelId,
                             @Valid @RequestBody UpdateChannelRequestDto request){
        return ResponseEntity.ok(channelService.update(channelId, request));
    }

        @RequestMapping(method = RequestMethod.DELETE, value = "/{channelId}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void delete(@PathVariable UUID channelId){
        channelService.delete(channelId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ChannelDto>> findAllByUser(@RequestParam UUID userId){
        return ResponseEntity.ok(channelService.findAllByUserId(userId));
    }
}
