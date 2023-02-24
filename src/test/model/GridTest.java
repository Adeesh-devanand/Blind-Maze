package model;

import model.maze.Grid;
import model.maze.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GridTest {
    Grid grid1;
    Grid grid2;

    Position p1;
    Position p2;
    Position p3;
    Position p4;

    @BeforeEach
    public void setup() {
        grid1 = new Grid(5, 6);
        grid2 = new Grid(6, 6);

        p1 = new Position(0, 0);
        p2 = new Position(4, 5);
        p3 = new Position(5, 5);
        p4 = new Position(2, 3);

    }

    @Test
    public void placeTest() {
        assertEquals("empty", grid1.getStatus(p1));
        assertEquals("empty", grid1.getStatus(p2));

        assertEquals("empty", grid2.getStatus(p3));
        assertEquals("empty", grid2.getStatus(p4));

        placeObjects();

        assertEquals("obstacle", grid1.getStatus(p1));
        assertEquals("monster", grid1.getStatus(p2));

        assertEquals("obstacle", grid2.getStatus(p3));
        assertEquals("player", grid2.getStatus(p4));
    }

    @Test
    public void moveTest() {
        placeObjects();

        Position new_p = new Position(4, 4);

        assertEquals("empty", grid1.getStatus(new_p));
        assertEquals("monster", grid1.getStatus(p2));
        grid1.moveMonster("up");
        assertEquals("monster", grid1.getStatus(new_p));
        assertEquals("empty", grid1.getStatus(p2));

        new_p = new Position(2, 5);

        assertEquals("empty", grid2.getStatus(new_p));
        assertEquals("player", grid2.getStatus(p4));
        grid2.movePlayer("down");
        grid2.movePlayer("down");
        //Trying to move against edge
        grid2.movePlayer("down");
        assertEquals("player", grid2.getStatus(new_p));
        assertEquals("empty", grid2.getStatus(p4));

        new_p = new Position(4, 5);

        assertEquals("empty", grid2.getStatus(new_p));
        grid2.movePlayer("right");
        grid2.movePlayer("right");
        //Trying to move against obstacle
        grid2.movePlayer("right");
        assertEquals("player", grid2.getStatus(new_p));
    }

    public void placeObjects() {
        grid1.placeObstacle(p1);
        grid1.placeMonster(p2);

        grid2.placeObstacle(p3);
        grid2.placePlayer(p4);
    }
}
