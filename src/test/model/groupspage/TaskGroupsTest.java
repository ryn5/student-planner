package model.groupspage;

import model.Task;
import model.tagspage.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskGroupsTest {
    TaskGroups taskGroups;
    Tag tag1;
    Tag tag2;
    Task task1;
    Task task2;
    Task task3;

    @BeforeEach
    void setup() {
        taskGroups = new TaskGroups();
        tag1 = new Tag("Tag 1");
        tag2 = new Tag("Tag 2");
        task1 = new Task(tag1, -1, "task 1");
        task2 = new Task(tag1, 0, "task 2");
        task3 = new Task(tag2, -2, "task 3");

    }

    @Test
    void testAddTaskGroup() {
        taskGroups.addTaskGroup(tag1);
        assertEquals(1, taskGroups.getTaskGroups().size());
        assertEquals(tag1, taskGroups.getTaskGroups().get(0).getTag());

        taskGroups.addTaskGroup(tag2);
        assertEquals(2, taskGroups.getTaskGroups().size());
        assertEquals(tag2, taskGroups.getTaskGroups().get(1).getTag());

    }

//    @Test
//    void testRemoveTaskGroup () {
//        taskGroups.addTaskGroup(tag1);
//        taskGroups.addTaskGroup(tag2);
//
//        taskGroups.removeTaskGroup(tag1);
//        assertEquals(1, taskGroups.getTaskGroups().size());
//        assertEquals(tag2, taskGroups.getTaskGroups().get(0).getTag());
//    }

    @Test
    void testAddTaskToCorrectTaskGroup() {
        taskGroups.addTaskGroup(tag1);
        taskGroups.addTaskGroup(tag2);

        taskGroups.addTaskToCorrectTaskGroup(task3);

        assertEquals(1, taskGroups.getTaskGroups().get(1).getTaskList().size());
    }

    @Test
    void testClearOverdueTasks () {
        taskGroups.addTaskGroup(tag1);
        taskGroups.addTaskGroup(tag2);

        taskGroups.getTaskGroups().get(0).getTaskList().add(task1);
        taskGroups.getTaskGroups().get(0).getTaskList().add(task2);
        taskGroups.getTaskGroups().get(1).getTaskList().add(task3);

        taskGroups.clearOverdueTasks();

        assertEquals(1, taskGroups.getTaskGroups().get(0).getTaskList().size());
        assertEquals(0, taskGroups.getTaskGroups().get(1).getTaskList().size());
        assertEquals("task 2", taskGroups.getTaskGroups().get(0).getTaskList().get(0).getText());
    }

}
