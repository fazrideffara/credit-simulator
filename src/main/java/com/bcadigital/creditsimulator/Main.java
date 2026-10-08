package com.bcadigital.creditsimulator;

import com.bcadigital.creditsimulator.cli.CommandHandler;

public class Main {

    public static void main(String[] args) {
        CommandHandler commandHandler = new CommandHandler();

        if (args.length > 0 && args[0] != null && !args[0].isBlank()) {
            commandHandler.processFileInput(args[0]);
        } else {
            commandHandler.runInteractive();
        }
    }
}
