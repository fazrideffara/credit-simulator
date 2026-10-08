package com.bcadigital.creditsimulator.util;

import com.bcadigital.creditsimulator.model.LoanRequest;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonUtils {

    public static LoanRequest parseLoanRequest(String json) {
        if (json == null || json.isBlank()) {
            throw new IllegalArgumentException("JSON string tidak boleh kosong.");
        }

        String vehicleType = extractStringValue(json, "vehicleType");
        String vehicleCondition = extractStringValue(json, "vehicleCondition");
        int vehicleYear = extractIntValue(json, "vehicleYear");
        BigDecimal totalLoanAmount = extractBigDecimalValue(json, "totalLoanAmount");
        int loanTenure = extractIntValue(json, "loanTenure");
        BigDecimal downPayment = extractBigDecimalValue(json, "downPayment");

        return new LoanRequest(vehicleType, vehicleCondition, vehicleYear, totalLoanAmount, loanTenure, downPayment);
    }

    private static String extractStringValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new IllegalArgumentException("Key JSON tidak ditemukan: " + key);
    }

    private static int extractIntValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*([0-9]+)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        throw new IllegalArgumentException("Key JSON tidak ditemukan: " + key);
    }

    private static BigDecimal extractBigDecimalValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*([0-9]+(?:\\.[0-9]+)?)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return new BigDecimal(matcher.group(1));
        }
        throw new IllegalArgumentException("Key JSON tidak ditemukan: " + key);
    }
}
