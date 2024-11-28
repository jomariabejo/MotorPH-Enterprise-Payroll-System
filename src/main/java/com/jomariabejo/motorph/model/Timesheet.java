package com.jomariabejo.motorph.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Time;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "timesheet", schema = "payroll_system")
@NamedQueries({
        @NamedQuery(
                name = "getYearsOfTimesheet",
                query = "SELECT DISTINCT FUNCTION('YEAR', ts.date) AS Year FROM Timesheet ts WHERE ts.employeeID = :employee ORDER BY Year DESC"
        ),
        @NamedQuery(
                name = "fetchTimesheetByEmployeeAndDate",
                query = "SELECT ts FROM Timesheet ts WHERE ts.employeeID = :employee AND ts.date = :date"
        ),
        @NamedQuery(
                name = "fetchEmployeeTimesheetsByMonthAndYear",
                query = "SELECT TS FROM Timesheet TS WHERE TS.employeeID = :EMPLOYEE " +
                        "AND FUNCTION('YEAR', TS.date) = :YEAR " +
                        "AND FUNCTION('MONTH', TS.date) = :MONTH " +
                        "ORDER BY TS.date DESC"
        ),
        @NamedQuery(
                name = "findTimesheetBySpecifiedDate",
                query = "SELECT TS FROM Timesheet TS WHERE \n" +
                        "TS.date = :LOCAL_DATE"
        ),
        @NamedQuery(
                name = "findTimesheetByMonthAndYear",
                query = "SELECT TS FROM Timesheet TS WHERE " +
                        "YEAR(TS.date) = YEAR(:LOCAL_DATE) AND " +
                        "MONTH(TS.date) = MONTH(:LOCAL_DATE)"
        ),
        @NamedQuery(
                name = "findAllTimesheetByYear",
                query = "SELECT TS FROM Timesheet TS WHERE " +
                        "YEAR(TS.date) = YEAR(:LOCAL_DATE)"
        ),
        @NamedQuery(
                name = "findTimesheetByEmployeeAndDate",
                query = "SELECT TS FROM Timesheet TS WHERE TS.date = :DATE AND CONCAT(TS.employeeID.firstName, ' ', TS.employeeID.lastName) = :NAME"
        ),
        @NamedQuery(
                name = "findTimesheetByEmployeeAndMonth",
                query = "SELECT TS FROM Timesheet TS WHERE " +
                        "MONTH(TS.date) = MONTH(:DATE)" +
                        "AND YEAR(TS.date) = YEAR (:DATE)" +
                        "AND CONCAT(TS.employeeID.firstName, ' ', TS.employeeID.lastName) = :NAME"
        ),
        @NamedQuery(
                name = "findTimesheetByEmployeeAndYear",
                query = "SELECT TS FROM Timesheet TS WHERE YEAR(TS.date) = YEAR(:DATE) " +
                        "AND CONCAT(TS.employeeID.firstName, ' ', TS.employeeID.lastName) = :NAME"
        )
})
public class Timesheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TimesheetID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EmployeeID", nullable = false)
    private Employee employeeID;

    @Column(name = "Date", nullable = false)
    private LocalDate date;

    @Column(name = "TimeIn", nullable = false)
    private Time timeIn;

    @Column(name = "TimeOut")
    private Time timeOut;

    @Column(name = "Remarks")
    private String remarks;

    @Column(name = "HoursWorked")
    private Float hoursWorked;

    @ColumnDefault("'Submitted'")
    @Column(name = "Status", nullable = false, length = 20)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Approver")
    private Employee approver;


    public String getEmployeeName() {
        if (employeeID != null) {
            return employeeID.getFirstName() + " " + employeeID.getLastName();
        }
        return "";
    }

}