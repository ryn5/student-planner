package model.tagspage;

import exceptions.TagAlreadyExistsException;
import model.Planner;

import java.util.ArrayList;
import java.util.List;

// Represents a list of tags
public class TagList {
    private List<Tag> tagList;

    // EFFECTS: instantiates TagList with
    public TagList() {
        tagList = new ArrayList<>();
    }

    // getter
    public List<Tag> getTagList() {
        return tagList;
    }

    // EFFECTS: returns tag with given name
    public Tag getTag(String tagName) {
        for (Tag t : tagList) {
            if (tagName.equals(t.getName())) {
                return t;
            }
        }
        return null;
    }

    // MODIFIES: this
    // EFFECTS: creates and adds new tag with given name, then creates new TaskGroup in GroupsPage
    public void addTag(String tagName) throws TagAlreadyExistsException {
        Tag newTag = new Tag(tagName);
        for (Tag t : tagList) {
            if (t.getName().equals(tagName)) {
                throw new TagAlreadyExistsException();
            }
        }
        tagList.add(newTag);
    }

    // MODIFIES: this
    // EFFECTS: removes tag with given name from tagList
    public void removeTag(String tagName) {
        tagList.removeIf(t -> t.getName().equals(tagName));
    }

}
