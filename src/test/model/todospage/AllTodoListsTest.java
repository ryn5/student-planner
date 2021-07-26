package model.todospage;

import model.Task;
import model.tagspage.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AllTodoListsTest {
    AllTodoLists allTodoLists;

    @BeforeEach
    void setup() {
        allTodoLists = new AllTodoLists();
    }

    @Test
    void testAddTodoList() {
        allTodoLists.addTodoList("Tuesday");
        assertEquals(8, allTodoLists.getAllTodoLists().size());
        assertEquals("Tuesday", allTodoLists.getAllTodoLists().get(7).getDayOfWeek());
    }

    @Test
    void testRemoveTodoList() {
        allTodoLists.removeTodoList("Tuesday");
        assertEquals(6, allTodoLists.getAllTodoLists().size());
        assertNull(allTodoLists.getTodoList("Tuesday"));
    }

    @Test
    void testGetTodoList() {
        assertEquals("Tuesday", allTodoLists.getTodoList("Tuesday").getDayOfWeek());
    }

    @Test
    void testAddTaskToCorrectDayOfWeek() {
        Tag tag = new Tag("Tag");
        Task task = new Task(tag, 3, "task");
        allTodoLists.addTaskToCorrectDayOfWeek(task, "Tuesday");
        assertEquals(task, allTodoLists.getTodoList("Tuesday").getTaskList().get(0));
    }
}
