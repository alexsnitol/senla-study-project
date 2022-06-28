package ru.senla.autoservice.view.cli;

import ru.senla.autoservice.view.cli.builder.impl.BuilderImpl;

import java.util.Scanner;

import static java.lang.System.out;

public class MenuController {

    private BuilderImpl builder;
    private Navigator navigator;
    public static final String CONSOLE_POINTER = "> ";

    public void run() throws Exception {
        builder = new BuilderImpl(null);
        navigator = new Navigator(builder.buildMenu());

        Scanner scanner = new Scanner(System.in);
        int userInput = 0;

        while (userInput != -1) {
            navigator.printMenu();

            out.println("");
            out.print(CONSOLE_POINTER);
            userInput = scanner.nextInt();

            navigator.navigate(userInput);
        }
    }

}
