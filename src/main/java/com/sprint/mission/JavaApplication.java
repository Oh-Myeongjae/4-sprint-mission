package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;

public class JavaApplication {

    public static void main(String[] args) {
        // 하나의 repository 인스턴스를 생성하여 재사용
        UserRepository userRepository = new FileUserRepository();
        ChannelRepository channelRepository = new FileChannelRepository();
        MessageRepository messageRepository = new FileMessageRepository();

        // 서비스 클래스에 repository 주입
        JCFUserRepository userService = new JCFUserRepository(userRepository, channelRepository);
        JCFChannelRepository channelService = new JCFChannelRepository(userRepository, channelRepository);
        JCFMessageRepository messageService = new JCFMessageRepository(userRepository, channelRepository, messageRepository);

        // 테스트 실행
        userTest(userService);
        channelTest(channelService, userService);
        messageTest(messageService, channelService, userService);
    }

    static void userTest(JCFUserRepository userService) {
        System.out.println("\n===== 사용자 테스트 시작 =====");

        // 사용자 생성
        User user1 = userService.createUser("유저1");
        User user2 = userService.createUser("유저2");

        System.out.println("모든 사용자 목록: " + userService.getAllUsers());

        // 사용자 단건 조회
        System.out.println("유저1 조회: " + userService.getUser(user1));
        System.out.println("유저2 조회: " + userService.getUser(user2));

        // 사용자 삭제
        userService.deleteUser(user1);
        System.out.println("모든 사용자 목록 (삭제 후): " + userService.getAllUsers());

        System.out.println("===== 사용자 테스트 종료 =====\n");
    }

    static void channelTest(JCFChannelRepository channelService, JCFUserRepository userService) {
        System.out.println("\n===== 채널 테스트 시작 =====");

        // 사용자 생성
        User user1 = userService.createUser("유저3");
        User user2 = userService.createUser("유저4");

        // 채널 생성
        Channel channel1 = channelService.createChannel(user1, "채널1");
        Channel channel2 = channelService.createChannel(user2, "채널2");

        System.out.println("모든 채널 목록: " + channelService.getAllChannel());

        // 채널 단건 조회
        System.out.println("채널1 채널 조회: " + channelService.getChannel(channel1));
        System.out.println("채널2 채널 조회: " + channelService.getChannel(channel2));

        // 채널 입장
        channelService.enterChannel(user2, channel1);
        System.out.println("채널1 채널의 사용자 목록: " + channelService.getUsers(channel1));

        // 채널 삭제
        channelService.deleteChannel(channel2);
        System.out.println("모든 채널 목록 (삭제 후): " + channelService.getAllChannel());

        System.out.println("===== 채널 테스트 종료 =====\n");
    }

    static void messageTest(JCFMessageRepository messageService, JCFChannelRepository channelService, JCFUserRepository userService) {
        System.out.println("\n===== 메시지 테스트 시작 =====");

        // 사용자 및 채널 생성
        User user1 = userService.createUser("유저5");
        Channel channel1 = channelService.createChannel(user1, "채널3");

        // 메시지 생성
        Message message1 = messageService.create(channel1, "안녕하세요!", user1);
        Message message2 = messageService.create(channel1, "반갑습니다!", user1);

        System.out.println("채널3의 메시지 목록: " + channelService.getMessages(channel1));

        // 메시지 단건 조회
        System.out.println("메시지 1 조회: " + messageService.getMessage(message1));
        System.out.println("메시지 2 조회: " + messageService.getMessage(message2));

        // 메시지 다건 조회
        System.out.println("모든 메시지 목록: " + messageService.getAllMessages());

        // 메시지 수정
        messageService.update(message1, "안녕하세요! 수정된 메시지입니다.");
        System.out.println("수정된 메시지: " + messageService.getMessage(message1));

        // 메시지 삭제
        messageService.delete(message2);
        System.out.println("채널3의 메시지 목록 (삭제 후): " + channelService.getMessages(channel1));

        System.out.println("===== 메시지 테스트 종료 =====\n");
    }
}

