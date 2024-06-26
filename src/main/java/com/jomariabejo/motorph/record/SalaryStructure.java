package com.jomariabejo.motorph.record;

import java.math.BigDecimal;

public record SalaryStructure(BigDecimal basicSalary, BigDecimal grossSemiMonthlyRate, BigDecimal hourlyrate) {
}
