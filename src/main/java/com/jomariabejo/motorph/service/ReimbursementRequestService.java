package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.ReimbursementRequest;
import com.jomariabejo.motorph.repository.ReimbursementRequestRepository;

import java.util.List;

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
}
