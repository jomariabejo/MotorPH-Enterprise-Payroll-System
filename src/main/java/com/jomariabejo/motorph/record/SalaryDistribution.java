package com.jomariabejo.motorph.record;

import java.math.BigDecimal;

public record SalaryDistribution(
        BigDecimal grossSalary,
        BigDecimal netSalary) {
}
