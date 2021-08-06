package model.tagspage;

import exceptions.TagAlreadyExistsException;
import exceptions.TagNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TagListTest {
    TagList tagList;

    @BeforeEach
    void setup() {
        tagList = new TagList();
    }

    @Test
    void testAddTag() {
        try {
            tagList.addTag("Tag 1");
        } catch (TagAlreadyExistsException e) {
            fail();
        }
        assertEquals(1, tagList.getTagList().size());
        assertEquals("Tag 1", tagList.getTagList().get(0).getName());
    }

    @Test
    void testAddTagAlreadyExistsException() {
        try {
            tagList.addTag("Tag 1");
        } catch (TagAlreadyExistsException e) {
            fail();
        }
        try {
            tagList.addTag("Tag 1");
            fail();
        } catch (TagAlreadyExistsException e) {
            e.getMessage();
        }
    }

    @Test
    void testRemoveTag() throws TagAlreadyExistsException {
        tagList.addTag("Tag 1");
        tagList.addTag("Tag 2");
        tagList.removeTag("Tag 1");

        assertEquals(1, tagList.getTagList().size());
        assertEquals("Tag 2", tagList.getTagList().get(0).getName());
    }

    @Test
    void testGetTag() throws TagAlreadyExistsException, TagNotFoundException {
        tagList.addTag("Tag 1");
        tagList.addTag("Tag 2");
        assertEquals("Tag 2", tagList.getTag("Tag 2").getName());
    }

    @Test
    void testGetTagNull() throws TagNotFoundException {
        assertNull(tagList.getTag("Tag 1"));
    }
}
