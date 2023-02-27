package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {
    @Test
    public void getTest(){
        Position p1 = new Position(12, 13);
        Position p2 = new Position(0, 0);
        Position p3 = new Position(-1, 6);

        assertEquals(-1, p3.getPosY());
        assertEquals(12, p1.getPosY());
        assertEquals(13, p1.getPosX());
        assertEquals(0, p2.getPosX());

    }
}
