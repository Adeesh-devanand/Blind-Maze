package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {
    @Test
    public void getTest(){
        Position p1 = new Position(12, 13);
        Position p2 = new Position(0, 0);
        Position p3 = new Position(-1, 6);

        assertEquals(-1, p3.getPosX());
        assertEquals(12, p1.getPosX());
        assertEquals(0, p2.getPosY());
    }

    @Test
    public void equalsTest(){
        assertEquals(new Position(12, 13), new Position(12, 13));
        assertTrue(new Position(2, 6).equals(new Position(2, 6)));
        assertFalse(new Position(12, 13).equals(new Position(12, 2)));
    }
}
