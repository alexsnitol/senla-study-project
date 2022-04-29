package autoservice.view.cli;

import autoservice.view.cli.builder.impl.BuilderImpl;

import java.io.IOException;
import java.util.Scanner;

public class MenuController {

    private BuilderImpl builder;
    private Navigator navigator;
    public static final String CONSOLE_POINTER = "> ";

    public void run() throws Exception {
        builder = new BuilderImpl(null);
        navigator = new Navigator(builder.buildMenu());

        Scanner scanner = new Scanner(System.in);
        Integer userInput = 0;

        while(userInput != -1) {
            navigator.printMenu();

            System.out.println("");
            System.out.print(CONSOLE_POINTER);
            userInput = scanner.nextInt();

            navigator.navigate(userInput);
        }
    }

}
