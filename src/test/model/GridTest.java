package model;

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
        grid1 = new Grid(5, 8);
        grid2 = new Grid(7, 6);

        p1 = new Position(1, 0);
        p2 = new Position(4, 5);
        p3 = new Position(5, 5);
        p4 = new Position(2, 3);

    }

    @Test
    public void placeTest() {
        assertEquals("e", grid1.getStatus(p1));
        assertEquals("e", grid1.getStatus(p2));

        assertEquals("e", grid2.getStatus(p3));
        assertEquals("e", grid2.getStatus(p4));

        placeObjects();

        assertEquals("o", grid1.getStatus(p1));
        assertEquals("m", grid1.getStatus(p2));

        assertEquals("o", grid2.getStatus(p3));
        assertEquals("p", grid2.getStatus(p4));
    }

    @Test
    public void moveTest() {
        placeObjects();

        Position new_p = new Position(4, 4);

        assertEquals("e", grid1.getStatus(new_p));
        assertEquals("m", grid1.getStatus(p2));
        grid1.moveMonster("l");
        assertEquals("m", grid1.getStatus(new_p));
        assertEquals("e", grid1.getStatus(p2));

        new_p = new Position(2, 5);

        assertEquals("e", grid2.getStatus(new_p));
        assertEquals("p", grid2.getStatus(p4));
        grid2.movePlayer("r");
        grid2.movePlayer("r");
        //Trying to move against edge
        grid2.movePlayer("r");
        assertEquals("p", grid2.getStatus(new_p));
        assertEquals("e", grid2.getStatus(p4));

        new_p = new Position(4, 5);

        assertEquals("e", grid2.getStatus(new_p));
        grid2.movePlayer("d");
        grid2.movePlayer("d");
        //Trying to move against obstacle
        grid2.movePlayer("d");
        assertEquals("p", grid2.getStatus(new_p));
    }

    public void placeObjects() {
        grid1.placeObstacle(p1);
        grid1.placeMonster(p2);

        grid2.placeObstacle(p3);
        grid2.placePlayer(p4);
    }
}
