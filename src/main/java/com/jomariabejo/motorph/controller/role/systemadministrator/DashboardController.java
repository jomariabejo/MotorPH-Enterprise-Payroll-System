package com.jomariabejo.motorph.controller.role.systemadministrator;

import com.jomariabejo.motorph.controller.nav.SystemAdministratorNavigationController;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.Setter;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
public class DashboardController {

    private SystemAdministratorNavigationController systemAdministratorNavigationController;
    private static final String STATUS_ACTIVE = "Active";

    @FXML
    private Label lblTotalUsers;

    @FXML
    private Label lblTotalRoles;

    @FXML
    private Label lblTotalPermissions;

    @FXML
    private Label lblActiveUsers;

    @FXML
    private BarChart<String, Number> barUsersByRole;

    @FXML
    private PieChart pieUserStatus;

    @FXML
    private BarChart<String, Number> barLogsByAction;

    @FXML
    private LineChart<String, Number> lineAnnouncementsByMonth;

    public DashboardController() {
        // Default constructor required by JavaFX FXML loader.
    }

    @FXML
    void initialize() {
        // Don't populate statistics here - wait for controller to be set
        // populateStatistics() will be called after systemAdministratorNavigationController is set
        // This prevents NullPointerException since initialize() is called before the controller is set
    }

    /**
     * Populates the dashboard statistics.
     * Should be called after systemAdministratorNavigationController is set.
     */
    public void populateStatistics() {
        if (systemAdministratorNavigationController == null) {
            return; // Controller not set yet, skip
        }

        var serviceFactory = systemAdministratorNavigationController.getMainViewController().getServiceFactory();

        var users = serviceFactory.getUserService().getAllUsers();
        int totalUsers = users.size();
        int totalRoles = serviceFactory.getRoleService().getAllRoles().size();
        int totalPermissions = serviceFactory.getPermissionService().getAllPermissions().size();

        long activeUsers = users.stream()
                .filter(user -> STATUS_ACTIVE.equalsIgnoreCase(user.getStatus()))
                .count();

        lblTotalUsers.setText(String.valueOf(totalUsers));
        lblTotalRoles.setText(String.valueOf(totalRoles));
        lblTotalPermissions.setText(String.valueOf(totalPermissions));
        lblActiveUsers.setText(String.valueOf(activeUsers));

        populateCharts(users);
    }

    private void populateCharts(List<com.jomariabejo.motorph.model.User> users) {
        populateUsersByRole(users);
        populateUserStatus(users);
        populateLogsByAction();
        populateAnnouncementsByMonth();
    }

    private void populateUsersByRole(List<com.jomariabejo.motorph.model.User> users) {
        if (barUsersByRole == null) return;
        barUsersByRole.getData().clear();

        Map<String, Long> counts = users.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        u -> (u.getRoleID() != null && u.getRoleID().getRoleName() != null) ? u.getRoleID().getRoleName() : "Unassigned",
                        Collectors.counting()
                ));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        counts.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(String.CASE_INSENSITIVE_ORDER))
                .forEach(e -> series.getData().add(new XYChart.Data<>(e.getKey(), e.getValue())));
        barUsersByRole.getData().add(series);
    }

    private void populateUserStatus(List<com.jomariabejo.motorph.model.User> users) {
        if (pieUserStatus == null) return;
        pieUserStatus.getData().clear();

        long active = users.stream().filter(u -> u != null && STATUS_ACTIVE.equalsIgnoreCase(u.getStatus())).count();
        long inactive = users.stream().filter(u -> u != null && !STATUS_ACTIVE.equalsIgnoreCase(u.getStatus())).count();

        pieUserStatus.getData().add(new javafx.scene.chart.PieChart.Data(STATUS_ACTIVE, active));
        pieUserStatus.getData().add(new javafx.scene.chart.PieChart.Data("Inactive", inactive));
    }

    private void populateLogsByAction() {
        if (barLogsByAction == null) return;
        barLogsByAction.getData().clear();

        var logs = systemAdministratorNavigationController.getMainViewController()
                .getServiceFactory().getUserLogService().getAllUserLog();

        Map<String, Long> topCounts = logs.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(com.jomariabejo.motorph.model.UserLog::getLogDateTime, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .limit(200)
                .map(l -> (l.getAction() == null || l.getAction().isBlank()) ? "Unknown" : l.getAction())
                .collect(Collectors.groupingBy(a -> a, Collectors.counting()));

        // show top 8 actions
        List<Map.Entry<String, Long>> top = topCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(8)
                .toList();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        top.forEach(e -> series.getData().add(new XYChart.Data<>(e.getKey(), e.getValue())));
        barLogsByAction.getData().add(series);
    }

    private void populateAnnouncementsByMonth() {
        if (lineAnnouncementsByMonth == null) return;
        lineAnnouncementsByMonth.getData().clear();

        var announcements = systemAdministratorNavigationController.getMainViewController()
                .getServiceFactory().getAnnouncementService().getAllAnnouncements();

        YearMonth now = YearMonth.now();
        List<YearMonth> months = java.util.stream.IntStream.rangeClosed(0, 5)
                .mapToObj(i -> now.minusMonths(5L - i))
                .toList();

        Map<YearMonth, Long> counts = announcements.stream()
                .filter(a -> a != null && a.getAnnouncementDate() != null)
                .collect(Collectors.groupingBy(a -> YearMonth.from(a.getAnnouncementDate()), Collectors.counting()));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM yyyy");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (YearMonth ym : months) {
            long c = counts.getOrDefault(ym, 0L);
            series.getData().add(new XYChart.Data<>(ym.format(fmt), c));
        }
        lineAnnouncementsByMonth.getData().add(series);
    }
}
