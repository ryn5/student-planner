package ui;

import exceptions.TagAlreadyExistsException;
import model.Planner;
import model.Task;
import model.TaskList;
import model.groupspage.TaskGroup;
import model.tagspage.Tag;
import model.todospage.TodoList;

import java.util.Locale;
import java.util.Scanner;

// Student planner application
//  ATTRIBUTIONS: parts of code were modeled after the following TellerApp:
//                https://github.students.cs.ubc.ca/CPSC210/TellerApp
public class PlannerApp {
    private Scanner input;
    private Planner planner;
    private boolean running;

    public PlannerApp() {
        planner = new Planner();
        runPlanner();
    }

    private void runPlanner() {
        running = true;
        String command;
        input = new Scanner(System.in);

        while (running) {
            displayMenu();
            command = input.next().toLowerCase();

            if (command.equals("q")) {
                running = false;
            } else {
                processMenuCommand(command);
            }
        }
    }

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

    private void displayMenu() {
        System.out.println("Select a page: ");
        System.out.println("\ttodos -> Todos Page");
        System.out.println("\tgroups -> Groups Page");
        System.out.println("\ttags -> Tags Page");
        System.out.println("\tquit -> Quit");
    }

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
            displayMenu();
            processMenuCommand(input.next());
        } else {
            System.out.println("Invalid input.  Please try again.");
            runTodosPage(currentIndex);
        }
    }

    private void processAddTask(int currentIndex) {
        System.out.println("Enter new task tag, number of days until due date, and a description, separately: ");
        String newTaskTag = input.next();
        int newTaskDueIn = Integer.parseInt(input.next());
        String newTaskText = input.next();
        Planner.createTask(newTaskTag, newTaskDueIn, newTaskText,
                Planner.getTodosPage().getAllTodoLists().get(currentIndex).getDayOfWeek());
        runTodosPage(currentIndex);
    }

    private void processDelTask(int currentIndex) {
        System.out.println("Enter number of task to be deleted: ");
        int taskNumber = Integer.parseInt(input.next());
        Planner.deleteTask(Planner.getTodosPage().getAllTodoLists().get(currentIndex).getTask(taskNumber - 1));
        runTodosPage(currentIndex);
    }

    private void printTodoList(int index) {
        TodoList todoList = Planner.getTodosPage().getAllTodoLists().get(index);
        System.out.println(todoList.getDayOfWeek() + ":");
        for (Task t : todoList.getTaskList()) {
            String printStatement = " " + (todoList.getTaskList().indexOf(t) + 1) + ". " + t.getTag().getName() + ": "
                    + t.getText() + " (due in " + t.getDueIn() + " days)";
            System.out.println(printStatement);
        }
    }

    private void printDueSoon() {
        System.out.println("Tasks due soon: ");
        TaskList taskList = Planner.getDueSoon();
        for (Task t : taskList.getTaskList()) {
            String printStatement = "\tDue in " + t.getDueIn() + " days: " + t.getText();
            System.out.println(printStatement);
        }
    }

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

    private void processGroupsCommand(String command) {
        if (command.equals("back")) {
            displayMenu();
            processMenuCommand(input.next());
        } else {
            System.out.println("Invalid input.  Please try again.");
            runTagsPage();
        }
    }

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
            displayMenu();
            processMenuCommand(input.next());
        } else {
            System.out.println("Invalid input.  Please try again.");
            runTagsPage();
        }
    }

    private void emptyLine() {
        System.out.println("");
    }

}
