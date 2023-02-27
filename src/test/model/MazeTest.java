package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MazeTest {
    Maze m1;
    Maze m2;
    Maze m3;

    @BeforeEach
    public void setup(){
        m1 = new Maze("MyFirstMaze");
        m2 = new Maze("SmallerMaze", 3);
        m3 = new Maze(m1);//copy of maze1
    }

    @Test
    public void movePlayerTest(){
        assertEquals("p", m2.getStatus(0, 0));
        assertEquals("e", m2.getStatus(1, 0));
        assertEquals("e", m2.getStatus(1, 1));
        assertEquals("e", m2.getStatus(1, 2));

        assertFalse(m2.movePlayer("d"));
        assertFalse(m2.movePlayer("r"));
        assertFalse(m2.movePlayer("r"));
        assertEquals("e", m2.getStatus(0, 0));
        assertEquals("e", m2.getStatus(1, 0));
        assertEquals("e", m2.getStatus(1, 1));
        assertEquals("p", m2.getStatus(1, 2));


        assertEquals("m", m2.getStatus(2, 2));
        boolean result = m2.movePlayer("d");
        assertTrue(result);
    }
}
