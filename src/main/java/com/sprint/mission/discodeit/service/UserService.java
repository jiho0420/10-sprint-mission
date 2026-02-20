package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.UpdateUserRequestDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User create(CreateUserRequestDto request);
    Optional<UserDto> findById(UUID userId);
    UserDto find(UUID userId);
    List<UserDto> findAll();
    User update(UUID userId, UpdateUserRequestDto request);
    void delete(UUID userId);
}
