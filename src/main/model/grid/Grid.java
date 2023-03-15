/*
 * Handles all the computation and back-end functioning
 * keeps a track of the grid for the maze along with the elements and their respective positions
 *
 * NOTE: All positions start from the top left corner i.e top left is (0, 0)
 * 'y' represents the y axes, and 'x' represents the x axes */

package model.grid;

import model.Position;
import model.exceptions.ContactException;
import model.exceptions.ElementAlreadyExistsException;
import model.exceptions.OutOfBoundsException;
import org.json.JSONObject;
import persistence.Writable;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Grid implements Writable {
    private final int gridSize;
    private final Map<Position, Element> grid;
    private Cursor cursor;
    private Player player;
    private Monster monster;

    public Grid(int gridSize) {
        this.gridSize = gridSize;
        this.grid = new HashMap<>();

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                this.grid.put(new Position(j, i), new Empty());
            }
        }

        player = new Player(new Position(0, 0));
        monster = new Monster(new Position(gridSize - 1, gridSize - 1));
        cursor = new Cursor(new Position(0, 0));

        grid.replace(player.getPosition(), player);
        grid.replace(monster.getPosition(), monster);
    }

    public Grid(Grid oldGrid) {
        this(oldGrid.gridSize);

        for (Entry<Position, Element> entry : oldGrid.grid.entrySet()) {
            Position key = entry.getKey();
            Element value = entry.getValue();

            if (value.getType().equals("Obstacle")) {
                this.grid.replace(key, new Obstacle());
            }
        }

        this.player = new Player(oldGrid.player);
        this.monster = new Monster(oldGrid.monster);
        this.cursor = new Cursor(oldGrid.cursor);

        grid.replace(this.player.getPosition(), this.player);
        grid.replace(this.monster.getPosition(), this.monster);
        grid.replace(this.cursor.getPosition(), this.cursor);
    }

    public Grid(JSONObject gridJson) {
        this(gridJson.getInt("gridSize"));

        JSONObject internalGridJson = gridJson.getJSONObject("internalGrid");
        Set<String> keys = internalGridJson.keySet();
        String cursorPos = gridJson.getString("cursorPos");
        this.cursor = new Cursor(new Position(cursorPos));

        for (String key : keys) {
            String type = internalGridJson.getString(key);
            Position position = new Position(key);
            switch (type) {
                case "Obstacle":
                    grid.replace(position, new Obstacle());
                    break;
                case "Player":
                    Player player = new Player(position);
                    grid.replace(position, player);
                    this.player = player;
                    break;
                case "Monster":
                    Monster monster = new Monster(position);
                    grid.replace(position, monster);
                    this.monster = monster;
            }
        }
    }

    public void setCursorPosition(Position p) throws OutOfBoundsException {
        if (isOutOfBounds(p)) {
            throw new OutOfBoundsException();
        }
        cursor.setPosition(p);
    }

    public void setPlayerPosition(Position position) throws ElementAlreadyExistsException, OutOfBoundsException {
        Position oldPos = player.getPosition();
        placeOnGrid(position, player);
        grid.replace(oldPos, new Empty());
        player.setPosition(position);
    }

    public void setMonsterPosition(Position p) throws ElementAlreadyExistsException, OutOfBoundsException {
        Position oldPos = monster.getPosition();
        placeOnGrid(p, monster);
        grid.replace(oldPos, new Empty());
        monster.setPosition(p);
    }

    public void setObstacle(Position position) throws ElementAlreadyExistsException, OutOfBoundsException {
        placeOnGrid(position, new Obstacle());
    }

    public void moveCursor(String dir) {
        Position newPos = moveInDirection(cursor, dir);
        try {
            setCursorPosition(newPos);
        } catch (OutOfBoundsException e) {
            //simply don't move
        }
    }

    public void movePlayer(String dir) throws ContactException {
        Position newPos = moveInDirection(player, dir);
        if (newPos.equals(getMonsterPos())) {
            throw new ContactException();
        }
        try {
            setPlayerPosition(newPos);
        } catch (OutOfBoundsException | ElementAlreadyExistsException ignored) {
            //simply don't move
        }
    }

    public void moveMonster(String dir) throws ContactException {
        Position newPos = moveInDirection(monster, dir);
        if (newPos.equals(getPlayerPos())) {
            throw new ContactException();
        }
        try {
            setMonsterPosition(newPos);
        } catch (OutOfBoundsException | ElementAlreadyExistsException ignored) {
            //simply don't move
        }
    }

    public Position getCursorPos() {
        return cursor.getPosition();
    }

    public Position getPlayerPos() {
        return player.getPosition();
    }

    public Position getMonsterPos() {
        return monster.getPosition();
    }

    public String getStatus(Position position) throws OutOfBoundsException {
        if (isOutOfBounds(position)) {
            throw new OutOfBoundsException();
        }
        return grid.get(position).getType();
    }

    public int getSize() {
        return gridSize;
    }

    private boolean isCellOccupied(Position position) {
        Element e = grid.get(position);
        return !"Empty".equals(e.getType());
    }

    private boolean isOutOfBounds(Position position) {
        int x = position.getPosX();
        int y = position.getPosY();
        return x < 0 || y < 0 || x >= gridSize || y >= gridSize;
    }

    private void placeOnGrid(Position p, Element e) throws OutOfBoundsException, ElementAlreadyExistsException {
        if (isOutOfBounds(p)) {
            throw new OutOfBoundsException();
        }
        if (isCellOccupied(p)) {
            throw new ElementAlreadyExistsException();
        }
        grid.replace(p, e);
    }

    private Position moveInDirection(Element movableObj, String dir) {
        Position oldPos = movableObj.getPosition();
        int newX = oldPos.getPosX();
        int newY = oldPos.getPosY();

        switch (dir) {
            case "r":
                newX++;
                break;
            case "l":
                newX--;
                break;
            case "u":
                newY--;
                break;
            case "d":
                newY++;
        }
        return new Position(newX, newY);
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("gridSize", gridSize);
        json.put("cursorPos", getCursorPos().toString());

        JSONObject jsonGrid = new JSONObject();

        for (Entry<Position, Element> pairs : grid.entrySet()) {
            jsonGrid.put(pairs.getKey().toString(), pairs.getValue().toString());
        }
        json.put("internalGrid", jsonGrid);
        return json;
    }
}