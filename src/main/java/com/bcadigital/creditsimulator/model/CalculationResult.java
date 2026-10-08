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
        StringBuilder sb = new StringBuilder();
        sb.append(LINE).append('\n');
        sb.append(center("HASIL PERHITUNGAN")).append('\n');
        sb.append(LINE).append('\n');
        sb.append(String.format("Jenis kendaraan   : %s\n", request.getVehicleType().getDisplayName()));
        sb.append(String.format("Kondisi kendaraan : %s\n", request.getVehicleCondition().getDisplayName()));
        sb.append(String.format("Tahun kendaraan   : %d\n", request.getVehicleYear()));
        sb.append(String.format("Jumlah pinjaman   : %s\n", rupiah(request.getTotalLoanAmount())));
        sb.append(String.format("Tenor             : %d tahun\n", request.getLoanTenure()));
        sb.append(String.format("Uang muka (DP)    : %s\n", rupiah(request.getDownPayment())));
        sb.append(String.format("Pokok pinjaman    : %s\n", rupiah(principalAmount)));
        sb.append(LINE).append('\n');

        for (YearlyInstallment installment : yearlyInstallments) {
            String rateText = String.format(Locale.GERMAN, "%.1f%%", installment.interestRatePercent())
                    .replace(",0%", "%");
            sb.append(String.format("tahun %d : %s/bln , Suku Bunga : %s\n",
                    installment.year(),
                    rupiah(installment.monthlyInstallment()),
                    rateText));
        }
        sb.append(LINE).append('\n');
        return sb.toString();
    }

    private static String center(String text) {
        int pad = Math.max(0, (LINE.length() - text.length()) / 2);
        return " ".repeat(pad) + text;
    }

    private String rupiah(BigDecimal amount) {
        DecimalFormat format = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.US));
        return "Rp. " + format.format(amount);
    }
}
