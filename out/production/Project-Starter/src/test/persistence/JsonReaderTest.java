package persistence;

import ui.exceptions.MazeDoesNotExistException;
import org.junit.jupiter.api.Test;
import model.Game;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {
    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            reader.loadGame();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderNewGame() {
        JsonReader reader = new JsonReader("./data/testReaderNewGame.json");
        try {
            Game game = reader.loadGame();
            assertFalse(game.isRunning());
            assertEquals("EditMode", game.getMode());
            assertThrows(MazeDoesNotExistException.class, () -> game.selectMaze(""));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderRunningGame() {
        JsonReader reader = new JsonReader("./data/testReaderRunningGame.json");
        try {
            Game game = reader.loadGame();
            assertTrue(game.isRunning());
            assertEquals("EditMode", game.getMode());
           game.selectMaze("TestMaze");
        } catch (MazeDoesNotExistException e) {
          fail();
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
