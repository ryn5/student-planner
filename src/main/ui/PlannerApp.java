package ui;

import exceptions.TagAlreadyExistsException;
import model.Planner;
import model.Task;
import model.TaskList;
import model.groupspage.TaskGroup;
import model.tagspage.Tag;
import model.todospage.TodoList;

import java.util.Calendar;
import java.util.Scanner;

// Student planner application
//  ATTRIBUTIONS: parts of this code were modeled after the following TellerApp:
//                https://github.students.cs.ubc.ca/CPSC210/TellerApp
public class PlannerApp {
    private Planner planner;
    private Scanner input;
    private boolean running;
    private String today;

    // EFFECTS: instantiates Planner and runs the application
    public PlannerApp() {
        runPlanner();
    }

    // MODIFIES: this
    // EFFECTS: runs the application by opening the main menu and processes user input
    private void runPlanner() {
        init();

        while (running) {
            if (Planner.getDayOfWeekForCalendar(Calendar.getInstance()) != today) {
                Planner.updateDay();
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
        today = Planner.getDayOfWeekForCalendar(Calendar.getInstance());
        running = true;
    }

    // EFFECTS: displays main menu
    private void displayMenu() {
        System.out.println("Select a page: ");
        System.out.println("\ttodos -> Todos Page");
        System.out.println("\tgroups -> Groups Page");
        System.out.println("\ttags -> Tags Page");
        System.out.println("\tquit -> Quit");
    }

    // MODIFIES: this
    // EFFECTS: processes user command in main menu to open selected page
    private void processMenuCommand(String command) {
        if (command.equals("todos")) {
            runTodosPage(0);
        } else if (command.equals("groups")) {
            runGroupsPage();
        } else if (command.equals("tags")) {
            runTagsPage();
        } else {
            System.out.println("Invalid input.  Please try again.");
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
        if (command.equals("add")) {
            processAddTask(currentIndex);
        } else if (command.equals("del")) {
            processDelTask(currentIndex);
        } else if (command.equals("next")) {
            if (currentIndex == 6) {
                runTodosPage(0);
            } else {
                runTodosPage(currentIndex + 1);
            }
        } else if (command.equals("prev")) {
            if (currentIndex == 0) {
                runTodosPage(6);
            } else {
                runTodosPage(currentIndex - 1);
            }
        } else if (command.equals("back")) {
            ;
        } else {
            System.out.println("Invalid input.  Please try again.");
            runTodosPage(currentIndex);
        }
    }

    // MODIFIES: this, Planner
    // EFFECTS: creates new task in current to-do list and rest of Planner
    private void processAddTask(int currentIndex) {
        System.out.println("Enter new task tag, number of days until due date, and a description, separately: ");

        String newTaskTag = input.next();
        int newTaskDueIn = Integer.parseInt(input.next());
        String newTaskText = input.next();

        Planner.createTask(newTaskTag, newTaskDueIn, newTaskText,
                Planner.getTodosPage().getAllTodoLists().get(currentIndex).getDayOfWeek());

        runTodosPage(currentIndex);
    }

    // MODIFIES: this, Planner
    // EFFECTS: deletes all instances of task at given index in current to-do list from Planner
    private void processDelTask(int currentIndex) {
        System.out.println("Enter number of task to be deleted: ");

        int taskNumber = Integer.parseInt(input.next());
        Planner.deleteTask(Planner.getTodosPage().getAllTodoLists().get(currentIndex).getTask(taskNumber - 1));

        runTodosPage(currentIndex);
    }

    // EFFECTS: prints to-do list at given index in Todos Page
    private void printTodoList(int index) {
        TodoList todoList = Planner.getTodosPage().getAllTodoLists().get(index);
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
        TaskList taskList = Planner.getDueSoon();

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

        for (TaskGroup tg : Planner.getGroupsPage().getTaskGroups()) {
            System.out.println(tg.getTag().getName() + ": ");
            for (Task t : tg.getTaskList()) {
                String printStatement = "\t" + t.getText() + " (due in " + t.getDueIn() + " days)";
                System.out.println(printStatement);
                emptyLine();
            }
        }
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
        emptyLine();

        for (Tag t : Planner.getTagsPage().getTagList()) {
            System.out.println(t.getName());
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
        if (command.equals("add")) {
            System.out.println("Enter new tag name: ");
            try {
                Planner.createTag(input.next());
            } catch (TagAlreadyExistsException e) {
                System.out.println("Tag with name already exists.");
            } finally {
                runTagsPage();
            }
        } else if (command.equals("del")) {
            System.out.println("Enter name of tag to be deleted: ");
            Planner.deleteTag(Planner.getTagsPage().getTag(input.next()));
            runTagsPage();
        } else if (command.equals("back")) {
            ;
        } else {
            System.out.println("Invalid input.  Please try again.");
            runTagsPage();
        }
    }

    // EFFECTS: prints empty line
    private void emptyLine() {
        System.out.println("");
    }

}
