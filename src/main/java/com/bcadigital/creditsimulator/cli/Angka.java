package com.bcadigital.creditsimulator.cli;

import java.math.BigDecimal;

final class Angka {

    private Angka() {
    }

    static int tahun(String input) {
        if (input == null || !input.trim().matches("\\d{4}")) {
            throw new IllegalArgumentException("Tahun kendaraan harus 4 digit angka.");
        }
        return Integer.parseInt(input.trim());
    }

    static int tenor(String input) {
        if (input == null || !input.trim().matches("\\d{1,2}")) {
            throw new IllegalArgumentException("Tenor Pinjaman harus antara 1 sampai 6 tahun.");
        }
        int value = Integer.parseInt(input.trim());
        if (value < 1 || value > 6) {
            throw new IllegalArgumentException("Tenor Pinjaman harus antara 1 sampai 6 tahun.");
        }
        return value;
    }

    static BigDecimal uang(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Angka tidak boleh kosong.");
        }

        String text = input.trim().replace(" ", "");
        if (text.regionMatches(true, 0, "Rp.", 0, 3)) {
            text = text.substring(3).trim();
        } else if (text.regionMatches(true, 0, "Rp", 0, 2)) {
            text = text.substring(2).trim();
        }

        boolean negatif = false;
        if (text.startsWith("-")) {
            negatif = true;
            text = text.substring(1);
        } else if (text.startsWith("+")) {
            text = text.substring(1);
        }

        if (text.isEmpty() || !text.matches("[0-9.,]+")) {
            throw new IllegalArgumentException("Format angka tidak valid.");
        }

        int dots = count(text, '.');
        int commas = count(text, ',');
        if (dots > 0 && commas > 0) {
            if (text.lastIndexOf(',') > text.lastIndexOf('.')) {
                text = text.replace(".", "").replace(',', '.');
            } else {
                text = text.replace(",", "");
            }
        } else if (dots > 1) {
            text = text.replace(".", "");
        } else if (commas > 1) {
            text = text.replace(",", "");
        } else if (dots == 1) {
            text = satuPemisah(text, '.');
        } else if (commas == 1) {
            text = satuPemisah(text, ',');
        }

        if (!text.matches("\\d+(\\.\\d+)?")) {
            throw new IllegalArgumentException("Format angka tidak valid.");
        }

        BigDecimal value = new BigDecimal(text);
        if (negatif) {
            value = value.negate();
        }
        return value;
    }

    private static String satuPemisah(String text, char separator) {
        int index = text.lastIndexOf(separator);
        String whole = text.substring(0, index);
        String fraction = text.substring(index + 1);
        if (fraction.length() == 3) {
            return whole + fraction;
        }
        if (separator == ',') {
            return whole + "." + fraction;
        }
        return text;
    }

    private static int count(String text, char character) {
        int total = 0;
        for (int index = 0; index < text.length(); index++) {
            if (text.charAt(index) == character) {
                total++;
            }
        }
        return total;
    }
}
