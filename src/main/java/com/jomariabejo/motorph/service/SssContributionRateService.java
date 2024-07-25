package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.SssContributionRate;
import com.jomariabejo.motorph.repository.SssContributionRateRepository;

import java.util.List;

public class SssContributionRateService {

    private final SssContributionRateRepository sssContributionRateRepository;

    public SssContributionRateService(SssContributionRateRepository sssContributionRateRepository) {
        this.sssContributionRateRepository = sssContributionRateRepository;
    }

    public SssContributionRate getSssContributionRateById(Integer id) {
        return sssContributionRateRepository.findById(id);
    }

    public List<SssContributionRate> getAllSssContributionRates() {
        return sssContributionRateRepository.findAll();
    }

    public void saveSssContributionRate(SssContributionRate sssContributionRate) {
        sssContributionRateRepository.save(sssContributionRate);
    }

    public void updateSssContributionRate(SssContributionRate sssContributionRate) {
        sssContributionRateRepository.update(sssContributionRate);
    }

    public void deleteSssContributionRate(SssContributionRate sssContributionRate) {
        sssContributionRateRepository.delete(sssContributionRate);
    }
}
