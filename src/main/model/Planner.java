package model;

import exceptions.TagAlreadyExistsException;
import model.groupspage.TaskGroup;
import model.groupspage.TaskGroups;
import model.tagspage.Tag;
import model.tagspage.TagList;
import model.todospage.DueSoon;
import model.todospage.AllTodoLists;
import model.todospage.TodoList;

import java.text.SimpleDateFormat;
import java.util.Calendar;

// Represents a student planner with pages for to-do lists, tasks that are due soon, tags, and tasks grouped by tag
public class Planner {
    private static AllTodoLists todosPage;
    private static DueSoon dueSoon;
    private static TaskGroups groupsPage;
    private static TagList tagsPage;

    // EFFECTS: instantiates Planner with newly instantiated AllTodoLists, DueSoon, TaskGroups, and TagList, and sets
    //          current date as first day in TodosPage

    public Planner() {
        todosPage = new AllTodoLists();
        dueSoon = new DueSoon();
        groupsPage = new TaskGroups();
        tagsPage = new TagList();

        setFirstDay();
    }

    // getters
    public static AllTodoLists getTodosPage() {
        return todosPage;
    }

    public static DueSoon getDueSoon() {
        return dueSoon;
    }

    public static TaskGroups getGroupsPage() {
        return groupsPage;
    }

    public static TagList getTagsPage() {
        return tagsPage;
    }


    // MODIFIES: this
    // EFFECTS: creates and adds new task to todosPage in TodoList with given dayOfWeek, groupsPage in TaskGroup with
    //          given tag, and dueSoon if given dueIn <= 2
    public static void createTask(String tagName, int dueIn, String text, String dayOfWeek) {
        Task newTask = new Task(getTagsPage().getTag(tagName), dueIn, text);
        todosPage.addTaskToCorrectDayOfWeek(newTask, dayOfWeek);
        groupsPage.addTaskToCorrectTaskGroup(newTask);
        if (dueIn <= 2) {
            dueSoon.getTaskList().add(newTask);
        }
    }

    // MODIFIES: this
    // EFFECTS: removes given task from todosPage, dueSoon, and groupsPage
    public static void deleteTask(Task task) {
        for (TodoList tl : todosPage.getAllTodoLists()) {
            tl.getTaskList().remove(task);
        }
        dueSoon.getTaskList().remove(task);
        for (TaskGroup tg : groupsPage.getTaskGroups()) {
            tg.getTaskList().remove(task);
        }
    }

    // MODIFIES: this
    // EFFECTS: create new tag and add it to tagsPage, then create TaskGroup in groupsPage with new tag
    public static void createTag(String name) throws TagAlreadyExistsException {
        tagsPage.addTag(name);
        groupsPage.addTaskGroup(tagsPage.getTag(name));
    }

    // MODIFIES: this
    // EFFECTS: deletes all tasks in todosPage and dueSoon with given tag, then removes tag from tagsPage and
    //          removes TaskGroup with tag from groupsPage
    public static void deleteTag(Tag tag) {
        for (TodoList tl : todosPage.getAllTodoLists()) {
            tl.removeTask(tag);
        }
        dueSoon.removeTask(tag);

        tagsPage.removeTag(tag.getName());
        groupsPage.removeTaskGroup(tag);
    }

    // EFFECTS: returns dayOfWeek in String format for given calendar
    public static String getDayOfWeekForCalendar(Calendar c) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEEE");
        return dateFormat.format(c.getTime());
    }

    // EFFECTS: returns dayOfWeek of first TodoList in TodosPage
    public static String getFirstDay() {
        return todosPage.getAllTodoLists().get(0).getDayOfWeek();
    }

    // MODIFIES: this
    // EFFECTS: cycles TodoLists in allTodoLists so that the first TodoList dayOfWeek matches today's dayOfWeek
    public static void setFirstDay() {
        Calendar c = Calendar.getInstance();
        while (!(getDayOfWeekForCalendar(c).equals(getFirstDay()))) {
            cycleTodoLists();
        }
    }

    // MODIFIES: this
    // EFFECTS: removes oldest TodoList from TodosPage and adds new one to the end with same dayOfWeek
    public static void cycleTodoLists() {
        todosPage.addTodoList(todosPage.getAllTodoLists().get(0).getDayOfWeek());
        todosPage.getAllTodoLists().remove(0);
    }

    // MODIFIES: this
    // EFFECTS: subtracts 1 day from all due dates in planner
    public static void updateDueDates() {
        for (TaskGroup tg : groupsPage.getTaskGroups()) {
            tg.updateDueDates();
        }
    }

    // MODIFIES: this
    // EFFECTS: removes all tasks with dueIn < 0 from planner
    public static void clearOverdueTasks() {
        for (TodoList tl : todosPage.getAllTodoLists()) {
            tl.clearOverdue();
        }
        dueSoon.clearOverdue();
        groupsPage.clearOverdueTasks();
    }

    // MODIFIES: this
    // EFFECTS: adds all tasks with dueIn == 2 to DueSoon
    public static void addToDueSoon() {
        for (TaskGroup tg : groupsPage.getTaskGroups()) {
            for (Task t : tg.getTaskList()) {
                if (t.getDueIn() == 2) {
                    dueSoon.getTaskList().add(t);
                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: cycles TodoLists in TodosPage, subtracts 1 from all due dates, and then removes overdue tasks
    public static void updateDay() {
        cycleTodoLists();
        updateDueDates();
        clearOverdueTasks();
        addToDueSoon();
    }


}
