package model;

import model.exceptions.MazeAlreadyExistsException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Game {
    private boolean gameRunning;
    private int playerVisibilty;
    private final Map<String, Maze> mazeList;
    private Maze currMaze;
    private boolean editMode;
    private static final String[] legalStrings = new String[] {};
    private static final HashSet legalCharacters = new HashSet<>(Arrays.asList(legalStrings));

    public Game() {
        gameRunning = true;
        playerVisibilty = 1;
        mazeList = new HashMap();
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public void toggleMode() {
        editMode = !editMode;
    }

    public void updateMaze(String key) {
        editMode ? currMaze.moveCursor(key) : currMaze.movePlayer(dir);
    }

    private void createMaze(String name, int gridSize) throws MazeAlreadyExistsException {
        if (mazeList.get(name) != null) {
            throw new MazeAlreadyExistsException();
        } else {
            mazeList.put(name, new Maze(name, gridSize));
        }
    }

    private void selectMaze(String name) {
        currMaze = mazeList.get(name);
    }

    private void deleteMaze(String name) {
        mazeList.remove(name);
    }

}
