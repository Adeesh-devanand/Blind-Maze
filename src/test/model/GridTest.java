package model;

import model.exceptions.PositionOccupiedException;
import model.grid.Grid;
import model.exceptions.ContactException;
import model.exceptions.OutOfBoundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GridTest {
    Grid grid;

    Position p0_0;
    Position p0_1;
    Position p3_1;
    Position p3_2;
    Position p4_5;
    Position p5_5;
    Position p7_7;
    Position p7_8;
    Position p8_7;
    Position p1_neg2;
    Position pneg1_1;

    @BeforeEach
    public void setup() {
        grid = new Grid(8);

        p0_0 = new Position(0, 0);
        p0_1 = new Position(0, 1);
        p3_1 = new Position(3, 1);
        p3_2 = new Position(3, 2);
        p4_5 = new Position(5, 4);
        p5_5 = new Position(5, 5);
        p7_7 = new Position(7, 7);

        p7_8 = new Position(7, 8);
        p8_7 = new Position(8, 7);

        p1_neg2 = new Position(1, -2);
        pneg1_1 = new Position(-1, 1);

    }

    @Test
    public void copyTest(){}

    @Test
    public void getTest() {
        // getPosTest()
        assertEquals(p0_0, grid.getCursorPos());
        assertEquals(p0_0, grid.getPlayerPos());
        assertEquals(p7_7, grid.getMonsterPos());

        // getSizeTest()
        assertEquals(8, grid.getSize());

        // getStatusTest()
        try {
            assertEquals("Player", grid.getStatus(p0_0));
            assertEquals("Monster", grid.getStatus(p7_7));
            assertEquals("Empty", grid.getStatus(p3_2));
            assertEquals("Empty", grid.getStatus(p5_5));
        } catch (OutOfBoundsException e) {
            fail("Within Bounds but still got error");
        }

        try {
            grid.getStatus(p1_neg2);
            fail("Should be out of bounds");
        } catch (OutOfBoundsException ignored){
            //pass
        }

        try {
            grid.getStatus(p7_8);
            fail("Should be out of bounds");
        } catch (OutOfBoundsException ignored){
            //pass
        }

        try {
            grid.getStatus(p8_7);
            fail("Should be out of bounds");
        } catch (OutOfBoundsException ignored){
            //pass
        }

        try {
            grid.getStatus(pneg1_1);
            fail("Should be out of bounds");
        } catch (OutOfBoundsException ignored){
            //pass
        }
    }

    @Test
    public void setCursorPositionTest() {}

    @Test
    public void setPlayerPositionTest() {
        try {
            assertEquals(p0_0, grid.getPlayerPos());
            assertEquals("Player", grid.getStatus(p0_0));
            assertEquals("Empty", grid.getStatus(p4_5));
            grid.setPlayerPosition(p4_5);
            assertEquals(p4_5, grid.getPlayerPos());
            assertEquals("Empty", grid.getStatus(p0_0));
            assertEquals("Player", grid.getStatus(p4_5));
        } catch (OutOfBoundsException | PositionOccupiedException e) {
            fail("Unexpected exception thrown");
        }

        try {
            grid.setPlayerPosition(pneg1_1);
        } catch (OutOfBoundsException e) {
            //pass
        } catch (PositionOccupiedException e) {
            fail("OutOfBoundsException should have been thrown first");
        }

        try {
            grid.setPlayerPosition(p7_7);
        } catch (OutOfBoundsException e) {
            fail("Within bounds but OutOfBoundsException still thrown");
        } catch (PositionOccupiedException e) {
            //pass
        }
    }

    @Test
    public void setMonsterPositionTest(){
        try {
            assertEquals(p7_7, grid.getMonsterPos());
            assertEquals("Monster", grid.getStatus(p7_7));
            assertEquals("Empty", grid.getStatus(p3_2));
            grid.setMonsterPosition(p3_2);
            assertEquals(p3_2, grid.getMonsterPos());
            assertEquals("Empty", grid.getStatus(p7_7));
            assertEquals("Monster", grid.getStatus(p3_2));
        } catch (OutOfBoundsException | PositionOccupiedException e) {
            fail("Unexpected exception thrown");
        }

        try {
            grid.setMonsterPosition(p8_7);
        } catch (OutOfBoundsException e) {
            //pass
        } catch (PositionOccupiedException e) {
            fail("OutOfBoundsException should have been thrown first");
        }

        try {
            grid.setPlayerPosition(p7_7);
        } catch (OutOfBoundsException e) {
            fail("Within bounds but OutOfBoundsException still thrown");
        } catch (PositionOccupiedException e) {
            //pass
        }
    }

    @Test
    public void setObstacleTest() {
        try {
            assertEquals("Empty", grid.getStatus(p0_1));
            grid.placeObstacle(p0_1);
            assertEquals("Obstacle", grid.getStatus(p0_1));
        } catch (OutOfBoundsException | PositionOccupiedException e) {
            fail("Unexpected Exception thrown");
        }

        try {
            grid.placeObstacle(pneg1_1);
            fail("OutOfBoundsException should have been thrown");
        } catch (OutOfBoundsException e) {
            //pass
        } catch (PositionOccupiedException e) {
            fail("OutOfBoundsException should have been thrown first");
        }

        try {
            grid.placeObstacle(p0_1);
            fail("PositionOccupiedException should have been thrown");
        } catch (OutOfBoundsException e) {
            fail("PositionOccupiedException should have been thrown");
        } catch (PositionOccupiedException e) {
            //pass
        }
    }

    @Test
    public void moveCursorTest() {}

    @Test
    public void movePlayerTest() {}

    @Test
    public void moveMonsterTest() {
        try {
            grid.moveMonster("d");
        } catch (ContactException e) {
            // contact with Player
        }
    }

    @Test
    public void contactTest() {
        try {
            grid.setMonsterPosition(p0_1);
            assertThrows(ContactException.class, () -> grid.movePlayer("d"));
        } catch (PositionOccupiedException | OutOfBoundsException e) {
            fail();
        }
    }

//    @Test
//    public void moveTest() {
//
//        try {
//            Position p7_6 = new Position(7, 6);
//            Position p5_7 = new Position(5, 7);
//
//            grid.setMonsterPosition(p3_1);
//            grid.setMonsterPosition(p7_7);
//            assertEquals("Empty", grid.getStatus(p7_6));
//            assertEquals("Monster", grid.getStatus(p7_7));
//            grid.moveMonster("u");
//            assertEquals("Monster", grid.getStatus(p7_6));
//            assertEquals("Empty", grid.getStatus(p7_7));
//
//            assertEquals("Player", grid.getStatus(p4_5));
//            assertEquals("Empty", grid.getStatus(p5_5));
//            grid.movePlayer("d");
//            assertEquals("Empty", grid.getStatus(p4_5));
//            assertEquals("Player", grid.getStatus(p5_5));
//
//            grid.movePlayer("d");
//            grid.movePlayer("d");
//
//
//            //Trying to move against edge
//            assertEquals("Player", grid.getStatus(p5_7));
//            grid.movePlayer("d");
//            assertEquals("Player", grid.getStatus(p5_7));
//
//            grid.placeObstacle(p7_7);
//            assertEquals("Obstacle", grid.getStatus(p7_7));
//            assertEquals("Monster", grid.getStatus(p7_6));
//
//            grid.moveCursor("d");
//            assertEquals(new Position(0, 1), grid.getCursorPos());
//
//
//            try {
//                grid.moveMonster("d");
//            } catch (ContactException e) {
//                fail();
//            }
//            assertEquals("Monster", grid.getStatus(p7_6));
//            assertEquals("Obstacle", grid.getStatus(p7_7));
//        } catch (OutOfBoundsException | PositionOccupiedException | ContactException e) {
//            fail();
//        }
//    }
}
