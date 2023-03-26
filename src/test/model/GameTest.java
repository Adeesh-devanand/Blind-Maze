package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.exceptions.MazeAlreadyExistsException;
import ui.exceptions.MazeDoesNotExistException;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private Game game;

    @BeforeEach
    public void setup() {
        game = new Game();
    }

    @Test
    public void constructorTest() {
        assertFalse(game.isRunning());
        assertTrue(game.isEmpty());
        assertEquals("EditMode", game.getMode());
    }

    @Test
    public void toggleTest() {
        assertEquals("EditMode", game.getMode());
        game.toggleMode();
        assertEquals("PlayMode", game.getMode());
        game.toggleMode();
        assertEquals("EditMode", game.getMode());
    }

    @Test
    public void runningTest() {

        game.setRunning(false);
        assertFalse(game.isRunning());

        game.setRunning(true);
        assertTrue(game.isRunning());

        game.setRunning(true);
        assertTrue(game.isRunning());

        game.setRunning(false);
        assertFalse(game.isRunning());
    }

    @Test
    public void createMazeTest() {
        try {
            assertTrue(game.isEmpty());

            game.createMaze("MyFirstMaze", 10);
            assertFalse(game.isEmpty());

            game.createMaze("MySecondMaze", 3);
            assertFalse(game.isEmpty());
        } catch (MazeAlreadyExistsException e) {
            fail("Unexpected exception thrown");
        }

        try {
            game.createMaze("ThirdMaze", 2);
            game.createMaze("ThirdMaze", 2);
            fail("Same maze was created twice");
        } catch (MazeAlreadyExistsException e) {
            //pass
        }
    }

    //have to add more concrete test
    @Test
    public void selectMazeTest() {
        try {
            game.createMaze("MyFirstMaze", 10);
            game.createMaze("MySecondMaze", 3);

            game.selectMaze("MyFirstMaze");
            game.selectMaze("MyFirstMaze");
        } catch (MazeAlreadyExistsException | MazeDoesNotExistException e) {
            fail("Unexpected exception thrown");
        }

        try {
            game.selectMaze("No such Maze");
            fail("No such maze exists");
        } catch (MazeDoesNotExistException e){
            //pass
        }
    }

    @Test
    public void updateMazeQuitTest() {
        openNewMaze();

        assertTrue(game.isRunning());
        game.updateMaze("Q");
        assertFalse(game.isRunning());
    }

    @Test
    public void updateMazeEditModeTest() {
        openNewMaze();

        assertEquals("EditMode", game.getMode());
        game.updateMaze("r");
        game.updateMaze("l");
        game.updateMaze("d");
        game.updateMaze("u");
        game.updateMaze("r");
        game.updateMaze("obstacle");
        game.updateMaze("player");
        game.updateMaze("monster");

        assertTrue(game.isRunning());
        game.updateMaze("Q");
        assertFalse(game.isRunning());
    }

    @Test
    public void updateMazePlayModeTest() {
        openNewMaze();
        game.toggleMode();

        assertEquals("PlayMode", game.getMode());
        game.updateMaze("r");
        game.updateMaze("l");
        game.updateMaze("d");
        game.updateMaze("u");
        game.updateMaze("player");

        game.updateMaze("d");
        game.updateMaze("r");
    }

    @Test
    public void getGridTest() {
        openNewMaze();
        assertEquals("EditMode", game.getMode());
        game.getGrid();

        game.toggleMode();
        assertEquals("PlayMode", game.getMode());
        game.getGrid();

    }

    private void openNewMaze() {
        try {
            game.setRunning(true);
            game.createMaze("MyFirstMaze", 2);
            game.selectMaze("MyFirstMaze");
        } catch (MazeAlreadyExistsException | MazeDoesNotExistException e) {
            fail("Unexpected exception thrown");
        }
    }
}
