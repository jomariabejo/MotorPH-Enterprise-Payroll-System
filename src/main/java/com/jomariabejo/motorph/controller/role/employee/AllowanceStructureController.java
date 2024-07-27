package com.jomariabejo.motorph.controller.role.employee;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllowanceStructureController {

    private MoreInfoController moreInfoController;

    @FXML
    private TextField tfClothingAllowance;

    @FXML
    private TextField tfPhoneAllowance;

    @FXML
    private TextField tfRiceSubsidy;
}
