package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.ReimbursementRequest;
import com.jomariabejo.motorph.repository.ReimbursementRequestRepository;

import java.util.List;
import java.util.Optional;

public class ReimbursementRequestService {

    private final ReimbursementRequestRepository requestRepository;

    public ReimbursementRequestService(ReimbursementRequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public ReimbursementRequest getRequestById(Integer id) {
        return requestRepository.findById(id);
    }

    public List<ReimbursementRequest> getAllRequests() {
        return requestRepository.findAll();
    }

    public void saveRequest(ReimbursementRequest request) {
        requestRepository.save(request);
    }

    public void updateRequest(ReimbursementRequest request) {
        requestRepository.update(request);
    }

    public void deleteRequest(ReimbursementRequest request) {
        requestRepository.delete(request);
    }

    public Optional<List<Integer>> fetchEmployeeYearsOfReimbursements(Employee employee) {
        return requestRepository.findEmployeeYearsOfReimbursement(employee);
    }

    public Optional<List<ReimbursementRequest>> fetchReimbursementByEmployeeIdAndYear(Employee employee, Integer year) {
        return requestRepository.findReimbursementByEmployeeAndYear(employee, year);
    }
}
