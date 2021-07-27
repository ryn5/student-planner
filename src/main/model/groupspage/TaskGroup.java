package model.groupspage;

import model.tagspage.Tag;
import model.Task;
import model.TaskList;

// Represents a group of tasks with the same tag
public class TaskGroup extends TaskList {
    private Tag tag;

    // EFFECTS: instantiates new TaskList and sets tag
    public TaskGroup(Tag tag) {
        super();
        this.tag = tag;
    }

    // getter
    public Tag getTag() {
        return tag;
    }


}
