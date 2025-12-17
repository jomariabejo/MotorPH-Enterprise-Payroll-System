package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import com.jomariabejo.motorph.model.Conversation;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ConversationController {

    private EmployeeRoleNavigationController employeeRoleNavigationController;

    @FXML
    private Button btnNewConversation;

    @FXML
    private TableView<Conversation> tvConversations;

    @FXML
    private Pagination paginationConversations;

    private List<Conversation> allConversations;
    private Employee currentEmployee;

    public ConversationController() {
    }

    @FXML
    void initialize() {
        setupTableView();
        customizeNewConversationButton();
        // populateConversations() will be called after navigation controller is set
    }

    public void setup() {
        if (employeeRoleNavigationController != null) {
            currentEmployee = employeeRoleNavigationController.getMainViewController().getEmployee();
            populateConversations();
        }
    }

    @FXML
    void newConversationClicked() {
        openNewConversationDialog();
    }

    private void openNewConversationDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/employee/new-conversation-form.fxml"));
            AnchorPane formPane = loader.load();
            Stage formStage = new Stage();
            formStage.setTitle("Start New Conversation");
            formStage.setScene(new Scene(formPane));

            NewConversationFormController formController = loader.getController();
            formController.setConversationController(this);
            formController.setCurrentEmployee(currentEmployee);
            formController.setup();

            formStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupTableView() {
        createTableColumns();
    }

    private void createTableColumns() {
        TableColumn<Conversation, String> participantColumn = new TableColumn<>("Conversation With");
        participantColumn.setCellValueFactory(cellData -> {
            Conversation conv = cellData.getValue();
            Employee otherParticipant = getOtherParticipant(conv);
            if (otherParticipant != null) {
                return new javafx.beans.property.SimpleStringProperty(
                        otherParticipant.getEmployeeNumber() + " - " + 
                        otherParticipant.getFirstName() + " " + otherParticipant.getLastName());
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        participantColumn.setPrefWidth(250);

        TableColumn<Conversation, String> lastMessageColumn = new TableColumn<>("Last Message");
        lastMessageColumn.setCellValueFactory(cellData -> {
            String content = cellData.getValue().getLastMessageContent();
            String display = content != null && content.length() > 100 ? content.substring(0, 100) + "..." : (content != null ? content : "");
            return new javafx.beans.property.SimpleStringProperty(display);
        });
        lastMessageColumn.setPrefWidth(400);

        TableColumn<Conversation, String> timestampColumn = new TableColumn<>("Last Message Time");
        timestampColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getLastMessageTimestamp() != null 
                                ? cellData.getValue().getLastMessageTimestamp().toString() : ""));
        timestampColumn.setPrefWidth(200);

        TableColumn<Conversation, String> unreadColumn = new TableColumn<>("Unread");
        unreadColumn.setCellValueFactory(cellData -> {
            Integer unread = cellData.getValue().getUnreadCount();
            return new javafx.beans.property.SimpleStringProperty(unread != null && unread > 0 ? String.valueOf(unread) : "0");
        });
        unreadColumn.setPrefWidth(80);

        // Style unread conversations
        unreadColumn.setCellFactory(column -> new TableCell<Conversation, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    Conversation conv = getTableView().getItems().get(getIndex());
                    if (conv != null && conv.getUnreadCount() != null && conv.getUnreadCount() > 0) {
                        setStyle("-fx-font-weight: bold; -fx-text-fill: #1976d2;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        TableColumn<Conversation, Void> actionsColumn = createActionsColumn();
        actionsColumn.setPrefWidth(200);

        tvConversations.getColumns().addAll(participantColumn, lastMessageColumn, timestampColumn, unreadColumn, actionsColumn);
        tvConversations.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private Employee getOtherParticipant(Conversation conversation) {
        if (conversation.getParticipant1Employee() != null && 
            conversation.getParticipant1Employee().getEmployeeNumber().equals(currentEmployee.getEmployeeNumber())) {
            return conversation.getParticipant2Employee();
        } else if (conversation.getParticipant2Employee() != null && 
                   conversation.getParticipant2Employee().getEmployeeNumber().equals(currentEmployee.getEmployeeNumber())) {
            return conversation.getParticipant1Employee();
        }
        return null;
    }

    private TableColumn<Conversation, Void> createActionsColumn() {
        TableColumn<Conversation, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button openButton = new Button(null, new FontIcon(Feather.MESSAGE_CIRCLE));
            private final Button deleteButton = new Button(null, new FontIcon(Feather.TRASH));

            {
                openButton.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED);
                deleteButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);

                openButton.setOnAction(event -> {
                    Conversation selected = getTableView().getItems().get(getIndex());
                    if (selected != null) {
                        openConversation(selected);
                    }
                });

                deleteButton.setOnAction(event -> {
                    Conversation selected = getTableView().getItems().get(getIndex());
                    if (selected != null) {
                        deleteConversation(selected);
                    }
                });
            }

            private final HBox actionsBox = new HBox(openButton, deleteButton);

            {
                actionsBox.setAlignment(Pos.CENTER);
                actionsBox.setSpacing(10);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : actionsBox);
            }
        });
        return actionsColumn;
    }

    public void openConversation(Conversation conversation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/employee/message.fxml"));
            AnchorPane messagePane = loader.load();
            Stage messageStage = new Stage();
            messageStage.setTitle("Messages");
            messageStage.setScene(new Scene(messagePane));

            MessageController messageController = loader.getController();
            messageController.setConversationController(this);
            messageController.setConversation(conversation);
            messageController.setCurrentEmployee(currentEmployee);
            messageController.setup();

            messageStage.showAndWait();
            populateConversations(); // Refresh after closing message window
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteConversation(Conversation conversation) {
        if (employeeRoleNavigationController == null) {
            return; // Navigation controller not set
        }
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Delete Conversation",
                "Are you sure you want to delete this conversation? All messages will be deleted."
        );
        var result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            employeeRoleNavigationController.getMainViewController()
                    .getServiceFactory().getConversationService().deleteConversation(conversation);

            CustomAlert successAlert = new CustomAlert(
                    Alert.AlertType.INFORMATION,
                    "Conversation Deleted",
                    "Conversation has been deleted."
            );
            successAlert.showAndWait();
            populateConversations();
        }
    }

    private void customizeNewConversationButton() {
        FontIcon fontIcon = new FontIcon(Feather.PLUS);
        btnNewConversation.setGraphic(fontIcon);
        btnNewConversation.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
    }

    public void populateConversations() {
        if (employeeRoleNavigationController == null || currentEmployee == null) {
            return; // Navigation controller or employee not set yet
        }
        // Get all conversations where current employee is a participant
        allConversations = employeeRoleNavigationController.getMainViewController()
                .getServiceFactory().getConversationService().getAllConversations()
                .stream()
                .filter(conv -> {
                    Employee p1 = conv.getParticipant1Employee();
                    Employee p2 = conv.getParticipant2Employee();
                    return (p1 != null && p1.getEmployeeNumber().equals(currentEmployee.getEmployeeNumber())) ||
                           (p2 != null && p2.getEmployeeNumber().equals(currentEmployee.getEmployeeNumber()));
                })
                .sorted((c1, c2) -> c2.getLastMessageTimestamp().compareTo(c1.getLastMessageTimestamp())) // Newest first
                .collect(Collectors.toList());

        int itemsPerPage = 25;
        int pageCount = Math.max(1, (int) Math.ceil((double) allConversations.size() / itemsPerPage));
        paginationConversations.setPageCount(pageCount);

        paginationConversations.setPageFactory(pageIndex -> {
            updateTableView(pageIndex, itemsPerPage);
            return new StackPane();
        });
    }

    private void updateTableView(int pageIndex, int itemsPerPage) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, allConversations.size());
        List<Conversation> pageData = allConversations.subList(fromIndex, toIndex);
        tvConversations.setItems(FXCollections.observableList(pageData));
    }
}

