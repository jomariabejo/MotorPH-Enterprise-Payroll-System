package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import com.jomariabejo.motorph.model.Conversation;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.Message;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MessageController {

    private ConversationController conversationController;
    private Conversation conversation;
    private Employee currentEmployee;
    private Employee otherParticipant;

    @FXML
    private Label lblConversationWith;

    @FXML
    private ScrollPane scrollPaneMessages;

    @FXML
    private VBox vboxMessages;

    @FXML
    private TextArea taMessageContent;

    @FXML
    private Button btnSendMessage;

    private List<Message> allMessages;

    public MessageController() {
    }

    public void setup() {
        determineOtherParticipant();
        setupUI();
        customizeSendButton();
        populateMessages();
    }

    private void determineOtherParticipant() {
        if (conversation.getParticipant1Employee() != null && 
            conversation.getParticipant1Employee().getEmployeeNumber().equals(currentEmployee.getEmployeeNumber())) {
            otherParticipant = conversation.getParticipant2Employee();
        } else if (conversation.getParticipant2Employee() != null && 
                   conversation.getParticipant2Employee().getEmployeeNumber().equals(currentEmployee.getEmployeeNumber())) {
            otherParticipant = conversation.getParticipant1Employee();
        }

        if (otherParticipant != null) {
            lblConversationWith.setText("Conversation with: " + otherParticipant.getFirstName() + " " + otherParticipant.getLastName() + 
                                       " (" + otherParticipant.getEmployeeNumber() + ")");
        }
    }

    private void setupUI() {
        vboxMessages.setSpacing(10);
        vboxMessages.setPadding(new Insets(10));
        scrollPaneMessages.setFitToWidth(true);
    }

    @FXML
    void sendMessageClicked() {
        if (validateMessage()) {
            sendMessage();
        }
    }

    private boolean validateMessage() {
        if (taMessageContent.getText().trim().isEmpty()) {
            CustomAlert alert = new CustomAlert(
                    Alert.AlertType.WARNING,
                    "Empty Message",
                    "Please enter a message before sending."
            );
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private void sendMessage() {
        Message newMessage = new Message();
        newMessage.setSenderEmployee(currentEmployee);
        newMessage.setRecipientEmployee(otherParticipant);
        newMessage.setMessageContent(taMessageContent.getText().trim());
        newMessage.setTimestamp(Instant.now());
        newMessage.setStatus("unread");
        newMessage.setConversation(conversation);

        conversationController.getEmployeeRoleNavigationController()
                .getMainViewController().getServiceFactory()
                .getMessageService().saveMessage(newMessage);

        // Update conversation
        conversation.setLastMessageContent(newMessage.getMessageContent());
        conversation.setLastMessageTimestamp(newMessage.getTimestamp());
        if (conversation.getUnreadCount() == null) {
            conversation.setUnreadCount(0);
        }
        // Increment unread count for recipient
        conversation.setUnreadCount(conversation.getUnreadCount() + 1);
        conversationController.getEmployeeRoleNavigationController()
                .getMainViewController().getServiceFactory()
                .getConversationService().updateConversation(conversation);

        taMessageContent.clear();
        populateMessages();
    }

    private void customizeSendButton() {
        FontIcon fontIcon = new FontIcon(Feather.SEND);
        btnSendMessage.setGraphic(fontIcon);
        btnSendMessage.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
    }

    public void populateMessages() {
        // Get all messages for this conversation
        allMessages = conversationController.getEmployeeRoleNavigationController()
                .getMainViewController().getServiceFactory().getMessageService().getAllMessages()
                .stream()
                .filter(msg -> msg.getConversation() != null && 
                              msg.getConversation().getId().equals(conversation.getId()))
                .sorted((m1, m2) -> m1.getTimestamp().compareTo(m2.getTimestamp())) // Oldest first
                .collect(Collectors.toList());

        displayMessages();
    }

    private void displayMessages() {
        vboxMessages.getChildren().clear();

        for (Message message : allMessages) {
            boolean isSentByMe = message.getSenderEmployee() != null && 
                                message.getSenderEmployee().getEmployeeNumber().equals(currentEmployee.getEmployeeNumber());

            HBox messageBox = createMessageBox(message, isSentByMe);
            vboxMessages.getChildren().add(messageBox);

            // Mark as read if I'm the recipient
            if (!isSentByMe && "unread".equals(message.getStatus())) {
                message.setStatus("read");
                conversationController.getEmployeeRoleNavigationController()
                        .getMainViewController().getServiceFactory()
                        .getMessageService().updateMessage(message);

                // Update conversation unread count
                if (conversation.getUnreadCount() != null && conversation.getUnreadCount() > 0) {
                    conversation.setUnreadCount(conversation.getUnreadCount() - 1);
                    conversationController.getEmployeeRoleNavigationController()
                            .getMainViewController().getServiceFactory()
                            .getConversationService().updateConversation(conversation);
                }
            }
        }

        // Scroll to bottom
        scrollPaneMessages.setVvalue(1.0);
    }

    private HBox createMessageBox(Message message, boolean isSentByMe) {
        HBox messageBox = new HBox();
        messageBox.setSpacing(10);
        messageBox.setPadding(new Insets(5));

        VBox contentBox = new VBox();
        contentBox.setSpacing(5);

        Label senderLabel = new Label();
        if (isSentByMe) {
            senderLabel.setText("You");
            senderLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1976d2;");
        } else {
            Employee sender = message.getSenderEmployee();
            senderLabel.setText(sender != null ? sender.getFirstName() + " " + sender.getLastName() : "Unknown");
            senderLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #666;");
        }

        Label contentLabel = new Label(message.getMessageContent());
        contentLabel.setWrapText(true);
        contentLabel.setMaxWidth(400);

        Label timeLabel = new Label(message.getTimestamp().toString());
        timeLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #999;");

        contentBox.getChildren().addAll(senderLabel, contentLabel, timeLabel);

        if (isSentByMe) {
            messageBox.setAlignment(Pos.CENTER_RIGHT);
            contentBox.setAlignment(Pos.CENTER_RIGHT);
            contentBox.setStyle("-fx-background-color: #e3f2fd; -fx-background-radius: 10; -fx-padding: 10;");
        } else {
            messageBox.setAlignment(Pos.CENTER_LEFT);
            contentBox.setAlignment(Pos.CENTER_LEFT);
            contentBox.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 10; -fx-padding: 10;");
        }

        messageBox.getChildren().add(contentBox);
        return messageBox;
    }
}










