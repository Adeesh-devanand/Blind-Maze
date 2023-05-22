package model;

import model.exceptions.ContactException;
import model.exceptions.PositionOccupiedException;
import model.exceptions.OutOfBoundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MazeTest {
    Maze m1;
    Maze m2;
    Maze m3;

    @BeforeEach
    public void setup() {
        m1 = new Maze("MyFirstMaze", 10);
        m2 = new Maze("SmallerMaze", 3);
    }

    @Test
    public void copyTest() {
        try {
            m1.setCursorPos(new Position(1, 1));
            m1.placeEntity("Player");
            m1.setCursorPos(new Position(1, 5));
            m1.placeEntity("Obstacle");
        } catch (PositionOccupiedException | OutOfBoundsException e) {
            fail("Unexpected Error thrown");
        }

        m3 = new Maze(m1);//deep copy of maze1

        assertNotEquals(null, m3);
        assertNotEquals(m3, new Maze("MyFirstMaze", 10));
        assertEquals(m3, m3);
        assertEquals(m3, m1);

        assertEquals("MyFirstMaze", m3.getName());
        assertEquals(10, m3.getGridSize());
        assertEquals(new Position(1, 1), m3.getPlayerPosition());

        try {
            m1.setCursorPos(new Position(0, 1));
            m1.placeEntity("Obstacle");

            assertEquals("Obstacle", m1.getStatus(new Position(0, 1)));
            assertEquals("Empty", m3.getStatus(new Position(0, 1)));
        } catch (PositionOccupiedException | OutOfBoundsException e) {
            fail("Unexpected Error thrown");
        }
    }

    @Test
    public void simpleGetTest() {
        assertEquals("MyFirstMaze", m1.getName());
        assertEquals("SmallerMaze", m2.getName());

        assertEquals(10, m1.getGridSize());
        assertEquals(3, m2.getGridSize());

        assertEquals(new Position(0, 0), m1.getPlayerPosition());
        assertEquals(new Position(0, 0), m1.getPlayerPosition());
    }

    @Test
    public void getStatusTest() {
        try {
            assertEquals("Player", m1.getStatus(new Position(0, 0)));
            assertEquals("Monster", m1.getStatus(new Position(9, 9)));

            assertEquals("Player", m2.getStatus(new Position(0, 0)));
            assertEquals("Monster", m2.getStatus(new Position(2, 2)));
        } catch (OutOfBoundsException e) {
            fail("Unexpected error thrown");
        }

        try {
            m1.getStatus(new Position(-1, -1));
            fail("Should be out of bounds");
        } catch (OutOfBoundsException e) {
            //pass
        }
    }

    @Test
    public void cursorTest() {
        try {
            assertEquals("Empty", m1.getStatus(new Position(9, 8)));
            m1.setCursorPos(new Position(9, 8));
            m1.placeEntity("Monster");
            assertEquals("Monster", m1.getStatus(new Position(9, 8)));
            //pass
        } catch (PositionOccupiedException | OutOfBoundsException e) {
            fail("Unexpected error thrown");
        }

        try {
            m1.setCursorPos(new Position(10, 9));
            fail("Out of bounds exception expected");
        } catch (OutOfBoundsException e) {
            //pass
        }


        try {
            m1.moveCursor("u");
            // moving against edge
            m1.moveCursor("r");
            m1.moveCursor("r");
            m1.moveCursor("r");
            m1.placeEntity("Obstacle");
            assertEquals("Obstacle", m1.getStatus(new Position(9, 7)));
        } catch (PositionOccupiedException | OutOfBoundsException e) {
            fail("Unexpected error thrown");
        }
    }

    @Test
    public void movePlayerTest() {
        try {
            assertEquals("Player", m2.getStatus(new Position(0, 0)));
            assertEquals("Empty", m2.getStatus(new Position(0, 1)));
            assertEquals("Empty", m2.getStatus(new Position(1, 1)));

            m2.movePlayer("d");
            m2.movePlayer("r");

            assertEquals("Empty", m2.getStatus(new Position(0, 0)));
            assertEquals("Empty", m2.getStatus(new Position(0, 1)));
            assertEquals("Player", m2.getStatus(new Position(1, 1)));
        } catch (OutOfBoundsException | ContactException e) {
            fail("Unexpected Exception thrown");
        }

        try {
            m2.movePlayer("d");
            m2.movePlayer("r");
            fail("Contact with monster expected");
        } catch (ContactException e) {
            //pass
        }


        try {
            //moving against bottom border
            m2.movePlayer("d");
            m2.movePlayer("d");
            m2.movePlayer("d");
            assertEquals(new Position(1, 2), m2.getPlayerPosition());
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }

    @Test
    public void placeEntityTest() {
        try {
            assertEquals("Player", m2.getStatus(new Position(0, 0)));
            assertEquals("Empty", m2.getStatus(new Position(1, 0)));
            m2.setCursorPos(new Position(1, 0));
            m2.placeEntity("Player");
            assertEquals("Empty", m2.getStatus(new Position(0, 0)));
            assertEquals("Player", m2.getStatus(new Position(1, 0)));

        } catch (OutOfBoundsException | PositionOccupiedException e) {
            fail("Unexpected Exception thrown");
        }

        try {
            m2.placeEntity("Monster");
            fail("Exception was expected to be thrown");
        } catch (PositionOccupiedException e) {
            //pass
        }

        try {
            m2.setCursorPos(new Position(2, 2));
            m2.placeEntity("Monster");
            fail("Exception was expected to be thrown");
        } catch (PositionOccupiedException e) {
            //pass
        } catch (OutOfBoundsException e) {
            fail("Wrong exception was thrown");
        }
    }
}
