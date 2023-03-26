package model;

import model.exceptions.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import persistence.Writable;
import ui.exceptions.MazeAlreadyExistsException;
import ui.exceptions.MazeDoesNotExistException;

import java.util.*;

public class Game implements Writable {
    private final int playerVisibility;
    private final Map<String, Maze> mazeList;
    private boolean gameRunning;
    private boolean editMode;
    private Maze currMaze;

    public Game() {
        gameRunning = false;
        editMode = true;
        playerVisibility = 2;
        mazeList = new HashMap<>();
    }

    public Game(JSONObject gameJson) {
        try {
            JSONObject currMaze = gameJson.getJSONObject("currMaze");
            this.currMaze = new Maze(currMaze);
        } catch (JSONException e) {
            // there is no currMaze
        }

        this.mazeList = parseMazeList(gameJson.getJSONArray("mazeList"));
        gameRunning = gameJson.getBoolean("gameRunning");
        editMode = gameJson.getBoolean("editMode");
        playerVisibility = gameJson.getInt("playerVisibility");
    }

    public void createMaze(String name, int gridSize) throws MazeAlreadyExistsException {
        if (mazeList.get(name) != null) {
            throw new MazeAlreadyExistsException();
        }
        mazeList.put(name, new Maze(name, gridSize));
    }

    public void selectMaze(String name) throws MazeDoesNotExistException {
        if (mazeList.get(name) == null) {
            throw new MazeDoesNotExistException();
        }
        currMaze = mazeList.get(name);
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

    public boolean isEmpty() {
        return mazeList.isEmpty();
    }


    //Requires: currMaze != null, gamerRunning
    public void updateMaze(String key) {
        if (key.equalsIgnoreCase("q")) {
            setRunning(false);
            currMaze = null;
        } else if (editMode) {
            assert currMaze != null;
            assert gameRunning;
            updateEditMode(key.toLowerCase());
        } else {
            assert currMaze != null;
            assert gameRunning;
            updatePlayMode(key.toLowerCase());
        }
    }

    private void updateEditMode(String key) {
        try {
            switch (key) {
                case "obstacle":
                case "player":
                case "monster":
                    currMaze.placeEntity(key);
                    break;
                case "l":
                case "r":
                case "u":
                case "d":
                    currMaze.moveCursor(key);
            }
        } catch (ElementAlreadyExistsException e) {
            System.out.println("Can't place object here");
        }
    }

    private void updatePlayMode(String key) {
        try {
            switch (key) {
                case "l":
                case "r":
                case "u":
                case "d":
                    currMaze.movePlayer(key);
            }
        } catch (ContactException e) {
            System.out.println("Contact!!!");
        }
    }

    public String[][] getGrid() {
        if (editMode) {
            return getEntireGrid();
        } else {
            return getPlayerGrid();
        }
    }

    private String[][] getEntireGrid() {
        int gridSize = currMaze.getGridSize();
        String[][] grid = new String[gridSize][gridSize];
        for (int y = 0; y < gridSize; y++) {
            for (int x = 0; x < gridSize; x++) {
                try {
                    grid[y][x] = currMaze.getStatus(new Position(x, y));
                } catch (OutOfBoundsException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return grid;
    }

    private String[][] getPlayerGrid() {
        int gridSize = 2 * playerVisibility + 1;
        String[][] grid = new String[gridSize][gridSize];
        Position playerPos = currMaze.getPlayerPosition();
        int playerX = playerPos.getPosX();
        int playerY = playerPos.getPosY();
        for (int y = 0; y < gridSize; y++) {
            for (int x = 0; x < gridSize; x++) {
                try {
                    grid[y][x] = currMaze.getStatus(new Position(x + playerX - playerVisibility,
                            y + playerY - playerVisibility));
                } catch (OutOfBoundsException e) {
                    grid[y][x] = "~";
                }
            }
        }
        return grid;
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonGame = new JSONObject();
        jsonGame.put("playerVisibility", playerVisibility);
        jsonGame.put("gameRunning", gameRunning);
        jsonGame.put("editMode", editMode);
        jsonGame.put("mazeList", mazeListToJsonObject());
        try {
            jsonGame.put("currMaze", currMaze.toJson());
        } catch (NullPointerException ignore) {
            //No current maze
        }
        return jsonGame;
    }

    private JSONArray mazeListToJsonObject() {
        JSONArray json = new JSONArray();
        for (Maze tempMaze : mazeList.values()) {
            json.put(tempMaze.toJson());
        }
        return json;
    }

    private Map<String, Maze> parseMazeList(JSONArray mazeList) {
        Map<String, Maze> mazeMap = new HashMap<>();

        for (Object json : mazeList) {
            JSONObject mazeJson = (JSONObject) json;
            Maze tempMaze = new Maze(mazeJson);
            mazeMap.put(tempMaze.getName(), tempMaze);
        }
        return mazeMap;
    }
}
