package ui;

import model.Game;

public class ConsoleOutput {
    private Page page;
    private Game game;

    public enum Page {
        MAIN,
        CREATE,
        OPEN,
        TOGGLE,
        GAME;
    }

    public ConsoleOutput(Game game) {
        page = Page.MAIN;
        this.game = game;
    }

    public void drawApplication() {
        switch (page) {
            case MAIN:
                drawMainMenu();
                break;
            case CREATE:
                drawCreateScreen();
                break;
            case TOGGLE:
                drawToggleScreen();
            case OPEN:
                drawOpenMaze();
                break;
            case GAME:
                drawGame();
        }
    }

    private void drawGame() {
    }

    private void drawCreateScreen() {
        System.out.println("Enter a name for the maze");
    }

    private void drawMainMenu() {
        System.out.println("Main Menu");
    }

    private void drawOpenMaze() {
        System.out.println("Enter the maze you want to open");
    }
    
    private void drawToggleScreen() {
        System.out.println("Currently  in " + game.getMode());
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
