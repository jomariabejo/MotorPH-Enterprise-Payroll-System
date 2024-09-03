package com.jomariabejo.motorph.sampler;

import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.repository.PayslipRepository;
import com.jomariabejo.motorph.service.PayslipService;

import java.time.Year;

public class sampleYTD {
    public static void main(String[] args) {
        PayslipService payslipService = new PayslipService(new PayslipRepository());
        Employee employee = new Employee();
        employee.setEmployeeNumber(4);
        Year year = Year.of(2024);
        System.out.println(payslipService.getYearToDateFigures(employee,year).toString());
    }
}
