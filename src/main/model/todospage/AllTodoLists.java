package model.todospage;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class AllTodoLists {
    private List<TodoList> allTodoLists;

    // EFFECTS: instantiates AllTodoLists with an array list of 7 new TodoLists, one for each day of the week
    public AllTodoLists() {
        allTodoLists = new ArrayList<>();

        TodoList todoListSun = new TodoList("Sunday");
        TodoList todoListMon = new TodoList("Monday");
        TodoList todoListTue = new TodoList("Tuesday");
        TodoList todoListWed = new TodoList("Wednesday");
        TodoList todoListThu = new TodoList("Thursday");
        TodoList todoListFri = new TodoList("Friday");
        TodoList todoListSat = new TodoList("Saturday");

        allTodoLists.add(todoListSun);
        allTodoLists.add(todoListMon);
        allTodoLists.add(todoListTue);
        allTodoLists.add(todoListWed);
        allTodoLists.add(todoListThu);
        allTodoLists.add(todoListFri);
        allTodoLists.add(todoListSat);

    }

    // getter
    public List<TodoList> getAllTodoLists() {
        return allTodoLists;
    }

    // MODIFIES: this
    // EFFECTS: creates new TodoList with given dayOfWeek and adds it to TodoLists
    public void addTodoList(String dayOfWeek) {
        TodoList newTodoList = new TodoList(dayOfWeek);
        allTodoLists.add(newTodoList);
    }

    // MODIFIES: this
    // EFFECTS: removes TodoList with given dayOfWeek from TodoLists
    public void removeTodoList(String dayOfWeek) {
        allTodoLists.removeIf(tl -> tl.getDayOfWeek().equals(dayOfWeek));
    }

    // EFFECTS: returns TodoList in TodoLists with given dayOfWeek
    public TodoList getTodoList(String dayOfWeek) {
        for (TodoList tl: allTodoLists) {
            if (tl.getDayOfWeek().equals(dayOfWeek)) {
                return tl;
            }
        }
        return null;
    }

    // MODIFIES: this
    // EFFECTS: adds given task to TodoList with given dayOfWeek
    public void addTaskToCorrectDayOfWeek(Task task, String dayOfWeek) {
        for (TodoList tl : allTodoLists) {
            if (tl.getDayOfWeek().equals(dayOfWeek)) {
                tl.getTaskList().add(task);
            }
        }
    }
}
