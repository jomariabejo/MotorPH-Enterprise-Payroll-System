package com.jomariabejo.motorph.model;

import java.math.BigDecimal;

public record YearToDateFigures(
        BigDecimal ytdGrossIncome,
        BigDecimal ytdTotalBenefits,
        BigDecimal ytdBonus,
        BigDecimal ytdTaxableIncome,
        BigDecimal ytdWitholdingTax,
        BigDecimal ytdTotalDeductions,
        BigDecimal ytdNetPay
) {
}
