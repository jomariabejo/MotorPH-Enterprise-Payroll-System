package com.jomariabejo.motorph.controller.role.employee;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalaryStructureController {

    private MoreInfoController moreInfoController;

    @FXML
    private TextField tfBasicSalary;

    @FXML
    private TextField tfGrossSemiMonthlyRate;

    @FXML
    private TextField tfHourlyRate;

}
