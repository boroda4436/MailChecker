package com.mailchecker.dto;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Bohdan on 24.08.2016.
 */
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name= "increment", strategy= "increment")
    @Column(name = "id", length = 6, nullable = false)
    private Long id;
    @Column(name = "sender")
    private String sender;
    @Column(name = "subject")
    private String subject;
    @Column(name = "messageContent")
    private String messageContent;
    @OneToMany
    private List<StoredAttachment> attachedFiles = new ArrayList<>();
    @Column(name = "sentDate")
    private Date sentDate;

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getMessageContent() {
        return messageContent;
    }
    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
    public Date getSentDate() {
        return sentDate;
    }
    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }
    public List<StoredAttachment> getAttachedFiles() {
        return attachedFiles;
    }
    public void setAttachedFiles(List<StoredAttachment> attachedFiles) {
        this.attachedFiles = attachedFiles;
    }
}
