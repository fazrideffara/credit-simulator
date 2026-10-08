package com.bcadigital.creditsimulator.service;

import com.bcadigital.creditsimulator.model.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultInterestRateStrategyTest {

    private DefaultInterestRateStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new DefaultInterestRateStrategy();
    }

    @Test
    void testMobilInterestRateYoY() {
        assertEquals(8.0, strategy.calculateInterestRate(VehicleType.MOBIL, 1), 0.001);
        assertEquals(8.1, strategy.calculateInterestRate(VehicleType.MOBIL, 2), 0.001);
        assertEquals(8.6, strategy.calculateInterestRate(VehicleType.MOBIL, 3), 0.001);
        assertEquals(8.6, strategy.calculateInterestRate(VehicleType.MOBIL, 4), 0.001);
        assertEquals(9.1, strategy.calculateInterestRate(VehicleType.MOBIL, 5), 0.001);
        assertEquals(9.1, strategy.calculateInterestRate(VehicleType.MOBIL, 6), 0.001);
    }

    @Test
    void testMotorInterestRateYoY() {
        assertEquals(9.0, strategy.calculateInterestRate(VehicleType.MOTOR, 1), 0.001);
        assertEquals(9.1, strategy.calculateInterestRate(VehicleType.MOTOR, 2), 0.001);
        assertEquals(9.6, strategy.calculateInterestRate(VehicleType.MOTOR, 3), 0.001);
        assertEquals(9.6, strategy.calculateInterestRate(VehicleType.MOTOR, 4), 0.001);
        assertEquals(10.1, strategy.calculateInterestRate(VehicleType.MOTOR, 5), 0.001);
        assertEquals(10.1, strategy.calculateInterestRate(VehicleType.MOTOR, 6), 0.001);
    }

    @Test
    void testInvalidYearThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> strategy.calculateInterestRate(VehicleType.MOBIL, 0));
    }
}
