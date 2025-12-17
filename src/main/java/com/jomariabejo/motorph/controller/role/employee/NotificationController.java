package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.Notification;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class NotificationController {

    private EmployeeRoleNavigationController employeeRoleNavigationController;

    @FXML
    private TableView<Notification> tvNotifications;

    @FXML
    private Pagination paginationNotifications;

    @FXML
    private Button btnMarkAsRead;

    private List<Notification> allNotifications;

    public NotificationController() {
    }

    @FXML
    void initialize() {
        setupTableView();
        customizeMarkAsReadButton();
        // populateNotifications() will be called after navigation controller is set
    }

    public void setup() {
        if (employeeRoleNavigationController != null) {
            populateNotifications();
        }
    }

    @FXML
    void markAsReadClicked() {
        Notification selected = tvNotifications.getSelectionModel().getSelectedItem();
        if (selected != null) {
            markNotificationAsRead(selected);
        } else {
            CustomAlert alert = new CustomAlert(
                    Alert.AlertType.WARNING,
                    "No Selection",
                    "Please select a notification to mark as read."
            );
            alert.showAndWait();
        }
    }

    private void setupTableView() {
        createTableColumns();
    }

    private void createTableColumns() {
        TableColumn<Notification, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        idColumn.setPrefWidth(80);

        TableColumn<Notification, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNotificationType()));
        typeColumn.setPrefWidth(150);

        TableColumn<Notification, String> contentColumn = new TableColumn<>("Content");
        contentColumn.setCellValueFactory(cellData -> {
            String content = cellData.getValue().getNotificationContent();
            return new javafx.beans.property.SimpleStringProperty(
                    content != null && content.length() > 100 ? content.substring(0, 100) + "..." : content);
        });
        contentColumn.setPrefWidth(400);

        TableColumn<Notification, String> timestampColumn = new TableColumn<>("Date/Time");
        timestampColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getTimestamp() != null 
                                ? cellData.getValue().getTimestamp().toString() : ""));
        timestampColumn.setPrefWidth(200);

        TableColumn<Notification, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> {
            Boolean read = cellData.getValue().getReadStatus();
            return new javafx.beans.property.SimpleStringProperty(read != null && read ? "Read" : "Unread");
        });
        statusColumn.setPrefWidth(100);

        // Style unread notifications
        statusColumn.setCellFactory(column -> new TableCell<Notification, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    Notification notification = getTableView().getItems().get(getIndex());
                    if (notification != null && !notification.getReadStatus()) {
                        setStyle("-fx-font-weight: bold; -fx-text-fill: #1976d2;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        tvNotifications.getColumns().addAll(idColumn, typeColumn, contentColumn, timestampColumn, statusColumn);
        tvNotifications.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void markNotificationAsRead(Notification notification) {
        if (employeeRoleNavigationController == null) {
            return; // Navigation controller not set
        }
        if (!notification.getReadStatus()) {
            notification.setReadStatus(true);
            employeeRoleNavigationController.getMainViewController()
                    .getServiceFactory().getNotificationService().updateNotification(notification);

            CustomAlert alert = new CustomAlert(
                    Alert.AlertType.INFORMATION,
                    "Notification Read",
                    "Notification marked as read."
            );
            alert.showAndWait();
            populateNotifications();
        }
    }

    private void customizeMarkAsReadButton() {
        FontIcon fontIcon = new FontIcon(Feather.CHECK);
        btnMarkAsRead.setGraphic(fontIcon);
        btnMarkAsRead.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
    }

    public void populateNotifications() {
        if (employeeRoleNavigationController == null) {
            return; // Navigation controller not set yet
        }
        Employee currentEmployee = employeeRoleNavigationController.getMainViewController().getEmployee();
        
        // Filter notifications for current employee
        allNotifications = employeeRoleNavigationController.getMainViewController()
                .getServiceFactory().getNotificationService().getAllNotifications()
                .stream()
                .filter(n -> n.getEmployee() != null && 
                        n.getEmployee().getEmployeeNumber().equals(currentEmployee.getEmployeeNumber()))
                .sorted((n1, n2) -> n2.getTimestamp().compareTo(n1.getTimestamp())) // Newest first
                .collect(Collectors.toList());

        int itemsPerPage = 25;
        int pageCount = Math.max(1, (int) Math.ceil((double) allNotifications.size() / itemsPerPage));
        paginationNotifications.setPageCount(pageCount);

        paginationNotifications.setPageFactory(pageIndex -> {
            updateTableView(pageIndex, itemsPerPage);
            return new StackPane();
        });
    }

    private void updateTableView(int pageIndex, int itemsPerPage) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, allNotifications.size());
        List<Notification> pageData = allNotifications.subList(fromIndex, toIndex);
        tvNotifications.setItems(FXCollections.observableList(pageData));
    }
}

