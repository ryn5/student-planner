package model.tagspage;

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
}
