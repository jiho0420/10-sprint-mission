package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {
    private final List<Message> messageList;
    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(UserService userService, ChannelService channelService){
        this.messageList = new ArrayList<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    public Message createMessage(String content, UUID channelId, UUID userId){
        Channel channel = channelService.findChannelById(channelId);
        User user = userService.findUserById(userId);

        Message newMessage = new Message(content, channel, user);
        messageList.add(newMessage);

        return newMessage;
    }

    public List<Message> findMessagesByChannelId(UUID channelId){
        return messageList.stream()
                .filter(message -> message.getChannel().getId().equals(channelId))
                .collect(Collectors.toList());
    }

    public List<Message> findAllMessages(){
        return messageList;
    }

    public Message findMessageById(UUID id){
        return messageList.stream()
                .filter(msg -> msg.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 메시지 없음"));
    }

    public Message updateMessage(UUID id, String newContent){
        Message targetMessage = findMessageById(id);

        targetMessage.updateContent(newContent);
        System.out.println("메시지가 수정되었습니다");

        return targetMessage;
    }

    public void deleteMessage(UUID id){
        Message targetMessage = findMessageById(id);
        messageList.remove(targetMessage);
        System.out.println("메시지 삭제 완료: " + targetMessage.getContent());
    }

}
