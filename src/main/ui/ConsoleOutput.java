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
                drawOpenScreen();
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
        System.out.println("\nSelect from:");
        System.out.println("\to -> open maze");
        System.out.println("\tc -> create new maze");
        System.out.println("\tt -> toggle mode");
        System.out.println("\tq -> quit");
    }

    private void drawOpenScreen() {
        System.out.println("Enter the maze you want to open");
    }
    
    private void drawToggleScreen() {
        System.out.println("Currently  in " + game.getMode());
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
