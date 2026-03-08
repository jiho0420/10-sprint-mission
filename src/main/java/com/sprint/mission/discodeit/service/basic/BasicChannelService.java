package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.CreatePrivateChannelRequestDto;
import com.sprint.mission.discodeit.dto.CreatePublicChannelRequestDto;
import com.sprint.mission.discodeit.dto.UpdateChannelRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ChannelMapper channelMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Channel createPublic(CreatePublicChannelRequestDto request) {
        Channel channel = new Channel(ChannelType.PUBLIC, request.name(), request.description());
        return channelRepository.save(channel);
    }

    @Override
    @Transactional
    public Channel createPrivate(CreatePrivateChannelRequestDto request) {
        // 명세에 name이 없으므로 서버에서 기본 이름 자동 생성
        String defaultName = "Private";
        Channel channel = new Channel(ChannelType.PRIVATE, defaultName, null);
        channelRepository.save(channel);

        if (request.participantIds() != null) {
            for (UUID userId : request.participantIds()) {
                User user = userRepository.findById(userId).orElseThrow();
                ReadStatus status = new ReadStatus(user, channel);
                readStatusRepository.save(status);
            }
        }
        return channel;
    }

    @Override
    public ChannelDto find(UUID channelId) {
        Channel channel = getChannelEntity(channelId);
        return mapToDtoWithDependencies(channel);
    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        return channelRepository.findAll().stream()
                .filter(channel -> {
                    if (channel.getType() == ChannelType.PUBLIC) {
                        return true;
                    }
                    // private인 경우 내 ReadStatus가 있는지 확인
                    return readStatusRepository.findAllByUserId(userId).stream()
                            .anyMatch(rs -> rs.getChannel()
                                    .getId().equals(channel.getId()) && rs.getUser().getId().equals(userId));
                })
                .map(this::mapToDtoWithDependencies)
                .toList();
    }

    @Override
    public List<ChannelDto> findAll() {
        return channelRepository.findAll().stream()
                .map(this::mapToDtoWithDependencies)
                .toList();
    }

    @Override
    @Transactional
    public Channel update(UUID channelId, UpdateChannelRequestDto request) {
        Channel channel = getChannelEntity(channelId);

        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalArgumentException("비공개 채널은 수정할 수 없습니다.");
        }
        channel.update(request.newName(), request.newDescription());

        return channel;
    }

    @Override
    @Transactional
    public void delete(UUID channelId) {
        getChannelEntity(channelId);
        readStatusRepository.deleteAllByChannelId(channelId);
        messageRepository.deleteAllByChannelId(channelId);
        channelRepository.deleteById(channelId);
    }

    // 채널 엔티티 조회
    private Channel getChannelEntity(UUID channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel not found with id " + channelId));
    }

    // ChannelMapper 명세에 맞춰 의존 데이터(lastMessageAt, participantIds)를 조회해 전달
    private ChannelDto mapToDtoWithDependencies(Channel channel) {
        List<Message> messages = messageRepository.findAllByChannelId(channel.getId());
        Instant lastMessageAt = messages.stream()
                .map(Message::getCreatedAt)
                .max(Instant::compareTo)
                .orElse(null);

        List<UUID> participantIds = readStatusRepository.findAll().stream()
                .filter(rs -> rs.getChannel().getId().equals(channel.getId()))
                .map(rs -> rs.getUser().getId())
                .toList();

        return channelMapper.toDto(channel, lastMessageAt, participantIds);
    }

}
