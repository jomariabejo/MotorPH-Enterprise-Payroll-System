package com.jomariabejo.motorph.sampler;

import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.LeaveRequest;
import com.jomariabejo.motorph.repository.EmployeeRepository;
import com.jomariabejo.motorph.repository.LeaveRequestRepository;
import com.jomariabejo.motorph.service.EmployeeService;
import com.jomariabejo.motorph.service.LeaveRequestService;
import com.jomariabejo.motorph.utility.NetworkUtils;
import com.jomariabejo.motorph.utility.TimestampUtils;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SampleInstantCurrentTime {
    public static void main(String[] args) {
        LeaveRequestService leaveRequestService = new LeaveRequestService(new LeaveRequestRepository());
        EmployeeService employeeService = new EmployeeService(new EmployeeRepository());
        Employee employee = employeeService.getEmployeeById(4);


        List<LeaveRequest> leaveRequestList = leaveRequestService.fetchLeaveRequestsForEmployee(
                employee, Month.AUGUST.name(), String.valueOf(2024), "Pending", "Sick"
        );

        System.out.println(leaveRequestList.size());
    }
}
