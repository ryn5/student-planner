package model.todospage;

import model.TaskList;

// Represents a list of tasks associated with a day of the week
public class TodoList extends TaskList {
    String dayOfWeek;

    // EFFECTS: instantiates new TaskList and sets dayOfWeek
    public TodoList(String dayOfWeek) {
        super();
        this.dayOfWeek = dayOfWeek;
    }

    // getter
    public String getDayOfWeek() {
        return dayOfWeek;
    }


}
