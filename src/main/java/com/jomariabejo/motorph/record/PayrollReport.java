package com.jomariabejo.motorph.record;

import java.math.BigDecimal;

public record PayrollReport(String departmentName, Number grossIncomes, Number netIncomes, Number benefits, Number deductions) {
}
