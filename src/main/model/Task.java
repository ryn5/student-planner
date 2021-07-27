package model;

import model.tagspage.Tag;

// Represents a task with a tag, number of days until its due date, and a description
public class Task {
    private final Tag tag;
    private int dueIn;
    private final String text;

    // EFFECTS: instantiates Task and sets tag, dueIn, and text
    public Task(Tag tag, int dueIn, String text) {
        this.tag = tag;
        this.dueIn = dueIn;
        this.text = text;
    }

    // getters
    public Tag getTag() {
        return tag;
    }

    public int getDueIn() {
        return dueIn;
    }

    public String getText() {
        return text;
    }

    // setters
    public void setDueIn(int dueIn) {
        this.dueIn = dueIn;
    }
}
