package com.mailchecker.dto;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by Bohdan on 24.08.2016.
 */
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name= "increment", strategy= "increment")
    @Column(name = "message_id", length = 6, nullable = false)
    private Long id;
    @Column(name = "sender")
    private String sender;
    @Column(name = "subject")
    private String subject;
    @Column(name = "messageContent")
    private String messageContent;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "message", cascade = CascadeType.ALL)
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

    @Override
    public int hashCode() {
        Objects.hash(sender, subject, messageContent, sentDate, attachedFiles);
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Message)) return false;
        Message msg = (Message) obj;
        if (this==msg) return true;
        return Objects.equals(this.sender, msg.sender) &&
                Objects.equals(this.subject, msg.subject) &&
                Objects.equals(this.messageContent, msg.messageContent) &&
                Objects.equals(this.sentDate, msg.sentDate) &&
                Objects.equals(this.attachedFiles, msg.attachedFiles);
    }
}
