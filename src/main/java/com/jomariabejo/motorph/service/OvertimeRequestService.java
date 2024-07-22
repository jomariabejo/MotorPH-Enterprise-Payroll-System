package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.OvertimeRequest;
import com.jomariabejo.motorph.repository.OvertimeRequestRepository;

import java.util.List;

public class OvertimeRequestService {

    private final OvertimeRequestRepository overtimeRequestRepository;

    public OvertimeRequestService(OvertimeRequestRepository overtimeRequestRepository) {
        this.overtimeRequestRepository = overtimeRequestRepository;
    }

    public OvertimeRequest getOvertimeRequestById(Integer id) {
        return overtimeRequestRepository.findById(id);
    }

    public List<OvertimeRequest> getAllOvertimeRequests() {
        return overtimeRequestRepository.findAll();
    }

    public void saveOvertimeRequest(OvertimeRequest overtimeRequest) {
        overtimeRequestRepository.save(overtimeRequest);
    }

    public void updateOvertimeRequest(OvertimeRequest overtimeRequest) {
        overtimeRequestRepository.update(overtimeRequest);
    }

    public void deleteOvertimeRequest(OvertimeRequest overtimeRequest) {
        overtimeRequestRepository.delete(overtimeRequest);
    }
}
