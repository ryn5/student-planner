package model.groupspage;

import model.Task;
import model.tagspage.Tag;

import java.util.ArrayList;
import java.util.List;

public class TaskGroups {
    private List<TaskGroup> taskGroups;

    // EFFECTS: instantiates TaskGroups with empty array list for storing TaskGroups
    public TaskGroups() {
        taskGroups = new ArrayList<>();
    }

    // getter
    public List<TaskGroup> getTaskGroups() {
        return taskGroups;
    }

    // MODIFIES: this
    // EFFECTS: creates new task group with given tag and adds it to taskGroups
    public void addTaskGroup(Tag tag) {
        TaskGroup newTaskGroup = new TaskGroup(tag);
        taskGroups.add(newTaskGroup);
    }

    // MODIFIES: this
    // EFFECTS: removes task group with given tag from taskGroups
    public void removeTaskGroup(Tag tag) {
        taskGroups.removeIf(tg -> tg.getTag().equals(tag));
    }

    // MODIFIES: this
    // EFFECTS: adds given task to task group with matching tag
    public void addTaskToCorrectTaskGroup(Task task) {
        Tag taskTag = task.getTag();
        for (TaskGroup tg : taskGroups) {
            if (tg.getTag().equals(taskTag)) {
                tg.getTaskList().add(task);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: clears overdue tasks from each task group
    public void clearOverdueTasks() {
        for (TaskGroup tg : taskGroups) {
            tg.clearOverdue();
        }
    }
}
