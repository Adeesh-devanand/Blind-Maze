package ui;

import model.Game;
import model.exceptions.MazeAlreadyExistsException;
import model.exceptions.MazeDoesNotExistExcption;

import java.util.Scanner;

public class Controller {
    private Game game;
    private ConsoleOutput consoleOutput;
    private boolean inMenu;
    private Scanner scn;

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
        if (inMenu) {
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
                case "q":
                    System.exit(0);
            }
        } else {
            updateGame();
        }
    }

    private void updateGame() {
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
}
