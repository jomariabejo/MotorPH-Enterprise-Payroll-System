package com.jomariabejo.motorph.record;

import java.math.BigDecimal;

public record Salary(BigDecimal basicSalary, BigDecimal grossSemiMonthlyRate, BigDecimal hourlyrate) {
}
