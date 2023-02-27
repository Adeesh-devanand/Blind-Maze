/*

 */

package ui;

import model.ElementAlreadyExistsException;
import model.Maze;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class BlindMaze {
    private static final int PLAYER_VISIBILITY = 1;
    private static Scanner input;
    private static boolean flag;

    private boolean playMode;
    private ArrayList<Maze> mazeList;

    //EFFECTS: initializes BlindMaze and runs the Menu till the user quits
    public BlindMaze() {
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        mazeList = new ArrayList<>();
        playMode = true;
        flag = false;

        while (!flag) {
            runMenu();
        }
    }

    //MODIFIES: this
    //EFFECTS: opens the menu selection and takes action according to user input
    private void runMenu() {
        String choice = menuSelection();
        switch (choice) {
            case "o":
                if (mazeList.isEmpty()) {
                    System.out.println("Create a maze first");
                } else {
                    selectMaze();
                }
                break;
            case "c":
                createMaze();
                break;
            case "t":
                toggleMode();
                break;
            case "q":
                flag = true;
                break;
            default:
                System.out.println("Invalid input try again");
        }
    }

    //EFFECTS: prompts user to choose from one of the given option
    private String menuSelection() {
        System.out.println("\nSelect from:");
        System.out.println("\to -> open maze");
        System.out.println("\tc -> create new maze");
        System.out.println("\tt -> toggle mode");
        System.out.println("\tq -> quit");

        return input.next();
    }

    //MODIFIES: this
    //EFFECTS: Adds an empty maze to list of available mazes and opens it in edit mode
    private void createMaze() {
        String name;

        while (true) {
            System.out.println("Enter a name for the maze");
            name = input.next();

            Set<String> mazeNames = mazeList.stream().map(Maze::getName).collect(Collectors.toSet());

            if (mazeNames.contains(name)) {
                System.out.println("Name already exists, enter a different name");
            } else {
                break;
            }
        }

        Maze maze = new Maze(name);
        mazeList.add(maze);
        openMaze(maze, false);
    }

    //MODIFIES: this
    //EFFECTS: Toggles the default mode between Play and Toggle
    private void toggleMode() {
        playMode = !playMode;
        if (playMode) {
            System.out.println("Currently in PLAY mode");
        } else {
            System.out.println("Currently in EDIT mode");
        }
    }

    //REQUIRES: At least one Maze should be available
    //EFFECTS: prompts the user to open a maze form the available mazes
    private void selectMaze() {
        System.out.println("List of available mazes");
        int count = 1;
        for (Maze maze : mazeList) {
            System.out.println(count + ". " + maze.getName());
            count++;
        }

        Maze selectedMaze;
        System.out.println("Enter the maze number you want to open");

        while (true) {
            try {
                String inp = input.next();
                Integer.parseInt(inp);
                int mazeNo = Integer.parseInt(inp) - 1;
                selectedMaze = mazeList.get(mazeNo);
                break;

            } catch (NumberFormatException ex) {
                System.out.println("Value entered is not a number, try again");

            } catch (IndexOutOfBoundsException ex) {
                System.out.println("Value entered isn't in range");
            }
        }

        openMaze(selectedMaze, playMode);
    }

    //REQUIRES: No other maze should be open
    //EFFECTS:  connects the input feed with the maze, takes mode into count to the mode
    private void openMaze(Maze m, boolean playMode) {
        System.out.println("Remember the exit is always at the bottom right");

        if (playMode) {
            openPlayMode(m);
        } else {
            openEditMode(m);
        }
    }

    //EFFECTS: opens a maze in PlayMode;
    private void openPlayMode(Maze m) {
        boolean flag = false;
        int gridSize = PLAYER_VISIBILITY + 2;
        int[] playerPos;
        String[][] gridToBeDisplayed;
        Maze maze = new Maze(m);
        boolean finished;
        while (!flag) {
            playerPos  =  maze.getPlayerPosition();
            gridToBeDisplayed = getGridTOBeDisplayed(maze, gridSize, playerPos[0] - 1, playerPos[1] - 1);
            displayGrid(gridToBeDisplayed);
            String inp = input.next();
            finished = maze.movePlayer(inp);
            if (finished) {
                System.out.println("YOU WON!!!!!");
                flag = true;
            }
            if (inp.equals("q")) {
                flag = true;
            }
        }
    }

    //EFFECTS: opens a maze in EditMode;
    private void openEditMode(Maze maze) {
        boolean flag = false;
        String[][] gridToBeDisplayed;
        int[] cursor = new int[]{0, 0};

        while (!flag) {
            gridToBeDisplayed = getGridToBeDisplayed(maze);
            displayGrid(gridToBeDisplayed);
            String inp = input.next();
            updateCursor(inp, cursor);
            try {
                maze.placeEntity(cursor[0], cursor[1], inp);
            } catch (ElementAlreadyExistsException e) {
                System.out.println("Can't place object there");
                System.out.println("Enter any key");
                input.next();
            }
            if (inp.equals("q")) {
                flag = true;
            }
        }
    }

    private String[][] getGridToBeDisplayed(Maze maze) {
        int gridSize = maze.getGridSize();
        return getGridTOBeDisplayed(maze, gridSize, 0, 0);
    }

    private String[][] getGridTOBeDisplayed(Maze maze, int gridSize, int relY, int relX) {
        String[][] gridToBeDisplayed = new String[gridSize][gridSize];
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                try {
                    gridToBeDisplayed[row][col] = maze.getStatus(row + relY,col + relX);
                } catch (IndexOutOfBoundsException e) {
                    gridToBeDisplayed[row][col] = "~";
                }
            }
        }

        return gridToBeDisplayed;
    }

    //EFFECTS: displays the given grid on the display
    private void displayGrid(String[][] gridToBeDisplayed) {
        for (String[] row : gridToBeDisplayed) {
            for (String column : row) {
                System.out.print(column + " ");
            }
            System.out.println();
        }
    }

    private void updateCursor(String inp, int[] cursor) {
        switch (inp) {
            case "r":
                cursor[1]++;
                break;
            case "l":
                cursor[1]--;
                break;
            case "d":
                cursor[0]++;
                break;
            case "u":
                cursor[0]--;
                break;
            default:
                break;
        }
    }
}