package com.jomariabejo.motorph.controller.systemadmin;

import com.jomariabejo.motorph.entity.User;
import com.jomariabejo.motorph.service.UserService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class GeneratedCodeController {

    @FXML
    private TableView<User> tv_users;

    @FXML
    private void initialize() {
        UserService userService = new UserService();
        tv_users.setItems(FXCollections.observableList(userService.fetchUserWithVerificationCode()));
    }

}
