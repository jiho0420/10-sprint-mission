package com.sprint.mission.discodeit.listener;

import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketRequiredEventListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public void handleMessage(MessageCreatedEvent event) {
        messageRepository.findWithDetailsById(event.messageId())
                .map(messageMapper::toDto)
                .ifPresentOrElse(
                        dto -> messagingTemplate.convertAndSend(
                                "/sub/channels." + event.channelId() + ".messages", dto),
                        () -> log.warn("WS 전송 대상 메시지를 찾을 수 없음: messageId={}", event.messageId()));
    }
}
