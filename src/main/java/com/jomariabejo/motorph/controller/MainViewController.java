package com.jomariabejo.motorph.controller;

import atlantafx.base.theme.*;
import com.jomariabejo.motorph.Launcher;
import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import com.jomariabejo.motorph.controller.nav.HumanResourceAdministratorNavigationController;
import com.jomariabejo.motorph.controller.nav.PayrollAdministratorNavigationController;
import com.jomariabejo.motorph.constants.PermissionConstants;
import com.jomariabejo.motorph.controller.nav.SystemAdministratorNavigationController;
import com.jomariabejo.motorph.model.*;
import com.jomariabejo.motorph.service.*;
import com.jomariabejo.motorph.utility.LoggingUtility;
import com.jomariabejo.motorph.utility.PermissionChecker;
import com.jomariabejo.motorph.utility.SessionManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.stage.Stage;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@Getter
@Setter
public class MainViewController implements _ViewLoader {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private Label selectedButtonLabel;

    @FXML
    private Label lblEmployeeName;

    @FXML
    private Label lblRoleName;

    @FXML
    private MenuItem menuItemEmployee;

    @FXML
    private MenuItem menuItemHumanResource;

    @FXML
    private MenuItem menuItemAccounting;

    @FXML
    private MenuItem menuItemSystemAdmin;

    @FXML
    private Menu menuRoles;

    @FXML
    private MenuButton btnNotifications;

    @FXML
    private MenuButton btnMessages;

    @FXML
    private MenuButton btnSettings;

    private Employee employee;
    private User user;

    private ServiceFactory serviceFactory;
    
    // Store current navigation controller reference
    private Object currentNavigationController;
    
    // Store previous navigation controller when switching to Employee navigation for messages/notifications
    private Object previousNavigationController;

    // Auto-refresh dropdowns
    private Timeline dropdownRefreshTimeline;
    private Integer lastNotificationTopId;
    private long lastNotificationUnreadCount = -1;

    public MainViewController() {
        serviceFactory = new ServiceFactory();
    }

    public void rewriteLabel(String string) {
        selectedButtonLabel.setText(string);
    }

    public void initializeUserNavigation() {
        // Ensure System Administrator permissions are seeded/assigned automatically.
        // This prevents "no permission" issues when role_permission is not pre-seeded in the DB.
        try {
            if (user != null && user.getRoleID() != null && user.getRoleID().getRoleName() != null
                    && "System Administrator".equalsIgnoreCase(user.getRoleID().getRoleName())) {
                boolean hasSystemAdminDashboard = PermissionChecker.hasPermission(
                        user, PermissionConstants.SYSTEM_ADMIN_DASHBOARD_VIEW, serviceFactory);
                boolean hasSystemAdminAnnouncements = PermissionChecker.hasPermission(
                        user, PermissionConstants.SYSTEM_ADMIN_ANNOUNCEMENTS_MANAGE, serviceFactory);

                // If any core System Admin permission is missing, resync to pick up new constants and assign them.
                if (!hasSystemAdminDashboard || !hasSystemAdminAnnouncements) {
                    serviceFactory.getPermissionService().syncPermissionsFromConstants();
                }
            }
        } catch (Exception ignored) {
            // Best-effort: navigation should still work even if sync fails.
        }

        displayRoleName();
        rebuildRoleMenu();
        initializeDropdowns();
        
        // Route to appropriate navigation based on user's role and permissions
        if (user != null && user.getRoleID() != null) {
            String roleName = user.getRoleID().getRoleName();
            
            if (roleName != null) {
                // Prefer role-name routing first (works even when the permission table is not fully seeded)
                if ("System Administrator".equalsIgnoreCase(roleName)) {
                    displaySystemAdminNavigation();
                } else if ("HR Administrator".equalsIgnoreCase(roleName) || "Human Resource Administrator".equalsIgnoreCase(roleName)) {
                    displayHumanResourceNavigation();
                } else if ("Payroll Administrator".equalsIgnoreCase(roleName) || "Accounting Administrator".equalsIgnoreCase(roleName)) {
                    displayAccountingNavigation();
                }
                // Fall back to permissions to determine which navigation to show
                else if (PermissionChecker.hasPermission(user, PermissionConstants.HR_DASHBOARD_VIEW, serviceFactory)) {
                    displayHumanResourceNavigation();
                } else if (PermissionChecker.hasPermission(user, PermissionConstants.PAYROLL_DASHBOARD_VIEW, serviceFactory)) {
                    displayAccountingNavigation();
                } else if (PermissionChecker.hasPermission(user, PermissionConstants.SYSTEM_ADMIN_DASHBOARD_VIEW, serviceFactory)) {
                    displaySystemAdminNavigation();
                } else if (PermissionChecker.hasPermission(user, PermissionConstants.EMPLOYEE_PROFILE_VIEW, serviceFactory)) {
                    displayEmployeeRoleNavigation();
                } else {
                    // Default to employee navigation if no specific permission found
                    displayEmployeeRoleNavigation();
                }
            } else {
                // Default to employee navigation if role name is null
                displayEmployeeRoleNavigation();
            }
        } else {
            // Default to employee navigation if user or role is null
            displayEmployeeRoleNavigation();
        }
    }

    /**
     * Rebuild the "Switch Role" menu based on roles assigned to the user (multi-role).
     * This is intentionally role-driven (user_role) rather than permission-driven.
     */
    private void rebuildRoleMenu() {
        if (menuRoles == null) {
            return;
        }

        // Clear existing items (FXML defaults) and repopulate from assigned roles
        menuRoles.getItems().clear();

        if (user == null) {
            MenuItem none = new MenuItem("No user");
            none.setDisable(true);
            menuRoles.getItems().add(none);
            return;
        }

        // Build a unique role set: primary role + join-table roles
        Set<Role> roleSet = new HashSet<>();
        if (user.getRoleID() != null) {
            roleSet.add(user.getRoleID());
        }
        if (user.getRoles() != null) {
            roleSet.addAll(user.getRoles());
        }

        List<Role> roles = new ArrayList<>(roleSet);
        roles.sort((a, b) -> {
            String an = a != null && a.getRoleName() != null ? a.getRoleName() : "";
            String bn = b != null && b.getRoleName() != null ? b.getRoleName() : "";
            return an.compareToIgnoreCase(bn);
        });

        if (roles.isEmpty()) {
            MenuItem none = new MenuItem("No roles assigned");
            none.setDisable(true);
            menuRoles.getItems().add(none);
            return;
        }

        for (Role role : roles) {
            if (role == null || role.getRoleName() == null) continue;
            final String roleName = role.getRoleName();
            MenuItem item = new MenuItem(roleName);
            item.setOnAction(e -> switchNavigationForRoleName(roleName));
            menuRoles.getItems().add(item);
        }
    }

    private void switchNavigationForRoleName(String roleName) {
        // Clear previous navigation when manually switching roles
        previousNavigationController = null;

        if (roleName == null) {
            displayEmployeeRoleNavigation();
            return;
        }

        if ("System Administrator".equalsIgnoreCase(roleName)) {
            displaySystemAdminNavigation();
        } else if ("HR Administrator".equalsIgnoreCase(roleName) || "Human Resource Administrator".equalsIgnoreCase(roleName)) {
            displayHumanResourceNavigation();
        } else if ("Payroll Administrator".equalsIgnoreCase(roleName) || "Accounting Administrator".equalsIgnoreCase(roleName)) {
            displayAccountingNavigation();
        } else {
            // Default to employee navigation for any non-admin roles
            displayEmployeeRoleNavigation();
        }
    }

    /**
     * Refresh the \"Switch Role\" menu visibility (useful after updating user roles at runtime).
     */
    public void refreshRoleMenu() {
        rebuildRoleMenu();
    }

    private void displayRoleName() {
        lblRoleName.setText(employee.getPositionID().getPositionName());
    }

    private void displayHumanResourceNavigation() {
        // Clear center pane before loading new navigation
        mainBorderPane.setCenter(null);
        loadView("/com/jomariabejo/motorph/nav/role-human-resource.fxml", controller -> {
            if (controller instanceof HumanResourceAdministratorNavigationController) {
                HumanResourceAdministratorNavigationController humanResourceController = (HumanResourceAdministratorNavigationController) controller;
                humanResourceController.setMainViewController(this);
                currentNavigationController = humanResourceController;
                // updateButtonVisibility() is now called inside setMainViewController()
                // Call dashboard after a small delay to ensure mainViewController is set
                javafx.application.Platform.runLater(() -> {
                    if (humanResourceController.getMainViewController() != null) {
                        humanResourceController.humanResourceDashboardOnAction();
                    }
                });
            }
        });
    }


    private void displayAccountingNavigation() {
        // Clear center pane before loading new navigation
        mainBorderPane.setCenter(null);
        loadView("/com/jomariabejo/motorph/nav/role-accounting.fxml", controller -> {
            if (controller instanceof PayrollAdministratorNavigationController) {
                PayrollAdministratorNavigationController accountingController = (PayrollAdministratorNavigationController) controller;
                accountingController.setMainViewController(this);
                currentNavigationController = accountingController;
                // updateButtonVisibility() is now called inside setMainViewController()
                // Call dashboard after a small delay to ensure mainViewController is set
                javafx.application.Platform.runLater(() -> {
                    if (accountingController.getMainViewController() != null) {
                        accountingController.dashboardOnActtion();
                    }
                });
            }
        });
    }

    private void displaySystemAdminNavigation() {
        // Clear center pane before loading new navigation
        mainBorderPane.setCenter(null);
        loadView("/com/jomariabejo/motorph/nav/role-system-administrator.fxml", controller -> {
            if (controller instanceof SystemAdministratorNavigationController) {
                SystemAdministratorNavigationController systemAdminController = (SystemAdministratorNavigationController) controller;
                systemAdminController.setMainViewController(this);
                currentNavigationController = systemAdminController;
                // updateButtonVisibility() is now called inside setMainViewController()
                // Call dashboard after a small delay to ensure mainViewController is set
                javafx.application.Platform.runLater(() -> {
                    if (systemAdminController.getMainViewController() != null) {
                        systemAdminController.dashboard();
                    }
                });
            }
        });
    }

    private void displayEmployeeRoleNavigation() {
        displayEmployeeRoleNavigation(true);
    }

    /**
     * Display employee role navigation
     * @param loadDefaultView if true, loads profile view by default; if false, just loads navigation
     */
    private void displayEmployeeRoleNavigation(boolean loadDefaultView) {
        // Clear center pane before loading new navigation
        mainBorderPane.setCenter(null);
        loadView("/com/jomariabejo/motorph/nav/role-employee.fxml", controller -> {
            if (controller instanceof EmployeeRoleNavigationController) {
                EmployeeRoleNavigationController employeeController = (EmployeeRoleNavigationController) controller;
                employeeController.setMainViewController(this);
                currentNavigationController = employeeController;
                // updateButtonVisibility() is now called inside setMainViewController()
                
                // Show/hide back button based on whether we came from another role
                javafx.application.Platform.runLater(() -> {
                    if (employeeController.getMainViewController() != null) {
                        if (previousNavigationController != null) {
                            employeeController.showBackButton(previousNavigationController);
                        } else {
                            employeeController.hideBackButton();
                        }
                        
                        if (loadDefaultView) {
                            // Call myProfileOnAction after a small delay to ensure mainViewController is set
                            employeeController.myProfileOnAction();
                            employeeController.displayWelcome();
                        } else {
                            // Just display welcome message without loading profile
                            employeeController.displayWelcome();
                        }
                    }
                });
            }
        });
    }

    /**
     * Menu roles of the main view🫴
     */
    public void menuItemEmployeeOnAction(ActionEvent actionEvent) {
        // Clear previous navigation when manually switching roles
        previousNavigationController = null;
        displayEmployeeRoleNavigation();
    }

    public void menuItemHumanResourceOnAction(ActionEvent actionEvent) {
        // Clear previous navigation when manually switching roles
        previousNavigationController = null;
        displayHumanResourceNavigation();
    }

    public void menuItemAccountingOnAction(ActionEvent actionEvent) {
        // Clear previous navigation when manually switching roles
        previousNavigationController = null;
        displayAccountingNavigation();
    }

    public void menuISystemAdminOnAction(ActionEvent actionEvent) {
        // Clear previous navigation when manually switching roles
        previousNavigationController = null;
        displaySystemAdminNavigation();
    }

    @Override
    public <T> void loadView(String fxmlPath, Consumer<T> controllerInitializer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

            // Load the UI (AnchorPane)
            AnchorPane pane = loader.load();
            // Set navigation in the left pane - this replaces any existing navigation
            mainBorderPane.setLeft(pane);
            // Ensure the left pane is visible and managed
            pane.setVisible(true);
            pane.setManaged(true);

            // Initialize the controller
            T controller = loader.getController();
            controllerInitializer.accept(controller);
        } catch (IOException ioException) {
            throw new RuntimeException("Failed to load view: " + fxmlPath, ioException);
        }
    }

    /**
     * Navigate back to previous role navigation
     */
    public void navigateBackToPreviousRole() {
        if (previousNavigationController == null) {
            return;
        }
        
        // Navigate back based on previous navigation type
        if (previousNavigationController instanceof SystemAdministratorNavigationController) {
            previousNavigationController = null;
            displaySystemAdminNavigation();
        } else if (previousNavigationController instanceof HumanResourceAdministratorNavigationController) {
            previousNavigationController = null;
            displayHumanResourceNavigation();
        } else if (previousNavigationController instanceof PayrollAdministratorNavigationController) {
            previousNavigationController = null;
            displayAccountingNavigation();
        } else {
            // Unknown type, clear and default to employee
            previousNavigationController = null;
            displayEmployeeRoleNavigation();
        }
    }

    public void logoutClicked(ActionEvent actionEvent) {
        // Clear previous navigation when logging out
        previousNavigationController = null;
        if (dropdownRefreshTimeline != null) {
            dropdownRefreshTimeline.stop();
            dropdownRefreshTimeline = null;
        }
        // Log logout action before clearing session
        if (user != null) {
            LoggingUtility.logLogout(user);
        }
        // Clear saved session on logout
        SessionManager.clearSession();
        // Switch back to login screen
        Launcher.switchToLoginView();
    }

    public void lightModeClicked(ActionEvent actionEvent) {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
    }

    public void darkModeClicked(ActionEvent actionEvent) {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
    }

    public void nordLightClicked(ActionEvent actionEvent) {
        Application.setUserAgentStylesheet(new NordLight().getUserAgentStylesheet());
    }

    public void nordDarkClicked(ActionEvent actionEvent) {
        Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());
    }

    public void cupertinoLightClicked(ActionEvent actionEvent) {
        Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());
    }

    public void cupertinoDarkLight(ActionEvent actionEvent) {
        Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());
    }

    public void draculaClicked(ActionEvent actionEvent) {
        Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());
    }

    public void displayEmployeeName() {
        this.lblEmployeeName.setText(
                employee.getFirstName() + " " + employee.getLastName()
        );
    }

    /**
     * Initialize all dropdown menus (notifications, messages, settings)
     */
    private void initializeDropdowns() {
        if (employee == null) {
            return;
        }
        setupDropdownAutoRefresh();
        populateNotificationsDropdown();
        populateMessagesDropdown();
        populateSettingsDropdown();
    }

    private void setupDropdownAutoRefresh() {
        // Refresh immediately when the dropdown is opened (best UX).
        if (btnNotifications != null) {
            btnNotifications.setOnShowing(e -> populateNotificationsDropdown());
        }
        if (btnMessages != null) {
            btnMessages.setOnShowing(e -> populateMessagesDropdown());
        }

        if (dropdownRefreshTimeline != null) {
            dropdownRefreshTimeline.stop();
        }

        // Poll periodically to surface new notifications without requiring navigation.
        dropdownRefreshTimeline = new Timeline(
                new KeyFrame(Duration.seconds(5), e -> refreshNotificationsIfChanged())
        );
        dropdownRefreshTimeline.setCycleCount(Timeline.INDEFINITE);
        dropdownRefreshTimeline.play();
    }

    private void refreshNotificationsIfChanged() {
        if (btnNotifications == null || employee == null) {
            return;
        }
        try {
            List<Notification> notifications = serviceFactory.getNotificationService()
                    .getNotificationsForEmployee(employee);
            long unreadCount = serviceFactory.getNotificationService()
                    .getUnreadNotificationCount(employee);

            Integer topId = (notifications != null && !notifications.isEmpty())
                    ? notifications.get(0).getId()
                    : null;

            boolean changed = (topId != null ? !topId.equals(lastNotificationTopId) : lastNotificationTopId != null)
                    || unreadCount != lastNotificationUnreadCount;

            if (changed) {
                lastNotificationTopId = topId;
                lastNotificationUnreadCount = unreadCount;
                populateNotificationsDropdown();
            }
        } catch (Exception ignored) {
            // Best-effort; ignore polling failures.
        }
    }

    /**
     * Refresh all dropdown menus
     * Can be called externally to update dropdowns after actions
     */
    public void refreshDropdowns() {
        if (employee != null) {
            populateNotificationsDropdown();
            populateMessagesDropdown();
            // Settings dropdown doesn't need refreshing as it's static
        }
    }

    /**
     * Populate notifications dropdown with actual notifications for the current employee
     */
    private void populateNotificationsDropdown() {
        if (btnNotifications == null || employee == null) {
            return;
        }

        btnNotifications.getItems().clear();

        try {
            List<Notification> notifications = serviceFactory.getNotificationService()
                    .getNotificationsForEmployee(employee);
            long unreadCount = serviceFactory.getNotificationService()
                    .getUnreadNotificationCount(employee);

            // Update change-detection snapshot for polling
            lastNotificationUnreadCount = unreadCount;
            lastNotificationTopId = (!notifications.isEmpty() ? notifications.get(0).getId() : null);

            // Reset button text
            btnNotifications.setText("");
            
            if (notifications.isEmpty()) {
                MenuItem noNotifications = new MenuItem("No notifications");
                noNotifications.setDisable(true);
                btnNotifications.getItems().add(noNotifications);
            } else {
                // Show unread count in button text if there are unread notifications
                if (unreadCount > 0) {
                    btnNotifications.setText("Notifications (" + unreadCount + ")");
                }

                for (Notification notification : notifications) {
                    MenuItem item = createNotificationMenuItem(notification);
                    btnNotifications.getItems().add(item);
                }
                
                // Add separator and "View All" option
                btnNotifications.getItems().add(new SeparatorMenuItem());
                MenuItem viewAllItem = new MenuItem("View All Notifications");
                FontIcon viewAllIcon = new FontIcon(Feather.EYE);
                viewAllItem.setGraphic(viewAllIcon);
                viewAllItem.setOnAction(e -> navigateToNotificationsView());
                btnNotifications.getItems().add(viewAllItem);
            }
        } catch (Exception e) {
            MenuItem errorItem = new MenuItem("Error loading notifications");
            errorItem.setDisable(true);
            btnNotifications.getItems().add(errorItem);
        }
    }

    /**
     * Create a styled notification MenuItem with icon, timestamp, and read/unread indicator
     */
    private MenuItem createNotificationMenuItem(Notification notification) {
        MenuItem item = new MenuItem();
        
        // Create HBox for custom layout
        HBox contentBox = new HBox(8);
        contentBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        // Add icon based on notification type
        FontIcon icon = new FontIcon(Feather.BELL);
        String type = notification.getNotificationType() != null ? notification.getNotificationType().toLowerCase() : "";
        if (type.contains("leave")) {
            icon = new FontIcon(Feather.CALENDAR);
        } else if (type.contains("payroll") || type.contains("payslip")) {
            icon = new FontIcon(Feather.DOLLAR_SIGN);
        } else if (type.contains("overtime")) {
            icon = new FontIcon(Feather.CLOCK);
        } else if (type.contains("reimbursement")) {
            icon = new FontIcon(Feather.DOLLAR_SIGN);
        }
        
        // Style unread notifications
        if (!notification.getReadStatus()) {
            icon.getStyleClass().add("unread-notification");
            icon.setStyle("-fx-text-fill: #1976d2;");
        }
        
        // Create VBox for text content
        VBox textBox = new VBox(2);
        String content = notification.getNotificationContent();
        if (content.length() > 60) {
            content = content.substring(0, 57) + "...";
        }
        
        Label contentLabel = new Label(content);
        contentLabel.setFont(Font.font(14));
        if (!notification.getReadStatus()) {
            contentLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1976d2;");
        }
        
        // Add timestamp
        String timeAgo = formatTimeAgo(notification.getTimestamp());
        Label timeLabel = new Label(timeAgo);
        timeLabel.setFont(Font.font(10));
        timeLabel.setStyle("-fx-text-fill: #666;");
        
        textBox.getChildren().addAll(contentLabel, timeLabel);
        
        contentBox.getChildren().addAll(icon, textBox);
        item.setGraphic(contentBox);
        
                    final Notification notif = notification;
                    item.setOnAction(e -> {
                        // Mark as read when clicked
                        if (!notif.getReadStatus()) {
                            notif.setReadStatus(true);
                            serviceFactory.getNotificationService().updateNotification(notif);
                            // Refresh all dropdowns
                            refreshDropdowns();
                        }
                    });
        
        return item;
    }

    /**
     * Format timestamp as relative time (e.g., "2 hours ago", "Yesterday")
     */
    private String formatTimeAgo(Instant timestamp) {
        if (timestamp == null) {
            return "";
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime time = LocalDateTime.ofInstant(timestamp, ZoneId.systemDefault());
        
        long minutes = ChronoUnit.MINUTES.between(time, now);
        if (minutes < 1) {
            return "Just now";
        } else if (minutes < 60) {
            return minutes + " min ago";
        }
        
        long hours = ChronoUnit.HOURS.between(time, now);
        if (hours < 24) {
            return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
        }
        
        long days = ChronoUnit.DAYS.between(time, now);
        if (days == 1) {
            return "Yesterday";
        } else if (days < 7) {
            return days + " days ago";
        }
        
        return time.format(DateTimeFormatter.ofPattern("MMM d, yyyy"));
    }

    /**
     * Populate messages dropdown with actual conversations for the current employee
     */
    private void populateMessagesDropdown() {
        if (btnMessages == null || employee == null) {
            return;
        }

        btnMessages.getItems().clear();

        try {
            // Add "New Message" option at the top
            MenuItem newMessageItem = new MenuItem("New Message");
            FontIcon newMessageIcon = new FontIcon(Feather.PLUS);
            newMessageIcon.setStyle("-fx-text-fill: #4caf50;");
            newMessageItem.setGraphic(newMessageIcon);
            newMessageItem.setOnAction(e -> openNewMessageDialog());
            btnMessages.getItems().add(newMessageItem);
            btnMessages.getItems().add(new SeparatorMenuItem());

            List<Conversation> conversations = serviceFactory.getConversationService()
                    .getConversationsForEmployee(employee);
            long unreadCount = serviceFactory.getConversationService()
                    .getUnreadMessageCount(employee);

            // Reset button text
            btnMessages.setText("");
            
            if (conversations.isEmpty()) {
                MenuItem noMessages = new MenuItem("No messages");
                noMessages.setDisable(true);
                btnMessages.getItems().add(noMessages);
            } else {
                // Show unread count in button text if there are unread messages
                if (unreadCount > 0) {
                    btnMessages.setText("Messages (" + unreadCount + ")");
                }

                for (Conversation conversation : conversations) {
                    MenuItem item = createConversationMenuItem(conversation);
                    btnMessages.getItems().add(item);
                }
                
                // Add separator and "View All" option
                btnMessages.getItems().add(new SeparatorMenuItem());
                MenuItem viewAllItem = new MenuItem("View All Messages");
                FontIcon viewAllIcon = new FontIcon(Feather.MESSAGE_CIRCLE);
                viewAllItem.setGraphic(viewAllIcon);
                viewAllItem.setOnAction(e -> navigateToConversationsView());
                btnMessages.getItems().add(viewAllItem);
            }
        } catch (Exception e) {
            MenuItem errorItem = new MenuItem("Error loading messages");
            errorItem.setDisable(true);
            btnMessages.getItems().add(errorItem);
        }
    }

    /**
     * Create a styled conversation MenuItem with participant info, timestamp, and unread indicator
     */
    private MenuItem createConversationMenuItem(Conversation conversation) {
        MenuItem item = new MenuItem();
        
        // Create HBox for custom layout
        HBox contentBox = new HBox(8);
        contentBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        // Determine the other participant
        Employee otherParticipant = conversation.getParticipant1Employee().getEmployeeNumber().equals(employee.getEmployeeNumber())
                ? conversation.getParticipant2Employee() 
                : conversation.getParticipant1Employee();
        
        // Add avatar/icon
        FontIcon icon = new FontIcon(Feather.USER);
        boolean hasUnread = conversation.getUnreadCount() != null && conversation.getUnreadCount() > 0;
        if (hasUnread) {
            icon.setStyle("-fx-text-fill: #1976d2;");
        }
        
        // Create VBox for text content
        VBox textBox = new VBox(2);
        String participantName = otherParticipant.getFirstName() + " " + otherParticipant.getLastName();
        String lastMessage = conversation.getLastMessageContent();
        if (lastMessage.length() > 50) {
            lastMessage = lastMessage.substring(0, 47) + "...";
        }
        
        Label nameLabel = new Label(participantName);
        nameLabel.setFont(Font.font(14));
        if (hasUnread) {
            nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1976d2;");
        }
        
        Label messageLabel = new Label(lastMessage);
        messageLabel.setFont(Font.font(12));
        messageLabel.setStyle("-fx-text-fill: #666;");
        
        // Add timestamp
        String timeAgo = formatTimeAgo(conversation.getLastMessageTimestamp());
        Label timeLabel = new Label(timeAgo);
        timeLabel.setFont(Font.font(10));
        timeLabel.setStyle("-fx-text-fill: #999;");
        
        textBox.getChildren().addAll(nameLabel, messageLabel, timeLabel);
        
        contentBox.getChildren().addAll(icon, textBox);
        item.setGraphic(contentBox);
        
        // Store conversation ID for navigation
        final Conversation conv = conversation;
        item.setOnAction(e -> navigateToConversation(conv));
        
        return item;
    }

    /**
     * Populate settings dropdown with My Profile and Logout options
     */
    private void populateSettingsDropdown() {
        if (btnSettings == null) {
            return;
        }

        btnSettings.getItems().clear();

        // Add My Profile option
        MenuItem myProfileItem = new MenuItem("My Profile");
        myProfileItem.setOnAction(e -> navigateToMyProfile());
        btnSettings.getItems().add(myProfileItem);

        // Add separator
        btnSettings.getItems().add(new SeparatorMenuItem());

        // Add Logout option
        MenuItem logoutItem = new MenuItem("Logout");
        logoutItem.setOnAction(this::logoutClicked);
        btnSettings.getItems().add(logoutItem);
    }

    /**
     * Navigate to My Profile view
     */
    private void navigateToMyProfile() {
        // Check if already in employee navigation
        if (currentNavigationController instanceof EmployeeRoleNavigationController) {
            // Just navigate to profile without changing navigation
            EmployeeRoleNavigationController employeeController = (EmployeeRoleNavigationController) currentNavigationController;
            employeeController.myProfileOnAction();
        } else {
            // Switch to employee navigation which will show the profile
            // Store previous navigation before switching so back button can be shown
            previousNavigationController = currentNavigationController;
            displayEmployeeRoleNavigation();
        }
    }

    /**
     * Navigate to notifications view based on current role
     */
    private void navigateToNotificationsView() {
        if (currentNavigationController instanceof EmployeeRoleNavigationController) {
            EmployeeRoleNavigationController employeeController = (EmployeeRoleNavigationController) currentNavigationController;
            employeeController.notificationOnAction();
        } else {
            // For other roles, switch to employee navigation to access notifications
            // Store previous navigation before switching
            previousNavigationController = currentNavigationController;
            // Use loadDefaultView=false to avoid loading profile view
            displayEmployeeRoleNavigation(false);
            javafx.application.Platform.runLater(() -> {
                if (currentNavigationController instanceof EmployeeRoleNavigationController) {
                    ((EmployeeRoleNavigationController) currentNavigationController).notificationOnAction();
                }
            });
        }
    }

    /**
     * Navigate to conversations view based on current role
     */
    private void navigateToConversationsView() {
        if (currentNavigationController instanceof EmployeeRoleNavigationController) {
            EmployeeRoleNavigationController employeeController = (EmployeeRoleNavigationController) currentNavigationController;
            employeeController.conversationOnAction();
        } else {
            // For other roles, switch to employee navigation to access conversations
            // Store previous navigation before switching
            previousNavigationController = currentNavigationController;
            // Use loadDefaultView=false to avoid loading profile view
            displayEmployeeRoleNavigation(false);
            
            // Ensure conversation view is loaded after navigation is set
            // Use Platform.runLater to ensure navigation controller is fully initialized
            javafx.application.Platform.runLater(() -> {
                if (currentNavigationController instanceof EmployeeRoleNavigationController) {
                    EmployeeRoleNavigationController employeeController = (EmployeeRoleNavigationController) currentNavigationController;
                    // Verify navigation pane is loaded
                    if (mainBorderPane.getLeft() != null && employeeController.getMainViewController() != null) {
                        employeeController.conversationOnAction();
                    }
                }
            });
        }
    }

    /**
     * Navigate to a specific conversation and open it
     */
    private void navigateToConversation(Conversation conversation) {
        if (currentNavigationController instanceof EmployeeRoleNavigationController) {
            EmployeeRoleNavigationController employeeController = (EmployeeRoleNavigationController) currentNavigationController;
            // Open the conversation message window directly
            openConversationWindow(conversation, employeeController);
        } else {
            // Switch to employee navigation first
            navigateToConversationsView();
            javafx.application.Platform.runLater(() -> {
                if (currentNavigationController instanceof EmployeeRoleNavigationController) {
                    openConversationWindow(conversation, (EmployeeRoleNavigationController) currentNavigationController);
                }
            });
        }
    }

    /**
     * Open a conversation message window directly
     */
    private void openConversationWindow(Conversation conversation, EmployeeRoleNavigationController employeeController) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/jomariabejo/motorph/role/employee/message.fxml"));
            AnchorPane messagePane = loader.load();
            Stage messageStage = new Stage();
            messageStage.setTitle("Messages");
            messageStage.setScene(new Scene(messagePane));

            com.jomariabejo.motorph.controller.role.employee.MessageController messageController = loader.getController();
            // Create a temporary conversation controller for the message controller
            com.jomariabejo.motorph.controller.role.employee.ConversationController tempConvController = 
                new com.jomariabejo.motorph.controller.role.employee.ConversationController();
            tempConvController.setEmployeeRoleNavigationController(employeeController);
            messageController.setConversationController(tempConvController);
            messageController.setConversation(conversation);
            messageController.setCurrentEmployee(employee);
            messageController.setup();

            messageStage.showAndWait();
            // Refresh dropdowns after closing message window
            refreshDropdowns();
        } catch (IOException e) {
            e.printStackTrace();
            // Fallback: navigate to conversation view
            employeeController.conversationOnAction();
        }
    }

    /**
     * Open new message dialog
     */
    private void openNewMessageDialog() {
        if (currentNavigationController instanceof EmployeeRoleNavigationController) {
            EmployeeRoleNavigationController employeeController = (EmployeeRoleNavigationController) currentNavigationController;
            // Open new conversation dialog directly
            try {
                FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/jomariabejo/motorph/role/employee/new-conversation-form.fxml"));
                AnchorPane formPane = loader.load();
                Stage formStage = new Stage();
                formStage.setTitle("Start New Conversation");
                formStage.setScene(new Scene(formPane));

                com.jomariabejo.motorph.controller.role.employee.NewConversationFormController formController = loader.getController();
                // Create a temporary conversation controller
                com.jomariabejo.motorph.controller.role.employee.ConversationController tempConvController = 
                    new com.jomariabejo.motorph.controller.role.employee.ConversationController();
                tempConvController.setEmployeeRoleNavigationController(employeeController);
                formController.setConversationController(tempConvController);
                formController.setCurrentEmployee(employee);
                formController.setup();

                formStage.showAndWait();
                // Refresh dropdowns after closing dialog
                refreshDropdowns();
            } catch (IOException e) {
                e.printStackTrace();
                // Fallback: navigate to conversation view
                employeeController.conversationOnAction();
            }
        } else {
            // Switch to employee navigation first
            navigateToConversationsView();
            javafx.application.Platform.runLater(() -> {
                if (currentNavigationController instanceof EmployeeRoleNavigationController) {
                    openNewMessageDialog();
                }
            });
        }
    }
}
