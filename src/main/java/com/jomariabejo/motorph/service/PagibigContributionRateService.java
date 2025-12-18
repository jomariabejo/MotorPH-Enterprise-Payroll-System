package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.PagibigContributionRate;
import com.jomariabejo.motorph.repository.PagibigContributionRateRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    /**
     * Populates standard Pagibig contribution rates.
     * Rates:
     * - At least 1,000 to 1,500: 1% employee, 2% employer, 3% total
     * - Over 1,500: 2% employee, 2% employer, 4% total
     */
    public void populateStandardRates() {
        LocalDate effectiveDate = LocalDate.now();
        
        // Check if rates already exist
        List<PagibigContributionRate> existingRates = getAllPagibigContributionRates();
        if (!existingRates.isEmpty()) {
            // Rates already exist, skip population
            return;
        }
        
        // Rate 1: 1,000 to 1,500 - 1% employee, 2% employer
        PagibigContributionRate rate1 = new PagibigContributionRate();
        rate1.setSalaryBracketFrom(BigDecimal.valueOf(1000));
        rate1.setSalaryBracketTo(BigDecimal.valueOf(1500));
        rate1.setEmployeeShare(BigDecimal.valueOf(1)); // 1%
        rate1.setEmployerShare(BigDecimal.valueOf(2)); // 2%
        rate1.setEffectiveDate(effectiveDate);
        savePagibigContributionRate(rate1);
        
        // Rate 2: Over 1,500 - 2% employee, 2% employer
        PagibigContributionRate rate2 = new PagibigContributionRate();
        rate2.setSalaryBracketFrom(BigDecimal.valueOf(1500.01));
        rate2.setSalaryBracketTo(BigDecimal.valueOf(999999.99)); // Very high upper limit
        rate2.setEmployeeShare(BigDecimal.valueOf(2)); // 2%
        rate2.setEmployerShare(BigDecimal.valueOf(2)); // 2%
        rate2.setEffectiveDate(effectiveDate);
        savePagibigContributionRate(rate2);
    }
}
