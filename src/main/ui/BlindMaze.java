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

    public BlindMaze() {
        input.useDelimiter("\n");
        mazeList = new ArrayList<>();
        playMode = true;
        runMenu();
    }

    private void runMenu() {
        String choice = menuSelection();
        switch (choice) {
            case "o":
                if (mazeList.isEmpty()) {
                    System.out.println("Create a maze first");
                    runMenu();
                } else {
                    Maze selectedMaze = selectMaze();
                    openMaze(selectedMaze, playMode);
                }
                break;
            case "c":
                createMaze();
                break;
            case "t":
                toggleMode();
                break;
            case "q":
                break;
            default:
                System.out.println("Invalid input try again");
                runMenu();
        }
    }


    //MODIFIES: this
    //EFFECTS: Adds an empty maze to list of available mazes and opens it
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
    //EFFECTS: Toggles state between Play and Toggle
    private void toggleMode() {
        playMode = !playMode;
        if (playMode) {
            System.out.println("Currently in PLAY mode");
        } else {
            System.out.println("Currently in EDIT mode");
        }
    }


    //REQUIRES: At least one Maze should be available
    //EFFECTS: Makes the user choose a maze form the available mazes
    private Maze selectMaze() {
        System.out.println("List of available mazes");
        int count = 1;
        for (Maze maze : mazeList) {
            System.out.println(count + ". " + maze.getName());
            count++;
        }

        System.out.println("Enter the maze number you want to open");
        Maze selectedMaze;

        while (true) {
            try {
                String inp = input.next();
                Integer.parseInt(inp);
                int mazeNo = Integer.parseInt(inp) - 1;
                selectedMaze = mazeList.get(mazeNo);
                break;

            } catch (NumberFormatException ex) {

                System.out.println("Value entered is not a integer, try again");

            } catch (IndexOutOfBoundsException ex) {

                System.out.println("There aren't that many mazes, try again");
            }
        }

        return selectedMaze;
    }

    //EFFECTS: opens a given maze
    private void openMaze(Maze maze, boolean playMode) {

    }


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
