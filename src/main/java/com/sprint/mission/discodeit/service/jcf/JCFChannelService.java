package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final List<Channel> channelList;

    public JCFChannelService(){
        this.channelList = new ArrayList<>();
    }

    @Override
    public Channel createChannel(String channelName) {
        Channel channel = new Channel(channelName);
        channelList.add(channel);
        System.out.println(channel.getChannelName() + "채널 생성이 완료되었습니다.");
        return channel;
    }

    @Override
    public Channel findChannelById(UUID id){
        return channelList.stream()
                .filter(channel -> channel.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 채널 없음"));
    }

    @Override
    public List<Channel> findAllChannels(){
        return channelList;
    }

    @Override
    public void deleteChannel(UUID id){ // 예외 처리가 구현되어있는 findChannelById(id) 사용
        Channel targetChannel = findChannelById(id);
        channelList.remove(targetChannel);
    }

    public Channel updateChannel(UUID id, String channelName){
        Channel targetChannel = findChannelById(id); // 예외 처리가 구현되어있는 findChannelById(id) 사용
        targetChannel.updateChannelInfo(channelName);
        return targetChannel;
    }
}
