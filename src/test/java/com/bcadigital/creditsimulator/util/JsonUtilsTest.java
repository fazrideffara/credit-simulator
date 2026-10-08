package com.bcadigital.creditsimulator.util;

import com.bcadigital.creditsimulator.model.LoanRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class JsonUtilsTest {

    @Test
    void bacaPayloadLoad() {
        String json = """
                {
                  "vehicleType": "Mobil",
                  "vehicleCondition": "Baru",
                  "vehicleYear": 2025,
                  "totalLoanAmount": 1000000000,
                  "loanTenure": 6,
                  "downPayment": 500000000
                }
                """;

        LoanRequest request = JsonUtils.parseLoanRequest(json);
        assertEquals("Mobil", request.getRawVehicleType());
        assertEquals("Baru", request.getRawVehicleCondition());
        assertEquals(2025, request.getVehicleYear());
        assertEquals(6, request.getLoanTenure());
        assertEquals(0, new BigDecimal("1000000000").compareTo(request.getTotalLoanAmount()));
        assertEquals(0, new BigDecimal("500000000").compareTo(request.getDownPayment()));
    }

    @Test
    void jsonKosongDitolak() {
        assertThrows(IllegalArgumentException.class, () -> JsonUtils.parseLoanRequest("  "));
    }
}
