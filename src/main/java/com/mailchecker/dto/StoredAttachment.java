package com.mailchecker.dto;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by Bohdan on 25.08.2016.
 */
@Entity
@Table(name = "stored_attachments")
public class StoredAttachment {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name= "increment", strategy= "increment")
    @Column(name = "stored_attachment_id", length = 6, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @Column(name = "file_path")
    private String filePath;

    public StoredAttachment() {}
    public StoredAttachment(String filePath) {
        this.filePath = filePath;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Message getMessage() {
        return message;
    }
    public void setMessage(Message message) {
        this.message = message;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public int hashCode() {
        return Objects.hash(filePath);
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equals(this.filePath, obj);
    }
}
