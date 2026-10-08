package com.bcadigital.creditsimulator.service;

import com.bcadigital.creditsimulator.model.VehicleType;

public interface InterestRateStrategyService {

    double calculateInterestRate(VehicleType vehicleType, int year);
}
