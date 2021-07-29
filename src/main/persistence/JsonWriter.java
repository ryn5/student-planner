package persistence;

import model.Planner;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class JsonWriter {
    private PrintWriter writer;
    private String destination;
    
    // EFFECTS: instantiates a writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }
    
    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if file can't be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }
    
    // MODIFIES: this
    // EFFECTS: writes and saves JSON representation of planner to file
    public void write(Planner planner) {
        JSONObject jsonPlanner = planner.toJson();
        saveToFile(jsonPlanner.toString());
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String jsonString) {
        writer.print(jsonString);
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }


}
