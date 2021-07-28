package persistence;

import model.Planner;

import java.io.IOException;

public class JsonReader {
    private String source;

    // EFFECTS: instantiates reader and sets a source file to read
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads source file and returns planner
    public Planner read() throws IOException {
        String jsonData = readFile(source);
        return null;
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        return null;
    }
}
