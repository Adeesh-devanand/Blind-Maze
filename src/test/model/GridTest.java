package model;

import model.exceptions.Contact;
import model.exceptions.ElementAlreadyExistsException;
import model.exceptions.OutOfBoundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GridTest {
    Grid grid1;

    Position p0_0;
    Position p0_1;
    Position p3_1;
    Position p3_2;
    Position p4_5;
    Position p5_5;
    Position p7_7;

    @BeforeEach
    public void setup() {
        grid1 = new Grid(8);

        p0_0 = new Position(0, 0);
        p0_1 = new Position(0, 1);
        p3_1 = new Position(3, 1);
        p3_2 = new Position(3, 2);
        p4_5 = new Position(5, 4);
        p5_5 = new Position(5, 5);
        p7_7 = new Position(7, 7);
    }

    @Test
    public void placeTest() {
        try {
            assertEquals("Empty", grid1.getStatus(p0_1));
            assertEquals("Player", grid1.getStatus(p0_0));
            assertEquals("Empty", grid1.getStatus(p4_5));

            grid1.placeObstacle(p0_1);
            grid1.setPlayerPosition(p4_5);

            assertEquals("Obstacle", grid1.getStatus(p0_1));
            assertEquals("Empty", grid1.getStatus(p0_0));
            assertEquals("Player", grid1.getStatus(p4_5));
            assertEquals("Monster", grid1.getStatus(p7_7));

            assertEquals("Monster", grid1.getStatus(p7_7));
            assertEquals("Empty", grid1.getStatus(p0_0));
            grid1.setMonsterPosition(p0_0);
            assertEquals("Monster", grid1.getStatus(p0_0));
            assertEquals("Empty", grid1.getStatus(p7_7));
        } catch (Exception e) {
            fail();
        }

        assertThrows(ElementAlreadyExistsException.class, () -> grid1.placeObstacle(p0_0));
    }

    @Test
    public void moveTest() {
        placeTest();

        try {
            Position p7_6 = new Position(7, 6);
            Position p5_7 = new Position(5, 7);

            grid1.setMonsterPosition(p3_1);
            grid1.setMonsterPosition(p7_7);
            assertEquals("Empty", grid1.getStatus(p7_6));
            assertEquals("Monster", grid1.getStatus(p7_7));
            grid1.moveMonster("u");
            assertEquals("Monster", grid1.getStatus(p7_6));
            assertEquals("Empty", grid1.getStatus(p7_7));

            assertEquals("Player", grid1.getStatus(p4_5));
            assertEquals("Empty", grid1.getStatus(p5_5));
            grid1.movePlayer("d");
            assertEquals("Empty", grid1.getStatus(p4_5));
            assertEquals("Player", grid1.getStatus(p5_5));

            grid1.movePlayer("d");
            grid1.movePlayer("d");


            //Trying to move against edge
            assertEquals("Player", grid1.getStatus(p5_7));
            grid1.movePlayer("d");
            assertEquals("Player", grid1.getStatus(p5_7));

            grid1.placeObstacle(p7_7);
            assertEquals("Obstacle", grid1.getStatus(p7_7));
            assertEquals("Monster", grid1.getStatus(p7_6));

            try {
                grid1.moveMonster("d");
            } catch (Contact e) {
                fail();
            }
            assertEquals("Monster", grid1.getStatus(p7_6));
            assertEquals("Obstacle", grid1.getStatus(p7_7));
        } catch (OutOfBoundsException | ElementAlreadyExistsException | Contact e) {
            fail();
        }
    }
}
