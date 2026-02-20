package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(method = RequestMethod.POST,
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public User create(
            @RequestPart("userCreateRequest") @Valid CreateUserRequestDto request,
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) throws IOException {

        // 파일이 전송되었다면 DTO로 변환하여 request 객체에 주입
        if (profile != null && !profile.isEmpty()) {
            BinaryContentDto profileImage = new BinaryContentDto(
                    profile.getOriginalFilename(),
                    profile.getContentType(),
                    profile.getSize(),
                    profile.getBytes()
            );
            request.setProfileImage(profileImage);
        }

        return userService.create(request);
    }

//    // 단건 조회는 api 요구 스펙에 따라 주석 처리
//    @RequestMapping(method = RequestMethod.GET, value = "/{userId}")
//    public UserDto findOne(@PathVariable UUID userId){
//        return userService.find(userId);
//    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> findAll(){
        return ResponseEntity.ok(userService.findAll());
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{userId}",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<User> update(
            @PathVariable UUID userId,
            @Parameter(name = "userUpdateRequest", required = false) @RequestPart(value = "userUpdateRequest", required = false) UpdateUserRequestDto request,
            @Parameter(name = "profile", required = false) @RequestPart(value = "profile", required = false) MultipartFile profile
    ) throws IOException {
        if (request == null) {
            request = new UpdateUserRequestDto(null, null, null, null);
        }

        // 파일이 전송되었다면 DTO로 변환하여 request 객체의 newProfileImage에 주입
        if (profile != null && !profile.isEmpty()) {
            BinaryContentDto newProfileImage = new BinaryContentDto(
                    profile.getOriginalFilename(),
                    profile.getContentType(),
                    profile.getSize(),
                    profile.getBytes()
            );

            request.setNewProfileImage(newProfileImage);
        }
        return ResponseEntity.ok(userService.update(userId, request));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID userId){
        userService.delete(userId);
    }

    // 값을 직접 수정하지 않고 서버에서 시간을 Instant()로 불러와서 Post 사용
    // 멱등성 고려
    @RequestMapping(method = RequestMethod.PATCH, value = "/{userId}/userStatus")
    public ResponseEntity<UserStatusDto> updateStatus(@PathVariable UUID userId,
                                                      @RequestBody UpdateUserStatusRequestDto request){
        return ResponseEntity.ok(userStatusService.updateByUserId(userId, request));
    }

}
