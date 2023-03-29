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
    private final int defaultGridSize = 10;
    private int playerVisibility = 1;
    private final Map<String, Maze> mazeList;
    private boolean gameRunning;
    private boolean editMode;
    private Maze currMaze;

    //Effects: creates a Game that is not running, and in editMode by default
    public Game() {
        gameRunning = false;
        editMode = true;
        mazeList = new HashMap<>();
    }

    //Requires: gameJson must be a valid Json object created using toJson()
    //Effects:  translates a Game object from a Json object
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

    //Modifies: this
    //Effects: creates an empty maze and adds it to the list of available mazes
    public void createMaze(String name) throws MazeAlreadyExistsException {
        createMaze(name, defaultGridSize);
    }

    //Modifies: this
    //Effects: creates an empty maze and adds it to the list of available mazes
    public void createMaze(String name, int gridSize) throws MazeAlreadyExistsException {
        if (mazeList.get(name) != null) {
            throw new MazeAlreadyExistsException();
        }
        mazeList.put(name, new Maze(name, gridSize));
    }

    //Requires: there must be at least one maze created
    //Modifies: this
    //Effects:  - sets the active maze to the given maze,
    //          - and sets the game to running
    public void selectMaze(String name) throws MazeDoesNotExistException {
        if (mazeList.get(name) == null) {
            throw new MazeDoesNotExistException();
        }
        currMaze = mazeList.get(name);
        gameRunning = true;
    }

    //Modifies: this
    //Effects:  sets the game to not running
    public void quitGame() {
        this.gameRunning = false;
    }

    //Effects: returns if the game is running
    public boolean isRunning() {
        return gameRunning;
    }

    //Modifies: this
    //Effects: toggles between editMode and playMode
    public void toggleMode() {
        editMode = !editMode;
    }

    //Effects: returns the game mode currently set
    public String getMode()     {
        return editMode ? "EditMode" : "PlayMode";
    }

    //Effects: returns true if there are no mazes created
    public boolean isEmpty() {
        return mazeList.isEmpty();
    }

    //Requires: requires a maze to be selected, and the game to be running
    //Modifies: this, currMaze
    //Effects:  - updates the active maze based on the user input, and the current game mode
    //          - quits the game, and deactivates active maze if user quits
    public void updateMaze(String key) {
        if (key.equalsIgnoreCase("q")) {
            quitGame();
            currMaze = null;
        } else if (editMode) {
            updateEditMode(key.toLowerCase());
        } else {
            updatePlayMode(key.toLowerCase());
        }
    }

    //Requires: requires a maze to be selected
    //Modifies: this, currMaze
    //Effects:  updates the active maze in EDIT mode
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
        } catch (PositionOccupiedException e) {
            System.out.println("Can't place object here");
        }
    }

    //Requires: requires a maze to be selected
    //Modifies: this, currMaze
    //Effects:  updates the active maze in PLAY mode
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

    //Requires: requires a maze to be selected
    //Effects:  gets the active grid based on the current game mode
    public String[][] getGrid() {
        if (editMode) {
            return getEntireGrid();
        } else {
            return getPlayerGrid();
        }
    }

    //Requires: requires a maze to be selected
    //Effects:  gets the entire active grid
    private String[][] getEntireGrid() {
        int gridSize = currMaze.getGridSize();
        String[][] grid = new String[gridSize][gridSize];
        for (int y = 0; y < gridSize; y++) {
            for (int x = 0; x < gridSize; x++) {
                grid[y][x] = currMaze.getStatus(new Position(x, y));
            }
        }
        return grid;
    }

    //Requires: requires a maze to be selected
    //Effects:  gets the active grid with only the given visibility around the player
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
                    grid[y][x] = "Void";
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
