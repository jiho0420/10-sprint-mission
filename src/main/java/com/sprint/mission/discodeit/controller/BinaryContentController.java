package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContents")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @RequestMapping(method = RequestMethod.GET,value = "/{binaryContentId}")
    public ResponseEntity<BinaryContentDto> find(@PathVariable UUID binaryContentId){
        return ResponseEntity.ok(binaryContentService.find(binaryContentId));
    }

//    // 단건 조회(다운로드)는 api 명세에 나와있지 않음
//    @RequestMapping(method = RequestMethod.GET, value = "/{binaryContentId}")
//    public ResponseEntity<?> download(@PathVariable UUID binaryContentId){
//        // 단건 조회
//        BinaryContentDto binaryContentDto = binaryContentService.find(binaryContentId);
//        // 한글 깨짐 방지
//        String encodedFileName = new String(binaryContentDto.getFileName()
//                .getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
//        // 헤더 설정
//        HttpHeaders headers = new HttpHeaders();
//        // 파일 종류
//        headers.setContentType(MediaType.parseMediaType(binaryContentDto.getContentType()));
//        // 파일 크기
//        headers.setContentLength(binaryContentDto.getSize());
//        // Content-Disposition: 다운로드 시 저장될 파일 이름 설정
//        headers.setContentDispositionFormData("attachment", encodedFileName);
//
//        return new ResponseEntity<>(binaryContentDto.getBytes(), headers, HttpStatus.OK);
//
//    }

    // 여러 개 조회
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContentDto>> findAll(@RequestParam("binaryContentIds") List<UUID> binaryContentIds){
        return ResponseEntity.ok(binaryContentService.findAllByIdIn(binaryContentIds));
    }

}
