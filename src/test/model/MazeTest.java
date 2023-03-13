//package model;
//
//import model.exceptions.ElementAlreadyExistsException;
//import model.exceptions.OutOfBoundsException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class MazeTest {
//    Maze m1;
//    Maze m2;
//    Maze m3;
//
//    private final int defaultGridSize = 10;
//
//    @BeforeEach
//    public void setup() {
//        m1 = new Maze("MyFirstMaze");
//        m2 = new Maze("SmallerMaze", 3);
//        m3 = new Maze(m1);//copy of maze1
//    }
//
//    @Test
//    public void copyTest() {
//        try {
//            assertEquals("Monster", m3.getStatus(defaultGridSize - 1, defaultGridSize - 1));
//            assertEquals("Empty", m3.getStatus(1, 3));
//            //m3.placeEntity(new Position(1, 3), "Monster");
//            assertEquals("Empty", m3.getStatus(defaultGridSize - 1, defaultGridSize - 1));
//            assertEquals("Monster", m3.getStatus(1, 3));
//
//            assertEquals("Empty", m3.getStatus(defaultGridSize - 1, defaultGridSize - 1));
//            //m3.placeEntity(1, 3, "q");
//            assertEquals("Empty", m3.getStatus(defaultGridSize - 1, defaultGridSize - 1));
//        } catch (OutOfBoundsException | ElementAlreadyExistsException e) {
//            fail();
//        }
//    }
//
//    @Test
//    public void movePlayerTest() {
//        try {
//            assertEquals("Player", m2.getStatus(0, 0));
//            assertEquals("Empty", m2.getStatus(1, 0));
//            assertEquals("Empty", m2.getStatus(1, 1));
//            assertEquals("Empty", m2.getStatus(1, 2));
//
//            assertFalse(m2.movePlayer("d"));
//            assertFalse(m2.movePlayer("r"));
//            assertFalse(m2.movePlayer("r"));
//            assertEquals("Empty", m2.getStatus(0, 0));
//            assertEquals("Empty", m2.getStatus(1, 0));
//            assertEquals("Empty", m2.getStatus(1, 1));
//            assertEquals("Player", m2.getStatus(1, 2));
//
//
//            assertEquals("Monster", m2.getStatus(2, 2));
//            boolean result = m2.movePlayer("d");
//            assertTrue(result);
//        } catch (OutOfBoundsException e) {
//            fail();
//        }
//    }
//
//    @Test
//    public void placeEntityTest() {
//        try {
//            assertEquals("Player", m2.getStatus(0, 0));
//            assertEquals("Empty", m2.getStatus(1, 0));
//            //m2.placeEntity(1, 0, "Player");
//            assertEquals("Empty", m2.getStatus(0, 0));
//            assertEquals("Player", m2.getStatus(1, 0));
//
//            assertThrows(ElementAlreadyExistsException.class, () -> m1.placeEntity(0, 0, "Obstacle"));
//
//            assertEquals("Empty", m1.getStatus(1, 0));
//            //m1.placeEntity(1, 0, "Obstacle");
//            assertEquals("Obstacle", m1.getStatus(1, 0));
//
//        } catch (OutOfBoundsException | ElementAlreadyExistsException e) {
//            fail();
//        }
//    }
//
//    @Test
//    public void getTest() {
//        assertEquals("MyFirstMaze", m1.getName());
//        int[] playerPos = m2.getPlayerPosition();
//        assertEquals(0, playerPos[0]);
//        assertEquals(0, playerPos[1]);
//
//        m2.movePlayer("r");
//        playerPos = m2.getPlayerPosition();
//        assertEquals(0, playerPos[0]);
//        assertEquals(1, playerPos[1]);
//
//        assertThrows(OutOfBoundsException.class, () -> m3.getStatus(defaultGridSize, defaultGridSize));
//    }
//}
