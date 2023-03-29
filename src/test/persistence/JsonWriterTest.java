package persistence;

import ui.exceptions.MazeAlreadyExistsException;
import org.junit.jupiter.api.Test;
import model.Game;
import ui.exceptions.MazeDoesNotExistException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest {
    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException ignore) {
            // pass
        }
    }

    @Test
    void testWriterEmptyGame() {
        try {
            Game game = new Game();
            JsonWriter writer = new JsonWriter("data/testWriterEmptyGame.json");
            writer.open();
            writer.writeGame(game);
            writer.close();

            JsonReader reader = new JsonReader("data/testWriterEmptyGame.json");
            game = reader.loadGame();
            assertTrue(game.isEmpty());
            assertFalse(game.isRunning());
            assertEquals("EditMode", game.getMode());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterNonEmptyGame() {
        try {
            Game game = new Game();
            game.createMaze("FirstMaze", 5);
            JsonWriter writer = new JsonWriter("data/testWriterNonEmptyGame.json");
            writer.open();
            writer.writeGame(game);
            writer.close();

            JsonReader reader = new JsonReader("data/testWriterNonEmptyGame.json");
            game = reader.loadGame();
            assertFalse(game.isEmpty());
            assertFalse(game.isRunning());
            assertEquals("EditMode", game.getMode());

        } catch (IOException | MazeAlreadyExistsException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterRunningGame() {
        try {
            Game game = new Game();
            game.createMaze("FirstMaze", 5);
            game.selectMaze("FirstMaze");
            JsonWriter writer = new JsonWriter("data/TestWriterRunningGame.json");
            writer.open();
            writer.writeGame(game);
            writer.close();

            JsonReader reader = new JsonReader("data/testWriterRunningGame.json");
            game = reader.loadGame();
            assertFalse(game.isEmpty());
            assertTrue(game.isRunning());
            assertEquals("EditMode", game.getMode());

        } catch (IOException | MazeAlreadyExistsException | MazeDoesNotExistException e) {
            fail("Exception should not have been thrown");
        }
    }
}