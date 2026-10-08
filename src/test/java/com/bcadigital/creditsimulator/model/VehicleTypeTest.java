package com.bcadigital.creditsimulator.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehicleTypeTest {

    @Test
    void terimaMobilDanMotorTanpaPeduliHuruf() {
        assertEquals(VehicleType.MOBIL, VehicleType.fromString("mobil"));
        assertEquals(VehicleType.MOTOR, VehicleType.fromString("MOTOR"));
    }

    @Test
    void tolakJenisKosongAtauAneh() {
        assertThrows(IllegalArgumentException.class, () -> VehicleType.fromString(""));
        assertThrows(IllegalArgumentException.class, () -> VehicleType.fromString("truk"));
    }
}
