package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "binary_contents")
@Getter
@NoArgsConstructor
public class BinaryContent extends BaseEntity {

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(nullable = false)
    private Long size;

    @Column(name = "content_type", nullable = false, length = 100)
    private String contentType;

    @Lob
    @Column(nullable = false)
    private byte[] bytes;


    public BinaryContent(String fileName, String contentType, long size, byte[] bytes) {
        Assert.hasText(fileName, "파일 이름이 없습니다!");
        Assert.isTrue(size > 0, "파일의 크기가 유효하지 않습니다!");
        Assert.notNull(bytes, "첨부할 파일이 없습니다!");
        Assert.isTrue(bytes.length > 0, "첨부할 파일이 비어있습니다!");

        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;
        this.bytes = bytes;
    }
}
