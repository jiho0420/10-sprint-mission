package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class ChannelMapper {

    @Autowired
    protected MessageRepository messageRepository;

    @Autowired
    protected ReadStatusRepository readStatusRepository;

    @Mapping(target = "lastMessageAt", expression = "java(getLastMessageAt(channel))")
    @Mapping(target = "participantIds", expression = "java(getParticipantIds(channel))")
    public abstract ChannelDto toDto(Channel channel);

    protected Instant getLastMessageAt(Channel channel) {
        return messageRepository.findTopByChannelIdOrderByCreatedAtDesc(channel.getId())
                .map(Message::getCreatedAt)
                .orElse(null);
    }

    protected List<UUID> getParticipantIds(Channel channel) {
        return readStatusRepository.findAll().stream()
                .filter(rs -> rs.getChannel().getId().equals(channel.getId()))
                .map(rs -> rs.getUser().getId())
                .toList();
    }
}