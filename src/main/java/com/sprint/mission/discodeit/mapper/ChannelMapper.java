package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class ChannelMapper {

    @Mapping(source = "lastMessageAt", target = "lastMessageAt")
    @Mapping(source = "participantIds", target = "participantIds")
    public abstract ChannelDto toDto(Channel channel, Instant lastMessageAt, List<UUID> participantIds);
}