package persistence;

import model.Planner;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

// ATTRIBUTIONS: tests were modeled after those in JsonWriterTest in the following WorkRoomApp:
//               https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonWriterTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Planner planner = new Planner();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail();
        } catch (FileNotFoundException e) {
        }
    }

    @Test
    void testWriterEmptyPlanner() {
        try {
            Planner planner = new Planner();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyPlanner.json");
            writer.open();
            writer.write(planner);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyPlanner.json");
            planner = reader.read();
            assertEquals(0, planner.getTagsPage().getTagList().size());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testWriterGeneralPlanner() {
        try {
            Planner planner = new Planner();
            planner.createTag("tag1");
            planner.createTag("tag2");
            planner.createTask("tag1", 2, "task1", "Monday");
            planner.createTask("tag2", 3, "task2", "Tuesday");

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralPlanner.json");
            writer.open();
            writer.write(planner);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralPlanner.json");
            planner = reader.read();
            assertEquals(2, planner.getTagsPage().getTagList().size());
            assertEquals(1, planner.getDueSoon().getTaskList().size());
        } catch (Exception e) {
            fail();
        }
    }



}
