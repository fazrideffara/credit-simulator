package com.bcadigital.creditsimulator.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Sheet {

    private final String sheetId;
    private final String name;
    private final CalculationResult calculationResult;
    private final LocalDateTime createdAt;

    public Sheet(String sheetId, String name, CalculationResult calculationResult) {
        this.sheetId = sheetId;
        this.name = name;
        this.calculationResult = calculationResult;
        this.createdAt = LocalDateTime.now();
    }

    public String getSheetId() {
        return sheetId;
    }

    public String getName() {
        return name;
    }

    public CalculationResult getCalculationResult() {
        return calculationResult;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getFormattedCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return createdAt.format(formatter);
    }
}
