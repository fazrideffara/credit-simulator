package com.bcadigital.creditsimulator.model;

public enum VehicleCondition {
    BARU("Baru", 0.35),
    BEKAS("Bekas", 0.25);

    private final String displayName;
    private final double minDownPaymentPercentage;

    VehicleCondition(String displayName, double minDownPaymentPercentage) {
        this.displayName = displayName;
        this.minDownPaymentPercentage = minDownPaymentPercentage;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getMinDownPaymentPercentage() {
        return minDownPaymentPercentage;
    }

    public static VehicleCondition fromString(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Kondisi Kendaraan tidak boleh kosong.");
        }
        String cleanText = text.trim().toUpperCase();
        if (cleanText.equals("BARU") || cleanText.equals("NEW")) {
            return BARU;
        }
        if (cleanText.equals("BEKAS") || cleanText.equals("LAMA") || cleanText.equals("USED")) {
            return BEKAS;
        }
        throw new IllegalArgumentException("Kondisi Kendaraan tidak valid. Pilih Baru atau Bekas.");
    }
}
