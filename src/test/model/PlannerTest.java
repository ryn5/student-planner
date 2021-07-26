package model;

import exceptions.TagAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

public class PlannerTest {
    Planner planner = new Planner();
    Calendar today;
    Calendar tomorrow;

    @BeforeEach
    void setup() {
        today = Calendar.getInstance();
        tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_YEAR, 1);
    }

    @Test
    void testCreateTag() throws TagAlreadyExistsException {
        Planner.createTag("Tag");

        assertEquals(1, Planner.getTagsPage().getTagList().size());
        assertEquals(1, Planner.getGroupsPage().getTaskGroups().size());
    }

    @Test
    void testDeleteTag() throws TagAlreadyExistsException {
        Planner.createTag("Tag");
        Planner.createTask("Tag", 2, "task1", "Tuesday");
        Planner.createTask("Tag", 3, "task2", "Tuesday");

        Planner.deleteTag(Planner.getTagsPage().getTag("Tag"));
        assertEquals(0, Planner.getTodosPage().getTodoList("Tuesday").getTaskList().size());
        assertEquals(0, Planner.getDueSoon().getTaskList().size());
        assertEquals(0, Planner.getGroupsPage().getTaskGroups().size());
    }

    @Test
    void testCreateTask() throws TagAlreadyExistsException {
        Planner.createTag("Tag");

        Planner.createTask("Tag", 2, "task1", "Tuesday");
        Planner.createTask("Tag", 3, "task2", "Tuesday");

        assertEquals(2, Planner.getTodosPage().getTodoList("Tuesday").getTaskList().size());
        assertEquals(1, Planner.getDueSoon().getTaskList().size());
        assertEquals(2, Planner.getGroupsPage().getTaskGroups().get(0).getTaskList().size());
    }

    @Test
    void testDeleteTask() throws TagAlreadyExistsException {
        Planner.createTag("Tag");
        Planner.createTask("Tag", 2, "task1", "Tuesday");

        Planner.deleteTask(Planner.getDueSoon().getTask(0));

        assertEquals(0, Planner.getTodosPage().getTodoList("Tuesday").getTaskList().size());
        assertEquals(0, Planner.getDueSoon().getTaskList().size());
        assertEquals(0, Planner.getGroupsPage().getTaskGroups().get(0).getTaskList().size());

    }

    @Test
    void testGetDayOfWeekToday() {
        assertEquals("String".getClass(), Planner.getDayOfWeekForCalendar(today).getClass());
    }

    @Test
    void testUpdateDueDates() throws TagAlreadyExistsException {
        Planner.createTag("Tag");
        Planner.createTask("Tag", 2, "task1", "Tuesday");
        Planner.createTask("Tag", 0, "task2", "Tuesday");

        Planner.updateDueDates();

        assertEquals(1, Planner.getDueSoon().getTask(0).getDueIn());
        assertEquals(-1, Planner.getDueSoon().getTask(1).getDueIn());

    }

    @Test
    void testClearOverDueTasks() throws TagAlreadyExistsException {
        Planner.createTag("Tag");
        Planner.createTask("Tag", -1, "task1", "Tuesday");
        Planner.createTask("Tag", 0, "task2", "Tuesday");

        Planner.clearOverdueTasks();

        assertEquals(1, Planner.getDueSoon().getTaskList().size());
    }

    @Test
    void testAddToDueSoon() throws TagAlreadyExistsException {
        Planner.createTag("Tag");
        Planner.createTask("Tag", 3, "task1", "Tuesday");
        Planner.createTask("Tag", 0, "task2", "Tuesday");

        assertEquals(1, Planner.getDueSoon().getTaskList().size());

        Planner.updateDueDates();
        Planner.addToDueSoon();

        assertEquals(2, Planner.getDueSoon().getTaskList().size());
    }

    @Test
    void testUpdateDay() throws TagAlreadyExistsException {
        Planner.createTag("Tag");
        Planner.createTask("Tag", 3, "task1", "Tuesday");
        Planner.createTask("Tag", 0, "task2", "Tuesday");

        Planner.updateDay();

        assertEquals(Planner.getDayOfWeekForCalendar(tomorrow),
                Planner.getTodosPage().getAllTodoLists().get(0).getDayOfWeek());
        assertEquals(1, Planner.getTodosPage().
                getTodoList(Planner.getDayOfWeekForCalendar(tomorrow)).getTaskList().size());
        assertEquals(1, Planner.getDueSoon().getTaskList().size());
        assertEquals(1, Planner.getGroupsPage().getTaskGroups().get(0).getTaskList().size());

    }
}
