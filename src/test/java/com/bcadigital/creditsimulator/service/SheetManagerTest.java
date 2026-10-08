package com.bcadigital.creditsimulator.service;

import com.bcadigital.creditsimulator.model.CalculationResult;
import com.bcadigital.creditsimulator.model.LoanRequest;
import com.bcadigital.creditsimulator.model.Sheet;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SheetManagerTest {

    @Test
    void simpanLaluPindahLewatIdAtauNama() {
        SheetManager sheets = new SheetManager();
        CalculationResult result = hitung();

        Sheet saved = sheets.saveSheet("Mobil-1M", result);
        assertEquals("Sheet-1", saved.getSheetId());
        assertEquals("Mobil-1M", saved.getName());

        assertTrue(sheets.switchSheet("Sheet-1"));
        assertTrue(sheets.switchSheet("mobil-1m"));
        assertEquals("Sheet-1", sheets.getActiveSheet().getSheetId());
        assertFalse(sheets.switchSheet("nggak-ada"));
    }

    @Test
    void namaKosongPakaiId() {
        SheetManager sheets = new SheetManager();
        Sheet saved = sheets.saveSheet("  ", hitung());
        assertEquals("Sheet-1", saved.getName());
    }

    private CalculationResult hitung() {
        LoanRequest request = new LoanRequest(
                "Mobil", "Bekas", 2020,
                new BigDecimal("100000000"), 1, new BigDecimal("25000000"));
        return new LoanCalculatorService().calculate(request);
    }
}
