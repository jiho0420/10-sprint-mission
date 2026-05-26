package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.JwtRefreshResultDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.exception.auth.InvalidRefreshTokenException;
import com.sprint.mission.discodeit.security.JwtTokenProvider;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicAuthService implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Override
    public JwtRefreshResultDto refresh(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new InvalidRefreshTokenException("missing");
        }

        Map<String, Object> claims;
        try {
            claims = jwtTokenProvider.getClaims(refreshToken);
        } catch (RuntimeException e) {
            log.debug("리프레시 토큰 검증 실패: {}", e.getMessage());
            throw new InvalidRefreshTokenException("invalid");
        }

        UUID userId;
        try {
            userId = UUID.fromString((String) claims.get("sub"));
        } catch (RuntimeException e) {
            throw new InvalidRefreshTokenException("malformed-subject");
        }

        UserDto userDto = userService.find(userId);

        String subject = userDto.id().toString();
        Map<String, Object> newClaims = Map.of(
                "username", userDto.username(),
                "role", userDto.role().name()
        );
        String newAccessToken = jwtTokenProvider.generateAccessToken(newClaims, subject);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(subject);

        return new JwtRefreshResultDto(userDto, newAccessToken, newRefreshToken);
    }
}
