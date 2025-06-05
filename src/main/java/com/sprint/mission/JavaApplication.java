package com.sprint.mission;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;

public class JavaApplication {

    public static void main(String[] args) {
        // 1. 사용자 테스트
        System.out.println("==== 사용자 테스트 ====");
        JCFUserService userService = new JCFUserService();

        // 사용자 생성
        User user1 = userService.createUser("유저 1");
        System.out.println("생성된 사용자: " + user1);
        System.out.println("닉네임: " + user1.getNickName());

        // 사용자 조회
        User retrievedUser = userService.getUser(user1);
        System.out.println("조회된 사용자: " + retrievedUser);

        // 닉네임 업데이트
        userService.updateNickname(user1, "업데이트 유저 1");
        System.out.println("업데이트된 닉네임: " + user1.getNickName());

        // 전체 사용자 목록 출력
        System.out.println("전체 사용자 목록: " + userService.getAllUsers());

        // 사용자 삭제 및 삭제 확인 테스트
        userService.deleteUser(user1);
        System.out.println("전체 사용자 목록: " + userService.getAllUsers());
        System.out.println();

        // 2. 채널 서비스 테스트
        System.out.println("==== 채널 서비스 테스트 ====");
        JCFChannelService channelService = new JCFChannelService();

        // 채널 생성
        Channel channel1 = channelService.createChannel("채널 1");
        System.out.println("생성된 채널: " + channel1);
        System.out.println("채널 이름: " + channel1.getName());

        // 채널 조회
        Channel retrievedChannel = channelService.getChannel(channel1);
        System.out.println("조회된 채널: " + retrievedChannel);

        // 채널 이름 업데이트
        channelService.updateChannel(channel1, "업데이트 채널 1");
        System.out.println("업데이트된 채널 이름: " + channel1.getName());

        // 사용자 채널 입장 & 탈장 테스트
        User user2 = userService.createUser("유저 2");
        channelService.enterChannel(user2, channel1);
        System.out.println("채널에 입장한 사용자 목록: " + channelService.getUsers(channel1));

        channelService.leaveChannel(user2, channel1);
        System.out.println("채널에 입장한 사용자 목록: " + channelService.getUsers(channel1));

        // 채널 삭제 및 삭제 확인 테스트
        channelService.deleteChannel(channel1);
        System.out.println("전체 채널 목록: " + channelService.getAllChannel());
        System.out.println();

        // 3. 메시지 서비스 테스트
        System.out.println("==== 메시지 서비스 테스트 ====");
        JCFMessageService messageService = new JCFMessageService();

        // 메시지 테스트용 채널 생성
        Channel channel2 = channelService.createChannel("채널 2");

        // 사용자가 채널에 입장
        channelService.enterChannel(user2, channel2);

        // 메시지 생성
        Message message1 = messageService.create(channel2, "첫 메시지 생성", user2);
        System.out.println("생성된 메시지: " + message1);
        System.out.println("메시지 내용: " + message1.getContent());
        System.out.println("메시지 발신자: " + message1.getSender());
        System.out.println("메시지가 속한 채널: " + message1.getChannel());

        // 메시지 업데이트
        messageService.update(message1, "첫 메세지 수정");
        System.out.println("업데이트된 메시지 내용: " + message1.getContent());

        // 메시지 삭제 및 삭제 확인 테스트
        messageService.delete(message1);
        System.out.println("채널에 속한 메시지 목록: " + channelService.getMessages(channel2));
    }
}
