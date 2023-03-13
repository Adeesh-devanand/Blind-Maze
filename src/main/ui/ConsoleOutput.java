package ui;

public class ConsoleOutput {
    private Page page;
    private final Game game;

    public enum Page {
        CREATE,
        GAME,
        MAIN,
        OPEN,
        TOGGLE

    }

    public ConsoleOutput(Game game) {
        page = Page.MAIN;
        this.game = game;
    }

    public void drawApplication() {
        if (!game.isRunning() && page == Page.GAME) {
            page = Page.MAIN;
        }
        switch (page) {
            case MAIN:
                drawMainMenu();
                break;
            case CREATE:
                drawCreateScreen();
                break;
            case TOGGLE:
                drawToggleScreen();
                break;
            case OPEN:
                drawOpenScreen();
                break;
            case GAME:
                drawGame();
        }
    }

    private void drawGame() {
        String[][] gridToBeDisplayed =  game.getGrid();
        for (String[] row : gridToBeDisplayed) {
            for (String column : row) {
                System.out.print(column + " ");
            }
            System.out.println();
        }
    }

    private void drawCreateScreen() {
        System.out.println("Enter a name for the maze");
    }

    private void drawMainMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\to -> open maze");
        System.out.println("\tc -> create new maze");
        System.out.println("\tt -> toggle mode");
        System.out.println("\tl -> load previous game");
        System.out.println("\te -> exit");
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
