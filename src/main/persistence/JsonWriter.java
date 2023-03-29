package persistence;

import model.Game;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private final String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(destination);
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }

    //MODIFIES: this
    //EFFECTS: saves the game to the destination file
    public void writeGame(Game game) {
        JSONObject json = game.toJson();
        saveToFile(json.toString(TAB));
    }
}
