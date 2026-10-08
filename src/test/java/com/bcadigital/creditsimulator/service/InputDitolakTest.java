package com.bcadigital.creditsimulator.service;

import com.bcadigital.creditsimulator.model.LoanRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class InputDitolakTest {

    private final LoanCalculatorService calculator = new LoanCalculatorService();

    @Test
    void batasYangMasihSah() {
        int tahun = Year.now().getValue();
        assertDoesNotThrow(() -> calculator.calculate(buatRequest("Mobil", "Baru", tahun - 1, "100000000", 3, "35000000")));
        assertDoesNotThrow(() -> calculator.calculate(buatRequest("Motor", "Bekas", 2020, "20000000", 2, "5000000")));
        assertDoesNotThrow(() -> calculator.calculate(buatRequest("Mobil", "Bekas", 2020, "1000000000", 1, "250000000")));
        assertDoesNotThrow(() -> calculator.calculate(buatRequest("Mobil", "Baru", tahun, "1000000000", 6, "350000000")));
    }

    @Test
    void inputTidakSahTidakLolos() {
        int tahun = Year.now().getValue();
        assertDitolak(buatRequest("truk", "Baru", tahun, "100000000", 1, "50000000"));
        assertDitolak(buatRequest("", "Baru", tahun, "100000000", 1, "50000000"));
        assertDitolak(buatRequest("Mobil", "rusak", tahun, "100000000", 1, "50000000"));
        assertDitolak(buatRequest("Mobil", "", tahun, "100000000", 1, "50000000"));
        assertDitolak(buatRequest("Mobil", "Baru", 999, "100000000", 1, "50000000"));
        assertDitolak(buatRequest("Mobil", "Baru", 10000, "100000000", 1, "50000000"));
        assertDitolak(buatRequest("Mobil", "Baru", tahun - 2, "100000000", 1, "50000000"));
        assertDitolak(buatRequest("Mobil", "Bekas", 2020, "0", 1, "1"));
        assertDitolak(buatRequest("Mobil", "Bekas", 2020, "-100", 1, "10"));
        assertDitolak(buatRequest("Mobil", "Bekas", 2020, "1000000001", 1, "500000000"));
        assertDitolak(buatRequest("Mobil", "Bekas", 2020, "100000000", 0, "25000000"));
        assertDitolak(buatRequest("Mobil", "Bekas", 2020, "100000000", -1, "25000000"));
        assertDitolak(buatRequest("Mobil", "Bekas", 2020, "100000000", 8, "25000000"));
        assertDitolak(buatRequest("Mobil", "Bekas", 2020, "100000000", 1, "0"));
        assertDitolak(buatRequest("Mobil", "Bekas", 2020, "100000000", 1, "-5"));
        assertDitolak(buatRequest("Mobil", "Bekas", 2020, "100000000", 1, "100000000"));
        assertDitolak(buatRequest("Mobil", "Baru", tahun, "100000000", 1, "34999999"));
        assertDitolak(buatRequest("Motor", "Lama", 2020, "20000000", 2, "4999999"));
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(null));
    }

    private void assertDitolak(LoanRequest request) {
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(request));
    }

    private LoanRequest buatRequest(String jenis, String kondisi, int tahun, String pinjaman, int tenor, String downPayment) {
        return new LoanRequest(jenis, kondisi, tahun, new BigDecimal(pinjaman), tenor, new BigDecimal(downPayment));
    }
}
