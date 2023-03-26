package ui;

import model.Game;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.exceptions.MazeAlreadyExistsException;
import ui.exceptions.MazeDoesNotExistException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Controller {
    private static final String JSON_STORE = "./data/workspace.json";
    private Game game;
    private ConsoleOutput consoleOutput;
    private final Scanner scn;
    private final JsonReader reader;
    private final JsonWriter writer;

    public Controller() {
        game = new Game();
        consoleOutput = new ConsoleOutput(game);
        scn = new Scanner(System.in);
        reader = new JsonReader(JSON_STORE);
        writer = new JsonWriter(JSON_STORE);
    }

    public void updateApplication(String inp) {
        if (inp.equals("e")) {
            exitApplication();
        } else if (!game.isRunning()) {
            switch (inp) {
                case "c":
                    consoleOutput.setPage(ConsoleOutput.Page.CREATE);
                    createMaze();
                    break;
                case "o":
                    consoleOutput.setPage(ConsoleOutput.Page.OPEN);
                    openMaze();
                    break;
                case "t":
                    consoleOutput.setPage(ConsoleOutput.Page.TOGGLE);
                    toggleMode();
                    break;
                case "l":
                    loadSession();
                    break;
//                case "s":
//                    save();
            }
        } else {
            updateGame(inp);
        }
    }

    private void loadSession() {
        try {
            game = reader.loadGame();
            System.out.println("Loaded Previous Session from " + JSON_STORE);
            consoleOutput = new ConsoleOutput(game);
            if (game.isRunning()) {
                consoleOutput.setPage(ConsoleOutput.Page.GAME);
            }
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }

    }

    private void exitApplication() {
        System.out.println("Do you want to save the application");
        String inp = scn.next();
        if (inp.equalsIgnoreCase("yes")) {
            save();
            System.exit(1);
        } else {
            System.exit(0);
        }
    }

    private void updateGame(String inp) {
        game.updateMaze(inp);
    }

    private void toggleMode() {
        updateScreen();
        game.toggleMode();
        consoleOutput.setPage(ConsoleOutput.Page.MAIN);
    }

    private void openMaze() {
        if (game.isEmpty()) {
            System.out.println("Create a maze first");
            consoleOutput.setPage(ConsoleOutput.Page.MAIN);
            return;
        }

        try {
            updateScreen();
            String name = scn.next();
            game.selectMaze(name);
            consoleOutput.setPage(ConsoleOutput.Page.GAME);
            game.setRunning(true);
        } catch (MazeDoesNotExistException e) {
            System.out.println("Maze doesn't exist");
            openMaze();
        }
    }

    private void createMaze() {
        try {
            updateScreen();
            String name = scn.next();
            game.createMaze(name, 5);
            consoleOutput.setPage(ConsoleOutput.Page.MAIN);
        } catch (MazeAlreadyExistsException e) {
            System.out.println("Maze already exists");
            createMaze();
        }
    }

    public void updateScreen() {
        consoleOutput.drawApplication();
    }

    public void save() {
        try {
            writer.open();
            writer.writeGame(game);
            writer.close();
            System.out.println("Saved game to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }
}
