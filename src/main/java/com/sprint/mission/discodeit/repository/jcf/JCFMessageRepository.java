package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.NoSuchElementException;

public class JCFMessageRepository implements MessageService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;

    public JCFMessageRepository(UserRepository userRepository, ChannelRepository channelRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public Message create(Channel channel, String message, User sender) {
        Message newMessage = new Message(channel, message, sender);
        channel.addMessage(newMessage);
        sender.addMessage(newMessage);

        messageRepository.setData(newMessage);
        channelRepository.setData(channel);
        userRepository.setData(sender);

        return newMessage;
    }

    @Override
    public Message getMessage(Message message) {
        validationMessage(message);
        return messageRepository.getMessage(message);
    }

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.getAllMessages();
    }

    @Override
    public Message update(Message message, String content) {
        validationMessage(message);

        Message updateMessage = messageRepository.getMessage(message);
        updateMessage.updateMessage(content);

        messageRepository.setData(updateMessage);
        channelRepository.setData(updateMessage.getChannel());
        userRepository.setData(updateMessage.getSender());

        return updateMessage;
    }

    @Override
    public void delete(Message message) {
        validationMessage(message);

        message.getChannel().removeMessage(message);
        message.getSender().removeMessage(message);

        messageRepository.removeData(message);
        channelRepository.setData(message.getChannel());
        userRepository.setData(message.getSender());
    }

    @Override
    public void validationMessage(Message message) {
        if (messageRepository.getMessage(message) == null) {
            throw new NoSuchElementException("유효하지 않은 메세지 입니다.");
        }
    }
}
