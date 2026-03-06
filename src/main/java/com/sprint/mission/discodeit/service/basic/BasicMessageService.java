package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.MessageSentEvent;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final MessageMapper messageMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public MessageDto create(CreateMessageRequestDto request, List<BinaryContentDto> attachments) {

        Channel channel = channelRepository.findById(request.getChannelId())
                .orElseThrow(() -> new NoSuchElementException("Channel not found with id " + request.getChannelId()));
        User author = userRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new NoSuchElementException("Author not found with id " + request.getAuthorId()));

        Message message = new Message(request.getContent(), channel, author);

        // Message 객체에 BinaryContent 엔티티를 연결
        if (attachments != null && !attachments.isEmpty()) {
            for (BinaryContentDto dto : attachments) {
                BinaryContent content = new BinaryContent(
                        dto.getFileName(),
                        dto.getContentType(),
                        dto.getSize(),
                        dto.getBytes()
                );
                binaryContentRepository.save(content);
                message.addAttachment(content); // 직접 객체 연결
            }
        }
        // 메시지 생성 및 저장
        messageRepository.save(message);
        // 메시지 생성 시 그 유저는 활동중임을 나타냄
        eventPublisher.publishEvent(new MessageSentEvent(request.getAuthorId()));
        return messageMapper.toDto(message);
    }

    @Override
    public MessageDto find(UUID messageId) {
        Message message = getMessageEntity(messageId);
        return messageMapper.toDto(message);
    }

    @Override
    public List<MessageDto> findAllByChannelId(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel not found with id " + channelId);
        }


        return messageRepository.findAllByChannelId(channelId).stream()
                .map(messageMapper::toDto)
                .toList();
    }

    @Override
    public MessageDto update(UUID messageId, UpdateMessageRequestDto request) {
        Message message = getMessageEntity(messageId);

        message.update(request.getNewContent());
        messageRepository.save(message);

        return messageMapper.toDto(message);
    }

    @Override
    public void delete(UUID messageId) {
        Message message = getMessageEntity(messageId);

        // 연관된 첨부파일 삭제 (내부 메서드)
        deleteAttachedFiles(message);

        messageRepository.deleteById(messageId);
    }

    //  ------ 내부 메서드 -------

    // 메시지 엔티티 조회
    private Message getMessageEntity(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
    }


    // 첨부파일 저장 후 id로 반환
    private List<UUID> processAttachments(List<BinaryContentDto> attachments) {
        if (attachments == null || attachments.isEmpty()) {
            return new ArrayList<>();
        }

        List<UUID> attachmentIds = new ArrayList<>();
        for (BinaryContentDto dto : attachments) {
            BinaryContent content = new BinaryContent(
                    dto.getFileName(),
                    dto.getContentType(),
                    dto.getSize(),
                    dto.getBytes()
            );
            binaryContentRepository.save(content);
            attachmentIds.add(content.getId());
        }
        return attachmentIds;
    }

    // 메시지에 포함된 첨부파일 삭제
    private void deleteAttachedFiles(Message message) {
        List<BinaryContent> attachments = message.getAttachments();
        if (attachments != null && !attachments.isEmpty()) {
            for (BinaryContent file : attachments) {
                binaryContentRepository.deleteById(file.getId());
            }
        }
    }
}
