package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.Payslip;
import com.jomariabejo.motorph.model.YearToDateFigures;
import com.jomariabejo.motorph.repository.PayslipRepository;
import com.jomariabejo.motorph.repository.UserRepository;
import com.jomariabejo.motorph.service.PayslipService;
import com.jomariabejo.motorph.service.UserService;
import com.jomariabejo.motorph.utility.PesoUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Year;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class PayslipController {

    private EmployeeRoleNavigationController employeeRoleNavigationController;

    private ObservableList<Payslip> payslips;

    @FXML
    private ComboBox<Integer> cbYear;

    @FXML
    private TableView<Payslip> tvPayslip;

    @FXML
    private Pagination pagination;

    public PayslipController() {
    }

    public void paginationOnDragDetected(MouseEvent mouseEvent) {
    }

    @FXML
    private void initialize() {
        setupPayslipTableView();
    }


    public void cbYearChanged() {
        populatePayslipTableView();
    }

    public void populatePayslipTableView() {
        Employee employee = this.getEmployeeRoleNavigationController().getMainViewController().getEmployee();
        Integer year = this.cbYear.getSelectionModel().getSelectedItem();

        payslips = FXCollections.observableList(
                this.getEmployeeRoleNavigationController()
                        .getMainViewController()
                        .getServiceFactory()
                        .getPayslipService()
                        .getPayslipByEmployeeIdAndYear(
                                employee, year
                        ).get()
        );
        tvPayslip.setItems(payslips);
    }

    public void populateYears() {
        this.cbYear.setItems(FXCollections.observableArrayList(
                this.getEmployeeRoleNavigationController()
                        .getMainViewController()
                        .getServiceFactory()
                        .getPayslipService()
                        .getEmployeeYearsOfPayslip(this.getEmployeeRoleNavigationController().getMainViewController().getEmployee())
                        .get()
        ));
        // select latest year fetched.
        cbYear.getSelectionModel().selectFirst();
    }

    private void updateTableView(int pageIndex, int itemsPerPage) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, payslips.size());
        List<Payslip> pageData = payslips.subList(fromIndex, toIndex);
        tvPayslip.setItems(FXCollections.observableList(pageData));
    }

    public void setupPayslipTableView() {
        TableColumn<Payslip, Void> actionsColumn = createActionsColumn();
        this.tvPayslip.getColumns().add(actionsColumn);
    }

    private TableColumn<Payslip, Void> createActionsColumn() {
        TableColumn<Payslip, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setPrefWidth(200);
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button downloadBtn = createDownloadButton();
            private final HBox actionsBox = new HBox(downloadBtn);

            {
                actionsBox.setAlignment(Pos.CENTER);
                actionsBox.setSpacing(10);
                setGraphic(actionsBox);

                downloadBtn.setOnAction(event -> {
                    /**
                     * TODO:
                     * Main Objective:
                     * Gusto ko ma download ang payslip ng employee in pdf format.
                     * Flow chart
                     */

                    Payslip payslip = getTableView().getItems().get(getIndex());
                    UserService userService = new UserService(new UserRepository());
                    PayslipService payslipService = new PayslipService(new PayslipRepository());


                    Optional<YearToDateFigures> yearToDateFigures = payslipService.getYearToDateFigures(payslip.getEmployeeID(), Year.now());

                    String strPayslipHTMLSourceCodeByXinTaroAndModifiedByJomariAbejo = "<!--Source code: https://codepen.io/xintaro/pen/bRVYjM -->\n" +
                            "<!DOCTYPE html>\n" +
                            "<html>\n" +
                            "<head>\n" +
                            "    <meta charset=\"UTF-8\">\n" +
                            "    <meta name=\"viewport\" content=\"width-device-width\" , initial-scale=1.0\">\n" +
                            "    <title>Payslip</title>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "<div id=\"payslip\">\n" +
                            "    <div style=\"text-align: center\">\n" +
                            "        <div id=\"title\">Employee Payslip</div>\n" +
                            "        <p>\n" +
                            "            7 Jupiter Avenue cor. F. Sandoval Jr., Bagong Nayon, Quezon City\n" +
                            "            <br>\n" +
                            "            Phone: (028) 911-5071 / (028) 911-5072 / (028) 911-5073\n" +
                            "            <br>\n" +
                            "            Email: corporate@motorph.com\n" +
                            "        </p>\n" +
                            "    </div>\n" +
                            "    <div id=\"scope\">\n" +
                            "        <div class=\"scope-entry\">\n" +
                            "            <div class=\"title\">PAYROLL RUN</div>\n" +
                            "            <div class=\"value\">" + payslip.getPayrollID().getPayrollRunDate() + "</div>\n" +
                            "        </div>\n" +
                            "        <div class=\"scope-entry\">\n" +
                            "            <div class=\"title\">PAY PERIOD</div>\n" +
                            "            <div class=\"value\">" + payslip.getPeriodStartDate() + " to " + payslip.getPeriodEndDate() + "</div>\n" +
                            "        </div>\n" +
                            "    </div>\n" +
                            "    <div class=\"content\">\n" +
                            "        <div class=\"left-panel\">\n" +
                            "            <div id=\"employee\">\n" +
                            "                <div id=\"name\">\n" +
                            "                    " + payslip.getEmployeeID().getFirstName() + " " + payslip.getEmployeeID().getLastName() + "\n" +
                            "                </div>\n" +
                            "                <div id=\"email\">\n" +
                            userService.fetchEmailByEmployeeId(payslip.getEmployeeID()).get().getEmail() +
                            "                </div>\n" +
                            "            </div>\n" +
                            "            <div class=\"details\">\n" +
                            "                <div class=\"entry\">\n" +
                            "                    <div class=\"label\">Employee ID</div>\n" +
                            "                    <div class=\"value\">" + payslip.getEmployeeID().getId() + "</div>\n" +
                            "                </div>\n" +
                            "                <div class=\"entry\">\n" +
                            "                    <div class=\"label\">Hourly Rate</div>\n" +
                            "                    <div class=\"value\">" + PesoUtility.formatToPeso(String.valueOf(payslip.getEmployeeID().getHourlyRate())) + "</div>\n" +
                            "                </div>\n" +
                            "                <div class=\"entry\">\n" +
                            "                    <div class=\"label\">Date Hired</div>\n" +
                            "                    <div class=\"value\">" + payslip.getEmployeeID().getDateHired() + "</div>\n" +
                            "                </div>\n" +
                            "                <div class=\"entry\">\n" +
                            "                    <div class=\"label\">Position</div>\n" +
                            "                    <div class=\"value\">" + payslip.getEmployeeID().getPositionID().getPositionName() + "</div>\n" +
                            "                </div>\n" +
                            "                <div class=\"entry\">\n" +
                            "                    <div class=\"label\">Department</div>\n" +
                            "                    <div class=\"value\">" + payslip.getEmployeeID().getPositionID().getDepartmentID().getDepartmentName() + "</div>\n" +
                            "                </div>\n" +
                            "                <div class=\"entry\">\n" +
                            "                    <div class=\"label\">Payroll Cycle</div>\n" +
                            "                    <div class=\"value\">Monthly</div>\n" + // this system is designed for monthly basis
                            "                </div>\n" +
                            "                <div class=\"entry\">\n" +
                            "                    <div class=\"label\">TIN</div>\n" +
                            "                    <div class=\"value\">" + payslip.getEmployeeID().getTINNumber() + "</div>\n" +
                            "                </div>\n" +
                            "                <div class=\"entry\">\n" +
                            "                    <div class=\"label\">SSS</div>\n" +
                            "                    <div class=\"value\">" + payslip.getEmployeeID().getSSSNumber() + "</div>\n" +
                            "                </div>\n" +
                            "                <div class=\"entry\">\n" +
                            "                    <div class=\"label\">Philhealth</div>\n" +
                            "                    <div class=\"value\">" + payslip.getEmployeeID().getPhilhealthNumber() + "</div>\n" +
                            "                </div>\n" +
                            "                <div class=\"entry\">\n" +
                            "                    <div class=\"label\">Pagibig</div>\n" +
                            "                    <div class=\"value\">" + payslip.getEmployeeID().getPagibigNumber() + "</div>\n" +
                            "                </div>\n" +
                            "                <div class=\"entry\">\n" +
                            "                    <div class=\"label\">Prepared by</div>\n" +
                            "                    <div class=\"value\">" + payslip.getCreatedBy().getFirstName() + " " + payslip.getCreatedBy().getFirstName() + "</div>\n" +
                            "                </div>\n" +
                            "            </div>\n" +
                            "\n" + "    <div class=\"title\">Year To Date Figures</div>\n" +
                            "                <div class=\"entry\">\n" +
                            "                    <div class=\"label\">Taxable Income</div>\n" +
                            "                    <div class=\"value\">"+PesoUtility.formatToPeso(String.valueOf(yearToDateFigures.get().ytdTaxableIncome()))+"</div>\n" +
                            "                </div>\n" +
                            "                <div class=\"entry\">\n" +
                            "                    <div class=\"label\">Withholding Tax</div>\n" +
                            "                    <div class=\"value\">"+PesoUtility.formatToPeso(String.valueOf(yearToDateFigures.get().ytdWitholdingTax()))+"</div>\n" +
                            "                </div>\n" +
                            "                <div class=\"entry\">\n" +
                            "                    <div class=\"label\">Net Pay</div>\n" +
                            "                    <div class=\"value\">"+PesoUtility.formatToPeso(String.valueOf(yearToDateFigures.get().ytdNetPay()))+"</div>\n" +
                            "                </div>\n" +
                            "                <div class=\"entry\">\n" +
                            "                    <div class=\"label\">Benefits</div>\n" +
                            "                    <div class=\"value\">"+PesoUtility.formatToPeso(String.valueOf(yearToDateFigures.get().ytdTotalBenefits()))+"</div>\n" +
                            "                </div>\n" +
                            "                <div class=\"entry\">\n" +
                            "                    <div class=\"label\">Bonus</div>\n" +
                            "                    <div class=\"value\">"+PesoUtility.formatToPeso(String.valueOf(yearToDateFigures.get().ytdBonus()))+"</div>\n" +
                            "                </div>\n" +
                            "                <div class=\"entry\">\n" +
                            "                    <div class=\"label\">Deduction</div>\n" +
                            "                    <div class=\"value\">"+PesoUtility.formatToPeso(String.valueOf(yearToDateFigures.get().ytdTotalDeductions()))+"</div>\n" +
                            "                </div>\n" +
                            "            </div>\n" +
                            "        </div>\n" +
                            "        <div class=\"right-panel\">\n" +
                            "            <div class=\"details\">\n" +
                            "                <div class=\"salary\">\n" +
                            "                    <div class=\"entry\">\n" +
                            "                        <div class=\"label\">Earnings</div>\n" +
                            "                        <div class=\"detail\"></div>\n" +
                            "                        <div class=\"rate\"></div>\n" +
                            "                        <div class=\"amount\"></div>\n" +
                            "                    </div>\n" +
                            "                    <div class=\"basic-pay\">\n" +
                            "                        <div class=\"entry\">\n" +
                            "                            <div class=\"label\"></div>\n" +
                            "                            <div class=\"detail\">Basic Pay</div>\n" +
                            "                            <div class=\"rate\">"+payslip.getEmployeeID().getBasicSalary()+"/Month</div>\n" +
                            "                            <div class=\"amount\">"+PesoUtility.formatToPeso(String.valueOf(payslip.getEmployeeID().getBasicSalary()))+"</div>\n" +
                            "                        </div>\n" +
                            "                    </div>\n" +
                            "                    <div class=\"entry\">\n" +
                            "                        <div class=\"label\"></div>\n" +
                            "                        <div class=\"detail\">Hours Worked</div>\n" +
                            "                        <div class=\"rate\">"+payslip.getTotalHoursWorked()+"/hr</div>\n" +
                            "                        <div class=\"amount\">"+PesoUtility.formatToPeso(String.valueOf(payslip.getTotalHoursWorked().multiply(payslip.getEmployeeID().getHourlyRate())))+"</div>\n" +
                            "                    </div>\n" +
                            "                    <div class=\"entry\">\n" +
                            "                        <div class=\"label\"></div>\n" +
                            "                        <div class=\"detail\">Overtime</div>\n" +
                            "                        <div class=\"rate\">"+payslip.getOvertimeHours()+"/hr</div>\n" +
                            "                        <div class=\"amount\">"+PesoUtility.formatToPeso(String.valueOf(payslip.getOvertimeHours().multiply(payslip.getEmployeeID().getHourlyRate())))+"</div>\n" +
                            "                    </div>\n" +
                            "                    <div class=\"entry\">\n" +
                            "                        <div class=\"label\"></div>\n" +
                            "                        <div class=\"detail\">Taxable Bonus</div>\n" +
                            "                        <div class=\"rate\"></div>\n" +
                            "                        <div class=\"amount\">"+PesoUtility.formatToPeso(String.valueOf(payslip.getBonus()))+"</div>\n" +
                            "                    </div>\n" +
                            "                    <div class=\"nti\">\n" +
                            "                        <div class=\"entry\">\n" +
                            "                            <div class=\"label\"></div>\n" +
                            "                            <div class=\"detail\">GROSS PAY</div>\n" +
                            "                            <div class=\"rate\"></div>\n" +
                            "                            <div class=\"amount\">"+PesoUtility.formatToPeso(String.valueOf(payslip.getGrossIncome()))+"</div>\n" +
                            "                        </div>\n" +
                            "                    </div>\n" +
                            "                </div>\n" +
                            "                <div class=\"leaves\">\n" +
                            "                    <div class=\"entry\">\n" +
                            "                        <div class=\"label\">Benefits</div>\n" +
                            "                        <div class=\"detail\"></div>\n" +
                            "                        <div class=\"rate\"></div>\n" +
                            "                        <div class=\"amount\">"+PesoUtility.formatToPeso(String.valueOf(payslip.getTotalBenefits()))+"</div>\n" +
                            "                    </div>\n" +
                            "                    <div class=\"entry paid\">\n" +
                            "                        <div class=\"label\"></div>\n" +
                            "                        <div class=\"detail\">Rice Subsidy</div>\n" +
                            "                        <div class=\"rate\"></div>\n" +
                            "                        <div class=\"amount\">"+PesoUtility.formatToPeso(String.valueOf(payslip.getRiceSubsidy()))+"</div>\n" +
                            "                    </div>\n" +
                            "                    <div class=\"entry unpaid\">\n" +
                            "                        <div class=\"label\"></div>\n" +
                            "                        <div class=\"detail\">Clothing</div>\n" +
                            "                        <div class=\"rate\"></div>\n" +
                            "                        <div class=\"amount\">"+PesoUtility.formatToPeso(String.valueOf(payslip.getClothingAllowance()))+"</div>\n" +
                            "                    </div>\n" +
                            "                    <div class=\"entry \">\n" +
                            "                        <div class=\"label\"></div>\n" +
                            "                        <div class=\"detail\">Phone</div>\n" +
                            "                        <div class=\"rate\"></div>\n" +
                            "                        <div class=\"amount\">"+PesoUtility.formatToPeso(String.valueOf(payslip.getPhoneAllowance()))+"</div>\n" +
                            "                    </div>\n" +
                            "                    <div class=\"nti\">\n" +
                            "                        <div class=\"entry\">\n" +
                            "                            <div class=\"label\"></div>\n" +
                            "                            <div class=\"detail\">TOTAL BENEFITS</div>\n" +
                            "                            <div class=\"rate\"></div>\n" +
                            "                            <div class=\"amount\">"+PesoUtility.formatToPeso(String.valueOf(payslip.getTotalBenefits()))+"</div>\n" +
                            "                        </div>\n" +
                            "                    </div>\n" +
                            "                </div>\n" +
                            "\n" +
                            "                <div class=\"taxable_commission\"></div>\n" +
                            "                <div class=\"contributions\">\n" +
                            "                    <div class=\"entry\">\n" +
                            "                        <div class=\"label\">Deductions</div>\n" +
                            "                        <div class=\"detail\"></div>\n" +
                            "                        <div class=\"rate\"></div>\n" +
                            "                        <div class=\"amount\"></div>\n" +
                            "                    </div>\n" +
                            "                    <div class=\"entry\">\n" +
                            "                        <div class=\"label\"></div>\n" +
                            "                        <div class=\"detail\">SSS</div>\n" +
                            "                        <div class=\"rate\"></div>\n" +
                            "                        <div class=\"amount\">"+PesoUtility.formatToPeso(String.valueOf(payslip.getSss()))+"</div>\n" +
                            "                    </div>\n" +
                            "                    <div class=\"entry\">\n" +
                            "                        <div class=\"label\"></div>\n" +
                            "                        <div class=\"detail\">PhilHealth</div>\n" +
                            "                        <div class=\"rate\"></div>\n" +
                            "                        <div class=\"amount\">"+PesoUtility.formatToPeso(String.valueOf(payslip.getPhilhealth()))+"</div>\n" +
                            "                    </div>\n" +
                            "                    <div class=\"entry\">\n" +
                            "                        <div class=\"label\"></div>\n" +
                            "                        <div class=\"detail\">Pagibig</div>\n" +
                            "                        <div class=\"rate\"></div>\n" +
                            "                        <div class=\"amount\">"+PesoUtility.formatToPeso(String.valueOf(payslip.getPagIbig()))+"</div>\n" +
                            "                    </div>\n" +
                            "                    <div class=\"entry\">\n" +
                            "                        <div class=\"label\"></div>\n" +
                            "                        <div class=\"detail\">Witholding-Tax</div>\n" +
                            "                        <div class=\"rate\"></div>\n" +
                            "                        <div class=\"amount\">"+PesoUtility.formatToPeso(String.valueOf(payslip.getWithholdingTax()))+"</div>\n" +
                            "                    </div>\n" +
                            "                    <div class=\"nti\">\n" +
                            "                        <div class=\"entry\">\n" +
                            "                            <div class=\"label\"></div>\n" +
                            "                            <div class=\"detail\">TOTAL DEDUCTIONS</div>\n" +
                            "                            <div class=\"rate\"></div>\n" +
                            "                            <div class=\"amount\">"+PesoUtility.formatToPeso(String.valueOf(payslip.getTotalDeductions()))+"</div>\n" +
                            "                        </div>\n" +
                            "                    </div>\n" +
                            "                    <div class=\"entry\">\n" +
                            "                        <div class=\"label\">Summary</div>\n" +
                            "                        <div class=\"detail\"></div>\n" +
                            "                        <div class=\"rate\"></div>\n" +
                            "                        <div class=\"amount\"></div>\n" +
                            "                    </div>\n" +
                            "                    <div class=\"entry\">\n" +
                            "                        <div class=\"label\"></div>\n" +
                            "                        <div class=\"detail\">Gross Income</div>\n" +
                            "                        <div class=\"rate\"></div>\n" +
                            "                        <div class=\"amount\">"+PesoUtility.formatToPeso(String.valueOf(payslip.getGrossIncome()))+"</div>\n" +
                            "                    </div>\n" +
                            "                    <div class=\"entry\">\n" +
                            "                        <div class=\"label\"></div>\n" +
                            "                        <div class=\"detail\">Deductions</div>\n" +
                            "                        <div class=\"rate\"></div>\n" +
                            "                        <div class=\"amount\">"+PesoUtility.formatToPeso(String.valueOf(payslip.getTotalDeductions()))+"</div>\n" +
                            "                    </div>\n" +
                            "                    <div class=\"entry\">\n" +
                            "                        <div class=\"label\"></div>\n" +
                            "                        <div class=\"detail\">Benefits</div>\n" +
                            "                        <div class=\"rate\"></div>\n" +
                            "                        <div class=\"amount\">"+PesoUtility.formatToPeso(String.valueOf(payslip.getTotalBenefits()))+"</div>\n" +
                            "                    </div>\n" +
                            "                </div>\n" +
                            "\n" +
                            "\n" +
                            "                <div class=\"net_pay\">\n" +
                            "                    <div class=\"entry\">\n" +
                            "                        <div class=\"label\"></div>\n" +
                            "                        <div class=\"detail\">NET PAY</div>\n" +
                            "                        <div class=\"rate\"></div>\n" +
                            "                        <div class=\"amount\">"+ PesoUtility.formatToPeso(String.valueOf(payslip.getNetPay())) +"</div>\n" +
                            "                    </div>\n" +
                            "                </div>\n" +
                            "            </div>\n" +
                            "        </div>\n" +
                            "    </div>\n" +
                            "</div>\n" +
                            "</body>\n" +
                            "\n" +
                            "<style>\n" +
                            "    body {\n" +
                            "        background: #f0f0f0;\n" +
                            "        width: 100vw;\n" +
                            "        height: 100vh;\n" +
                            "        display: flex;\n" +
                            "        justify-content: center;\n" +
                            "        padding: 20px;\n" +
                            "        height: 100%;\n" +
                            "    }\n" +
                            "\n" +
                            "    @import url('https://fonts.googleapis.com/css?family=Roboto:200,300,400,600,700');\n" +
                            "\n" +
                            "    * {\n" +
                            "        font-family: 'Roboto', sans-serif;\n" +
                            "        font-size: 12px;\n" +
                            "        color: #444;\n" +
                            "    }\n" +
                            "\n" +
                            "    #payslip {\n" +
                            "        width: calc( 8.5in - 80px );\n" +
                            "        height: calc( 11in - 60px );\n" +
                            "        background: #fff;\n" +
                            "        padding: 30px 40px;\n" +
                            "    }\n" +
                            "\n" +
                            "    #title {\n" +
                            "        margin-bottom: 20px;\n" +
                            "        font-size: 38px;\n" +
                            "        font-weight: 600;\n" +
                            "    }\n" +
                            "\n" +
                            "    #scope {\n" +
                            "        border-top: 1px solid #ccc;\n" +
                            "        border-bottom: 1px solid #ccc;\n" +
                            "        padding: 7px 0 4px 0;\n" +
                            "        display: flex;\n" +
                            "        justify-content: space-around;\n" +
                            "    }\n" +
                            "\n" +
                            "    #scope > .scope-entry {\n" +
                            "        text-align: center;\n" +
                            "    }\n" +
                            "\n" +
                            "    #scope > .scope-entry > .value {\n" +
                            "        font-size: 14px;\n" +
                            "        font-weight: 700;\n" +
                            "    }\n" +
                            "\n" +
                            "    .content {\n" +
                            "        display: flex;\n" +
                            "        border-bottom: 1px solid #ccc;\n" +
                            "        height: 880px;\n" +
                            "    }\n" +
                            "\n" +
                            "    .content .left-panel {\n" +
                            "        border-right: 1px solid #ccc;\n" +
                            "        min-width: 200px;\n" +
                            "        padding: 20px 16px 0 0;\n" +
                            "    }\n" +
                            "\n" +
                            "    .content .right-panel {\n" +
                            "        width: 100%;\n" +
                            "        padding: 10px 0  0 16px;\n" +
                            "    }\n" +
                            "\n" +
                            "    #employee {\n" +
                            "        text-align: center;\n" +
                            "        margin-bottom: 20px;\n" +
                            "    }\n" +
                            "    #employee #name {\n" +
                            "        font-size: 15px;\n" +
                            "        font-weight: 700;\n" +
                            "    }\n" +
                            "\n" +
                            "    #employee #email {\n" +
                            "        font-size: 11px;\n" +
                            "        font-weight: 300;\n" +
                            "    }\n" +
                            "\n" +
                            "    .details, .contributions, .ytd, .gross {\n" +
                            "        margin-bottom: 20px;\n" +
                            "    }\n" +
                            "\n" +
                            "    .details .entry, .contributions .entry, .ytd .entry {\n" +
                            "        display: flex;\n" +
                            "        justify-content: space-between;\n" +
                            "        margin-bottom: 6px;\n" +
                            "    }\n" +
                            "\n" +
                            "    .details .entry .value, .contributions .entry .value, .ytd .entry .value {\n" +
                            "        font-weight: 700;\n" +
                            "        max-width: 130px;\n" +
                            "        text-align: right;\n" +
                            "    }\n" +
                            "\n" +
                            "    .gross .entry .value {\n" +
                            "        font-weight: 700;\n" +
                            "        text-align: right;\n" +
                            "        font-size: 16px;\n" +
                            "    }\n" +
                            "\n" +
                            "    .contributions .title, .ytd .title, .gross .title {\n" +
                            "        font-size: 15px;\n" +
                            "        font-weight: 700;\n" +
                            "        border-bottom: 1px solid #ccc;\n" +
                            "        padding-bottom: 4px;\n" +
                            "        margin-bottom: 6px;\n" +
                            "    }\n" +
                            "\n" +
                            "    .content .right-panel .details {\n" +
                            "        width: 100%;\n" +
                            "    }\n" +
                            "\n" +
                            "    .content .right-panel .details .entry {\n" +
                            "        display: flex;\n" +
                            "        padding: 0 10px;\n" +
                            "        margin: 6px 0;\n" +
                            "    }\n" +
                            "\n" +
                            "    .content .right-panel .details .label {\n" +
                            "        font-weight: 700;\n" +
                            "        width: 120px;\n" +
                            "    }\n" +
                            "\n" +
                            "    .content .right-panel .details .detail {\n" +
                            "        font-weight: 600;\n" +
                            "        width: 130px;\n" +
                            "    }\n" +
                            "\n" +
                            "    .content .right-panel .details .rate {\n" +
                            "        font-weight: 400;\n" +
                            "        width: 80px;\n" +
                            "        font-style: italic;\n" +
                            "        letter-spacing: 1px;\n" +
                            "    }\n" +
                            "\n" +
                            "    .content .right-panel .details .amount {\n" +
                            "        text-align: right;\n" +
                            "        font-weight: 700;\n" +
                            "        width: 90px;\n" +
                            "    }\n" +
                            "\n" +
                            "    .content .right-panel .details .net_pay div, .content .right-panel .details .nti div {\n" +
                            "        font-weight: 600;\n" +
                            "        font-size: 12px;\n" +
                            "    }\n" +
                            "\n" +
                            "    .content .right-panel .details .net_pay, .content .right-panel .details .nti {\n" +
                            "        padding: 3px 0 2px 0;\n" +
                            "        margin-bottom: 10px;\n" +
                            "        background: rgba(0, 0, 0, 0.04);\n" +
                            "    }\n" +
                            "\n" +
                            "</style>\n" +
                            "\n" +
                            "</html>";


                    List<String> lines = Arrays.asList(strPayslipHTMLSourceCodeByXinTaroAndModifiedByJomariAbejo);
                    FileChooser fileChooser = new FileChooser();
                    File filepath = fileChooser.showOpenDialog(getTableView().getScene().getWindow()).getAbsoluteFile();
                    try {
                        writeSmallTextFile(lines, String.valueOf(filepath));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : actionsBox);
            }
        });
        return actionsColumn;
    }

    private static void writeSmallTextFile(List<String> aLines, String aFileName) throws IOException {
        Path path = Paths.get(aFileName);
        Files.write(path, aLines, StandardCharsets.UTF_8);
    }

    private Button createDownloadButton() {
        FontIcon fontIcon = new FontIcon(Feather.DOWNLOAD);
        Button updateButton = new Button(null, fontIcon);
        updateButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
        return updateButton;
    }
}
