package com.bcadigital.creditsimulator.cli;

import com.bcadigital.creditsimulator.model.CalculationResult;
import com.bcadigital.creditsimulator.model.LoanRequest;
import com.bcadigital.creditsimulator.model.Sheet;
import com.bcadigital.creditsimulator.model.VehicleType;
import com.bcadigital.creditsimulator.service.LoanCalculatorService;
import com.bcadigital.creditsimulator.service.MockyApiClient;
import com.bcadigital.creditsimulator.service.SheetManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;

public class CommandHandler {

    private static final String LINE = "==========================================================";

    private final LoanCalculatorService calculator;
    private final MockyApiClient api;
    private final SheetManager sheets;
    private CalculationResult lastResult;

    public CommandHandler() {
        this.calculator = new LoanCalculatorService();
        this.api = new MockyApiClient();
        this.sheets = new SheetManager();
    }

    public void runInteractive() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            showMenu();
            System.out.print("Pilih menu > ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String line = scanner.nextLine().trim();
            if (line.isBlank()) {
                continue;
            }
            if (isVehicleType(line)) {
                handleInteractiveForm(scanner, line);
                continue;
            }
            String pilihan = nomorMenu(line);
            if (pilihan.equals("show")) {
                continue;
            }
            running = jalankan(pilihan, scanner, true);
        }
        System.out.println();
        banner("Selesai");
    }

    public void processFileInput(String filePath) {
        System.out.println();
        banner("INPUT DARI FILE");
        System.out.println("  " + filePath);
        System.out.println(LINE);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Scanner noInput = new Scanner("");
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isBlank() || line.startsWith("#")) {
                    continue;
                }
                System.out.println();
                System.out.println("> " + line);
                if (!jalankan(line, noInput, false)) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("[ERROR] Gagal membaca file input: " + e.getMessage());
        }
    }

    private boolean jalankan(String line, Scanner scanner, boolean tanya) {
        try {
            return processCommand(line, scanner, tanya);
        } catch (Exception e) {
            System.err.println("[ERROR] " + pesan(e));
            return true;
        }
    }

    public boolean processCommand(String commandLine, Scanner scanner) {
        return processCommand(commandLine, scanner, false);
    }

    private boolean processCommand(String commandLine, Scanner scanner, boolean tanya) {
        String cmd = commandLine.trim();
        String lower = cmd.toLowerCase();

        if (lower.equals("show") || lower.equals("help") || lower.equals("menu")) {
            showMenu();
            return true;
        }

        if (lower.equals("load")) {
            handleLoadCommand();
            return true;
        }

        if (lower.equals("save") || lower.startsWith("save ")) {
            String name = stripSheetWord(tail(cmd, "save"));
            if (name.isBlank() && tanya) {
                System.out.print("Nama sheet (Enter = otomatis): ");
                name = scanner.nextLine().trim();
            }
            handleSaveSheet(name);
            return true;
        }

        if (lower.equals("sheet") || lower.equals("switch") || lower.startsWith("switch ")) {
            String key = lower.equals("sheet") ? "" : stripSheetWord(tail(cmd, "switch"));
            handleSwitchSheet(key, scanner, tanya);
            return true;
        }

        if (lower.equals("calculate") || lower.equals("simulasi") || lower.equals("1")) {
            handleInteractiveForm(scanner, null);
            return true;
        }

        if (lower.equals("exit") || lower.equals("quit") || lower.equals("q")) {
            return false;
        }

        if (cmd.contains(",")) {
            handleDirectLineInput(cmd);
            return true;
        }

        System.out.println("[WARNING] Perintah tidak dikenali: '" + cmd + "'. Ketik show untuk bantuan.");
        return true;
    }

    private void showMenu() {
        System.out.println();
        banner("Credit Simulator BCA Digital");
        item("1. show", "daftar perintah");
        item("2. calculate", "isi data kredit");
        item("3. load", "ambil data, lalu hitung");
        item("4. save sheet", "simpan hasil terakhir");
        item("5. switch sheet", "pindah ke sheet");
        item("6. exit", "keluar");
        System.out.println(LINE);
        System.out.println("  Atau tempel satu baris:");
        System.out.println("  Mobil, Baru, 2025, 1000000000, 6, 500000000");
        System.out.println(LINE);
        System.out.println();
    }

    private void handleLoadCommand() {
        System.out.println("Mengambil data dari web service...");
        LoanRequest request;
        try {
            request = api.fetchExistingCalculation();
            System.out.println("Data berhasil diambil.");
        } catch (Exception e) {
            System.out.println("Web service tidak terjangkau. Memakai data contoh.");
            request = api.sampleRequest();
        }

        try {
            CalculationResult result = calculator.calculate(request);
            this.lastResult = result;
            System.out.println();
            System.out.println(result.formatSummary());
        } catch (Exception e) {
            System.err.println("[ERROR] Gagal memproses load: " + e.getMessage());
        }
    }

    private void handleSaveSheet(String customName) {
        if (lastResult == null) {
            System.out.println("[WARNING] Belum ada hasil perhitungan. Jalankan calculate atau load dulu.");
            return;
        }
        Sheet sheet = sheets.saveSheet(customName, lastResult);
        System.out.println("[SUCCESS] Disimpan ke " + sheet.getSheetId() + " (" + sheet.getName() + ").");
    }

    private void handleSwitchSheet(String searchKey, Scanner scanner, boolean tanya) {
        if (sheets.isEmpty()) {
            System.out.println("Belum ada sheet yang tersimpan.");
            return;
        }

        if (searchKey.isBlank()) {
            System.out.println();
            banner("SHEET TERSIMPAN");
            Sheet active = sheets.getActiveSheet();
            for (Sheet s : sheets.getAllSheets()) {
                String mark = (active != null && active.getSheetId().equalsIgnoreCase(s.getSheetId())) ? " [aktif]" : "";
                System.out.println("  " + s.getSheetId() + " | " + s.getName() + " | " + s.getFormattedCreatedAt() + mark);
            }
            System.out.println(LINE);

            if (!tanya) {
                return;
            }
            System.out.print("Id atau nama sheet (Enter untuk batal): ");
            String key = scanner.nextLine().trim();
            if (key.isBlank()) {
                return;
            }
            searchKey = key;
        }

        if (!sheets.switchSheet(searchKey)) {
            System.out.println("[ERROR] Sheet tidak ditemukan: '" + searchKey + "'.");
            return;
        }

        Sheet active = sheets.getActiveSheet();
        this.lastResult = active.getCalculationResult();
        System.out.println("[SUCCESS] Pindah ke " + active.getSheetId() + " (" + active.getName() + ").");
        System.out.println();
        System.out.println(active.getCalculationResult().formatSummary());
    }

    private void handleInteractiveForm(Scanner scanner, String jenisAwal) {
        try {
            System.out.println();
            banner("INPUT KREDIT");

            String rawType = jenisAwal;
            if (rawType == null) {
                System.out.print("Input Jenis Kendaraan (Motor|Mobil): ");
                rawType = scanner.nextLine();
            }

            System.out.print("Input Kondisi Kendaraan (Bekas|Baru): ");
            String rawCondition = scanner.nextLine();

            System.out.print("Input Tahun Kendaraan: ");
            int year = Angka.tahun(scanner.nextLine());

            System.out.print("Input Jumlah Pinjaman Total: ");
            BigDecimal totalLoan = Angka.uang(scanner.nextLine());

            System.out.print("Input Tenor Pinjaman (1-6): ");
            int tenure = Angka.tenor(scanner.nextLine());

            System.out.print("Input Jumlah DP: ");
            BigDecimal dp = Angka.uang(scanner.nextLine());

            printResult(new LoanRequest(rawType, rawCondition, year, totalLoan, tenure, dp));
        } catch (Exception e) {
            System.err.println("[ERROR] " + pesan(e));
        }
    }

    private void handleDirectLineInput(String line) {
        String[] parts = line.split(",");
        if (parts.length < 6) {
            System.err.println("[ERROR] Butuh 6 nilai, dipisah koma.");
            return;
        }

        try {
            LoanRequest request = new LoanRequest(
                    parts[0].trim(),
                    parts[1].trim(),
                    Angka.tahun(parts[2]),
                    Angka.uang(parts[3]),
                    Angka.tenor(parts[4]),
                    Angka.uang(parts[5]));
            printResult(request);
        } catch (Exception e) {
            System.err.println("[ERROR] " + pesan(e));
        }
    }

    private void printResult(LoanRequest request) {
        CalculationResult result = calculator.calculate(request);
        this.lastResult = result;
        System.out.println();
        System.out.println(result.formatSummary());
    }

    private static String pesan(Exception e) {
        String message = e.getMessage();
        if (message == null || message.isBlank() || e instanceof NumberFormatException) {
            return "Input tidak sesuai. Ketik show untuk bantuan.";
        }
        return message;
    }

    private static String tail(String cmd, String prefix) {
        if (cmd.length() <= prefix.length()) {
            return "";
        }
        return cmd.substring(prefix.length()).trim();
    }

    private static String stripSheetWord(String rest) {
        if (rest.equalsIgnoreCase("sheet")) {
            return "";
        }
        if (rest.toLowerCase().startsWith("sheet ")) {
            return rest.substring(6).trim();
        }
        return rest;
    }

    private static String nomorMenu(String line) {
        return switch (line) {
            case "1" -> "show";
            case "2" -> "calculate";
            case "3" -> "load";
            case "4" -> "save sheet";
            case "5" -> "switch sheet";
            case "6" -> "exit";
            default -> line;
        };
    }

    private static void banner(String text) {
        int pad = Math.max(0, (LINE.length() - text.length()) / 2);
        System.out.println(LINE);
        System.out.println(" ".repeat(pad) + text);
        System.out.println(LINE);
    }

    private static void item(String cmd, String ket) {
        System.out.println("  " + String.format("%-24s", cmd) + ket);
    }

    private static boolean isVehicleType(String text) {
        try {
            VehicleType.fromString(text);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
