package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.PagibigContributionRate;
import com.jomariabejo.motorph.repository.PagibigContributionRateRepository;

import java.util.List;

public class PagibigContributionRateService {

    private final PagibigContributionRateRepository pagibigContributionRateRepository;

    public PagibigContributionRateService(PagibigContributionRateRepository pagibigContributionRateRepository) {
        this.pagibigContributionRateRepository = pagibigContributionRateRepository;
    }

    public PagibigContributionRate getPagibigContributionRateById(Integer id) {
        return pagibigContributionRateRepository.findById(id);
    }

    public List<PagibigContributionRate> getAllPagibigContributionRates() {
        return pagibigContributionRateRepository.findAll();
    }

    public void savePagibigContributionRate(PagibigContributionRate pagibigContributionRate) {
        pagibigContributionRateRepository.save(pagibigContributionRate);
    }

    public void updatePagibigContributionRate(PagibigContributionRate pagibigContributionRate) {
        pagibigContributionRateRepository.update(pagibigContributionRate);
    }

    public void deletePagibigContributionRate(PagibigContributionRate pagibigContributionRate) {
        pagibigContributionRateRepository.delete(pagibigContributionRate);
    }
}
