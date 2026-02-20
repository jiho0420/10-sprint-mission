package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.LoginRequestDto;
import com.sprint.mission.discodeit.entity.User;

@FunctionalInterface
public interface AuthService {
    User login(LoginRequestDto request);
}
