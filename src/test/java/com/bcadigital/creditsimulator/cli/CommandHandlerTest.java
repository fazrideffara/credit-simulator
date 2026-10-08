package com.bcadigital.creditsimulator.cli;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CommandHandlerTest {

    @Test
    void inputAnehTidakBikinCrash() {
        PrintStream out = System.out;
        PrintStream err = System.err;
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
        System.setErr(new PrintStream(new ByteArrayOutputStream()));
        try {
            CommandHandler cli = new CommandHandler();
            Scanner kosong = new Scanner("");
            assertDoesNotThrow(() -> cli.processCommand("asdf", kosong));
            assertDoesNotThrow(() -> cli.processCommand("Mobil, Baru, xx, yy, zz, ww", kosong));
            assertDoesNotThrow(() -> cli.processCommand("Mobil, Baru, 2020, -5, 1, 1", kosong));
            assertDoesNotThrow(() -> cli.processCommand("Mobil, Baru, 999, 100, 1, 10", kosong));
            assertDoesNotThrow(() -> cli.processCommand("truk, bekas, 2020, 100000, 2, 50000", kosong));
            assertDoesNotThrow(() -> cli.processCommand(",,,,,", kosong));
            assertDoesNotThrow(() -> cli.processCommand("calculate", kosong));
            assertDoesNotThrow(() -> cli.processCommand("save sheet X", kosong));
            assertDoesNotThrow(() -> cli.processCommand("switch sheet tidak-ada", kosong));
            assertFalse(cli.processCommand("exit", kosong));
        } finally {
            System.setOut(out);
            System.setErr(err);
        }
    }
}
