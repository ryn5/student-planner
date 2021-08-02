package ui;

import exceptions.TagAlreadyExistsException;
import model.Planner;
import model.Task;
import model.TaskList;
import model.groupspage.TaskGroup;
import model.tagspage.Tag;
import model.todospage.TodoList;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

// Student planner application
//  ATTRIBUTIONS: parts of this code were modeled after the following TellerApp:
//                https://github.students.cs.ubc.ca/CPSC210/TellerApp
public class PlannerApp {
    private Planner planner;
    private Scanner input;
    private boolean running;
    private Date today;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String JSON_STORE = "./data/planner.json";

    // EFFECTS: instantiates Planner and runs the application
    public PlannerApp() {
        runPlanner();
    }

    // MODIFIES: this
    // EFFECTS: runs the application by opening the main menu and processes user input
    private void runPlanner() {
        init();

        while (running) {
            while (!planner.formatDate(planner.getCurrentDate()).equals(planner.formatDate(today))) {
                planner.updateDay();
            }

            displayMenu();
            String command = input.next();

            if (command.equals("quit")) {
                running = false;
            } else {
                processMenuCommand(command);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes the application
    private void init() {
        planner = new Planner();
        input = new Scanner(System.in);
        running = true;
        today = Calendar.getInstance().getTime();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // EFFECTS: displays main menu
    private void displayMenu() {
        System.out.println("Select a page: ");
        System.out.println("\ttodos -> Todos Page");
        System.out.println("\tgroups -> Groups Page");
        System.out.println("\ttags -> Tags Page");
        System.out.println("\tsave -> Save Planner");
        System.out.println("\tload -> Load Planner");
        System.out.println("\tquit -> Quit");
    }

    // MODIFIES: this
    // EFFECTS: processes user command in main menu to open selected page
    private void processMenuCommand(String command) {
        switch (command) {
            case "todos":
                runTodosPage(0);
                break;
            case "groups":
                runGroupsPage();
                break;
            case "tags":
                runTagsPage();
                break;
            case "save":
                savePlanner();
                break;
            case "load":
                loadPlanner();
                break;
            default:
                System.out.println("Invalid input.  Please try again.");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: displays first to-do list in Todos Page, displays due soon, and takes user input
    private void runTodosPage(int index) {
        printTodoList(index);
        emptyLine();

        printDueSoon();
        emptyLine();

        System.out.println("Select an action: ");
        System.out.println("\tadd -> add new task");
        System.out.println("\tdel -> delete a task");
        System.out.println("\tnext -> next list");
        System.out.println("\tprev -> previous list");
        System.out.println("\tback -> return to menu");

        processTodosCommand(input.next(), index);
    }

    // MODIFIES: this, Planner
    // EFFECTS: processes user command in Todos Page
    private void processTodosCommand(String command, int currentIndex) {
        switch (command) {
            case "add":
                processAddTask(currentIndex);
                break;
            case "del":
                processDelTask(currentIndex);
                break;
            case "next":
                processNextList(currentIndex);
                break;
            case "prev":
                processPrevList(currentIndex);
                break;
            case "back":
                ;
                break;
            default:
                System.out.println("Invalid input.  Please try again.");
                runTodosPage(currentIndex);
                break;
        }
    }

    // MODIFIES: this, Planner
    // EFFECTS: creates new task in current to-do list and rest of Planner
    private void processAddTask(int currentIndex) {
        System.out.println("Enter new task tag, number of days until due date, and a description, separately: ");

        String newTaskTag = input.next();
        int newTaskDueIn = Integer.parseInt(input.next());
        String newTaskText = input.nextLine();

        planner.createTask(newTaskTag, newTaskDueIn, newTaskText,
                planner.getTodosPage().getAllTodoLists().get(currentIndex).getDayOfWeek());

        runTodosPage(currentIndex);
    }

    // MODIFIES: this, Planner
    // EFFECTS: deletes all instances of task at given index in current to-do list from Planner
    private void processDelTask(int currentIndex) {
        System.out.println("Enter number of task to be deleted: ");

        int taskNumber = Integer.parseInt(input.next());
        TodoList todoList = planner.getTodosPage().getAllTodoLists().get(currentIndex);

        if (taskNumber > todoList.getTaskList().size()) {
            System.out.println("Task not found; please try again.");
        } else {
            planner.deleteTask(todoList.getTask(taskNumber - 1));
        }
        runTodosPage(currentIndex);
    }

    // MODIFIES: this
    // EFFECTS: displays next to-do list
    private void processNextList(int currentIndex) {
        if (currentIndex == 6) {
            runTodosPage(0);
        } else {
            runTodosPage(currentIndex + 1);
        }
    }

    // MODIFIES: this
    // EFFECTS: displays previous to-do list
    private void processPrevList(int currentIndex) {
        if (currentIndex == 0) {
            runTodosPage(6);
        } else {
            runTodosPage(currentIndex - 1);
        }
    }

    // EFFECTS: prints to-do list at given index in Todos Page
    private void printTodoList(int index) {
        TodoList todoList = planner.getTodosPage().getAllTodoLists().get(index);
        System.out.println(todoList.getDayOfWeek() + ":");

        for (Task t : todoList.getTaskList()) {
            String printStatement = " " + (todoList.getTaskList().indexOf(t) + 1) + ". " + t.getTag().getName() + ": "
                    + t.getText() + " (due in " + t.getDueIn() + " days)";
            System.out.println(printStatement);
        }
    }

    // EFFECTS: prints all tasks that are due soon
    private void printDueSoon() {
        System.out.println("Tasks due soon: ");
        TaskList taskList = planner.getDueSoon();

        for (Task t : taskList.getTaskList()) {
            String printStatement = "\tDue in " + t.getDueIn() + " days: " + t.getText();
            System.out.println(printStatement);
        }
    }

    // MODIFIES: this
    // EFFECTS: displays tasks grouped by tag and takes user input
    private void runGroupsPage() {
        System.out.println("Tasks grouped by tag: ");
        emptyLine();

        for (TaskGroup tg : planner.getGroupsPage().getTaskGroups()) {
            System.out.println(tg.getTag().getName() + ": ");
            for (Task t : tg.getTaskList()) {
                String printStatement = "\t" + t.getText() + " (due in " + t.getDueIn() + " days)";
                System.out.println(printStatement);
            }
        }
        emptyLine();
        System.out.println("Enter \"back\" for menu.");
        processGroupsCommand(input.next());
    }

    // MODIFIES: this
    // EFFECTS: processes user command in Groups Page
    private void processGroupsCommand(String command) {
        if (command.equals("back")) {
            ;
        } else {
            System.out.println("Invalid input.  Please try again.");
            runGroupsPage();
        }
    }

    // MODIFIES: this
    // EFFECTS: displays tags and takes user input
    private void runTagsPage() {
        System.out.println("Tags: ");

        for (Tag t : planner.getTagsPage().getTagList()) {
            System.out.println("\t" + t.getName());
        }
        emptyLine();

        System.out.println("Select an action: ");
        System.out.println("\tadd -> add new tag");
        System.out.println("\tdel -> delete a tag");
        System.out.println("\tback -> return to menu");

        processTagsCommand(input.next());
    }

    // MODIFIES: this, Planner
    // EFFECTS: processes user command in Tags Page
    private void processTagsCommand(String command) {
        switch (command) {
            case "add":
                processAddTag();
                break;
            case "del":
                System.out.println("Enter name of tag to be deleted: ");
                planner.deleteTag(planner.getTagsPage().getTag(input.next()));
                runTagsPage();
                break;
            case "back":
                ;
                break;
            default:
                System.out.println("Invalid input.  Please try again.");
                runTagsPage();
                break;
        }
    }

    private void processAddTag() {
        System.out.println("Enter new tag name: ");
        try {
            planner.createTag(input.next());
        } catch (TagAlreadyExistsException e) {
            System.out.println("Tag with name already exists.");
        } finally {
            runTagsPage();
        }
    }

    // EFFECTS: saves planner to file
    private void savePlanner() {
        try {
            jsonWriter.open();
            jsonWriter.write(planner);
            jsonWriter.close();
            System.out.println("Successfully saved Planner to" + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this, Planner
    // EFFECTS: loads workroom from file
    private void loadPlanner() {
        try {
            planner = jsonReader.read();
            System.out.println("Successfully loaded Planner from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Couldn't read from file: " + JSON_STORE);
        } catch (TagAlreadyExistsException e) {
            System.out.println("Loaded duplicate tag");
        }
    }


    // EFFECTS: prints empty line
    private void emptyLine() {
        System.out.println("");
    }

}
