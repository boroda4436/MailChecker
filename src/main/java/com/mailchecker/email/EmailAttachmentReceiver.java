package com.mailchecker.email;

import com.mailchecker.dto.StoredAttachment;
import com.mailchecker.service.MessageService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
/**
 * Created by Bohdan on 24.08.2016.
 */
public class EmailAttachmentReceiver extends Thread {
    private String saveDirectory;
    private String host;
    private String port;
    private String userName;
    private String password;
    private Store store = null;
    private Map<String, com.mailchecker.dto.Message> messageMap = new ConcurrentHashMap();
    final static Logger logger = Logger.getLogger(EmailAttachmentReceiver.class);
    @Autowired
    private MessageService messageService;

    public void setSaveDirectory(String dir) {
        this.saveDirectory = dir;
    }

    public EmailAttachmentReceiver() {
    }

    public EmailAttachmentReceiver(String host, String port, String userName, String password) {
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.password = password;
    }

    public void initializeEmailAttachmentReceiver(){
        System.out.println("Check email: " + new Date());
        Properties properties = new Properties();
        properties.put("mail.pop3.host", "pop3."+host);
        properties.put("mail.pop3.port", port);
        properties.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.pop3.socketFactory.fallback", "false");
        properties.setProperty("mail.pop3.socketFactory.port", String.valueOf(port));

        Session session = Session.getDefaultInstance(properties);
        try {
            store = session.getStore("pop3");
            store.connect(userName, password);
            logger.debug("Connected using POP3 protocol");
        } catch (Exception e1){
            String protocol = "imap";
            port = "993";
            properties = new Properties();
            properties.put(String.format("mail.%s.host", protocol), "imap." + host);
            properties.put(String.format("mail.%s.port", protocol), port);
            properties.setProperty(String.format("mail.%s.socketFactory.class", protocol), "javax.net.ssl.SSLSocketFactory");
            properties.setProperty(String.format("mail.%s.socketFactory.fallback", protocol), "false");
            properties.setProperty(String.format("mail.%s.socketFactory.port", protocol), String.valueOf(port));
            session = Session.getInstance(properties);
            try {
                store = session.getStore(protocol);
                store.connect(userName, password);
                logger.debug("Connected using IMAP protocol");
            } catch (NoSuchProviderException e2) {
                logger.error(e2);
            } catch (MessagingException e2) {
                logger.error(e2);
            }
        }


    }
    public void downloadEmailAttachments() {
        initializeEmailAttachmentReceiver();
        try {
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);

            Message[] arrayMessages = folderInbox.getMessages();
            for (int i = 0; i < arrayMessages.length; i++) {
                Message message = arrayMessages[i];
                Address[] fromAddress = message.getFrom();
                String from = fromAddress[0].toString();
                String subject = message.getSubject();
                String sentDate = message.getSentDate().toString();
                String contentType = message.getContentType();

                if (contentType.contains("multipart")) {
                    String messageContent = "";
                    String attachFiles = "";

                    Multipart multiPart = (Multipart) message.getContent();
                    int numberOfParts = multiPart.getCount();
                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
                        com.mailchecker.dto.Message messageToSave = new com.mailchecker.dto.Message();
                        messageToSave.setSender(from);
                        messageToSave.setMessageContent(messageContent);
                        messageToSave.setSubject(subject);
                        messageToSave.setSentDate(message.getSentDate());
                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            String fileName = part.getFileName();
                            attachFiles += fileName + ", ";
                            part.saveFile(saveDirectory + File.separator + fileName);
                            logger.info("Message #" + (i + 1) + ":");
                            logger.info("\t From: " + from);
                            logger.info("\t Subject: " + subject);
                            logger.info("\t Sent Date: " + sentDate);
                            logger.info("\t Message: " + messageContent);
                            logger.info("\t Attachments: " + attachFiles);
                            messageToSave.getAttachedFiles().add(new StoredAttachment(fileName));
                        }
                        if (!messageMap.containsValue(messageToSave)){
                            messageMap.put(from, messageToSave);
                            try{
                                messageService.add(messageToSave);
                            }catch (NullPointerException e){
                                logger.error("MessageService is not autowired");
                            }
                        }
                    }
                }
            }
            folderInbox.close(false);
            store.close();
        }  catch (MessagingException ex) {
            logger.error("Could not connect to the message store ", ex);
        } catch (IOException ex) {
            logger.error(ex);
        }
    }

    @Override
    public void run() {
        logger.debug(Thread.currentThread().getName()+" Start. Time = "+new Date());
        downloadEmailAttachments();
        logger.debug(Thread.currentThread().getName()+" End. Time = "+new Date());
    }

}
