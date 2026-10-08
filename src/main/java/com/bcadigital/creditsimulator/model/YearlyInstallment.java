package com.bcadigital.creditsimulator.model;

import java.math.BigDecimal;

public record YearlyInstallment(
        int year,
        BigDecimal monthlyInstallment,
        double interestRatePercent
) {
}
