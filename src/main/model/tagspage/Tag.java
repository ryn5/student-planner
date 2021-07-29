package model.tagspage;

import org.json.JSONObject;

// A tag that can be attached to tasks
public class Tag {
    private final String name;

    // EFFECTS: instantiates a Tag and sets unchangeable name
    public Tag(String name) {
        this.name = name;
    }

    // getter
    public String getName() {
        return name;
    }

    public JSONObject toJson() {
        JSONObject jsonTag = new JSONObject();
        jsonTag.put("name", name);
        return jsonTag;
    }
}
