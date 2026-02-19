package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import java.time.Instant;
import java.util.UUID;
@Schema(name = "BinaryContent")
@Getter
public class BinaryContentDto {
    private UUID id;
    private Instant createdAt;

    @NotBlank(message = "파일 이름이 존재하지 않습니다.")
    private String fileName;

    @NotBlank(message = "파일 타입은 필수입니다.")
    private String contentType;

    @Min(value = 1, message = "파일 크기는 0보다 커야 합니다.")
    private long size;

    @NotEmpty(message = "파일 내용이 비어있습니다.")
    private byte[] bytes;

    public BinaryContentDto(UUID id, Instant createdAt, String fileName, String contentType, long size, byte[] bytes) {
        this.id = id;
        this.createdAt = createdAt;
        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;
        this.bytes = bytes;
    }

    public BinaryContentDto(String fileName, String contentType, long size, byte[] bytes) {
        this(null, null, fileName, contentType, size, bytes);
    }
}
