package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.UpdateUserRequestDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(method = RequestMethod.POST)
    public UserDto create(@Valid @RequestBody CreateUserRequestDto request){
        return userService.create(request);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{userId}")
    public UserDto findOne(@PathVariable UUID userId){
        return userService.find(userId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/findAll")
    public ResponseEntity<List<UserDto>> findAll(){
        return ResponseEntity.ok(userService.findAll());
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{userId}")
    public UserDto update(@PathVariable UUID userId, @RequestBody UpdateUserRequestDto request){
        return userService.update(userId, request);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{userId}")
    public void delete(@PathVariable UUID userId){
        userService.delete(userId);
    }

    // 값을 직접 수정하지 않고 서버에서 시간을 Instant()로 불러와서 Post 사용
    // 멱등성 고려
    @RequestMapping(method = RequestMethod.POST, value = "/{userId}/status")
    public UserStatusDto updateStatus(@PathVariable UUID userId){
        return userStatusService.updateByUserId(userId);
    }

}
