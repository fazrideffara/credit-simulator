package com.bcadigital.creditsimulator.service;

import com.bcadigital.creditsimulator.model.VehicleType;

public class DefaultInterestRateStrategy implements InterestRateStrategy {

    @Override
    public double calculateInterestRate(VehicleType vehicleType, int year) {
        if (year < 1) {
            throw new IllegalArgumentException("Tahun harus >= 1");
        }

        double baseRatePercent = vehicleType.getBaseInterestRate() * 100.0;

        if (year == 1) {
            return baseRatePercent;
        }

        if (year == 2) {
            return baseRatePercent + 0.1;
        }

        int step = (year - 1) / 2;
        return baseRatePercent + 0.1 + (step * 0.5);
    }
}
