package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat; // 이게 핵심 검증 도구!

@SpringBootTest // 스프링 컨테이너를 실제로 띄워서 테스트함 (JCF/File 설정 다 먹힘)
class DiscodeitApplicationTests {

    @Autowired UserService userService;
    @Autowired ChannelService channelService;
    @Autowired MessageService messageService;

    @Test
    @DisplayName("유저가 채널을 만들고 메시지를 보내면, 데이터가 정상적으로 조회되어야 한다.")
    void logicTest() {
        // 1. Given (준비)
        BinaryContentDto profileImg = new BinaryContentDto("face.png", "image/png", 100, "data".getBytes());
        CreateUserRequest userReq = new CreateUserRequest("woody", "test@test.com", "1234", profileImg);

        // 2. When (실행)
        UserDto createdUser = userService.create(userReq);
        UserDto foundUser = userService.findById(createdUser.getId()).orElseThrow();

        // 3. Then (검증 - 여기가 println 대신 들어가는 곳)
        // "찾아낸 유저의 이름은 woody여야만 해!" 라고 단언(Assert)합니다.
        assertThat(foundUser.getUsername()).isEqualTo("woody");
        assertThat(foundUser.getProfileImageId()).isNotNull();

        System.out.println("✅ 유저 검증 통과!"); // (없어도 되지만 확인용)

        // --- 채널 및 메시지 테스트 이어가기 ---
        CreatePrivateChannelRequestDto channelReq = new CreatePrivateChannelRequestDto(List.of(createdUser.getId()));
        ChannelDto channel = channelService.createPrivate(channelReq);

        assertThat(channel.getType()).isEqualTo(com.sprint.mission.discodeit.entity.ChannelType.PRIVATE);
        System.out.println("✅ 채널 검증 통과!");
    }
}