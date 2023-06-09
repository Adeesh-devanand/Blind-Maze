/*
 * Handles functionality for lading the game which is saved in the Json format  */

package persistence;

import model.Game;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class JsonReader {
    private final String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }


    // EFFECTS: reads source file as a string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    //Effects: reads the saved game from the source file and returns it
    public Game loadGame() throws IOException {
        String jsonData = readFile(source);
        JSONObject gameJson = new JSONObject(jsonData);
        return new Game(gameJson);
    }
}
