package com.jomariabejo.motorph.controller.role.systemadministrator;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.SystemAdministratorNavigationController;
import com.jomariabejo.motorph.model.Permission;
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
public class PermissionController {

    private SystemAdministratorNavigationController systemAdministratorNavigationController;

    @FXML
    private Button btnAddPermission;

    @FXML
    private Button btnSyncPermissions;

    @FXML
    private Pagination paginationPermissions;

    @FXML
    private TableView<Permission> tvPermissions;

    private List<Permission> allPermissions;

    public PermissionController() {
    }

    @FXML
    void initialize() {
        setupTableView();
        customizeAddPermissionButton();
        customizeSyncButton();
        populatePermissions();
    }

    @FXML
    void addPermissionClicked() {
        if (systemAdministratorNavigationController == null ||
                !systemAdministratorNavigationController.requirePermission(PermissionConstants.SYSTEM_ADMIN_PERMISSIONS_CREATE, "create permissions")) {
            return;
        }
        openPermissionForm(null);
    }

    @FXML
    void syncPermissionsClicked() {
        if (systemAdministratorNavigationController == null ||
                !systemAdministratorNavigationController.requirePermission(PermissionConstants.SYSTEM_ADMIN_PERMISSIONS_MANAGE, "sync permissions")) {
            return;
        }
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Sync Permissions",
                "This will synchronize all permission constants from the system with the database.\n" +
                "Missing permissions will be created. Continue?"
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                var syncResult = systemAdministratorNavigationController.getMainViewController()
                        .getServiceFactory().getPermissionService().syncPermissionsFromConstants();

                // Log sync operation
                User currentUser = systemAdministratorNavigationController.getMainViewController().getUser();
                if (currentUser != null) {
                    LoggingUtility.logPermissionSync(currentUser, syncResult.getCreated(), syncResult.getExisting());
                }

                // Show results
                StringBuilder message = new StringBuilder();
                message.append("Synchronization completed!\n\n");
                message.append("Created: ").append(syncResult.getCreated()).append(" permissions\n");
                message.append("Existing: ").append(syncResult.getExisting()).append(" permissions\n\n");
                message.append("Assigned to System Administrator: ").append(syncResult.getSystemAdminAssigned()).append(" permission(s)\n\n");
                message.append("Assigned to Employee: ").append(syncResult.getEmployeeAssigned()).append(" permission(s)\n");
                message.append("Assigned to HR Administrator: ").append(syncResult.getHrAssigned()).append(" permission(s)\n");
                message.append("Assigned to Payroll Administrator: ").append(syncResult.getPayrollAssigned()).append(" permission(s)\n\n");
                
                if (!syncResult.getCreatedPermissions().isEmpty()) {
                    message.append("New permissions created:\n");
                    for (String perm : syncResult.getCreatedPermissions()) {
                        message.append("  • ").append(perm).append("\n");
                    }
                }

                CustomAlert successAlert = new CustomAlert(
                        Alert.AlertType.INFORMATION,
                        "Sync Complete",
                        message.toString()
                );
                successAlert.showAndWait();

                // Refresh the table
                populatePermissions();
            } catch (Exception e) {
                CustomAlert errorAlert = new CustomAlert(
                        Alert.AlertType.ERROR,
                        "Sync Error",
                        "Failed to sync permissions: " + e.getMessage()
                );
                errorAlert.showAndWait();
            }
        }
    }

    private void openPermissionForm(Permission permission) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/systemadmin/permission-form.fxml"));
            AnchorPane formPane = loader.load();
            Stage formStage = new Stage();
            formStage.setTitle(permission == null ? "Add New Permission" : "Edit Permission");
            formStage.setScene(new Scene(formPane));

            PermissionFormController formController = loader.getController();
            formController.setPermissionController(this);
            formController.setPermission(permission);
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
        TableColumn<Permission, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        idColumn.setPrefWidth(80);

        TableColumn<Permission, String> nameColumn = new TableColumn<>("Permission Name");
        nameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPermissionName()));
        nameColumn.setPrefWidth(200);

        TableColumn<Permission, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(cellData -> {
            String category = cellData.getValue().getCategory();
            return new javafx.beans.property.SimpleStringProperty(category != null ? category : "");
        });
        categoryColumn.setPrefWidth(150);

        TableColumn<Permission, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(cellData -> {
            String desc = cellData.getValue().getDescription();
            return new javafx.beans.property.SimpleStringProperty(desc != null ? desc : "");
        });
        descriptionColumn.setPrefWidth(400);

        TableColumn<Permission, Void> actionsColumn = createActionsColumn();
        actionsColumn.setPrefWidth(200);

        tvPermissions.getColumns().addAll(idColumn, nameColumn, categoryColumn, descriptionColumn, actionsColumn);
        tvPermissions.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private TableColumn<Permission, Void> createActionsColumn() {
        TableColumn<Permission, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button(null, new FontIcon(Feather.EDIT));
            private final Button deleteButton = new Button(null, new FontIcon(Feather.TRASH));

            {
                editButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
                deleteButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);

                editButton.setOnAction(event -> {
                    Permission selectedPermission = getTableView().getItems().get(getIndex());
                    if (selectedPermission != null) {
                        if (systemAdministratorNavigationController == null ||
                                !systemAdministratorNavigationController.requirePermission(PermissionConstants.SYSTEM_ADMIN_PERMISSIONS_EDIT, "edit permissions")) {
                            return;
                        }
                        openPermissionForm(selectedPermission);
                    }
                });

                deleteButton.setOnAction(event -> {
                    Permission selectedPermission = getTableView().getItems().get(getIndex());
                    if (selectedPermission != null) {
                        if (systemAdministratorNavigationController == null ||
                                !systemAdministratorNavigationController.requirePermission(PermissionConstants.SYSTEM_ADMIN_PERMISSIONS_DELETE, "delete permissions")) {
                            return;
                        }
                        deletePermission(selectedPermission);
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
                        systemAdministratorNavigationController.hasPermission(PermissionConstants.SYSTEM_ADMIN_PERMISSIONS_EDIT);
                boolean canDelete = systemAdministratorNavigationController != null &&
                        systemAdministratorNavigationController.hasPermission(PermissionConstants.SYSTEM_ADMIN_PERMISSIONS_DELETE);

                editButton.setDisable(!canEdit);
                deleteButton.setDisable(!canDelete);
                setGraphic(actionsBox);
            }
        });
        return actionsColumn;
    }

    private void deletePermission(Permission permission) {
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Delete Permission",
                "Are you sure you want to delete permission: " + permission.getPermissionName() + "?"
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            systemAdministratorNavigationController.getMainViewController()
                    .getServiceFactory().getPermissionService().deletePermission(permission);

            // Log permission deletion
            User currentUser = systemAdministratorNavigationController.getMainViewController().getUser();
            if (currentUser != null) {
                LoggingUtility.logPermissionDeleted(currentUser, permission);
            }

            CustomAlert successAlert = new CustomAlert(
                    Alert.AlertType.INFORMATION,
                    "Permission Deleted",
                    "Permission " + permission.getPermissionName() + " has been deleted."
            );
            successAlert.showAndWait();
            populatePermissions();
        }
    }

    private void customizeAddPermissionButton() {
        FontIcon fontIcon = new FontIcon(Feather.PLUS);
        btnAddPermission.setGraphic(fontIcon);
        btnAddPermission.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
    }

    private void customizeSyncButton() {
        FontIcon fontIcon = new FontIcon(Feather.REFRESH_CCW);
        btnSyncPermissions.setGraphic(fontIcon);
        btnSyncPermissions.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED);
    }

    public void populatePermissions() {
        if (systemAdministratorNavigationController == null) {
            return;
        }
        if (!systemAdministratorNavigationController.requirePermission(PermissionConstants.SYSTEM_ADMIN_PERMISSIONS_VIEW, "view permissions")) {
            if (tvPermissions != null) tvPermissions.setItems(FXCollections.observableArrayList());
            if (paginationPermissions != null) paginationPermissions.setPageCount(1);
            if (btnAddPermission != null) btnAddPermission.setDisable(true);
            if (btnSyncPermissions != null) btnSyncPermissions.setDisable(true);
            return;
        }
        if (btnAddPermission != null) {
            btnAddPermission.setDisable(!systemAdministratorNavigationController.hasPermission(PermissionConstants.SYSTEM_ADMIN_PERMISSIONS_CREATE));
        }
        if (btnSyncPermissions != null) {
            btnSyncPermissions.setDisable(!systemAdministratorNavigationController.hasPermission(PermissionConstants.SYSTEM_ADMIN_PERMISSIONS_MANAGE));
        }
        allPermissions = systemAdministratorNavigationController.getMainViewController()
                .getServiceFactory().getPermissionService().getAllPermissions();

        int itemsPerPage = 25;
        int pageCount = Math.max(1, (int) Math.ceil((double) allPermissions.size() / itemsPerPage));
        paginationPermissions.setPageCount(pageCount);

        paginationPermissions.setPageFactory(pageIndex -> {
            updateTableView(pageIndex, itemsPerPage);
            return new StackPane();
        });
    }

    private void updateTableView(int pageIndex, int itemsPerPage) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, allPermissions.size());
        List<Permission> pageData = allPermissions.subList(fromIndex, toIndex);
        tvPermissions.setItems(FXCollections.observableList(pageData));
    }
}

