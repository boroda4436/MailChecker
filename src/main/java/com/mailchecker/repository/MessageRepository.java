package com.mailchecker.repository;

import com.mailchecker.dto.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Bohdan on 24.08.2016.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long>{

}
