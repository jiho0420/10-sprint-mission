package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        System.out.println("========== 스프링 부트 기반 테스트 시작 ==========\n");
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);
        messageCreateTest(messageService, channel, user);
    }

    static User setupUser(UserService userService) {
        return userService.createUser("woody", "woody1234", "woody@codeit.com");
    }

    static Channel setupChannel(ChannelService channelService) {
        return channelService.createChannel("공지");
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User user) {
        Message message = messageService.createMessage("안녕하세요.", channel.getId(), user.getId());
        System.out.println("메시지 생성 확인: " + message.getContent());
    }
}