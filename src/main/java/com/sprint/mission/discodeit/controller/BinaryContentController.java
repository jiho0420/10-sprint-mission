package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "BinaryContent")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContents")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @RequestMapping(method = RequestMethod.GET,value = "/{binaryContentId}")
    public ResponseEntity<BinaryContentDto> find(@PathVariable UUID binaryContentId){
        return ResponseEntity.ok(binaryContentService.find(binaryContentId));
    }


    // 여러 개 조회
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContentDto>> findAll(@RequestParam("binaryContentIds") List<UUID> binaryContentIds){
        return ResponseEntity.ok(binaryContentService.findAllByIdIn(binaryContentIds));
    }

}
