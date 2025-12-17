package com.jomariabejo.motorph.controller.role.hr;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.HumanResourceAdministratorNavigationController;
import com.jomariabejo.motorph.model.Announcement;
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
import java.util.Optional;

@Getter
@Setter
public class AnnouncementController {

    private HumanResourceAdministratorNavigationController humanResourceAdministratorNavigationController;

    @FXML
    private Button btnAddAnnouncement;

    @FXML
    private Pagination paginationAnnouncements;

    @FXML
    private TableView<Announcement> tvAnnouncements;

    private List<Announcement> allAnnouncements;

    public AnnouncementController() {
    }

    @FXML
    void initialize() {
        setupTableView();
        customizeAddAnnouncementButton();
        populateAnnouncements();
    }

    @FXML
    void addAnnouncementClicked() {
        openAnnouncementForm(null);
    }

    private void openAnnouncementForm(Announcement announcement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/human-resource/announcement-form.fxml"));
            AnchorPane formPane = loader.load();
            Stage formStage = new Stage();
            formStage.setTitle(announcement == null ? "Add New Announcement" : "Edit Announcement");
            formStage.setScene(new Scene(formPane));

            AnnouncementFormController formController = loader.getController();
            formController.setAnnouncementController(this);
            formController.setAnnouncement(announcement);
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
        TableColumn<Announcement, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        idColumn.setPrefWidth(80);

        TableColumn<Announcement, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitle()));
        titleColumn.setPrefWidth(200);

        TableColumn<Announcement, String> contentColumn = new TableColumn<>("Content");
        contentColumn.setCellValueFactory(cellData -> {
            String content = cellData.getValue().getContent();
            return new javafx.beans.property.SimpleStringProperty(
                    content != null && content.length() > 50 ? content.substring(0, 50) + "..." : content);
        });
        contentColumn.setPrefWidth(300);

        TableColumn<Announcement, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getAnnouncementDate() != null 
                                ? cellData.getValue().getAnnouncementDate().toString() : ""));
        dateColumn.setPrefWidth(120);

        TableColumn<Announcement, String> createdByColumn = new TableColumn<>("Created By");
        createdByColumn.setCellValueFactory(cellData -> {
            var emp = cellData.getValue().getCreatedByEmployee();
            return new javafx.beans.property.SimpleStringProperty(
                    emp != null ? emp.getFirstName() + " " + emp.getLastName() : "");
        });
        createdByColumn.setPrefWidth(200);

        TableColumn<Announcement, Void> actionsColumn = createActionsColumn();
        actionsColumn.setPrefWidth(200);

        tvAnnouncements.getColumns().addAll(idColumn, titleColumn, contentColumn, dateColumn, createdByColumn, actionsColumn);
        tvAnnouncements.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private TableColumn<Announcement, Void> createActionsColumn() {
        TableColumn<Announcement, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button(null, new FontIcon(Feather.EDIT));
            private final Button deleteButton = new Button(null, new FontIcon(Feather.TRASH));

            {
                editButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
                deleteButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);

                editButton.setOnAction(event -> {
                    Announcement selected = getTableView().getItems().get(getIndex());
                    if (selected != null) {
                        openAnnouncementForm(selected);
                    }
                });

                deleteButton.setOnAction(event -> {
                    Announcement selected = getTableView().getItems().get(getIndex());
                    if (selected != null) {
                        deleteAnnouncement(selected);
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
                setGraphic(empty ? null : actionsBox);
            }
        });
        return actionsColumn;
    }

    private void deleteAnnouncement(Announcement announcement) {
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Delete Announcement",
                "Are you sure you want to delete this announcement?"
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            humanResourceAdministratorNavigationController.getMainViewController()
                    .getServiceFactory().getAnnouncementService().deleteAnnouncement(announcement);

            CustomAlert successAlert = new CustomAlert(
                    Alert.AlertType.INFORMATION,
                    "Announcement Deleted",
                    "Announcement has been deleted."
            );
            successAlert.showAndWait();
            populateAnnouncements();
        }
    }

    private void customizeAddAnnouncementButton() {
        FontIcon fontIcon = new FontIcon(Feather.PLUS);
        btnAddAnnouncement.setGraphic(fontIcon);
        btnAddAnnouncement.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
    }

    public void populateAnnouncements() {
        allAnnouncements = humanResourceAdministratorNavigationController.getMainViewController()
                .getServiceFactory().getAnnouncementService().getAllAnnouncements();

        int itemsPerPage = 25;
        int pageCount = Math.max(1, (int) Math.ceil((double) allAnnouncements.size() / itemsPerPage));
        paginationAnnouncements.setPageCount(pageCount);

        paginationAnnouncements.setPageFactory(pageIndex -> {
            updateTableView(pageIndex, itemsPerPage);
            return new StackPane();
        });
    }

    private void updateTableView(int pageIndex, int itemsPerPage) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, allAnnouncements.size());
        List<Announcement> pageData = allAnnouncements.subList(fromIndex, toIndex);
        tvAnnouncements.setItems(FXCollections.observableList(pageData));
    }
}










