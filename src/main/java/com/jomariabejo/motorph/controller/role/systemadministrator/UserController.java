package com.jomariabejo.motorph.controller.role.systemadministrator;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.SystemAdministratorNavigationController;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.Role;
import com.jomariabejo.motorph.model.User;
import com.jomariabejo.motorph.constants.PermissionConstants;
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

import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class UserController {

    private SystemAdministratorNavigationController systemAdministratorNavigationController;

    @FXML
    private Button btnAddUser;

    @FXML
    private Pagination paginationUsers;

    @FXML
    private TableView<User> tvUsers;

    private List<User> allUsers;

    public UserController() {
    }

    @FXML
    void initialize() {
        setupTableView();
        customizeAddUserButton();
        // Don't populate users here - wait for controller to be set
        // populateUsers() will be called after systemAdministratorNavigationController is set
    }

    @FXML
    void addUserClicked() {
        if (systemAdministratorNavigationController == null ||
                !systemAdministratorNavigationController.requirePermission(PermissionConstants.SYSTEM_ADMIN_USERS_CREATE, "create users")) {
            return;
        }
        openUserForm(null);
    }

    private void openUserForm(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/systemadmin/user-form.fxml"));
            AnchorPane formPane = loader.load();
            Stage formStage = new Stage();
            formStage.setTitle(user == null ? "Add New User" : "Edit User");
            formStage.setScene(new Scene(formPane));
            formStage.setResizable(true);
            formStage.setMinWidth(820);
            formStage.setMinHeight(680);

            UserFormController formController = loader.getController();
            formController.setUserController(this);
            formController.setUser(user);
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
        TableColumn<User, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        idColumn.setPrefWidth(80);

        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUsername()));
        usernameColumn.setPrefWidth(150);

        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        emailColumn.setPrefWidth(200);

        TableColumn<User, String> fullNameColumn = new TableColumn<>("Full Name");
        fullNameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFullName()));
        fullNameColumn.setPrefWidth(200);

        TableColumn<User, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(cellData -> {
            Role role = cellData.getValue().getRoleID();
            return new javafx.beans.property.SimpleStringProperty(role != null ? role.getRoleName() : "");
        });
        roleColumn.setPrefWidth(150);

        TableColumn<User, String> employeeColumn = new TableColumn<>("Employee");
        employeeColumn.setCellValueFactory(cellData -> {
            Employee emp = cellData.getValue().getEmployee();
            return new javafx.beans.property.SimpleStringProperty(emp != null ? emp.getFirstName() + " " + emp.getLastName() : "");
        });
        employeeColumn.setPrefWidth(200);

        TableColumn<User, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));
        statusColumn.setPrefWidth(100);

        TableColumn<User, Void> actionsColumn = createActionsColumn();
        actionsColumn.setPrefWidth(200);

        tvUsers.getColumns().addAll(idColumn, usernameColumn, emailColumn, fullNameColumn, roleColumn, employeeColumn, statusColumn, actionsColumn);
        tvUsers.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private TableColumn<User, Void> createActionsColumn() {
        TableColumn<User, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button(null, new FontIcon(Feather.EDIT));
            private final Button deleteButton = new Button(null, new FontIcon(Feather.TRASH));

            {
                editButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);

                editButton.setOnAction(event -> {
                    User selectedUser = getTableView().getItems().get(getIndex());
                    if (selectedUser != null) {
                        if (systemAdministratorNavigationController == null ||
                                !systemAdministratorNavigationController.requirePermission(PermissionConstants.SYSTEM_ADMIN_USERS_EDIT, "edit users")) {
                            return;
                        }
                        openUserForm(selectedUser);
                    }
                });

                deleteButton.setOnAction(event -> {
                    User selectedUser = getTableView().getItems().get(getIndex());
                    if (selectedUser != null) {
                        if (systemAdministratorNavigationController == null ||
                                !systemAdministratorNavigationController.requirePermission(PermissionConstants.SYSTEM_ADMIN_USERS_DELETE, "delete users")) {
                            return;
                        }
                        deleteUser(selectedUser);
                    }
                });
            }


            {
                actionsBox.setAlignment(Pos.CENTER);
                actionsBox.setSpacing(10);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }

                boolean canEdit = systemAdministratorNavigationController != null &&
                        systemAdministratorNavigationController.hasPermission(PermissionConstants.SYSTEM_ADMIN_USERS_EDIT);
                boolean canDelete = systemAdministratorNavigationController != null &&
                        systemAdministratorNavigationController.hasPermission(PermissionConstants.SYSTEM_ADMIN_USERS_DELETE);

                editButton.setDisable(!canEdit);
                deleteButton.setDisable(!canDelete);
                setGraphic(actionsBox);
            }
        });
        return actionsColumn;
    }

    private void deleteUser(User user) {
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Delete User",
                "Are you sure you want to delete user: " + user.getUsername() + "?"
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Soft delete by setting status to Inactive
            user.setStatus("Inactive");
            systemAdministratorNavigationController.getMainViewController()
                    .getServiceFactory().getUserService().updateUser(user);

            // Log user deletion
            User currentUser = systemAdministratorNavigationController.getMainViewController().getUser();
            if (currentUser != null) {
                LoggingUtility.logUserDeleted(currentUser, user);
            }

            CustomAlert successAlert = new CustomAlert(
                    Alert.AlertType.INFORMATION,
                    "User Deactivated",
                    "User " + user.getUsername() + " has been deactivated."
            );
            successAlert.showAndWait();
            populateUsers();
        }
    }

    private void customizeAddUserButton() {
        FontIcon fontIcon = new FontIcon(Feather.USER_PLUS);
        btnAddUser.setGraphic(fontIcon);
        btnAddUser.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
    }

    /**
     * Populates the users table.
     * Should be called after systemAdministratorNavigationController is set.
     */
    public void populateUsers() {
        if (systemAdministratorNavigationController == null) {
            return; // Controller not set yet, skip
        }

        if (!systemAdministratorNavigationController.requirePermission(PermissionConstants.SYSTEM_ADMIN_USERS_VIEW, "view users")) {
            if (tvUsers != null) tvUsers.setItems(FXCollections.observableArrayList());
            if (paginationUsers != null) paginationUsers.setPageCount(1);
            if (btnAddUser != null) btnAddUser.setDisable(true);
            return;
        }
        if (btnAddUser != null) {
            btnAddUser.setDisable(!systemAdministratorNavigationController.hasPermission(PermissionConstants.SYSTEM_ADMIN_USERS_CREATE));
        }
        
        allUsers = systemAdministratorNavigationController.getMainViewController()
                .getServiceFactory().getUserService().getAllUsers();

        int itemsPerPage = 25;
        int pageCount = Math.max(1, (int) Math.ceil((double) allUsers.size() / itemsPerPage));
        paginationUsers.setPageCount(pageCount);

        paginationUsers.setPageFactory(pageIndex -> {
            updateTableView(pageIndex, itemsPerPage);
            return new StackPane();
        });
    }

    private void updateTableView(int pageIndex, int itemsPerPage) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, allUsers.size());
        List<User> pageData = allUsers.subList(fromIndex, toIndex);
        tvUsers.setItems(FXCollections.observableList(pageData));
    }
}
