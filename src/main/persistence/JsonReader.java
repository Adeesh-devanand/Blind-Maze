package persistence;

import ui.Game;
import org.json.JSONArray;
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


    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    public Game loadGame() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseGame(jsonObject);
    }

    private Game parseGame(JSONObject jsonObject) {
        JSONObject currMaze = null;
        try {
            currMaze = jsonObject.getJSONObject("currMaze");
        } catch (Exception e) {
            //pass
        }
        JSONArray mazeList = jsonObject.getJSONArray("mazeList");
        boolean gameRunning = jsonObject.getBoolean("gameRunning");
        boolean editMode = jsonObject.getBoolean("editMode");
        int playerVisibility = jsonObject.getInt("playerVisibility");
        return new Game(currMaze, mazeList, gameRunning, editMode, playerVisibility);
    }
}
