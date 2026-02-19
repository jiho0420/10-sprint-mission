package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
@Schema(name = "PrivateChannelCreateRequest")
@Getter
@AllArgsConstructor
public class CreatePrivateChannelRequestDto {
//    private String name;
    private List<UUID> participantIds;
}
