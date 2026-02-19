package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

        @RequestMapping(method = RequestMethod.POST,
                consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
        @ResponseStatus(HttpStatus.CREATED)
        public MessageDto send(
            @RequestPart("messageCreateRequest") @Valid CreateMessageRequestDto request,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) throws IOException {

        List<BinaryContentDto> attachmentDtos = new ArrayList<>();
        if(attachments != null && !attachments.isEmpty()){
            for(MultipartFile file : attachments){
                attachmentDtos.add(new BinaryContentDto(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getSize(),
                        file.getBytes()
                ));
            }
        }

            return messageService.create(request, attachmentDtos);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{messageId}")
    public ResponseEntity<MessageDto> update(@PathVariable UUID messageId, @Valid @RequestBody UpdateMessageRequestDto request){
        return ResponseEntity.ok(messageService.update(messageId, request));
    }

        @RequestMapping(method = RequestMethod.DELETE, value = "/{messageId}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void delete(@PathVariable UUID messageId){
        messageService.delete(messageId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<MessageDto>> findAllByChannel(@RequestParam UUID channelId){
        return ResponseEntity.ok(messageService.findAllByChannelId(channelId));
    }
}