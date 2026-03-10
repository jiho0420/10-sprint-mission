package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.UpdateUserRequestDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto create(CreateUserRequestDto request) {
        validateDuplicateUser(request.username(), request.email());

        User user = new User(request.username(), request.email(), request.password());
        uploadProfileImage(user, request.profileImage());

        userRepository.save(user);

        UserStatus status = new UserStatus(user);
        userStatusRepository.save(status);

        return userMapper.toDto(user);
    }

    @Override
    public Optional<UserDto> findById(UUID userId) {
        return userRepository.findById(userId)
                .map(userMapper::toDto);
    }

    @Override
    public UserDto find(UUID userId) {
        User user = getUserEntity(userId);
        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public UserDto update(UUID userId, UpdateUserRequestDto request) {
        User user = getUserEntity(userId);

        user.update(request.newUsername(), request.newEmail(), request.newPassword());

        if (request.newProfileImage() != null) {
            uploadProfileImage(user, request.newProfileImage());
        }

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void delete(UUID userId) {
        User user = getUserEntity(userId);
        userRepository.delete(user);
    }

    // --------- 내부 메서드 구현 ---------
    // 유저 엔티티 조회
    private User getUserEntity(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    }

    // 프로필 이미지 업로드 및 유저 정보 업데이트
    private void uploadProfileImage(User user, BinaryContentDto contentDto) {
        if (contentDto == null || contentDto.bytes() == null) return;

        BinaryContent content = new BinaryContent(
                contentDto.fileName(),
                contentDto.contentType(),
                contentDto.size()
        );
        content = binaryContentRepository.save(content);
        binaryContentStorage.put(content.getId(), contentDto.bytes());

        user.updateProfileImageId(content);
    }

    // 중복 가입 검증
    private void validateDuplicateUser(String username, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }
        boolean emailExists = userRepository.findAll().stream()
                .anyMatch(u -> u.getEmail().equals(email));
        if (emailExists) {
            throw new IllegalArgumentException("Email already exists: " + email);
        }
    }
}