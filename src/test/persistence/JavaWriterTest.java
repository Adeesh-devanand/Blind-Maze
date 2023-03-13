package persistence;

import model.exceptions.MazeAlreadyExistsException;
import org.junit.jupiter.api.Test;
import ui.Game;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest {
    @Test
    void testWriterInvalidFile() {
        try {
            Game game = new Game();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyGame() {
        try {
            Game game = new Game();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyWorkroom.json");
            writer.open();
            writer.writeGame(game);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyWorkroom.json");
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
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralWorkroom.json");
            writer.open();
            writer.writeGame(game);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralWorkroom.json");
            game = reader.loadGame();
            assertFalse(game.isEmpty());
            assertFalse(game.isRunning());
            assertEquals("EditMode", game.getMode());

        } catch (IOException | MazeAlreadyExistsException e) {
            fail("Exception should not have been thrown");
        }
    }
}