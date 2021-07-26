package model.todospage;

import model.Task;
import model.TaskList;

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
