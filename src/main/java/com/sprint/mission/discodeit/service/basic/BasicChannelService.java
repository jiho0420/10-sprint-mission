package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ChannelMapper channelMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public ChannelDto createPublic(CreatePublicChannelRequestDto request) {
        Channel channel = new Channel(ChannelType.PUBLIC, request.name(), request.description());
        Channel savedChannel = channelRepository.save(channel);
        return channelMapper.toDto(savedChannel);
    }

    @Override
    @Transactional
    public ChannelDto createPrivate(CreatePrivateChannelRequestDto request) {
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
        return channelMapper.toDto(channel);
    }

    @Override
    public ChannelDto find(UUID channelId) {
        Channel channel = getChannelEntity(channelId);
        return channelMapper.toDto(channel);
    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<Channel> channels = channelRepository.findAccessibleChannelsByUserId(userId);
        return mapChannelsToDtos(channels);
    }

    @Override
    public List<ChannelDto> findAll() {
        List<Channel> channels = channelRepository.findAll();
        return mapChannelsToDtos(channels);
    }

    @Override
    @Transactional
    public ChannelDto update(UUID channelId, UpdateChannelRequestDto request) {
        Channel channel = getChannelEntity(channelId);

        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalArgumentException("비공개 채널은 수정할 수 없습니다.");
        }
        channel.update(request.newName(), request.newDescription());

        return channelMapper.toDto(channel);
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

    private List<ChannelDto> mapChannelsToDtos(List<Channel> channels) {
        if (channels.isEmpty()) {
            return Collections.emptyList();
        }

        List<UUID> channelIds = channels.stream().map(Channel::getId).toList();

        List<ReadStatus> readStatuses = readStatusRepository.findAllByChannelIdIn(channelIds);
        Map<UUID, List<UserDto>> participantsMap = readStatuses.stream()
                .collect(Collectors.groupingBy(
                        rs -> rs.getChannel().getId(),
                        Collectors.mapping(rs -> userMapper.toDto(rs.getUser()), Collectors.toList())
                ));

        Map<UUID, Instant> lastMessageMap = new HashMap<>();
        for (UUID cid : channelIds) {
            messageRepository.findTopByChannelIdOrderByCreatedAtDesc(cid)
                    .ifPresent(m -> lastMessageMap.put(cid, m.getCreatedAt()));
        }

        return channels.stream()
                .map(channel -> channelMapper.toDtoWithContext(channel, lastMessageMap, participantsMap))
                .toList();
    }


}
