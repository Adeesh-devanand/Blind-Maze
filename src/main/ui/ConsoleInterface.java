/*
 * Controls all the interactions between the model and the User
 * Responsible for the Console based GUI
 * Displays a menu from where user prompts funnel the user through the application
 * Acts as both a controller and a View class */

//TODO: for GUI approach, remove all View functionalities and treat it as a controller

package ui;

import java.util.Scanner;

public class ConsoleInterface {
    private Scanner scn;
    private Controller controller;

    //EFFECTS: initializes the ConsoleInterface and runs the ConsoleOutput till the user quits
    public ConsoleInterface() {
        initialize();
        run();
    }

    private void initialize() {
        scn = new Scanner(System.in);
        scn.useDelimiter("\n");
        controller = new Controller();
    }

    private void run() {
        while (controller.isGameRunning()) {
            controller.updateScreen();
            String inp = readInput();
            controller.updateApplication(inp);
        }
    }

    private String readInput() {
        return scn.next();
    }

}