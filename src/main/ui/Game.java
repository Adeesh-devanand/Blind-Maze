package ui;

import model.Maze;
import model.Position;
import model.exceptions.*;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.*;

public class Game implements Writable {
    private boolean gameRunning;
    private final int playerVisibilty;
    private final Map<String, Maze> mazeList;
    private Maze currMaze;
    private boolean editMode;

    public Game() {
        gameRunning = false;
        editMode = true;
        playerVisibilty = 2;
        mazeList = new HashMap<>();
    }

    public Game(JSONObject currMaze, JSONArray mazeList, boolean gameRunning, boolean editMode, int playerVisibility) {
        this.gameRunning = gameRunning;
        this.editMode = editMode;
        this.playerVisibilty = playerVisibility;
        this.mazeList = parseMazeList(mazeList);
        if (currMaze != null) {
            this.currMaze = parseMaze(currMaze);
        }
    }

    private Map<String, Maze> parseMazeList(JSONArray mazeList) {
        Map<String, Maze> mazeMap = new HashMap<>();

        for (Object json : mazeList) {
            JSONObject mazeJson = (JSONObject) json;
            Maze tempMaze = parseMaze(mazeJson);
            mazeMap.put(tempMaze.getName(), tempMaze);
        }
        return mazeMap;
    }

    private Maze parseMaze(JSONObject mazeJson) {
        String name = mazeJson.getString("name");
        JSONObject grid = mazeJson.getJSONObject("grid");
        return new Maze(name, grid);
    }

    public void setRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    public boolean isRunning() {
        return gameRunning;
    }

    public void toggleMode() {
        editMode = !editMode;
    }

    public String getMode()     {
        return editMode ? "EditMode" : "PlayMode";
    }

    public void updateMaze(String key) {
        if (key.equalsIgnoreCase("q")) {
            setRunning(false);
            return;
        }
        if (editMode) {
            updateEditMode(key);
        } else {
            updatePlayMode(key);
        }
    }

    private void updateEditMode(String key) {
        try {
            switch (key.toLowerCase()) {
                case "o":
                    currMaze.placeEntity("Obstacle");
                    break;
                case "p":
                    currMaze.placeEntity("Player");
                    break;
                case "m":
                    currMaze.placeEntity("Monster");
                    break;
                case "l":
                case "r":
                case "u":
                case "d":
                    currMaze.moveCursor(key);
                    break;
            }
        } catch (ElementAlreadyExistsException e) {
            System.out.println("Can't place object here");
        } catch (OutOfBoundsException e) {
            throw new RuntimeException();
        }
    }

    private void updatePlayMode(String key) {
        switch (key.toLowerCase()) {
            case "l":
            case "r":
            case "u":
            case "d":
                currMaze.movePlayer(key);
                break;
        }
    }

    public void createMaze(String name, int gridSize) throws MazeAlreadyExistsException {
        if (mazeList.get(name) != null) {
            throw new MazeAlreadyExistsException();
        } else {
            mazeList.put(name, new Maze(name, gridSize));
        }
    }

    public boolean isEmpty() {
        return mazeList.isEmpty();
    }

    public void selectMaze(String name) throws MazeDoesNotExistException {
        Maze tempMaze = mazeList.get(name);

        if (tempMaze == null) {
            throw new MazeDoesNotExistException();
        }

        currMaze = tempMaze;
    }

    public void deleteMaze(String name) {
        mazeList.remove(name);
    }

    public String[][] getGrid() {
        if (editMode) {
            return getEntireGrid();
        } else {
            return getPlayerGrid();
        }
    }


    //TODO: temp changed i and j, but drawGame() needs to be fixed
    private String[][] getEntireGrid() {
        int gridSize = currMaze.getGridSize();
        String[][] grid = new String[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                try {
                    grid[j][i] = currMaze.getStatus(i, j);
                } catch (OutOfBoundsException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return grid;
    }

    private String[][] getPlayerGrid() {
        String[][] grid = new String[2 * playerVisibilty + 1][2 * playerVisibilty + 1];
        Position playerPos = currMaze.getPlayerPosition();
        int x = playerPos.getPosX();
        int y = playerPos.getPosY();
        for (int i = 0; i <= x + 2 * playerVisibilty; i++) {
            for (int j = 0; j <= y + 2 * playerVisibilty; j++) {
                try {
                    grid[i][j] = currMaze.getStatus(i - playerVisibilty, j - playerVisibilty);
                } catch (OutOfBoundsException e) {
                    grid[i][j] = "~";
                }
            }
        }
        return grid;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("gameRunning", gameRunning);
        json.put("playerVisibility", playerVisibilty);
        json.put("mazeList", mazeListToJsonObject());
        try {
            json.put("currMaze", currMaze.toJson());
        } catch (NullPointerException ignore) {
            ;
        }

        json.put("editMode", editMode);
        return json;
    }

    private JSONArray mazeListToJsonObject() {
        JSONArray json = new JSONArray();
        for (Map.Entry<String, Maze> pairs : mazeList.entrySet()) {
            json.put(pairs.getValue().toJson());
        }
        return json;
    }
}
