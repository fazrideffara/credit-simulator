package com.bcadigital.creditsimulator.service;

import com.bcadigital.creditsimulator.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class LoanCalculatorServiceTest {

    private LoanCalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        calculatorService = new LoanCalculatorService();
    }

    @Test
    @DisplayName("Valid calculation for Mobil Baru 6 Years Tenure")
    void testCalculateMobilBaruSuccess() {
        int tahun = Year.now().getValue();
        LoanRequest request = new LoanRequest(
                "Mobil",
                "Baru",
                tahun,
                new BigDecimal("1000000000"),
                6,
                new BigDecimal("500000000")
        );

        CalculationResult result = calculatorService.calculate(request);

        assertNotNull(result);
        assertEquals(0, new BigDecimal("500000000").compareTo(result.getPrincipalAmount()));
        assertEquals(6, result.getYearlyInstallments().size());

        assertInstallment(result, 0, 1, 8.0, "7500000.00");
        assertInstallment(result, 1, 2, 8.1, "8107500.00");
        assertInstallment(result, 2, 3, 8.6, "8804745.00");
        assertInstallment(result, 3, 4, 8.6, "9561953.07");
        assertInstallment(result, 4, 5, 9.1, "10432090.80");
        assertInstallment(result, 5, 6, 9.1, "11381411.06");

        String text = result.formatSummary();
        assertTrue(text.contains("tahun 1 : Rp. 7,500,000.00/bln , Suku Bunga : 8%"));
        assertTrue(text.contains("tahun 2 : Rp. 8,107,500.00/bln , Suku Bunga : 8,1%"));
    }

    @Test
    @DisplayName("Angka cicilan sama dengan contoh Rumus.xlsx")
    void testExcelSample() {
        LoanRequest request = new LoanRequest(
                "Mobil",
                "Bekas",
                2020,
                new BigDecimal("100000000"),
                3,
                new BigDecimal("25000000")
        );

        CalculationResult result = calculatorService.calculate(request);
        assertInstallment(result, 0, 1, 8.0, "2250000.00");
        assertInstallment(result, 1, 2, 8.1, "2432250.00");
        assertInstallment(result, 2, 3, 8.6, "2641423.50");
        assertTrue(result.formatSummary().contains("tahun 3 : Rp. 2,641,423.50/bln , Suku Bunga : 8,6%"));
    }

    private void assertInstallment(CalculationResult result, int index, int year, double rate, String amount) {
        YearlyInstallment row = result.getYearlyInstallments().get(index);
        assertEquals(year, row.year());
        assertEquals(rate, row.interestRatePercent(), 0.001);
        assertEquals(0, new BigDecimal(amount).compareTo(row.monthlyInstallment()));
    }

    @Test
    @DisplayName("Validation fails when DP for Mobil Baru is less than 35%")
    void testValidationFailsForInsufficientDpBaru() {
        LoanRequest request = new LoanRequest(
                "Mobil",
                "Baru",
                Year.now().getValue(),
                new BigDecimal("100000000"),
                3,
                new BigDecimal("30000000")
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> calculatorService.calculate(request));
        assertTrue(ex.getMessage().contains("minimal 35%"));
    }

    @Test
    @DisplayName("Validation fails when DP for Motor Bekas is less than 25%")
    void testValidationFailsForInsufficientDpBekas() {
        LoanRequest request = new LoanRequest(
                "Motor",
                "Bekas",
                2020,
                new BigDecimal("20000000"),
                2,
                new BigDecimal("4000000")
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> calculatorService.calculate(request));
        assertTrue(ex.getMessage().contains("minimal 25%"));
    }

    @Test
    @DisplayName("Validation fails when vehicle year for NEW condition is less than CurrentYear - 1")
    void testValidationFailsForInvalidNewVehicleYear() {
        int invalidYear = Year.now().getValue() - 2;
        LoanRequest request = new LoanRequest(
                "Mobil",
                "Baru",
                invalidYear,
                new BigDecimal("500000000"),
                3,
                new BigDecimal("200000000")
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> calculatorService.calculate(request));
        assertTrue(ex.getMessage().contains("tidak boleh memiliki tahun kurang dari"));
    }

    @Test
    @DisplayName("Validation fails when Loan Amount exceeds 1 Billion")
    void testValidationFailsForExceedingMaxLoan() {
        LoanRequest request = new LoanRequest(
                "Mobil",
                "Bekas",
                2022,
                new BigDecimal("1500000000"),
                5,
                new BigDecimal("400000000")
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> calculatorService.calculate(request));
        assertTrue(ex.getMessage().contains("tidak boleh melebihi Rp 1.000.000.000"));
    }

    @Test
    @DisplayName("Validation fails when Tenure is outside 1-6 years")
    void testValidationFailsForInvalidTenure() {
        LoanRequest request = new LoanRequest(
                "Motor",
                "Baru",
                Year.now().getValue(),
                new BigDecimal("30000000"),
                7,
                new BigDecimal("12000000")
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> calculatorService.calculate(request));
        assertTrue(ex.getMessage().contains("Tenor Pinjaman harus antara 1 sampai 6 tahun"));
    }
}
