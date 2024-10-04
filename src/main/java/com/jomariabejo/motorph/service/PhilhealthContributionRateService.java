package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.PhilhealthContributionRate;
import com.jomariabejo.motorph.repository.PhilhealthContributionRateRepository;

import java.util.List;

public class PhilhealthContributionRateService {

    private final PhilhealthContributionRateRepository rateRepository;

    public PhilhealthContributionRateService(PhilhealthContributionRateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    public PhilhealthContributionRate getRateById(Integer id) {
        return rateRepository.findById(id);
    }

    public List<PhilhealthContributionRate> getAllRates() {
        return rateRepository.findAll();
    }

    public void saveRate(PhilhealthContributionRate rate) {
        rateRepository.save(rate);
    }

    public void updateRate(PhilhealthContributionRate rate) {
        rateRepository.update(rate);
    }

    public void deleteRate(PhilhealthContributionRate rate) {
        rateRepository.delete(rate);
    }
}
