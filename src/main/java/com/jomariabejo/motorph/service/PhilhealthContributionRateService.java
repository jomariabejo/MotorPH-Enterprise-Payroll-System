package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.PhilhealthContributionRate;
import com.jomariabejo.motorph.repository.PhilhealthContributionRateRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    /**
     * Populates standard Philhealth contribution rates.
     * Rates based on Philhealth 2023 guidelines:
     * - 10,000: 3% premium rate, 300 monthly premium (150 employee, 150 employer)
     * - 10,000.01 to 59,999.99: 3% premium rate, 300 up to 1,800 monthly premium (50% each)
     * - 60,000: 3% premium rate, 1,800 monthly premium (900 employee, 900 employer)
     * 
     * Note: EmployeeShare and EmployerShare store the monthly premium amounts (not percentages).
     * For the variable bracket (10,000.01 to 59,999.99), EmployeeShare stores minimum (150)
     * and EmployerShare stores maximum (900) to indicate the range.
     */
    public void populateStandardRates() {
        LocalDate effectiveDate = LocalDate.now();
        
        // Check if rates already exist
        List<PhilhealthContributionRate> existingRates = getAllRates();
        if (!existingRates.isEmpty()) {
            // Rates already exist, skip population
            return;
        }
        
        // Rate 1: Exactly 10,000 - 300 monthly premium (150 employee, 150 employer)
        PhilhealthContributionRate rate1 = new PhilhealthContributionRate();
        rate1.setSalaryBracketFrom(BigDecimal.valueOf(10000));
        rate1.setSalaryBracketTo(BigDecimal.valueOf(10000));
        rate1.setEmployeeShare(BigDecimal.valueOf(150)); // 50% of 300
        rate1.setEmployerShare(BigDecimal.valueOf(150)); // 50% of 300
        rate1.setEffectiveDate(effectiveDate);
        saveRate(rate1);
        
        // Rate 2: 10,000.01 to 59,999.99 - 3% premium rate, 300 up to 1,800 monthly premium
        // Calculation: Premium = min(1800, max(300, salary * 3%))
        // EmployeeShare = 150 (minimum employee share), EmployerShare = 900 (maximum employee share)
        PhilhealthContributionRate rate2 = new PhilhealthContributionRate();
        rate2.setSalaryBracketFrom(BigDecimal.valueOf(10000.01));
        rate2.setSalaryBracketTo(BigDecimal.valueOf(59999.99));
        rate2.setEmployeeShare(BigDecimal.valueOf(150)); // Minimum employee share (50% of 300)
        rate2.setEmployerShare(BigDecimal.valueOf(900)); // Maximum employee share (50% of 1800)
        rate2.setEffectiveDate(effectiveDate);
        saveRate(rate2);
        
        // Rate 3: 60,000 and above - 1,800 monthly premium (900 employee, 900 employer)
        PhilhealthContributionRate rate3 = new PhilhealthContributionRate();
        rate3.setSalaryBracketFrom(BigDecimal.valueOf(60000));
        rate3.setSalaryBracketTo(BigDecimal.valueOf(999999.99)); // Very high upper limit
        rate3.setEmployeeShare(BigDecimal.valueOf(900)); // 50% of 1800
        rate3.setEmployerShare(BigDecimal.valueOf(900)); // 50% of 1800
        rate3.setEffectiveDate(effectiveDate);
        saveRate(rate3);
    }
}
