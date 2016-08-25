package com.mailchecker;

import com.mailchecker.scene.SceneBuilder;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MailCheckerApplication extends AbstractJavaFxApplicationSupport {

	@Value("${ui.title}")
	private String windowTitle;
	final static Logger logger = Logger.getLogger(MailCheckerApplication.class);

	public static void main(String[] args) {
		launchApp(MailCheckerApplication.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		logger.info("==========MailCheckerApplication==============");
		stage.setTitle(windowTitle);
		stage.setScene(new SceneBuilder().getScene(stage));
		stage.setResizable(true);
		stage.centerOnScreen();
		stage.show();
	}
}
