package com.bcadigital.creditsimulator.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class CalculationResult {

    private static final String LINE = "==========================================================";

    private final LoanRequest request;
    private final BigDecimal principalAmount;
    private final List<YearlyInstallment> yearlyInstallments;

    public CalculationResult(LoanRequest request, BigDecimal principalAmount, List<YearlyInstallment> yearlyInstallments) {
        this.request = request;
        this.principalAmount = principalAmount;
        this.yearlyInstallments = yearlyInstallments;
    }

    public LoanRequest getRequest() {
        return request;
    }

    public BigDecimal getPrincipalAmount() {
        return principalAmount;
    }

    public List<YearlyInstallment> getYearlyInstallments() {
        return yearlyInstallments;
    }

    public String formatSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(LINE).append('\n');
        summary.append(center("HASIL PERHITUNGAN")).append('\n');
        summary.append(LINE).append('\n');
        summary.append(String.format("Jenis kendaraan   : %s\n", request.getVehicleType().getDisplayName()));
        summary.append(String.format("Kondisi kendaraan : %s\n", request.getVehicleCondition().getDisplayName()));
        summary.append(String.format("Tahun kendaraan   : %d\n", request.getVehicleYear()));
        summary.append(String.format("Jumlah pinjaman   : %s\n", rupiah(request.getTotalLoanAmount())));
        summary.append(String.format("Tenor             : %d tahun\n", request.getLoanTenure()));
        summary.append(String.format("Uang muka (DP)    : %s\n", rupiah(request.getDownPayment())));
        summary.append(String.format("Pokok pinjaman    : %s\n", rupiah(principalAmount)));
        summary.append(LINE).append('\n');

        for (YearlyInstallment installment : yearlyInstallments) {
            String rateText = String.format(Locale.GERMAN, "%.1f%%", installment.interestRatePercent())
                    .replace(",0%", "%");
            summary.append(String.format("tahun %d : %s/bln , Suku Bunga : %s\n",
                    installment.year(),
                    rupiah(installment.monthlyInstallment()),
                    rateText));
        }
        summary.append(LINE).append('\n');
        return summary.toString();
    }

    private static String center(String text) {
        int padding = Math.max(0, (LINE.length() - text.length()) / 2);
        return " ".repeat(padding) + text;
    }

    private String rupiah(BigDecimal amount) {
        DecimalFormat format = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.US));
        return "Rp. " + format.format(amount);
    }
}
