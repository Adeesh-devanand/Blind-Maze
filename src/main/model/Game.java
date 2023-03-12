package model;

import model.exceptions.MazeAlreadyExistsException;
import model.exceptions.MazeDoesNotExistExcption;

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

    public boolean getGameRunner() {
        return gameRunning;
    }

    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    public void toggleMode() {
        editMode = !editMode;
    }

    public String getMode(){
        return editMode ? "EditMode" : "PlayMode";
    }

    public void updateMaze(String key) {
    }

    public void createMaze(String name, int gridSize) throws MazeAlreadyExistsException {
        if (mazeList.get(name) != null) {
            throw new MazeAlreadyExistsException();
        } else {
            mazeList.put(name, new Maze(name, gridSize));
        }
    }

    public void selectMaze(String name) throws MazeDoesNotExistExcption {
        Maze tempMaze = mazeList.get(name);
        if (tempMaze == null) {
            throw new MazeDoesNotExistExcption();
        }
    }

    public void deleteMaze(String name) {
        mazeList.remove(name);
    }

}
