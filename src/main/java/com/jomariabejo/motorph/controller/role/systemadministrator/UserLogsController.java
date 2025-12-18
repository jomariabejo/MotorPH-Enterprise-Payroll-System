package com.jomariabejo.motorph.controller.role.systemadministrator;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.SystemAdministratorNavigationController;
import com.jomariabejo.motorph.constants.PermissionConstants;
import com.jomariabejo.motorph.model.UserLog;
import com.jomariabejo.motorph.service.UserLogPdfService;
import com.jomariabejo.motorph.utility.CustomAlert;
import com.opencsv.CSVWriter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserLogsController {

    private SystemAdministratorNavigationController systemAdministratorNavigationController;

    @FXML
    private TableView<UserLog> tvUserLogs;

    @FXML
    private Button btnRefresh;

    @FXML
    private ComboBox<String> cbFilterAction;

    @FXML
    private TextField tfSearchUser;

    @FXML
    private Pagination paginationLogs;

    @FXML
    private Button btnExportCSV;

    @FXML
    private Button btnToggleAutoRefresh;

    @FXML
    private Label lblLastRefresh;

    @FXML
    private Label lblTotalLogs;

    @FXML
    private ComboBox<String> cbExportTimePeriod;

    private ObservableList<UserLog> allLogs;
    private FilteredList<UserLog> filteredLogs;
    private SortedList<UserLog> sortedLogs;
    private Timeline autoRefreshTimeline;
    private LocalDateTime lastRefreshTime;
    private boolean autoRefreshEnabled = true;

    private static final int REFRESH_INTERVAL_SECONDS = 10;
    private static final int ITEMS_PER_PAGE = 25;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public UserLogsController() {
        // Default constructor required by JavaFX FXML loader
    }

    @FXML
    void initialize() {
        setupTableView();
        setupFilters();
        customizeButtons();
        initializeLabels();
        // Don't populate logs here - wait for controller to be set
        // populateLogs() will be called after systemAdministratorNavigationController is set
        // startAutoRefresh() will be called after populateLogs()
    }

    private void setupTableView() {
        // Log ID Column
        TableColumn<UserLog, Long> idColumn = new TableColumn<>("Log ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(100);

        // User Column
        TableColumn<UserLog, String> userColumn = new TableColumn<>("User");
        userColumn.setCellValueFactory(cellData -> {
            UserLog log = cellData.getValue();
            if (log.getUserID() != null) {
                return new javafx.beans.property.SimpleStringProperty(
                        log.getUserID().getFullName() + " (" + log.getUserID().getUsername() + ")"
                );
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });
        userColumn.setPrefWidth(200);

        // Action Column
        TableColumn<UserLog, String> actionColumn = new TableColumn<>("Action");
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
        actionColumn.setPrefWidth(250);

        // IP Address Column
        TableColumn<UserLog, String> ipColumn = new TableColumn<>("IP Address");
        ipColumn.setCellValueFactory(new PropertyValueFactory<>("iPAddress"));
        ipColumn.setPrefWidth(150);

        // Date/Time Column
        TableColumn<UserLog, String> dateTimeColumn = new TableColumn<>("Date/Time");
        dateTimeColumn.setCellValueFactory(cellData -> {
            UserLog log = cellData.getValue();
            if (log.getLogDateTime() != null) {
                return new javafx.beans.property.SimpleStringProperty(
                        log.getLogDateTime().format(DATE_TIME_FORMATTER)
                );
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });
        dateTimeColumn.setPrefWidth(200);

<<<<<<< HEAD
        // Actions Column
        TableColumn<UserLog, Void> actionsColumn = createActionsColumn();
        actionsColumn.setPrefWidth(100);

        tvUserLogs.getColumns().addAll(idColumn, userColumn, actionColumn, ipColumn, dateTimeColumn, actionsColumn);
        tvUserLogs.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private TableColumn<UserLog, Void> createActionsColumn() {
        TableColumn<UserLog, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setPrefWidth(100);
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button viewBtn = createViewButton();

            private Button createViewButton() {
                Button btn = new Button(null, new FontIcon(Feather.EYE));
                btn.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED);
                btn.setTooltip(new Tooltip("View Log Details"));
                return btn;
            }

            private final HBox actionsBox = new HBox(viewBtn);

            {
                actionsBox.setAlignment(Pos.CENTER);
                actionsBox.setSpacing(10);

                viewBtn.setOnAction(event -> {
                    UserLog selectedLog = getTableView().getItems().get(getIndex());
                    if (selectedLog != null) {
                        viewLogDetails(selectedLog);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }
                setGraphic(actionsBox);
            }
        });
        return actionsColumn;
    }

    private void viewLogDetails(UserLog log) {
        // Create a dialog to show log details
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Log Details - Log ID: " + log.getId());
        dialog.setResizable(false);

        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));
        vbox.setPrefWidth(500);

        // Style constants
        final String BOLD_STYLE = "-fx-font-weight: bold; -fx-font-size: 14px;";
        final String NORMAL_STYLE = "-fx-font-size: 14px;";

        // Log ID
        Label idLabel = new Label("Log ID:");
        idLabel.setStyle(BOLD_STYLE);
        Label idValue = new Label(String.valueOf(log.getId()));
        idValue.setStyle(NORMAL_STYLE);

        // User
        Label userLabel = new Label("User:");
        userLabel.setStyle(BOLD_STYLE);
        String userInfo = log.getUserID() != null 
                ? log.getUserID().getFullName() + " (" + log.getUserID().getUsername() + ")"
                : "N/A";
        Label userValue = new Label(userInfo);
        userValue.setStyle(NORMAL_STYLE);
        userValue.setWrapText(true);

        // Action
        Label actionLabel = new Label("Action:");
        actionLabel.setStyle(BOLD_STYLE);
        Label actionValue = new Label(log.getAction() != null ? log.getAction() : "N/A");
        actionValue.setStyle(NORMAL_STYLE);
        actionValue.setWrapText(true);

        // IP Address
        Label ipLabel = new Label("IP Address:");
        ipLabel.setStyle(BOLD_STYLE);
        Label ipValue = new Label(log.getIPAddress() != null ? log.getIPAddress() : "N/A");
        ipValue.setStyle(NORMAL_STYLE);

        // Date/Time
        Label dateTimeLabel = new Label("Date/Time:");
        dateTimeLabel.setStyle(BOLD_STYLE);
        String dateTime = log.getLogDateTime() != null 
                ? log.getLogDateTime().format(DATE_TIME_FORMATTER)
                : "N/A";
        Label dateTimeValue = new Label(dateTime);
        dateTimeValue.setStyle(NORMAL_STYLE);

        // Download PDF button
        Button downloadPdfBtn = new Button("Download as PDF", new FontIcon(Feather.DOWNLOAD));
        downloadPdfBtn.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
        downloadPdfBtn.setOnAction(e -> downloadLogAsPdf(log, dialog));

        // Close button
        Button closeBtn = new Button("Close");
        closeBtn.getStyleClass().addAll(Styles.BUTTON_OUTLINED);
        closeBtn.setOnAction(e -> dialog.close());

        HBox buttonBox = new HBox(10, downloadPdfBtn, closeBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        vbox.getChildren().addAll(
                createDetailRow(idLabel, idValue),
                createDetailRow(userLabel, userValue),
                createDetailRow(actionLabel, actionValue),
                createDetailRow(ipLabel, ipValue),
                createDetailRow(dateTimeLabel, dateTimeValue),
                buttonBox
        );

        javafx.scene.Scene scene = new javafx.scene.Scene(vbox);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private HBox createDetailRow(Label label, Label value) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        label.setMinWidth(100);
        value.setMinWidth(350);
        row.getChildren().addAll(label, value);
        return row;
    }

    private void downloadLogAsPdf(UserLog log, Stage parentStage) {
        try {
            UserLogPdfService pdfService = new UserLogPdfService();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Log as PDF");
            fileChooser.setInitialFileName("Log_" + log.getId() + "_" + 
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

            File file = fileChooser.showSaveDialog(parentStage);
            if (file != null) {
                pdfService.generateLogPdf(log, file);
                
                CustomAlert successAlert = new CustomAlert(
                        Alert.AlertType.INFORMATION,
                        "PDF Generated",
                        "Log PDF has been saved successfully to:\n" + file.getAbsolutePath()
                );
                successAlert.showAndWait();
            }
        } catch (Exception e) {
            CustomAlert errorAlert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "PDF Generation Failed",
                    "Failed to generate PDF: " + e.getMessage()
            );
            errorAlert.showAndWait();
            e.printStackTrace();
        }
    }

    private void setupFilters() {
        // Initialize filter combobox with common actions including clock in/out
=======
        tvUserLogs.getColumns().addAll(idColumn, userColumn, actionColumn, ipColumn, dateTimeColumn);
        tvUserLogs.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupFilters() {
        // Initialize filter combobox with common actions
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
        cbFilterAction.getItems().addAll("All", "User logged in", "User logged out", 
                "User created", "User updated", "User deleted",
                "Role created", "Role updated", "Role deleted",
                "Permission created", "Permission updated", "Permission deleted",
<<<<<<< HEAD
                "Permission assigned", "Permission removed",
                "Employee clocked in", "Employee clocked out");
=======
                "Permission assigned", "Permission removed");
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
        cbFilterAction.getSelectionModel().selectFirst();
        
        // Initialize export time period filter
        if (cbExportTimePeriod != null) {
            cbExportTimePeriod.getItems().addAll("All", "Hourly", "Daily", "Weekly", "Monthly");
            cbExportTimePeriod.getSelectionModel().selectFirst(); // Default to "All"
        }
        
        // Add listeners for filtering
        cbFilterAction.setOnAction(e -> applyFilters());
        tfSearchUser.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
    }

    private void customizeButtons() {
        // Refresh button
        FontIcon refreshIcon = new FontIcon(Feather.REFRESH_CCW);
        btnRefresh.setGraphic(refreshIcon);
        btnRefresh.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED);

        // Export CSV button
        if (btnExportCSV != null) {
            FontIcon exportIcon = new FontIcon(Feather.DOWNLOAD);
            btnExportCSV.setGraphic(exportIcon);
            btnExportCSV.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
        }

        // Toggle auto-refresh button
        if (btnToggleAutoRefresh != null) {
            updateToggleButtonIcon();
            btnToggleAutoRefresh.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED);
        }
    }

    private void initializeLabels() {
        if (lblLastRefresh != null) {
            lblLastRefresh.setText("Last refreshed: --:--:--");
        }
        if (lblTotalLogs != null) {
            lblTotalLogs.setText("Total logs: 0");
        }
    }

    private void updateToggleButtonIcon() {
        if (btnToggleAutoRefresh != null) {
            FontIcon icon = new FontIcon(autoRefreshEnabled ? Feather.PAUSE : Feather.PLAY);
            btnToggleAutoRefresh.setGraphic(icon);
            btnToggleAutoRefresh.setText(autoRefreshEnabled ? "Pause Auto-Refresh" : "Resume Auto-Refresh");
        }
    }

    @FXML
    void refreshClicked() {
        populateLogs();
        updateLastRefreshLabel();
    }

    @FXML
    void exportCSVClicked() {
        exportToCSV();
    }

    @FXML
    void toggleAutoRefreshClicked() {
        autoRefreshEnabled = !autoRefreshEnabled;
        if (autoRefreshEnabled) {
            startAutoRefresh();
        } else {
            stopAutoRefresh();
        }
        updateToggleButtonIcon();
    }

    private void updateLastRefreshLabel() {
        if (lblLastRefresh != null) {
            Platform.runLater(() -> lblLastRefresh.setText("Last refreshed: " + LocalDateTime.now().format(TIME_FORMATTER)));
        }
    }

    /**
     * Populate logs table with all user logs.
     * Should be called after systemAdministratorNavigationController is set.
     */
    public void populateLogs() {
        if (systemAdministratorNavigationController == null) {
            System.out.println("UserLogsController: systemAdministratorNavigationController is null");
            return; // Controller not set yet, skip
        }
        if (!systemAdministratorNavigationController.requirePermission(PermissionConstants.SYSTEM_ADMIN_LOGS_VIEW, "view logs")) {
            Platform.runLater(() -> {
                tvUserLogs.setItems(FXCollections.observableArrayList());
                if (paginationLogs != null) paginationLogs.setPageCount(1);
                if (btnExportCSV != null) btnExportCSV.setDisable(true);
            });
            return;
        }
        if (btnExportCSV != null) {
            btnExportCSV.setDisable(!systemAdministratorNavigationController.hasPermission(PermissionConstants.SYSTEM_ADMIN_LOGS_VIEW));
        }

        try {
            List<UserLog> logs = systemAdministratorNavigationController.getMainViewController()
                    .getServiceFactory()
                    .getUserLogService()
                    .getAllUserLog();

            System.out.println("UserLogsController: Fetched " + logs.size() + " logs from database");

            if (logs == null || logs.isEmpty()) {
                System.out.println("UserLogsController: No logs found in database");
                allLogs = FXCollections.observableArrayList();
                filteredLogs = new FilteredList<>(allLogs, p -> true);
                sortedLogs = new SortedList<>(filteredLogs);
                updateTableView(0, ITEMS_PER_PAGE, sortedLogs);
                paginationLogs.setPageCount(1);
                updateTotalLogsLabel();
                return;
            }

            allLogs = FXCollections.observableArrayList(logs);
            filteredLogs = new FilteredList<>(allLogs, p -> true);

            // Apply sorting (newest first by default, but allow column sorting)
            sortedLogs = new SortedList<>(filteredLogs);
            
            // Bind to table view comparator first (this allows column header clicks to sort)
            sortedLogs.comparatorProperty().bind(tvUserLogs.comparatorProperty());
            
            // Set initial sort on the table view's date/time column (descending)
            // This will automatically update the SortedList comparator through the binding
            Platform.runLater(() -> {
                if (tvUserLogs.getColumns().size() > 4) {
                    TableColumn<UserLog, ?> dateTimeColumn = tvUserLogs.getColumns().get(4);
                    if (dateTimeColumn != null) {
                        tvUserLogs.getSortOrder().clear();
                        tvUserLogs.getSortOrder().add(dateTimeColumn);
                        dateTimeColumn.setSortType(TableColumn.SortType.DESCENDING);
                    }
                }
            });

            // Setup pagination
            int pageCount = Math.max(1, (int) Math.ceil((double) sortedLogs.size() / ITEMS_PER_PAGE));
            paginationLogs.setPageCount(pageCount);
            
            // Set page factory first
            paginationLogs.setPageFactory(pageIndex -> {
                updateTableView(pageIndex, ITEMS_PER_PAGE, sortedLogs);
                return new StackPane();
            });
            
            // Set current page index to trigger pageFactory and display first page
            paginationLogs.setCurrentPageIndex(0);
            
            // Also explicitly update table view to ensure data is displayed
            Platform.runLater(() -> updateTableView(0, ITEMS_PER_PAGE, sortedLogs));

            lastRefreshTime = LocalDateTime.now();
            updateLastRefreshLabel();
            updateTotalLogsLabel();
            
            System.out.println("UserLogsController: Successfully populated " + sortedLogs.size() + " logs");
        } catch (Exception e) {
            System.err.println("UserLogsController: Error populating logs: " + e.getMessage());
            e.printStackTrace();
            // Show error to user
            CustomAlert alert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Error Loading Logs",
                    "Failed to load logs: " + e.getMessage()
            );
            alert.showAndWait();
        }
    }

    private void updateTotalLogsLabel() {
        if (lblTotalLogs != null && filteredLogs != null) {
            Platform.runLater(() -> lblTotalLogs.setText("Total logs: " + filteredLogs.size()));
        }
    }

    private void updateTableView(int pageIndex, int itemsPerPage, SortedList<UserLog> sortedLogs) {
        if (sortedLogs == null || sortedLogs.isEmpty()) {
            tvUserLogs.setItems(FXCollections.observableArrayList());
            return;
        }
        
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, sortedLogs.size());
        
        if (fromIndex < sortedLogs.size()) {
            List<UserLog> pageData = sortedLogs.subList(fromIndex, toIndex);
            ObservableList<UserLog> observablePageData = FXCollections.observableList(pageData);
            Platform.runLater(() -> {
                tvUserLogs.setItems(observablePageData);
                tvUserLogs.refresh(); // Force table refresh
            });
        } else {
            Platform.runLater(() -> tvUserLogs.setItems(FXCollections.observableArrayList()));
        }
    }

    private void applyFilters() {
        if (filteredLogs == null) {
            return;
        }

        String searchText = tfSearchUser.getText().toLowerCase();
        String selectedAction = cbFilterAction.getSelectionModel().getSelectedItem();

        filteredLogs.setPredicate(log -> {
            // Filter by user search
            boolean matchesUser = true;
            if (searchText != null && !searchText.isEmpty()) {
                if (log.getUserID() != null) {
                    String userName = log.getUserID().getFullName() != null ? log.getUserID().getFullName().toLowerCase() : "";
                    String username = log.getUserID().getUsername() != null ? log.getUserID().getUsername().toLowerCase() : "";
                    matchesUser = userName.contains(searchText) || username.contains(searchText);
                } else {
                    matchesUser = false;
                }
            }

            // Filter by action (check if action starts with the selected filter)
            boolean matchesAction = true;
            if (selectedAction != null && !selectedAction.equals("All")) {
                String action = log.getAction() != null ? log.getAction() : "";
                matchesAction = action.startsWith(selectedAction);
            }

            return matchesUser && matchesAction;
        });

        // Update pagination
        int pageCount = Math.max(1, (int) Math.ceil((double) filteredLogs.size() / ITEMS_PER_PAGE));
        paginationLogs.setPageCount(pageCount);
        paginationLogs.setCurrentPageIndex(0);
        updateTotalLogsLabel();
    }

    /**
     * Start auto-refresh mechanism (refreshes every 10 seconds).
     * Should be called after populateLogs().
     */
    public void startAutoRefresh() {
        if (autoRefreshTimeline != null) {
            autoRefreshTimeline.stop();
        }
        autoRefreshTimeline = new Timeline(
                new KeyFrame(Duration.seconds(REFRESH_INTERVAL_SECONDS), e -> {
                    if (autoRefreshEnabled) {
                        refreshLogsIncrementally();
                    }
                })
        );
        autoRefreshTimeline.setCycleCount(Timeline.INDEFINITE);
        autoRefreshTimeline.play();
    }

    /**
     * Refresh logs incrementally (only fetch new logs since last refresh).
     */
    private void refreshLogsIncrementally() {
        if (systemAdministratorNavigationController == null || lastRefreshTime == null) {
            populateLogs();
            return;
        }

        List<UserLog> newLogs = systemAdministratorNavigationController.getMainViewController()
                .getServiceFactory()
                .getUserLogService()
                .getLogsAfter(lastRefreshTime);

        if (!newLogs.isEmpty()) {
            // Add new logs to the beginning of the list
            allLogs.addAll(0, newLogs);
            lastRefreshTime = LocalDateTime.now();
            
            // Refresh the table view
            applyFilters();
            updateLastRefreshLabel();
            updateTotalLogsLabel();
        }
    }

    /**
     * Stop auto-refresh when controller is destroyed.
     */
    public void stopAutoRefresh() {
        if (autoRefreshTimeline != null) {
            autoRefreshTimeline.stop();
        }
    }

    /**
     * Export filtered logs to CSV file.
     * Exports the current filtered view (respects search and action filters).
     */
    private void exportToCSV() {
        if (systemAdministratorNavigationController == null ||
                !systemAdministratorNavigationController.requirePermission(PermissionConstants.SYSTEM_ADMIN_LOGS_VIEW, "export logs")) {
            return;
        }
        if (sortedLogs == null || sortedLogs.isEmpty()) {
            CustomAlert alert = new CustomAlert(
                    Alert.AlertType.WARNING,
                    "No Data to Export",
                    "There are no logs to export. Please ensure logs are loaded."
            );
            alert.showAndWait();
            return;
        }

        // Get selected time period filter
        String timePeriod = cbExportTimePeriod != null && cbExportTimePeriod.getSelectionModel().getSelectedItem() != null
                ? cbExportTimePeriod.getSelectionModel().getSelectedItem()
                : "All";

        // Filter logs based on time period
        List<UserLog> logsToExport = filterLogsByTimePeriod(sortedLogs, timePeriod);

        if (logsToExport.isEmpty()) {
            CustomAlert alert = new CustomAlert(
                    Alert.AlertType.WARNING,
                    "No Data to Export",
                    "No logs found for the selected time period: " + timePeriod
            );
            alert.showAndWait();
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Logs to CSV");
        
        // Include time period in filename if not "All"
        String filenamePrefix = "All".equals(timePeriod) 
                ? "user_logs_" 
                : "user_logs_" + timePeriod.toLowerCase() + "_";
        fileChooser.setInitialFileName(filenamePrefix + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file = fileChooser.showSaveDialog(btnExportCSV.getScene().getWindow());
        if (file == null) {
            return; // User cancelled
        }

        try (FileWriter fileWriter = new FileWriter(file);
             CSVWriter csvWriter = new CSVWriter(fileWriter)) {

            // Write header
            csvWriter.writeNext(new String[]{
                    "Log ID", "Username", "Full Name", "Action", "IP Address", "Date/Time"
            });

            // Write data rows
            for (UserLog log : logsToExport) {
                String username = log.getUserID() != null ? log.getUserID().getUsername() : "N/A";
                String fullName = log.getUserID() != null ? log.getUserID().getFullName() : "N/A";
                String action = log.getAction() != null ? log.getAction() : "N/A";
                String ipAddress = log.getIPAddress() != null ? log.getIPAddress() : "N/A";
                String dateTime = log.getLogDateTime() != null 
                        ? log.getLogDateTime().format(DATE_TIME_FORMATTER) 
                        : "N/A";

                csvWriter.writeNext(new String[]{
                        String.valueOf(log.getId()),
                        username,
                        fullName,
                        action,
                        ipAddress,
                        dateTime
                });
            }

            csvWriter.flush();

            CustomAlert successAlert = new CustomAlert(
                    Alert.AlertType.INFORMATION,
                    "Export Successful",
                    String.format("Exported %d log(s) (%s) successfully to:\n%s", 
                            logsToExport.size(), 
                            timePeriod,
                            file.getAbsolutePath())
            );
            successAlert.showAndWait();

        } catch (IOException e) {
            CustomAlert errorAlert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Export Failed",
                    "Failed to export logs to CSV:\n" + e.getMessage()
            );
            errorAlert.showAndWait();
            e.printStackTrace();
        }
    }

    /**
     * Filter logs based on the selected time period.
     * 
     * @param logs The list of logs to filter
     * @param timePeriod The time period filter ("Hourly", "Daily", "Weekly", "Monthly", "All")
     * @return Filtered list of logs
     */
    private List<UserLog> filterLogsByTimePeriod(List<UserLog> logs, String timePeriod) {
        if ("All".equals(timePeriod) || logs == null || logs.isEmpty()) {
            return new ArrayList<>(logs);
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cutoffTime;

        switch (timePeriod) {
            case "Hourly":
                cutoffTime = now.minusHours(1);
                break;
            case "Daily":
                cutoffTime = now.minusDays(1);
                break;
            case "Weekly":
                cutoffTime = now.minusWeeks(1);
                break;
            case "Monthly":
                cutoffTime = now.minusMonths(1);
                break;
            default:
                return new ArrayList<>(logs);
        }

        return logs.stream()
                .filter(log -> {
                    if (log.getLogDateTime() == null) {
                        return false;
                    }
                    return log.getLogDateTime().isAfter(cutoffTime) || log.getLogDateTime().isEqual(cutoffTime);
                })
                .collect(java.util.stream.Collectors.toList());
    }
}
