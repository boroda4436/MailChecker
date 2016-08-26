package com.mailchecker;

import com.mailchecker.dto.Message;
import com.mailchecker.dto.StoredAttachment;
import com.mailchecker.repository.MessageRepository;
import com.mailchecker.repository.StoredAttachmentRepository;
import com.mailchecker.scene.SceneBuilder;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MailCheckerApplication extends AbstractJavaFxApplicationSupport {

	@Autowired
	private MessageRepository messageRepository;
	@Value("${ui.title}")
	private String windowTitle;
	final static Logger logger = Logger.getLogger(MailCheckerApplication.class);

	public static void main(String[] args) {
		launchApp(MailCheckerApplication.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		logger.info("saving message only for TEST! In test scope Repositories are not autowired!");
		Message message = new Message();
		message.setMessageContent("----content---");
		message.setSender("xx@i.ua");
		StoredAttachment storedAttachment = new StoredAttachment("C:\\xx.doc");
		storedAttachment.setMessage(message);
		message.getAttachedFiles().add(storedAttachment);
		message = messageRepository.saveAndFlush(message);
		Message msg = messageRepository.findOne(message.getId());
		logger.info("==========MailCheckerApplication==============");




		stage.setTitle(windowTitle);
		stage.setScene(new SceneBuilder().getScene(stage));
		stage.setResizable(true);
		stage.centerOnScreen();
		stage.show();
	}
}
