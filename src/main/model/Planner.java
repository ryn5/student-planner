package model;

import exceptions.TagAlreadyExistsException;
import exceptions.TagNotFoundException;
import model.groupspage.TaskGroup;
import model.groupspage.TaskGroups;
import model.tagspage.Tag;
import model.tagspage.TagList;
import model.todospage.DueSoon;
import model.todospage.AllTodoLists;
import model.todospage.TodoList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// Represents a student planner with pages for to-do lists, tasks that are due soon, tags, and tasks grouped by tag
public class Planner {
    private AllTodoLists todosPage;
    private DueSoon dueSoon;
    private TaskGroups groupsPage;
    private TagList tagsPage;
    private Date currentDate;

    // EFFECTS: instantiates Planner with newly instantiated AllTodoLists, DueSoon, TaskGroups, and TagList, and sets
    //          current date as first day in TodosPage

    public Planner() {
        todosPage = new AllTodoLists();
        dueSoon = new DueSoon();
        groupsPage = new TaskGroups();
        tagsPage = new TagList();
        currentDate = Calendar.getInstance().getTime();
        setupFirstDay();
    }

    // getters
    public AllTodoLists getTodosPage() {
        return todosPage;
    }

    public DueSoon getDueSoon() {
        return dueSoon;
    }

    public TaskGroups getGroupsPage() {
        return groupsPage;
    }

    public TagList getTagsPage() {
        return tagsPage;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    // setter
    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    // MODIFIES: this
    // EFFECTS: creates and adds new task to todosPage in TodoList with given dayOfWeek, groupsPage in TaskGroup with
    //          given tag, and dueSoon if given dueIn <= 2
    public void createTask(String tagName, int dueIn, String text, String dayOfWeek) throws TagNotFoundException {
        Task newTask = new Task(getTagsPage().getTag(tagName), dueIn, text);
        todosPage.addTaskToCorrectDayOfWeek(newTask, dayOfWeek);
        groupsPage.addTaskToCorrectTaskGroup(newTask);
        if (dueIn <= 2) {
            dueSoon.getTaskList().add(newTask);
        }
    }

    // MODIFIES: this
    // EFFECTS: removes given task from todosPage, dueSoon, and groupsPage
    public void deleteTask(Task task) {
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
    public void createTag(String name) throws TagAlreadyExistsException, TagNotFoundException {
        tagsPage.addTag(name);
        groupsPage.addTaskGroup(tagsPage.getTag(name));
    }

    // MODIFIES: this
    // EFFECTS: deletes all tasks in todosPage and dueSoon with given tag, then removes tag from tagsPage and
    //          removes TaskGroup with tag from groupsPage
    public void deleteTag(Tag tag) {
        if (tag != null) {
            for (TodoList tl : todosPage.getAllTodoLists()) {
                tl.removeTask(tag);
            }
            dueSoon.removeTask(tag);
            groupsPage.removeTaskGroup(tag);

            tagsPage.removeTag(tag.getName());
        }
    }

    // EFFECTS: returns dayOfWeek in String format for given calendar
    public String getDayOfWeekForCalendar(Calendar c) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEEE");
        return dateFormat.format(c.getTime());
    }

    // EFFECTS: returns dayOfWeek of first TodoList in TodosPage
    public String getFirstDay() {
        return todosPage.getAllTodoLists().get(0).getDayOfWeek();
    }

    // MODIFIES: this
    // EFFECTS: cycles TodoLists in allTodoLists so that the first TodoList dayOfWeek matches today's dayOfWeek
    public void setupFirstDay() {
        Calendar c = Calendar.getInstance();
        while (!(getDayOfWeekForCalendar(c).equals(getFirstDay()))) {
            cycleTodoLists();
        }
    }

    // MODIFIES: this
    // EFFECTS: removes oldest TodoList from TodosPage and adds new one to the end with same dayOfWeek
    public void cycleTodoLists() {
        todosPage.addTodoList(todosPage.getAllTodoLists().get(0).getDayOfWeek());
        todosPage.getAllTodoLists().remove(0);
    }

    // MODIFIES: this
    // EFFECTS: subtracts 1 day from all due dates in planner
    public void updateDueDates() {
        for (TaskGroup tg : groupsPage.getTaskGroups()) {
            tg.updateDueDates();
        }
    }

    // MODIFIES: this
    // EFFECTS: removes all tasks with dueIn < 0 from planner
    public void clearOverdueTasks() {
        for (TodoList tl : todosPage.getAllTodoLists()) {
            tl.clearOverdue();
        }
        dueSoon.clearOverdue();
        groupsPage.clearOverdueTasks();
    }

    // MODIFIES: this
    // EFFECTS: adds all tasks with dueIn == 2 to DueSoon
    public void addNewDueSoon() {
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
    public void updateDay() {
        cycleTodoLists();
        updateDueDates();
        clearOverdueTasks();
        addNewDueSoon();
        updateCurrentDate();
    }

    // MODIFIES: this
    // EFFECTS: updates currentDate by 1 day
    private void updateCurrentDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DAY_OF_MONTH, 1);
        currentDate = c.getTime();
    }

    // EFFECTS: returns date as a formatted string
    public String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(date);
    }

    // EFFECTS: returns date from a formatted string
    public Date parseDate(String formattedDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return simpleDateFormat.parse(formattedDate);
        } catch (ParseException e) {
            System.out.println("Date could not be parsed");
        }
        return null;
    }


    // EFFECTS: creates JSON representation of planner state
    public JSONObject toJson() {
        JSONObject jsonPlanner = new JSONObject();

        jsonPlanner.put("currentDate", formatDate(currentDate));
        jsonPlanner.put("tags", tagsToJson());
        jsonPlanner.put("tasks", tasksToJson());

        return jsonPlanner;
    }

    // EFFECTS: creates JSON representation of all tags in planner
    private JSONArray tagsToJson() {
        JSONArray tagsJson = new JSONArray();

        for (Tag t : tagsPage.getTagList()) {
            tagsJson.put(t.toJson());
        }
        return tagsJson;
    }

    // EFFECTS: creates JSON representation of all tasks in planner
    private JSONArray tasksToJson() {
        JSONArray tasksJson = new JSONArray();

        for (TodoList tl : getTodosPage().getAllTodoLists()) {
            for (Task t : tl.getTaskList()) {
                tasksJson.put(t.toJson(tl.getDayOfWeek()));
            }
        }
        return tasksJson;
    }

}
