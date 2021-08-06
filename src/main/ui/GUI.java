package ui;

import model.Planner;
import model.Task;
import model.TaskList;
import model.todospage.TodoList;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 620;
    private static final int BAR_HEIGHT = 40;
    private Planner planner;

    public GUI() {
        planner = new Planner();
        setupFrame();
        initPanels();
        setVisible(true);
    }

    private void setupFrame() {
        setSize(WIDTH, HEIGHT);
        setTitle("Planner");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void initPanels() {
        JTabbedPane panelContainer = new JTabbedPane();

        panelContainer.add(initTodosPanel(), "todos"); // first shows on startup
        panelContainer.add(initMenuPanel(), "menu");

        // panelContainer.add(initGroupsPanel(), "todos");
        // panelContainer.add(initTagsPanel(), "todos");

        // cards.show(panelContainer, "menu"); // test panels here
        add(panelContainer);
    }

    private JPanel initMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        JLabel menuLabel = new JLabel("Menu");
        JButton todosButton = new JButton("Todos Page");
        JButton groupsButton = new JButton("Groups Page");
        JButton tagsButton = new JButton("Tags Page");
        JButton saveButton = new JButton("Save State");
        JButton loadButton = new JButton("Load State");

        menuPanel.add(menuLabel);
        menuPanel.add(todosButton);
        menuPanel.add(groupsButton);
        menuPanel.add(tagsButton);
        menuPanel.add(saveButton);
        menuPanel.add(loadButton);

        return menuPanel;
    }


    private JPanel initTodosPanel() {
        JPanel todosPanel = new JPanel();
        todosPanel.setLayout(new BoxLayout(todosPanel, BoxLayout.Y_AXIS));

        todosPanel.add(initTodosTopBar());
        todosPanel.add(initTodoList(0));
        todosPanel.add(initDueSoon());
        todosPanel.add(initTodosBotBar());

        return todosPanel;
    }

    private JPanel initTodosTopBar() {
        JPanel todosTopBar = new JPanel(new FlowLayout());
        todosTopBar.setBackground(Color.green);
        todosTopBar.setMaximumSize(new Dimension(WIDTH, BAR_HEIGHT));
        todosTopBar.setPreferredSize(new Dimension(WIDTH, BAR_HEIGHT));

        JButton prevList = new JButton("<");
        JLabel dayLabel = new JLabel(planner.getFirstDay());
        JButton nextList = new JButton(">");

        todosTopBar.add(prevList);
        todosTopBar.add(dayLabel);
        todosTopBar.add(nextList);

        return todosTopBar;
    }

    private JPanel initTodoList(int index) {
        JPanel todoListPanel = new JPanel();
        todoListPanel.setBackground(Color.blue);
        todoListPanel.setMaximumSize(new Dimension(WIDTH, 350));
        todoListPanel.setPreferredSize(new Dimension(WIDTH, 350));

        TodoList todoList = planner.getTodosPage().getAllTodoLists().get(index);

        DefaultListModel<String> defaultListModel = new DefaultListModel<>();
//        for (Task t : todoList.getTaskList()) {
//            dlm.addElement((todoList.getTaskList().indexOf(t) + 1) + ". " + t.getTag().getName() + ": "
//                    + t.getText() + " (due in " + t.getDueIn() + " days)");
//        }

        for (int i = 0; i < 20; i++) {
            defaultListModel.addElement(Integer.toString(i));
        }

        JList<String> taskList = new JList<>(defaultListModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setSelectedIndex(0);
        //jList.addListSelectionListener();
        taskList.setVisibleRowCount(17);

        JScrollPane listScrollPane = new JScrollPane(taskList);
        listScrollPane.setMaximumSize(new Dimension(WIDTH - 30, 310));
        listScrollPane.setPreferredSize(new Dimension(WIDTH - 30, 310));
        todoListPanel.add(listScrollPane);

        return todoListPanel;
    }

    private JPanel initDueSoon() {
        JPanel dueSoon = new JPanel(null);
        dueSoon.setBackground(Color.red);
        dueSoon.setMaximumSize(new Dimension(WIDTH, 170));
        dueSoon.setPreferredSize(new Dimension(WIDTH, 170));

        JLabel dueSoonLabel = new JLabel(printDueSoon());
        dueSoonLabel.setBounds(5, 5, WIDTH - 30, 140);
        dueSoon.add(dueSoonLabel);

        return dueSoon;
    }

    private String printDueSoon() {
        StringBuilder text = new StringBuilder("Tasks due soon: \n");

        for (Task t : planner.getDueSoon().getTaskList()) {
            text.append("\nDue in ").append(t.getDueIn()).append(" days: ").append(t.getText()).append("\n");
        }
        text.append("\nhello");
        text.append("\nhello");
        text.append("\nhello");
        text.append("\nhello");

        return text.toString();
    }

    private JPanel initTodosBotBar() {
        JPanel todosBotBar = new JPanel(new FlowLayout());
        todosBotBar.setBackground(Color.orange);
        todosBotBar.setMaximumSize(new Dimension(WIDTH, BAR_HEIGHT));
        todosBotBar.setPreferredSize(new Dimension(WIDTH, BAR_HEIGHT));

        JButton addButton = new JButton("Add");
        JButton removeButton = new JButton("Remove");

        JTextField newTagField = new JTextField(3);
        JTextField newDueInField = new JTextField(2);
        JTextField newTextField = new JTextField(4);

        JLabel newTagLabel = new JLabel("Tag:");
        JLabel newDueInLabel = new JLabel("Due in:");
        JLabel newTextLabel = new JLabel("Text:");

        todosBotBar.add(newTagLabel);
        todosBotBar.add(newTagField);
        todosBotBar.add(newDueInLabel);
        todosBotBar.add(newDueInField);
        todosBotBar.add(newTextLabel);
        todosBotBar.add(newTextField);

        todosBotBar.add(addButton);
        todosBotBar.add(removeButton);


        return todosBotBar;
    }


}
