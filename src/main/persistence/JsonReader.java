package persistence;

import exceptions.TagAlreadyExistsException;
import exceptions.TagNotFoundException;
import model.Planner;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

// Represent a reader that reads planner state from JSON data stored in file
// ATTRIBUTIONS: class was modeled after the JsonReader in the following WorkRoomApp:
//               https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReader {
    private String source;

    // EFFECTS: instantiates reader and sets a source file to read
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads source file and returns planner
    public Planner read() throws IOException, TagAlreadyExistsException, TagNotFoundException {
        String jsonData = readFile();                     // file -> String
        JSONObject jsonObject = new JSONObject(jsonData); // String -> json
        return parsePlanner(jsonObject);                  // json -> planner
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile() throws IOException {
        Path sourcePath = Paths.get(source);
        StringBuilder stringBuilder = new StringBuilder();

        for (String s : Files.readAllLines(sourcePath)) {
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }

    // EFFECTS: parses planner from JSON object and returns it
    private Planner parsePlanner(JSONObject jsonObject) throws TagAlreadyExistsException, TagNotFoundException {
        Planner planner = new Planner();

        Date savedDate = planner.parseDate(jsonObject.getString("currentDate"));
        planner.setCurrentDate(savedDate);

        loadTags(planner, jsonObject);
        loadTasks(planner, jsonObject);

        return planner;
    }


    // EFFECTS: parses tags from JSON objects and creates them in planner
    private void loadTags(Planner planner, JSONObject jsonObject) throws TagAlreadyExistsException, TagNotFoundException {
        JSONArray jsonArray = jsonObject.getJSONArray("tags");
        for (Object json : jsonArray) {
            JSONObject jsonTag = (JSONObject) json;
            String name = jsonTag.getString("name");

            planner.createTag(name);
        }
    }

    // EFFECTS: parses tags from JSON objects and creates them in planner
    private void loadTasks(Planner planner, JSONObject jsonObject) throws TagNotFoundException {
        JSONArray jsonArray = jsonObject.getJSONArray("tasks");
        for (Object json : jsonArray) {
            JSONObject jsonTask = (JSONObject) json;

            String tagName = jsonTask.getString("tagName");
            int dueIn = jsonTask.getInt("dueIn");
            String text = jsonTask.getString("text");
            String dayOfWeek = jsonTask.getString("dayOfWeek");

            planner.createTask(tagName, dueIn, text, dayOfWeek);
        }
    }
}
