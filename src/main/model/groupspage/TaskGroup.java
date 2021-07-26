package model.groupspage;

import model.tagspage.Tag;
import model.Task;
import model.TaskList;

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
