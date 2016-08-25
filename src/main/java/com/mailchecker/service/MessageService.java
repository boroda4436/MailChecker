package com.mailchecker.service;

import com.mailchecker.dto.Message;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Bohdan on 25.08.2016.
 */
@Service
public interface MessageService {
    Message add(Message item);
    Message get(long id);
    void update(Message item);
    void delete(long id);
    List<Message> getAll();
}
