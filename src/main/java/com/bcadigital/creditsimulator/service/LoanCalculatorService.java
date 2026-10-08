package com.bcadigital.creditsimulator.service;

import com.bcadigital.creditsimulator.model.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class LoanCalculatorService {

    private static final BigDecimal MAX_LOAN_AMOUNT = new BigDecimal("1000000000");
    private final InterestRateStrategy interestRateStrategy;

    public LoanCalculatorService() {
        this(new DefaultInterestRateStrategy());
    }

    public LoanCalculatorService(InterestRateStrategy interestRateStrategy) {
        this.interestRateStrategy = interestRateStrategy;
    }

    public void validate(LoanRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Permintaan simulasi tidak boleh null.");
        }

        VehicleType type = request.getVehicleType();
        VehicleCondition condition = request.getVehicleCondition();

        int year = request.getVehicleYear();
        if (year < 1000 || year > 9999) {
            throw new IllegalArgumentException("Tahun kendaraan harus 4 digit angka.");
        }

        int currentYear = Year.now().getValue();
        if (condition == VehicleCondition.BARU && year < (currentYear - 1)) {
            throw new IllegalArgumentException(String.format(
                    "Kendaraan dengan kondisi BARU tidak boleh memiliki tahun kurang dari %d (CurrentYear - 1). Input tahun: %d",
                    currentYear - 1, year));
        }

        BigDecimal totalLoan = request.getTotalLoanAmount();
        if (totalLoan == null || totalLoan.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Jumlah Pinjaman Total harus lebih dari 0.");
        }
        if (totalLoan.compareTo(MAX_LOAN_AMOUNT) > 0) {
            throw new IllegalArgumentException("Jumlah Pinjaman Total tidak boleh melebihi Rp 1.000.000.000 (1 Miliar).");
        }

        int tenure = request.getLoanTenure();
        if (tenure < 1 || tenure > 6) {
            throw new IllegalArgumentException("Tenor Pinjaman harus antara 1 sampai 6 tahun.");
        }

        BigDecimal dp = request.getDownPayment();
        if (dp == null || dp.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Jumlah DP harus lebih dari 0.");
        }
        if (dp.compareTo(totalLoan) >= 0) {
            throw new IllegalArgumentException("Jumlah DP tidak boleh sama atau melebihi Jumlah Pinjaman Total.");
        }

        double minDpPercent = condition.getMinDownPaymentPercentage();
        BigDecimal minDpAmount = totalLoan.multiply(BigDecimal.valueOf(minDpPercent));

        if (dp.compareTo(minDpAmount) < 0) {
            throw new IllegalArgumentException(String.format(
                    "Jumlah DP untuk kendaraan %s %s minimal %.0f%% dari Jumlah Pinjaman (Minimal: Rp %,.2f).",
                    type.getDisplayName(), condition.getDisplayName(), minDpPercent * 100, minDpAmount));
        }
    }

    public CalculationResult calculate(LoanRequest request) {
        validate(request);

        BigDecimal totalLoan = request.getTotalLoanAmount();
        BigDecimal dp = request.getDownPayment();
        BigDecimal netPrincipal = totalLoan.subtract(dp);
        int tenure = request.getLoanTenure();
        VehicleType type = request.getVehicleType();

        List<YearlyInstallment> yearlyInstallments = new ArrayList<>();
        BigDecimal pokokSisa = netPrincipal;

        for (int year = 1; year <= tenure; year++) {
            double ratePercent = interestRateStrategy.calculateInterestRate(type, year);
            BigDecimal rate = BigDecimal.valueOf(ratePercent).movePointLeft(2);

            BigDecimal totalTahun = pokokSisa.multiply(BigDecimal.ONE.add(rate));
            int sisaBulan = (12 * tenure) - (12 * (year - 1));
            BigDecimal cicilanBulan = totalTahun.divide(BigDecimal.valueOf(sisaBulan), 2, RoundingMode.HALF_UP);
            BigDecimal cicilanTahun = cicilanBulan.multiply(BigDecimal.valueOf(12));
            pokokSisa = totalTahun.subtract(cicilanTahun);

            yearlyInstallments.add(new YearlyInstallment(year, cicilanBulan, ratePercent));
        }

        return new CalculationResult(request, netPrincipal, yearlyInstallments);
    }
}
