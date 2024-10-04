package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.TinCompliance;
import com.jomariabejo.motorph.repository.TinComplianceRepository;

import java.util.List;

public class TinComplianceService {

    private final TinComplianceRepository tinComplianceRepository;

    public TinComplianceService(TinComplianceRepository tinComplianceRepository) {
        this.tinComplianceRepository = tinComplianceRepository;
    }

    public TinCompliance getTinComplianceById(Integer id) {
        return tinComplianceRepository.findById(id);
    }

    public List<TinCompliance> getAllTinCompliances() {
        return tinComplianceRepository.findAll();
    }

    public void saveTinCompliance(TinCompliance tinCompliance) {
        tinComplianceRepository.save(tinCompliance);
    }

    public void updateTinCompliance(TinCompliance tinCompliance) {
        tinComplianceRepository.update(tinCompliance);
    }

    public void deleteTinCompliance(TinCompliance tinCompliance) {
        tinComplianceRepository.delete(tinCompliance);
    }
}
