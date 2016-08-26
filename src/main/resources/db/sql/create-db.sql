CREATE TABLE messages (
  message_id                  bigint auto_increment PRIMARY KEY,
  sender              VARCHAR(100),
  subject             VARCHAR(255),
  messageContent      TEXT,
  sentDate            TIMESTAMP
);

CREATE TABLE stored_attachments (
  stored_attachment_id bigint auto_increment PRIMARY KEY,
  message_id           INT,
  file_path            VARCHAR(100)
);