package com.bcadigital.creditsimulator.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehicleConditionTest {

    @Test
    void terimaBaruBekasDanAlias() {
        assertEquals(VehicleCondition.BARU, VehicleCondition.fromString("Baru"));
        assertEquals(VehicleCondition.BARU, VehicleCondition.fromString("new"));
        assertEquals(VehicleCondition.BEKAS, VehicleCondition.fromString("bekas"));
        assertEquals(VehicleCondition.BEKAS, VehicleCondition.fromString("lama"));
    }

    @Test
    void dpMinimumSesuaiKondisi() {
        assertEquals(0.35, VehicleCondition.BARU.getMinDownPaymentPercentage());
        assertEquals(0.25, VehicleCondition.BEKAS.getMinDownPaymentPercentage());
    }
}
