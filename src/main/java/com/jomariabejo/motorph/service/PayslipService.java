package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.Payslip;
import com.jomariabejo.motorph.repository.PayslipRepository;

import java.util.List;

public class PayslipService {

    private final PayslipRepository payslipRepository;

    public PayslipService(PayslipRepository payslipRepository) {
        this.payslipRepository = payslipRepository;
    }

    public Payslip getPayslipById(Integer id) {
        return payslipRepository.findById(id);
    }

    public List<Payslip> getAllPayslips() {
        return payslipRepository.findAll();
    }

    public void savePayslip(Payslip payslip) {
        payslipRepository.save(payslip);
    }

    public void updatePayslip(Payslip payslip) {
        payslipRepository.update(payslip);
    }

    public void deletePayslip(Payslip payslip) {
        payslipRepository.delete(payslip);
    }
}
