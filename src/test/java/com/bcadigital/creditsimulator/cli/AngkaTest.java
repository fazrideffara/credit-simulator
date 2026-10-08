package com.bcadigital.creditsimulator.cli;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AngkaTest {

    @Test
    void uangBiasaDanPemisahRibuan() {
        assertEquals(0, new BigDecimal("1000000").compareTo(Angka.uang("1000000")));
        assertEquals(0, new BigDecimal("1000000").compareTo(Angka.uang("1.000.000")));
        assertEquals(0, new BigDecimal("1000000").compareTo(Angka.uang("1,000,000")));
        assertEquals(0, new BigDecimal("1000000.50").compareTo(Angka.uang("1.000.000,50")));
        assertEquals(0, new BigDecimal("500000000.00").compareTo(Angka.uang("500000000.00")));
        assertEquals(0, new BigDecimal("1000000").compareTo(Angka.uang("Rp. 1.000.000")));
    }

    @Test
    void uangTidakValid() {
        assertThrows(IllegalArgumentException.class, () -> Angka.uang(""));
        assertThrows(IllegalArgumentException.class, () -> Angka.uang("abc"));
        assertThrows(IllegalArgumentException.class, () -> Angka.uang("1e6"));
        assertThrows(IllegalArgumentException.class, () -> Angka.uang("--5"));
        assertEquals(0, new BigDecimal("-10").compareTo(Angka.uang("-10")));
    }

    @Test
    void tahunHarusEmpatDigit() {
        assertEquals(2025, Angka.tahun(" 2025 "));
        assertThrows(IllegalArgumentException.class, () -> Angka.tahun("999"));
        assertThrows(IllegalArgumentException.class, () -> Angka.tahun("10000"));
        assertThrows(IllegalArgumentException.class, () -> Angka.tahun("-2025"));
        assertThrows(IllegalArgumentException.class, () -> Angka.tahun("20a5"));
        assertThrows(IllegalArgumentException.class, () -> Angka.tahun(""));
    }

    @Test
    void tenorHanyaSatuSampaiEnam() {
        assertEquals(1, Angka.tenor("1"));
        assertEquals(6, Angka.tenor("06"));
        assertThrows(IllegalArgumentException.class, () -> Angka.tenor("0"));
        assertThrows(IllegalArgumentException.class, () -> Angka.tenor("7"));
        assertThrows(IllegalArgumentException.class, () -> Angka.tenor("-1"));
        assertThrows(IllegalArgumentException.class, () -> Angka.tenor("2.5"));
        assertThrows(IllegalArgumentException.class, () -> Angka.tenor(""));
    }
}
