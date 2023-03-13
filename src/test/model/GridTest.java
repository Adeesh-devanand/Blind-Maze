package model;

import model.grid.Grid;
import model.exceptions.ContactException;
import model.exceptions.ElementAlreadyExistsException;
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
    Position p6_6;
    Position p7_7;
    Position p8_8;
    Position pneg1_neg1;

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
        p8_8 = new Position(8, 8);
        pneg1_neg1 = new Position(-1, -1);
    }

    @Test
    public void placeTest() {
        try {
            assertEquals("Empty", grid.getStatus(p0_1));
            assertEquals("Player", grid.getStatus(p0_0));
            assertEquals("Empty", grid.getStatus(p4_5));

            grid.placeObstacle(p0_1);
            grid.setPlayerPosition(p4_5);

            assertEquals("Obstacle", grid.getStatus(p0_1));
            assertEquals("Empty", grid.getStatus(p0_0));
            assertEquals("Player", grid.getStatus(p4_5));
            assertEquals("Monster", grid.getStatus(p7_7));

            assertEquals("Monster", grid.getStatus(p7_7));
            assertEquals("Empty", grid.getStatus(p0_0));
            grid.setMonsterPosition(p0_0);
            assertEquals("Monster", grid.getStatus(p0_0));
            assertEquals("Empty", grid.getStatus(p7_7));
        } catch (Exception e) {
            fail();
        }

        assertThrows(ElementAlreadyExistsException.class, () -> grid.placeObstacle(p0_0));
        assertThrows(OutOfBoundsException.class, () -> grid.placeObstacle(pneg1_neg1));

        assertThrows(ElementAlreadyExistsException.class, () -> grid.setPlayerPosition(p0_0));
        assertThrows(OutOfBoundsException.class, () -> grid.setPlayerPosition(p8_8));

        assertThrows(ElementAlreadyExistsException.class, () -> grid.setMonsterPosition(p0_1));
        assertThrows(OutOfBoundsException.class, () -> grid.setMonsterPosition(p8_8));

        assertThrows(OutOfBoundsException.class, () -> grid.setCursorPosition(pneg1_neg1));
    }

    @Test
    public void moveTest() {
        placeTest();

        try {
            Position p7_6 = new Position(7, 6);
            Position p5_7 = new Position(5, 7);

            grid.setMonsterPosition(p3_1);
            grid.setMonsterPosition(p7_7);
            assertEquals("Empty", grid.getStatus(p7_6));
            assertEquals("Monster", grid.getStatus(p7_7));
            grid.moveMonster("u");
            assertEquals("Monster", grid.getStatus(p7_6));
            assertEquals("Empty", grid.getStatus(p7_7));

            assertEquals("Player", grid.getStatus(p4_5));
            assertEquals("Empty", grid.getStatus(p5_5));
            grid.movePlayer("d");
            assertEquals("Empty", grid.getStatus(p4_5));
            assertEquals("Player", grid.getStatus(p5_5));

            grid.movePlayer("d");
            grid.movePlayer("d");


            //Trying to move against edge
            assertEquals("Player", grid.getStatus(p5_7));
            grid.movePlayer("d");
            assertEquals("Player", grid.getStatus(p5_7));

            grid.placeObstacle(p7_7);
            assertEquals("Obstacle", grid.getStatus(p7_7));
            assertEquals("Monster", grid.getStatus(p7_6));

            assertThrows(RuntimeException.class, () -> grid.moveCursor("u"));

            grid.moveCursor("d");
            assertEquals(new Position(0, 1), grid.getCursorPos());


            try {
                grid.moveMonster("d");
            } catch (ContactException e) {
                fail();
            }
            assertEquals("Monster", grid.getStatus(p7_6));
            assertEquals("Obstacle", grid.getStatus(p7_7));
        } catch (OutOfBoundsException | ElementAlreadyExistsException | ContactException e) {
            fail();
        }
    }

    @Test
    public void contactTest() {
        try {
            grid.setMonsterPosition(p0_1);
            assertThrows(ContactException.class, () -> grid.movePlayer("d"));
        } catch (ElementAlreadyExistsException | OutOfBoundsException e) {
            fail();
        }
    }
}
