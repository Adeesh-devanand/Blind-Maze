/*
 * Controls all the interactions between the model and the User
 * Responsible for the Console based GUI
 * Displays a menu from where user prompts funnel the user through the application
 * Acts as both a controller and a View class */

//TODO: for GUI approach, remove all View functionalities and treat it as a controller

package ui;

import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;

public class ConsoleInterface {
    private Screen screen;
    private Controller controller;
    private WindowBasedTextGUI textGUI;

    //EFFECTS: initializes the ConsoleInterface and runs the Menu till the user quits
    public ConsoleInterface() throws IOException {
        initialize();
        run();
    }

    private void initialize() throws IOException {
        screen = new DefaultTerminalFactory().createScreen();
        screen.startScreen();
        controller = new Controller(screen);
    }

    private void run() throws IOException {
        while (controller.isGameRunning()) {
            KeyStroke inp = screen.readInput();
            controller.updateGame(inp);
            controller.updateScreen();
        }
    }

}