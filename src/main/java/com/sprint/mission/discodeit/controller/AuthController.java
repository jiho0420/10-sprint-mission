package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Auth")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @RequestMapping(method = RequestMethod.GET, value = "/csrf-token")
    public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
        log.debug("CSRF 토큰 요청: {}", csrfToken.getToken());
        return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/me")
    public ResponseEntity<UserDto> getMe(@AuthenticationPrincipal DiscodeitUserDetails userDetails) {
        return ResponseEntity.ok(userDetails.getUserDto());
    }
}
