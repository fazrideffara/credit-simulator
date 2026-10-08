package com.bcadigital.creditsimulator.model;

public enum VehicleType {
    MOBIL("Mobil", 0.08),
    MOTOR("Motor", 0.09);

    private final String displayName;
    private final double baseInterestRate;

    VehicleType(String displayName, double baseInterestRate) {
        this.displayName = displayName;
        this.baseInterestRate = baseInterestRate;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getBaseInterestRate() {
        return baseInterestRate;
    }

    public static VehicleType fromString(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Jenis Kendaraan tidak boleh kosong.");
        }
        String cleanText = text.trim().toUpperCase();
        for (VehicleType type : values()) {
            if (type.name().equals(cleanText) || type.displayName.equalsIgnoreCase(cleanText)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Jenis Kendaraan tidak valid. Pilih Motor atau Mobil.");
    }
}
