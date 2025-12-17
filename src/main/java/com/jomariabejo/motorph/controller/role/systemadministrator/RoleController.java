package com.jomariabejo.motorph.controller.role.systemadministrator;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.SystemAdministratorNavigationController;
import com.jomariabejo.motorph.model.Role;
import com.jomariabejo.motorph.model.User;
import com.jomariabejo.motorph.constants.PermissionConstants;
import com.jomariabejo.motorph.utility.CustomAlert;
import com.jomariabejo.motorph.utility.LoggingUtility;
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
import java.util.Optional;

@Getter
@Setter
public class RoleController {

    private SystemAdministratorNavigationController systemAdministratorNavigationController;

    @FXML
    private Button btnAddRole;

    @FXML
    private Pagination paginationRoles;

    @FXML
    private TableView<Role> tvRoles;

    private List<Role> allRoles;

    public RoleController() {
    }

    @FXML
    void initialize() {
        setupTableView();
        customizeAddRoleButton();
        populateRoles();
    }

    @FXML
    void addRoleClicked() {
        if (systemAdministratorNavigationController == null ||
                !systemAdministratorNavigationController.requirePermission(PermissionConstants.SYSTEM_ADMIN_ROLES_CREATE, "create roles")) {
            return;
        }
        openRoleForm(null);
    }

    private void openRoleForm(Role role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/systemadmin/role-form.fxml"));
            AnchorPane formPane = loader.load();
            Stage formStage = new Stage();
            formStage.setTitle(role == null ? "Add New Role" : "Edit Role");
            formStage.setScene(new Scene(formPane));

            RoleFormController formController = loader.getController();
            formController.setRoleController(this);
            formController.setRole(role);
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
        TableColumn<Role, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        idColumn.setPrefWidth(80);

        TableColumn<Role, String> nameColumn = new TableColumn<>("Role Name");
        nameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRoleName()));
        nameColumn.setPrefWidth(200);

        TableColumn<Role, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(cellData -> {
            String desc = cellData.getValue().getDescription();
            return new javafx.beans.property.SimpleStringProperty(desc != null ? desc : "");
        });
        descriptionColumn.setPrefWidth(400);

        TableColumn<Role, Void> actionsColumn = createActionsColumn();
        actionsColumn.setPrefWidth(200);

        tvRoles.getColumns().addAll(idColumn, nameColumn, descriptionColumn, actionsColumn);
        tvRoles.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private TableColumn<Role, Void> createActionsColumn() {
        TableColumn<Role, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button(null, new FontIcon(Feather.EDIT));
            private final Button deleteButton = new Button(null, new FontIcon(Feather.TRASH));

            {
                editButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
                deleteButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);

                editButton.setOnAction(event -> {
                    Role selectedRole = getTableView().getItems().get(getIndex());
                    if (selectedRole != null) {
                        if (systemAdministratorNavigationController == null ||
                                !systemAdministratorNavigationController.requirePermission(PermissionConstants.SYSTEM_ADMIN_ROLES_EDIT, "edit roles")) {
                            return;
                        }
                        openRoleForm(selectedRole);
                    }
                });

                deleteButton.setOnAction(event -> {
                    Role selectedRole = getTableView().getItems().get(getIndex());
                    if (selectedRole != null) {
                        if (systemAdministratorNavigationController == null ||
                                !systemAdministratorNavigationController.requirePermission(PermissionConstants.SYSTEM_ADMIN_ROLES_DELETE, "delete roles")) {
                            return;
                        }
                        deleteRole(selectedRole);
                    }
                });
            }

            private final HBox actionsBox = new HBox(editButton, deleteButton);

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
                        systemAdministratorNavigationController.hasPermission(PermissionConstants.SYSTEM_ADMIN_ROLES_EDIT);
                boolean canDelete = systemAdministratorNavigationController != null &&
                        systemAdministratorNavigationController.hasPermission(PermissionConstants.SYSTEM_ADMIN_ROLES_DELETE);

                editButton.setDisable(!canEdit);
                deleteButton.setDisable(!canDelete);
                setGraphic(actionsBox);
            }
        });
        return actionsColumn;
    }

    private void deleteRole(Role role) {
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Delete Role",
                "Are you sure you want to delete role: " + role.getRoleName() + "?"
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            systemAdministratorNavigationController.getMainViewController()
                    .getServiceFactory().getRoleService().deleteRole(role);

            // Log role deletion
            User currentUser = systemAdministratorNavigationController.getMainViewController().getUser();
            if (currentUser != null) {
                LoggingUtility.logRoleDeleted(currentUser, role);
            }

            CustomAlert successAlert = new CustomAlert(
                    Alert.AlertType.INFORMATION,
                    "Role Deleted",
                    "Role " + role.getRoleName() + " has been deleted."
            );
            successAlert.showAndWait();
            populateRoles();
        }
    }

    private void customizeAddRoleButton() {
        FontIcon fontIcon = new FontIcon(Feather.PLUS);
        btnAddRole.setGraphic(fontIcon);
        btnAddRole.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
    }

    public void populateRoles() {
        if (systemAdministratorNavigationController == null) {
            return;
        }
        if (!systemAdministratorNavigationController.requirePermission(PermissionConstants.SYSTEM_ADMIN_ROLES_VIEW, "view roles")) {
            if (tvRoles != null) tvRoles.setItems(FXCollections.observableArrayList());
            if (paginationRoles != null) paginationRoles.setPageCount(1);
            if (btnAddRole != null) btnAddRole.setDisable(true);
            return;
        }
        if (btnAddRole != null) {
            btnAddRole.setDisable(!systemAdministratorNavigationController.hasPermission(PermissionConstants.SYSTEM_ADMIN_ROLES_CREATE));
        }
        allRoles = systemAdministratorNavigationController.getMainViewController()
                .getServiceFactory().getRoleService().getAllRoles();

        int itemsPerPage = 25;
        int pageCount = Math.max(1, (int) Math.ceil((double) allRoles.size() / itemsPerPage));
        paginationRoles.setPageCount(pageCount);

        paginationRoles.setPageFactory(pageIndex -> {
            updateTableView(pageIndex, itemsPerPage);
            return new StackPane();
        });
    }

    private void updateTableView(int pageIndex, int itemsPerPage) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, allRoles.size());
        List<Role> pageData = allRoles.subList(fromIndex, toIndex);
        tvRoles.setItems(FXCollections.observableList(pageData));
    }
}

