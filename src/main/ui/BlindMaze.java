/*

 */

package ui;

import model.Maze;

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
    private void openPlayMode(Maze m) {

    }

    //EFFECTS: opens a maze in EditMode;
    private void openEditMode(Maze m) {

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
}
