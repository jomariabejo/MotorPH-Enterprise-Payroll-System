package com.jomariabejo.motorph.controller.systemadmin;

import com.jomariabejo.motorph.controller.hr.HRViewEmployeeProfile;
import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.entity.Employee;
import com.jomariabejo.motorph.entity.User;
import com.jomariabejo.motorph.utility.AlertUtility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SystemAdministratorController {

        @FXML
        private Button btn_search;

        @FXML
        private TextField tf_searchField;

        @FXML
        private TableView<User> tv_users;

        @FXML
        private TableColumn<Integer, User> userId;

        @FXML
        void addNewUserClicked(ActionEvent event) {
            Stage addNewEmployeeStage = new Stage(StageStyle.UNDECORATED);
            // Load the FXML file for the first scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/create/add-new-user.fxml"));
            // Set the scene for the primary stage
            try {
                addNewEmployeeStage.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @FXML
        void searchBtnClicked(ActionEvent event) {

        }

        @FXML
        void searchiTextField(ActionEvent event) {

        }

        @FXML
        private void initialize() {
            try {
                setUsersTableView();
                populateTableView();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    private void populateTableView() {
        String query = "SELECT user_id, username, password FROM user";

        try(
            Connection connection = DatabaseConnectionUtility.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(query)) {

            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setUserID(resultSet.getInt(1));
                user.setUsername(resultSet.getString(2));
                user.setPassword(resultSet.getString(3));
                tv_users.getItems().add(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void setUsersTableView() throws SQLException {

        // Define the custom cell for the actions column
        TableColumn<User, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setPrefWidth(150);
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            final Button editButton = new Button();
            final Button viewButton = new Button();
            final Button deleteButton = new Button();

            final HBox actionsBox = new HBox(editButton, viewButton, deleteButton);
            {
                actionsBox.setAlignment(Pos.CENTER); // Align HBox content to center
                actionsBox.setSpacing(5); // Set spacing between buttons

                editButton.setOnAction(event -> {
                            // Handle edit action here ⚠️
                    });
                viewButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());

                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/center/employee-profile.fxml"));

                        Parent root = fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.initStyle(StageStyle.DECORATED);
                        stage.setTitle("Viewing " + user.getUserID() + " record");
                        stage.setScene(new Scene(root));
                        stage.show();

                        HRViewEmployeeProfile employeeProfile = fxmlLoader.getController();
                        employeeProfile.initData(user.getUserID());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });


                // Load the edit and view icon images
                Image editIcon = new Image(getClass().getResourceAsStream("/img/modify-icon.png"));
                Image viewIcon = new Image(getClass().getResourceAsStream("/img/view-icon.png"));

                ImageView editIconView = new ImageView(editIcon);
                editIconView.setFitWidth(24);
                editIconView.setFitHeight(24);

                ImageView viewIconView = new ImageView(viewIcon);
                viewIconView.setFitWidth(36);
                viewIconView.setFitHeight(24);


                // Set the icon images as graphics for the edit and view buttons
                deleteButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    // Handle delete action here
                    String customHeader = "Confirm Deletion";
                    // Ask for confirmation before deleting an employee
                    String customContent = "Are you sure you want to delete employee " + user.getUsername() + " ?";
                    boolean isDeletionConfirmed = AlertUtility.showConfirmation(
                            customContent, // Message asking if the user wants to delete the employee
                            customHeader, // Title of the confirmation dialog
                            "Once confirmed, the employee will be removed from the database."
                    );

                    if (isDeletionConfirmed) {
                        String title = user.getUsername() + " deleted successfully";
                        String header = "Deletion Confirmation";
                        String content = "The employee has been successfully deleted from the database.";
                        AlertUtility.showInformation(title, header, content);
                    }
                });

                // Load the delete icon image
                Image deleteIcon = new Image(getClass().getResourceAsStream("/img/delete-icon.png"));
                ImageView deleteIconView = new ImageView(deleteIcon);
                deleteIconView.setFitWidth(24);
                deleteIconView.setFitHeight(24);

                // Set the delete icon as the graphic for the delete button
                editButton.setGraphic(editIconView);
                viewButton.setGraphic(viewIconView);
                deleteButton.setGraphic(deleteIconView);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(actionsBox);
                }
            }
        });
        this.tv_users.getColumns().add(actionsColumn);
    }

    public void addNewEmployeeClicked(ActionEvent event) {
    }

    public void searching(ActionEvent event) {

    }
}
