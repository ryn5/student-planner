package model;

import model.tagspage.Tag;

import java.util.ArrayList;
import java.util.List;

// Represents a list of tasks
public abstract class TaskList {
    private List<Task> taskList;

    // EFFECTS: instantiates TaskList with empty array list for storing tasks
    public TaskList() {
        taskList = new ArrayList<>();
    }

    // getter
    public List<Task> getTaskList() {
        return taskList;
    }

    // MODIFIES: this
    // EFFECTS: removes all tasks with given tag from taskList
    public void removeTask(Tag tag) {
        taskList.removeIf(t -> t.getTag().equals(tag));
    }

    // EFFECTS: returns task at given index in taskList
    public Task getTask(int index) {
        return taskList.get(index);
    }

    // MODIFIES: this
    // EFFECTS: removes all tasks with dueIn < 0 from taskList
    public void clearOverdue() {
        taskList.removeIf(t -> t.getDueIn() < 0);
    }

    // MODIFIES: this
    // EFFECTS: subtracts 1 from dueIn of all tasks in taskList
    public void updateDueDates() {
        for (Task t : taskList) {
            t.setDueIn(t.getDueIn() - 1);
        }
    }

}
