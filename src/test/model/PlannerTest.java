package model;

import exceptions.TagAlreadyExistsException;
import exceptions.TagNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
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
    void testGetCurrentDate() {
        assertEquals(planner.formatDate(Calendar.getInstance().getTime()), planner.formatDate(planner.getCurrentDate()));
    }

    @Test
    void testCreateTag() throws TagAlreadyExistsException, TagNotFoundException {
        planner.createTag("Tag");

        assertEquals(1, planner.getTagsPage().getTagList().size());
        assertEquals(1, planner.getGroupsPage().getTaskGroups().size());
    }

    @Test
    void testDeleteTag() throws TagAlreadyExistsException, TagNotFoundException {
        planner.createTag("Tag");
        planner.createTask("Tag", 2, "task1", "Tuesday");
        planner.createTask("Tag", 3, "task2", "Tuesday");

        planner.deleteTag(planner.getTagsPage().getTag("Tag"));
        assertEquals(0, planner.getTodosPage().getTodoList("Tuesday").getTaskList().size());
        assertEquals(0, planner.getDueSoon().getTaskList().size());
        assertEquals(0, planner.getGroupsPage().getTaskGroups().size());
    }

    @Test
    void testDeleteTagNotFound() throws TagAlreadyExistsException, TagNotFoundException {
        planner.createTag("Tag");
        planner.createTask("Tag", 2, "task1", "Tuesday");
        planner.createTask("Tag", 3, "task2", "Tuesday");

        try {
            planner.deleteTag(planner.getTagsPage().getTag("Tag2"));
        } catch (TagNotFoundException e) {
            ;
        } finally {
            assertEquals(2, planner.getTodosPage().getTodoList("Tuesday").getTaskList().size());
            assertEquals(1, planner.getDueSoon().getTaskList().size());
            assertEquals(1, planner.getGroupsPage().getTaskGroups().size());
        }
    }

    @Test
    void testDeleteTagNull() throws TagAlreadyExistsException, TagNotFoundException {
        planner.createTag("Tag");
        planner.createTask("Tag", 2, "task1", "Tuesday");
        planner.createTask("Tag", 3, "task2", "Tuesday");

        planner.deleteTag(null);

        assertEquals(2, planner.getTodosPage().getTodoList("Tuesday").getTaskList().size());
        assertEquals(1, planner.getDueSoon().getTaskList().size());
        assertEquals(1, planner.getGroupsPage().getTaskGroups().size());
    }

    @Test
    void testCreateTask() throws TagAlreadyExistsException, TagNotFoundException {
        planner.createTag("Tag");

        planner.createTask("Tag", 2, "task1", "Tuesday");
        planner.createTask("Tag", 3, "task2", "Tuesday");

        assertEquals(2, planner.getTodosPage().getTodoList("Tuesday").getTaskList().size());
        assertEquals(1, planner.getDueSoon().getTaskList().size());
        assertEquals(2, planner.getGroupsPage().getTaskGroups().get(0).getTaskList().size());
    }

    @Test
    void testDeleteTask() throws TagAlreadyExistsException, TagNotFoundException {
        planner.createTag("Tag");
        planner.createTask("Tag", 2, "task1", "Tuesday");

        planner.deleteTask(planner.getDueSoon().getTask(0));

        assertEquals(0, planner.getTodosPage().getTodoList("Tuesday").getTaskList().size());
        assertEquals(0, planner.getDueSoon().getTaskList().size());
        assertEquals(0, planner.getGroupsPage().getTaskGroups().get(0).getTaskList().size());
    }

    @Test
    void testSetupFirstDay() {
        planner.cycleTodoLists();
        assertEquals(planner.getDayOfWeekForCalendar(tomorrow), planner.getFirstDay());
        planner.setupFirstDay();
        assertEquals(planner.getDayOfWeekForCalendar(today), planner.getFirstDay());
    }

    @Test
    void testUpdateDueDates() throws TagAlreadyExistsException, TagNotFoundException {
        planner.createTag("Tag");
        planner.createTask("Tag", 2, "task1", "Tuesday");
        planner.createTask("Tag", 0, "task2", "Tuesday");

        planner.updateDueDates();

        assertEquals(1, planner.getDueSoon().getTask(0).getDueIn());
        assertEquals(-1, planner.getDueSoon().getTask(1).getDueIn());

    }

    @Test
    void testClearOverDueTasks() throws TagAlreadyExistsException, TagNotFoundException {
        planner.createTag("Tag");
        planner.createTask("Tag", -1, "task1", "Tuesday");
        planner.createTask("Tag", 0, "task2", "Tuesday");

        planner.clearOverdueTasks();

        assertEquals(1, planner.getDueSoon().getTaskList().size());
    }

    @Test
    void testAddToDueSoon() throws TagAlreadyExistsException, TagNotFoundException {
        planner.createTag("Tag");
        planner.createTask("Tag", 3, "task1", "Tuesday");
        planner.createTask("Tag", 0, "task2", "Tuesday");

        assertEquals(1, planner.getDueSoon().getTaskList().size());

        planner.updateDueDates();
        planner.addNewDueSoon();

        assertEquals(2, planner.getDueSoon().getTaskList().size());
    }

    @Test
    void testUpdateDay() throws TagAlreadyExistsException, TagNotFoundException {
        planner.createTag("Tag");
        planner.createTask("Tag", 3, "task1", planner.getDayOfWeekForCalendar(tomorrow));
        planner.createTask("Tag", 0, "task2", planner.getDayOfWeekForCalendar(tomorrow));

        planner.updateDay();

        assertEquals(planner.getDayOfWeekForCalendar(tomorrow),
                planner.getTodosPage().getAllTodoLists().get(0).getDayOfWeek());
        assertEquals(1, planner.getTodosPage().
                getTodoList(planner.getDayOfWeekForCalendar(tomorrow)).getTaskList().size());
        assertEquals(1, planner.getDueSoon().getTaskList().size());
        assertEquals(1, planner.getGroupsPage().getTaskGroups().get(0).getTaskList().size());
    }

    @Test
    void testParseDateParseError() {
        assertNull(planner.parseDate("not a date"));
    }


}
