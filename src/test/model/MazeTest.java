package model;

import model.exceptions.ElementAlreadyExistsException;
import model.exceptions.OutOfBoundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MazeTest {
    Maze m1;
    Maze m2;
    Maze m3;

    private final int defaultGridSize = 10;

    @BeforeEach
    public void setup(){
        m1 = new Maze("MyFirstMaze");
        m2 = new Maze("SmallerMaze", 3);
        m3 = new Maze(m1);//copy of maze1
    }

    @Test
    public void movePlayerTest() throws OutOfBoundsException {
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

    @Test
    public void placeEntityTest() throws OutOfBoundsException, ElementAlreadyExistsException {
        assertEquals("p", m2.getStatus(0, 0));
        assertEquals("e", m2.getStatus(1, 0));
        m2.placeEntity(1, 0, "p");
        assertEquals("e", m2.getStatus(0, 0));
        assertEquals("p", m2.getStatus(1, 0));

        assertEquals("m", m3.getStatus(defaultGridSize-1, defaultGridSize-1));
        assertEquals("e", m3.getStatus(1, 3));
        m3.placeEntity(1, 3, "m");
        assertEquals("e", m3.getStatus(defaultGridSize-1, defaultGridSize-1));
        assertEquals("m", m3.getStatus(1, 3));

        try{
            assertEquals("p", m1.getStatus(0, 0));
            m1.placeEntity(0, 0, "o");
        } catch(ElementAlreadyExistsException e){
            ;
        }

        assertEquals("e", m1.getStatus(1, 0));
        m1.placeEntity(1, 0, "o");
        assertEquals("o", m1.getStatus(1, 0));
    }

    @Test
    public void getTest(){
        assertEquals("MyFirstMaze", m1.getName());
        int[] playerPos = m2.getPlayerPosition();
        assertEquals(0, playerPos[0]);
        assertEquals(0, playerPos[1]);

        m2.movePlayer("r");
        playerPos = m2.getPlayerPosition();
        assertEquals(0, playerPos[0]);
        assertEquals(1, playerPos[1]);


        try{
            String element = m3.getStatus(defaultGridSize,defaultGridSize);
        } catch (OutOfBoundsException e){

        }
    }
}
