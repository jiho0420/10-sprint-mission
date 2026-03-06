package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class MessageMapper {

    @Mapping(source = "message.channel.id", target = "channelId")
    @Mapping(source = "authorDto", target = "author")
    @Mapping(source = "attachmentDtos", target = "attachments")
    public abstract MessageDto toDto(Message message, UserDto authorDto, List<BinaryContentDto> attachmentDtos);

}