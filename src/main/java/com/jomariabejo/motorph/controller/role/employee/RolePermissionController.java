package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.SystemAdministratorNavigationController;
import com.jomariabejo.motorph.constants.PermissionConstants;
import com.jomariabejo.motorph.model.Permission;
import com.jomariabejo.motorph.model.Role;
import com.jomariabejo.motorph.model.User;
import com.jomariabejo.motorph.utility.CustomAlert;
import com.jomariabejo.motorph.utility.LoggingUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class RolePermissionController {

    private SystemAdministratorNavigationController systemAdministratorNavigationController;

    @FXML
    private ListView<Role> lvRoles;

    @FXML
    private TextField tfSearchPermissions;

    @FXML
    private ComboBox<String> cbFilterCategory;

    @FXML
    private ScrollPane spPermissions;

    @FXML
    private VBox vbPermissionsContainer;

    @FXML
    private Button btnAssignAll;

    @FXML
    private Button btnRemoveAll;

    @FXML
    private Button btnSaveChanges;

    @FXML
    private Label lblSelectedRole;

    private static final String ALL_CATEGORIES = "All Categories";
    private static final String NO_ROLE_SELECTED = "No Role Selected";
    private static final String SELECT_ROLE_FIRST = "Please select a role first.";
    
    private Map<Permission, CheckBox> permissionCheckBoxMap;
    private Map<Integer, CheckBox> permissionIdCheckBoxMap; // Map Permission ID to CheckBox for reliable lookup
    private Map<String, VBox> categoryBoxMap;
    private Role selectedRole;
    private Set<Permission> originalPermissions;
    private Set<Permission> currentPermissions;

    public RolePermissionController() {
        permissionCheckBoxMap = new HashMap<>();
        permissionIdCheckBoxMap = new HashMap<>();
        categoryBoxMap = new HashMap<>();
        originalPermissions = new HashSet<>();
        currentPermissions = new HashSet<>();
    }

    @FXML
    void initialize() {
        setupRoleList();
        setupPermissionFilters();
        customizeButtons();
        populateRoles();
        populatePermissions();
    }

    private void setupRoleList() {
        lvRoles.setCellFactory(param -> new ListCell<Role>() {
            @Override
            protected void updateItem(Role role, boolean empty) {
                super.updateItem(role, empty);
                if (empty || role == null) {
                    setText(null);
                } else {
                    setText(role.getRoleName());
                }
            }
        });

        lvRoles.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectRole(newValue);
            }
        });
    }

    private void setupPermissionFilters() {
        cbFilterCategory.getItems().addAll(ALL_CATEGORIES);
        cbFilterCategory.getSelectionModel().selectFirst();
        
        cbFilterCategory.setOnAction(e -> filterPermissions());
        tfSearchPermissions.textProperty().addListener((observable, oldValue, newValue) -> filterPermissions());
    }

    private void customizeButtons() {
        FontIcon assignAllIcon = new FontIcon(Feather.CHECK_SQUARE);
        btnAssignAll.setGraphic(assignAllIcon);
        btnAssignAll.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);

        FontIcon removeAllIcon = new FontIcon(Feather.SQUARE);
        btnRemoveAll.setGraphic(removeAllIcon);
        btnRemoveAll.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);

        FontIcon saveIcon = new FontIcon(Feather.SAVE);
        btnSaveChanges.setGraphic(saveIcon);
        btnSaveChanges.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED);
    }

    public void populateRoles() {
        if (systemAdministratorNavigationController == null) {
            return;
        }
        if (!systemAdministratorNavigationController.requirePermission(PermissionConstants.SYSTEM_ADMIN_ROLE_PERMISSIONS_VIEW, "view role permissions")) {
            lvRoles.setItems(FXCollections.observableArrayList());
            return;
        }

        List<Role> roles = systemAdministratorNavigationController.getMainViewController()
                .getServiceFactory()
                .getRoleService()
                .getAllRoles();

        ObservableList<Role> roleList = FXCollections.observableArrayList(roles);
        lvRoles.setItems(roleList);
    }

    /**
     * Ensure the first role is selected and its permissions are loaded.
     * Should be called after both populateRoles() and populatePermissions() have completed.
     */
    public void ensureFirstRoleSelected() {
        if (!lvRoles.getItems().isEmpty() && !permissionIdCheckBoxMap.isEmpty()) {
            // Only proceed if we have roles and permissions are populated
            if (lvRoles.getSelectionModel().getSelectedItem() == null) {
                lvRoles.getSelectionModel().selectFirst();
            }
            Role firstSelectedRole = lvRoles.getSelectionModel().getSelectedItem();
            if (firstSelectedRole != null) {
                // Explicitly call selectRole to ensure permissions are loaded and checkboxes are updated
                selectRole(firstSelectedRole);
            }
        }
    }

    public void populatePermissions() {
        if (systemAdministratorNavigationController == null) {
            return;
        }
        if (!systemAdministratorNavigationController.requirePermission(PermissionConstants.SYSTEM_ADMIN_ROLE_PERMISSIONS_VIEW, "view role permissions")) {
            vbPermissionsContainer.getChildren().clear();
            return;
        }

        List<Permission> allPermissions = systemAdministratorNavigationController.getMainViewController()
                .getServiceFactory()
                .getPermissionService()
                .getAllPermissions();

        // Group permissions by category
        Map<String, List<Permission>> permissionsByCategory = allPermissions.stream()
                .collect(Collectors.groupingBy(
                        perm -> perm.getCategory() != null && !perm.getCategory().isEmpty() 
                                ? perm.getCategory() 
                                : "Uncategorized"
                ));

        // Update category filter
        List<String> categories = new ArrayList<>(permissionsByCategory.keySet());
        categories.sort(String::compareToIgnoreCase);
        cbFilterCategory.getItems().clear();
        cbFilterCategory.getItems().add(ALL_CATEGORIES);
        cbFilterCategory.getItems().addAll(categories);
        cbFilterCategory.getSelectionModel().selectFirst();

        // Clear existing containers
        vbPermissionsContainer.getChildren().clear();
        permissionCheckBoxMap.clear();
        permissionIdCheckBoxMap.clear();
        categoryBoxMap.clear();

        // Create UI for each category
        for (Map.Entry<String, List<Permission>> entry : permissionsByCategory.entrySet()) {
            String category = entry.getKey();
            List<Permission> permissions = entry.getValue();
            permissions.sort(Comparator.comparing(Permission::getPermissionName));

            // Create category label
            Label categoryLabel = new Label(category);
            categoryLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            categoryLabel.setPadding(new Insets(10, 0, 5, 0));

            // Create VBox for this category's permissions
            VBox categoryBox = new VBox(5);
            categoryBox.setPadding(new Insets(0, 0, 15, 0));
            categoryBox.getChildren().add(categoryLabel);

            // Create checkboxes for each permission
            for (Permission permission : permissions) {
                CheckBox checkBox = new CheckBox(permission.getPermissionName());
                checkBox.setUserData(permission);
                
                // Add description as tooltip if available
                if (permission.getDescription() != null && !permission.getDescription().isEmpty()) {
                    Tooltip tooltip = new Tooltip(permission.getDescription());
                    checkBox.setTooltip(tooltip);
                }

                checkBox.setOnAction(e -> updatePermissionSelection(permission, checkBox.isSelected()));

                categoryBox.getChildren().add(checkBox);
                permissionCheckBoxMap.put(permission, checkBox);
                // Also store by ID for reliable lookup when permissions are fetched from DB
                if (permission.getId() != null) {
                    permissionIdCheckBoxMap.put(permission.getId(), checkBox);
                }
            }

            categoryBoxMap.put(category, categoryBox);
            vbPermissionsContainer.getChildren().add(categoryBox);
        }
        
        // After permissions are populated, if a role is already selected, refresh its checkboxes
        if (selectedRole != null) {
            javafx.application.Platform.runLater(() -> selectRole(selectedRole));
        }
    }

    /**
     * Select a role and load its permissions.
     * Can be called externally to programmatically select a role.
     */
    public void selectRole(Role role) {
        selectedRole = role;
        lblSelectedRole.setText("Selected Role: " + role.getRoleName());

        // Get current permissions for this role
        if (systemAdministratorNavigationController != null) {
            List<Permission> rolePermissions = systemAdministratorNavigationController.getMainViewController()
                    .getServiceFactory()
                    .getRolePermissionService()
                    .getPermissionsForRole(role);

            // Convert to HashSet, ensuring we use permissions with IDs for proper comparison
            originalPermissions = new HashSet<>();
            currentPermissions = new HashSet<>();
            
            for (Permission perm : rolePermissions) {
                if (perm != null && perm.getId() != null) {
                    originalPermissions.add(perm);
                    currentPermissions.add(perm);
                }
            }

            // Update checkboxes
            updateCheckBoxes();
        }
    }

    private void updateCheckBoxes() {
        // Clear all checkboxes first
        for (CheckBox checkBox : permissionCheckBoxMap.values()) {
            checkBox.setSelected(false);
        }

        // Check boxes for current permissions using ID-based lookup
        // This ensures we find the correct checkbox even if Permission objects are different instances
        for (Permission permission : currentPermissions) {
            if (permission != null && permission.getId() != null) {
                CheckBox checkBox = permissionIdCheckBoxMap.get(permission.getId());
                if (checkBox != null) {
                    checkBox.setSelected(true);
                } else {
                    // Fallback: try object-based lookup
                    checkBox = permissionCheckBoxMap.get(permission);
                    if (checkBox != null) {
                        checkBox.setSelected(true);
                    }
                }
            }
        }
    }

    private void updatePermissionSelection(Permission permission, boolean selected) {
        if (permission == null) {
            return;
        }
        
        // Find the actual permission object from currentPermissions by ID
        // This ensures we're working with the same permission instance
        Permission actualPermission = currentPermissions.stream()
                .filter(p -> p != null && p.getId() != null && p.getId().equals(permission.getId()))
                .findFirst()
                .orElse(permission);
        
        if (selected) {
            currentPermissions.add(actualPermission);
        } else {
            currentPermissions.removeIf(p -> p != null && p.getId() != null && p.getId().equals(permission.getId()));
        }
    }

    private void filterPermissions() {
        String searchText = tfSearchPermissions.getText().toLowerCase();
        String selectedCategory = cbFilterCategory.getSelectionModel().getSelectedItem();

        boolean showAllCategories = selectedCategory == null || selectedCategory.equals(ALL_CATEGORIES);

        // Show/hide category boxes and permissions
        for (Map.Entry<String, VBox> entry : categoryBoxMap.entrySet()) {
            String category = entry.getKey();
            VBox categoryBox = entry.getValue();

            boolean categoryMatches = showAllCategories || category.equals(selectedCategory);
            boolean hasVisiblePermissions = false;

            // Show/hide individual checkboxes
            for (int i = 1; i < categoryBox.getChildren().size(); i++) {
                CheckBox checkBox = (CheckBox) categoryBox.getChildren().get(i);
                Permission permission = (Permission) checkBox.getUserData();

                boolean matchesSearch = searchText.isEmpty() || 
                        permission.getPermissionName().toLowerCase().contains(searchText) ||
                        (permission.getDescription() != null && 
                         permission.getDescription().toLowerCase().contains(searchText));

                boolean visible = categoryMatches && matchesSearch;
                checkBox.setVisible(visible);
                checkBox.setManaged(visible);

                if (visible) {
                    hasVisiblePermissions = true;
                }
            }

            // Show/hide category label
            Label categoryLabel = (Label) categoryBox.getChildren().get(0);
            categoryLabel.setVisible(hasVisiblePermissions);
            categoryLabel.setManaged(hasVisiblePermissions);
            categoryBox.setVisible(hasVisiblePermissions);
            categoryBox.setManaged(hasVisiblePermissions);
        }
    }

    @FXML
    void assignAllClicked() {
        if (selectedRole == null) {
            showAlert(Alert.AlertType.WARNING, NO_ROLE_SELECTED, SELECT_ROLE_FIRST);
            return;
        }
        if (systemAdministratorNavigationController == null ||
                !systemAdministratorNavigationController.requirePermission(PermissionConstants.SYSTEM_ADMIN_ROLE_PERMISSIONS_ASSIGN, "assign role permissions")) {
            return;
        }

        // Add all visible permissions
        for (CheckBox checkBox : permissionCheckBoxMap.values()) {
            if (checkBox.isVisible() && checkBox.isManaged()) {
                Permission permission = (Permission) checkBox.getUserData();
                if (permission != null && permission.getId() != null) {
                    checkBox.setSelected(true);
                    // Add permission if not already in set (by ID)
                    boolean alreadyExists = currentPermissions.stream()
                            .anyMatch(p -> p != null && p.getId() != null && p.getId().equals(permission.getId()));
                    if (!alreadyExists) {
                        currentPermissions.add(permission);
                    }
                }
            }
        }
    }

    @FXML
    void removeAllClicked() {
        if (selectedRole == null) {
            showAlert(Alert.AlertType.WARNING, NO_ROLE_SELECTED, SELECT_ROLE_FIRST);
            return;
        }
        if (systemAdministratorNavigationController == null ||
                !systemAdministratorNavigationController.requirePermission(PermissionConstants.SYSTEM_ADMIN_ROLE_PERMISSIONS_REMOVE, "remove role permissions")) {
            return;
        }

        // Remove all visible permissions
        for (CheckBox checkBox : permissionCheckBoxMap.values()) {
            if (checkBox.isVisible() && checkBox.isManaged()) {
                Permission permission = (Permission) checkBox.getUserData();
                if (permission != null && permission.getId() != null) {
                    checkBox.setSelected(false);
                    // Remove permission by ID
                    currentPermissions.removeIf(p -> p != null && p.getId() != null && p.getId().equals(permission.getId()));
                }
            }
        }
    }

    @FXML
    void saveChangesClicked() {
        if (selectedRole == null) {
            showAlert(Alert.AlertType.WARNING, NO_ROLE_SELECTED, SELECT_ROLE_FIRST);
            return;
        }

        if (systemAdministratorNavigationController == null) {
            return;
        }
        if (!systemAdministratorNavigationController.requirePermission(PermissionConstants.SYSTEM_ADMIN_ROLE_PERMISSIONS_ASSIGN, "save role permission changes")) {
            return;
        }

        try {
            var rolePermissionService = systemAdministratorNavigationController.getMainViewController()
                    .getServiceFactory()
                    .getRolePermissionService();

            // Find permissions to add and remove using ID-based comparison
            Set<Integer> currentPermissionIds = new HashSet<>();
            for (Permission p : currentPermissions) {
                if (p != null && p.getId() != null) {
                    currentPermissionIds.add(p.getId());
                }
            }
            
            Set<Integer> originalPermissionIds = new HashSet<>();
            for (Permission p : originalPermissions) {
                if (p != null && p.getId() != null) {
                    originalPermissionIds.add(p.getId());
                }
            }
            
            // Find permissions to add (in current but not in original)
            Set<Permission> toAdd = new HashSet<>();
            for (Permission p : currentPermissions) {
                if (p != null && p.getId() != null && !originalPermissionIds.contains(p.getId())) {
                    toAdd.add(p);
                }
            }
            
            // Find permissions to remove (in original but not in current)
            Set<Permission> toRemove = new HashSet<>();
            for (Permission p : originalPermissions) {
                if (p != null && p.getId() != null && !currentPermissionIds.contains(p.getId())) {
                    toRemove.add(p);
                }
            }

            // Apply changes
            int added = rolePermissionService.assignPermissionsToRole(selectedRole, new ArrayList<>(toAdd));
            int removed = rolePermissionService.removePermissionsFromRole(selectedRole, new ArrayList<>(toRemove));

            // Log permission assignments and removals
            User currentUser = systemAdministratorNavigationController.getMainViewController().getUser();
            if (currentUser != null) {
                for (Permission permission : toAdd) {
                    LoggingUtility.logPermissionAssigned(currentUser, selectedRole, permission);
                }
                for (Permission permission : toRemove) {
                    LoggingUtility.logPermissionRemoved(currentUser, selectedRole, permission);
                }
            }

            // Clear permission cache
            if (selectedRole.getId() != null) {
                com.jomariabejo.motorph.utility.PermissionChecker.clearRoleCache(selectedRole.getId());
            }

            // Update original permissions
            originalPermissions = new HashSet<>(currentPermissions);

            // Reload permissions from database to ensure we have the latest state
            // This ensures checkboxes reflect the saved state when navigating back
            if (selectedRole != null) {
                List<Permission> refreshedPermissions = systemAdministratorNavigationController.getMainViewController()
                        .getServiceFactory()
                        .getRolePermissionService()
                        .getPermissionsForRole(selectedRole);
                
                originalPermissions = new HashSet<>(refreshedPermissions);
                currentPermissions = new HashSet<>(refreshedPermissions);
                
                // Update checkboxes with refreshed permissions
                updateCheckBoxes();
            }

            showAlert(Alert.AlertType.INFORMATION, "Success", 
                    String.format("Permissions updated successfully.%nAdded: %d%nRemoved: %d", added, removed));

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save permission changes: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        CustomAlert alert = new CustomAlert(alertType, title, message);
        alert.showAndWait();
    }
}
