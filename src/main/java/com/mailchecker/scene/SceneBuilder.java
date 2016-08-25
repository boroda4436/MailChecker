package com.mailchecker.scene;

import com.mailchecker.email.EmailAttachmentReceiver;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Bohdan on 23.08.2016.
 */
public class SceneBuilder {
    final static Logger logger = Logger.getLogger(SceneBuilder.class);

    public Scene getScene(Stage stage){

        GridPane root = new GridPane();

        Label labelEmailAddress = new Label();
        labelEmailAddress.setText("Email address: ");

        TextField inputEmailAddress = new TextField();
        inputEmailAddress.setPromptText("Enter your email address");
        inputEmailAddress.setPrefColumnCount(20);
        inputEmailAddress.getText();

        Label labelPassword = new Label();
        labelPassword.setText("Password: ");

        PasswordField inputPassword = new PasswordField();
        inputPassword.setPromptText("Enter your password");
        inputPassword.setPrefColumnCount(20);
        inputPassword.getText();

        Label labelPort = new Label();
        labelPort.setText("Port: ");

        TextField inputPort = new TextField();
        inputPort.setPromptText("Enter your port");
        inputPort.setPrefColumnCount(20);
        inputPort.getText();

        Label labelFilePath = new Label("Directory to save attachments");
        labelPort.setText("Port: ");

        final TextField pathField = new TextField("C:/Users");
        pathField.setPrefWidth(250);
        Button btnOpenDirectoryChooser = new Button("Choose Directory");
        btnOpenDirectoryChooser.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent t) {
                chooseDirectory(stage, pathField);
            }
        });

        Label errorLabel = new Label();
        errorLabel.setVisible(true);
        Label emailProcessedLabel = new Label();
        emailProcessedLabel.setVisible(true);
        Label emailProcessedCountLabel = new Label();
        emailProcessedCountLabel.setVisible(true);
        Label foundedAttachmentsLabel = new Label();
        foundedAttachmentsLabel.setVisible(true);
        Label foundedAttachmentsCountLabel = new Label();
        foundedAttachmentsCountLabel.setVisible(true);
        Button btnSubmit = new Button("Submit");
        btnSubmit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String email    = inputEmailAddress.getText();
                String password = inputPassword.getText();
                String port     = inputPort.getText();
                String path     = pathField.getText();
                String host     = email.substring(email.indexOf('@')+1, email.length());
                EmailAttachmentReceiver receiver = new EmailAttachmentReceiver(host, port, email, password);
                receiver.setSaveDirectory(path);
                ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
                scheduledThreadPool.scheduleAtFixedRate(receiver, 0, 60, TimeUnit.SECONDS);
                receiver.start();
                try {
                    String threadName = Thread.currentThread().getName();
                    receiver.join();
                    logger.debug(threadName + " is finished!");
                } catch (InterruptedException e) {
                    logger.debug(e);
                }
            }
        });
        GridPane.setConstraints(labelEmailAddress, 0, 0);
        GridPane.setConstraints(inputEmailAddress, 1, 0);
        GridPane.setConstraints(labelPassword, 0, 1);
        GridPane.setConstraints(inputPassword, 1, 1);
        GridPane.setConstraints(labelPort, 0, 2);
        GridPane.setConstraints(inputPort, 1, 2);
        GridPane.setConstraints(labelFilePath, 0, 3);
        GridPane.setConstraints(pathField, 1, 3);
        GridPane.setConstraints(btnOpenDirectoryChooser, 2, 3);
        GridPane.setConstraints(btnSubmit, 1,4);
        GridPane.setConstraints(errorLabel, 0,5);
        GridPane.setConstraints(emailProcessedLabel, 0,6);
        GridPane.setConstraints(emailProcessedCountLabel, 1,6);
        GridPane.setConstraints(foundedAttachmentsLabel, 0,7);
        GridPane.setConstraints(foundedAttachmentsCountLabel, 1,7);

        root.getChildren().addAll(btnSubmit, labelEmailAddress, inputEmailAddress, labelPassword, inputPassword, labelPort,
                inputPort,labelFilePath, pathField, btnOpenDirectoryChooser, errorLabel,emailProcessedLabel,emailProcessedCountLabel,foundedAttachmentsLabel, foundedAttachmentsCountLabel);
        Scene scene = new Scene(root, 400, 250);
        return scene;
    }
    private void chooseDirectory(Stage stage, TextField pathField) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select directory");
        chooser.setInitialDirectory(new File(pathField.getText()));
        File selectedDirectory = chooser.showDialog(stage);
        if (selectedDirectory != null) {
            pathField.setText(selectedDirectory.getAbsolutePath());
        }
    }
}
