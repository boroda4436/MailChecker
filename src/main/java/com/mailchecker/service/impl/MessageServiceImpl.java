package com.mailchecker.service.impl;

import com.mailchecker.dto.Message;
import com.mailchecker.repository.MessageRepository;
import com.mailchecker.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Bohdan on 25.08.2016.
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageRepository messageRepository;
    @Override
    public Message add(Message item) {
        return messageRepository.saveAndFlush(item);
    }

    @Override
    public Message get(long id) {
        return messageRepository.getOne(id);
    }

    @Override
    public void update(Message item) {
        messageRepository.saveAndFlush(item);
    }

    @Override
    public void delete(long id) {
        messageRepository.delete(id);
    }

    @Override
    public List<Message> getAll() {
        return messageRepository.findAll();
    }
}
