package com.bcadigital.creditsimulator.service;

import com.bcadigital.creditsimulator.model.VehicleType;

public class InterestRateStrategyServiceImpl implements InterestRateStrategyService {

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

        int yearBlock = (year - 1) / 2;
        return baseRatePercent + 0.1 + (yearBlock * 0.5);
    }
}
