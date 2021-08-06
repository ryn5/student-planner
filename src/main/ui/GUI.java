package ui;

import model.Planner;
import model.Task;
import model.TaskList;
import model.todospage.TodoList;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;
    private static final int BUTTON_WIDTH = 255;
    private static final int BUTTON_HEIGHT = 70;
    private static final int BUTTON_X = 15;
    private Planner planner;
    private CardLayout cards;

    public GUI() {
        planner = new Planner();
        cards = new CardLayout();
        setupFrame();
        initPanels();

    }

    private void setupFrame() {
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Planner");
    }

    public void initPanels() {
        JPanel panelContainer = new JPanel(cards);

        initMenuPanel(panelContainer);
        initTodosPanel(panelContainer);
       // initGroupsPanel(panelContainer);
       // initTagsPanel(panelContainer);

        cards.show(panelContainer, "menu");
        setVisible(true);
    }

    private void initMenuPanel(JPanel panelContainer) {
        JPanel menuPanel = new JPanel(null);

        JLabel menuLabel = new JLabel("Menu");
        menuPanel.add(menuLabel);

        JButton todosButton = new JButton("Todos Page");
        todosButton.setBounds(BUTTON_X, 100, BUTTON_WIDTH, BUTTON_HEIGHT);
        menuPanel.add(todosButton);

        JButton groupsButton = new JButton("Groups Page");
        groupsButton.setBounds(BUTTON_X, 200, BUTTON_WIDTH, BUTTON_HEIGHT);
        menuPanel.add(groupsButton);

        JButton tagsButton = new JButton("Tags Page");
        tagsButton.setBounds(BUTTON_X, 300, BUTTON_WIDTH, BUTTON_HEIGHT);
        menuPanel.add(tagsButton);

        panelContainer.add(menuPanel, "menu");
    }


    private void initTodosPanel(JPanel panelContainer) {
        JPanel todosPanel = new JPanel();
        todosPanel.setLayout(new BoxLayout(todosPanel, BoxLayout.Y_AXIS));

        todosPanel.add(initTodosTopBar());
        todosPanel.add(initTodosLists());
        todosPanel.add(initDueSoon());
        todosPanel.add(initTodosBotBar());

        panelContainer.add(todosPanel);

        setVisible(true);
    }

    private JPanel initTodosTopBar() {
        JPanel topBar = new JPanel(new FlowLayout());
        JButton prevList = new JButton("<");
        JLabel dayLabel = new JLabel(planner.getFirstDay());
        JButton nextList = new JButton(">");

        topBar.add(prevList);
        topBar.add(dayLabel);
        topBar.add(nextList);
        return topBar;
    }

    private JPanel initTodosLists() {
        JPanel todoList = new JPanel(null);

        JLabel todoListLabel = new JLabel(printTodoList(0));
        todoList.add(todoListLabel);

        return todoList;
    }

    private String printTodoList(int index) {
        TodoList todoList = planner.getTodosPage().getAllTodoLists().get(index);
        StringBuilder text = new StringBuilder();

        for (Task t : todoList.getTaskList()) {
            text.append(todoList.getTaskList().indexOf(t) + 1).append(". ").append(t.getTag().getName()).append(": ")
                    .append(t.getText()).append(" (due in ").append(t.getDueIn()).append(" days)\n");
        }
        return text.toString();
    }

    private JPanel initDueSoon() {
        JPanel dueSoon = new JPanel(null);

        JLabel dueSoonLabel = new JLabel(printDueSoon());
        dueSoon.add(dueSoonLabel);

        return dueSoon;
    }

    private String printDueSoon() {
        StringBuilder text = new StringBuilder("Tasks due soon: \n");

        for (Task t : planner.getDueSoon().getTaskList()) {
            text.append("\tDue in ").append(t.getDueIn()).append(" days: ").append(t.getText()).append("\n");
        }
        return text.toString();
    }

    private JPanel initTodosBotBar() {
        JPanel todosBotBar = new JPanel(cards);

        todosBotBar.add(initTodosBotBarDefault());
        todosBotBar.add(initTodosBotBarAdd());
        todosBotBar.add(initTodosBotBarRemove());

        return todosBotBar;
    }

    private JPanel initTodosBotBarDefault() {
        JButton backButton = new JButton("Back");
        JButton addButton = new JButton("Add");
        JButton removeButton = new JButton("Remove");

        JPanel botBarDefault = new JPanel(new FlowLayout());
        botBarDefault.add(backButton);
        botBarDefault.add(addButton);
        botBarDefault.add(removeButton);

        return botBarDefault;
    }

    private JPanel initTodosBotBarAdd() {
        JTextField newTagField = new JTextField("tag");
        JTextField newDueInField = new JTextField("due in");
        JTextField newTextField = new JTextField("description");
        JButton confirmAddButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");

        JPanel botBarAdd = new JPanel(new FlowLayout());
        botBarAdd.add(newTagField);
        botBarAdd.add(newDueInField);
        botBarAdd.add(newTextField);
        botBarAdd.add(confirmAddButton);
        botBarAdd.add(cancelButton);

        return botBarAdd;
    }

    private JPanel initTodosBotBarRemove() {
        JLabel removeIndexLabel = new JLabel("Index:");
        JTextField removeIndexField = new JTextField();
        JButton confirmRemoveButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");

        JPanel botBarRemove = new JPanel(new FlowLayout());
        botBarRemove.add(removeIndexLabel);
        botBarRemove.add(removeIndexField);
        botBarRemove.add(confirmRemoveButton);
        botBarRemove.add(cancelButton);

        return botBarRemove;
    }

}
