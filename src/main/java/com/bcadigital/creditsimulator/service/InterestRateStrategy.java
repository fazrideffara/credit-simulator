package com.bcadigital.creditsimulator.service;

import com.bcadigital.creditsimulator.model.VehicleType;

public interface InterestRateStrategy {

    double calculateInterestRate(VehicleType vehicleType, int year);
}
