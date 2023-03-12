package ui;

import model.Game;
import model.exceptions.EmptyListException;
import model.exceptions.MazeAlreadyExistsException;
import model.exceptions.MazeDoesNotExistExcption;
import org.json.JSONObject;

import java.util.Scanner;

public class Controller {
    private Game game;
    private ConsoleOutput consoleOutput;
    private Scanner scn;
    private boolean inMenu;

    public Controller() {
        inMenu = true;
        game = new Game();
        consoleOutput = new ConsoleOutput(game);
        scn = new Scanner(System.in);
    }

    public boolean isGameRunning() {
        return game.getGameRunner();
    }

    public void updateApplication(String inp) {
        if (inp.equals("e")) {
            exitApplication();
        } else if (inMenu) {
            switch (inp) {
                case "c":
                    consoleOutput.setPage(ConsoleOutput.Page.CREATE);
                    createMaze();
                    break;
                case "o":
                    consoleOutput.setPage(ConsoleOutput.Page.OPEN);
                    openMaze();
                    inMenu = false;
                    break;
                case "t":
                    consoleOutput.setPage(ConsoleOutput.Page.TOGGLE);
                    toggleMode();
                    break;
            }
        } else {
            updateGame(inp);
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
        try {
            updateScreen();
            String name = scn.next();
            game.selectMaze(name);
            consoleOutput.setPage(ConsoleOutput.Page.GAME);
        } catch (EmptyListException e) {
            System.out.println("Create a maze first");
            updateApplication("c");
        } catch (MazeDoesNotExistExcption e) {
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
        JSONObject json = new JSONObject();
    }
}
