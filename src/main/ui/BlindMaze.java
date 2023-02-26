/*

 */

package ui;

import model.Maze;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class BlindMaze {

    private boolean playMode;
    private Scanner input = new Scanner(System.in);
    private ArrayList<Maze> mazeList;
    private boolean flag = false;

    //EFFECTS: initializes BlindMaze and runs the Menu till the user quits
    public BlindMaze() {
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
    @SuppressWarnings("methodlength")
    private void runMenu() {
        String choice = menuSelection();
        switch (choice) {
            case "o":
            case "O":
                if (mazeList.isEmpty()) {
                    System.out.println("Create a maze first");
                } else {
                    selectMaze();
                }
                break;
            case "c":
            case "C":
                createMaze();
                break;
            case "t":
            case "T":
                toggleMode();
                break;
            case "q":
            case "Q":
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

        String choice = input.next();

        return choice;
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
    //EFFECTS:  connects the input feed with the maze corresponding to the mode
    private void openMaze(Maze m, boolean playMode) {
        System.out.println("Remember the exit is always at the bottom right");

        if (playMode) {
            openPlayMode(m);
        } else {
            openEditMode(m);
        }
    }

    //EFFECTS: opens a maze in PlayMode;
    //@SuppressWarnings("methodlength")
    private void openPlayMode(Maze m) {
        boolean flag = true;
        Maze maze = new Maze(m);

        int gridSize = 3; //gridToBeDisplayed size
        int[] position = maze.getPlayerPosition();
        String[][] gridToBeDisplayed = new String[3][3];

        while (flag) {
            int[] playerPos =  maze.getPlayerPosition();

            for (int row = 0; row < gridSize; row++) {
                for (int col = 0; col < gridSize; col++) {
                    try {
                        gridToBeDisplayed[row][col] = maze.getStatus(row + playerPos[0] - 1,col + playerPos[1] - 1);
                    } catch (IndexOutOfBoundsException e) {
                        gridToBeDisplayed[row][col] = "~";
                    }
                }
            }
            displayGrid(gridToBeDisplayed);
            String inp = input.next();
            maze.movePlayer(inp);

            if (inp.equals("q")) {
                flag = false;
            }
        }
    }

    //EFFECTS: opens a maze in EditMode;
    @SuppressWarnings("methodlength")
    private void openEditMode(Maze maze) {
        boolean flag = true;
        int gridSize = maze.getGridSize();
        String[][] gridToBeDisplayed = new String[gridSize][gridSize];

        Set<String> validInputs = Set.of("r", "l", "u", "d", "o", "p", "m", "q");
        int[] cursor = new int[]{0, 0};

        while (flag) {
            for (int row = 0; row < gridSize; row++) {
                for (int column = 0; column < gridSize; column++) {
                    gridToBeDisplayed[row][column] = maze.getStatus(row, column);
                }
            }

            displayGrid(gridToBeDisplayed);
            String inp = input.next();

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
                case "q":

                    flag = false;
                    break;
                default:
                    maze.placeEntity(cursor[0], cursor[1], inp);
            }
        }
    }

    //EFFECTS: return a row for the grid
//    private String[][] getGridToBeDisplayed() {
//        for (int i = 0; i < gridSize; i++) {
//            for (int j = 0; j < gridSize; j++) {
//                gridToBeDisplayed[j][i] = maze.getStatus(i, j);
//            }
//        }
//    }

    //EFFECTS: displays the given grid on the display
    private void displayGrid(String[][] gridToBeDisplayed) {
        for (String[] row : gridToBeDisplayed) {
            for (String column : row) {
                System.out.print(column + " ");
            }
            System.out.println();
        }
    }
}