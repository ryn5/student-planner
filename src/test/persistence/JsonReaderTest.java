package persistence;

import exceptions.TagAlreadyExistsException;
import model.Planner;
import model.tagspage.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/missingFile.json");
        try {
            Planner planner = reader.read();
            fail();
        } catch (IOException e) {
        } catch (TagAlreadyExistsException e) {
            fail();
        }
    }
}
