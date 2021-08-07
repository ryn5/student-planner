package ui;

import model.Planner;
import model.Task;
import model.groupspage.TaskGroup;
import model.tagspage.Tag;
import model.todospage.TodoList;
import ui.events.MenuEvents;
import ui.events.TagsEvents;
import ui.events.TodosEvents;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 620;
    private static final int BAR_HEIGHT = 40;

    private Planner planner;
    private JTabbedPane panelContainer;
    private JList<String> taskList;
    private JList<String> tagList;
    private DefaultListModel<String> tasksDLM;
    private DefaultListModel<String> tagsDLM;

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
        panelContainer = new JTabbedPane();

        panelContainer.add(initMenuPanel(), "Menu");
        panelContainer.add(initTodosPanel(0), "Todos");
        panelContainer.add(initGroupsPanel(), "Groups");
        panelContainer.add(initTagsPanel(), "Tags");

        add(panelContainer);
    }

    private JPanel initMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.add(initMenuLabelPanel());
        menuPanel.add(initMenuButtonsPanel());

        return menuPanel;
    }

    private JPanel initMenuLabelPanel() {
        JPanel menuLabelPanel = new JPanel();
        menuLabelPanel.setPreferredSize(new Dimension(WIDTH, 70));

        JLabel menuLabel = new JLabel("<html> <h1> Menu </h1> </html>");
        menuLabelPanel.add(menuLabel);

        return menuLabelPanel;
    }

    private JPanel initMenuButtonsPanel() {
        JPanel menuButtonsPanel = new JPanel();
        menuButtonsPanel.setPreferredSize(new Dimension(WIDTH, 500));

        JButton todosButton = new JButton("Todos Page");
        JButton groupsButton = new JButton("Groups Page");
        JButton tagsButton = new JButton("Tags Page");
        JButton saveButton = new JButton("Save State");
        JButton loadButton = new JButton("Load State");

        todosButton.addActionListener(new MenuEvents.TabEvent(panelContainer, 1));
        groupsButton.addActionListener(new MenuEvents.TabEvent(panelContainer, 2));
        tagsButton.addActionListener(new MenuEvents.TabEvent(panelContainer, 3));
        saveButton.addActionListener(new MenuEvents.SaveEvent(planner));
        loadButton.addActionListener(new MenuEvents.LoadEvent(planner));

        todosButton.setPreferredSize(new Dimension(250, 70));
        groupsButton.setPreferredSize(new Dimension(250, 70));
        tagsButton.setPreferredSize(new Dimension(250, 70));
        saveButton.setPreferredSize(new Dimension(250, 70));
        loadButton.setPreferredSize(new Dimension(250, 70));

        menuButtonsPanel.add(todosButton);
        menuButtonsPanel.add(groupsButton);
        menuButtonsPanel.add(tagsButton);
        menuButtonsPanel.add(saveButton);
        menuButtonsPanel.add(loadButton);

        return menuButtonsPanel;
    }

    public JPanel initTodosPanel(int currentIndex) {
        JPanel todosPanel = new JPanel();
        todosPanel.setLayout(new BoxLayout(todosPanel, BoxLayout.Y_AXIS));
        todosPanel.setBackground(Color.black);

        todosPanel.add(initTodosTopBar(currentIndex));
        todosPanel.add(initTodoList(currentIndex));
        todosPanel.add(initDueSoon());
        todosPanel.add(initTodosBotBar(currentIndex));

        return todosPanel;
    }

    private JPanel initTodosTopBar(int currentIndex) {
        JPanel todosTopBar = new JPanel(new FlowLayout());
        todosTopBar.setBackground(Color.green);
        todosTopBar.setPreferredSize(new Dimension(WIDTH, BAR_HEIGHT));

        JButton prevListButton = new JButton("<");
        JLabel dayLabel = new JLabel(planner.getTodosPage().getAllTodoLists().get(currentIndex).getDayOfWeek());
        JButton nextListButton = new JButton(">");

        todosTopBar.add(prevListButton);
        todosTopBar.add(dayLabel);
        todosTopBar.add(nextListButton);

        prevListButton.addActionListener(new TodosEvents.ScrollListEvent(panelContainer,
                processScrollList(currentIndex, "prev"), this));
        nextListButton.addActionListener(new TodosEvents.ScrollListEvent(panelContainer,
                processScrollList(currentIndex, "next"), this));

        return todosTopBar;
    }

    private int processScrollList(int currentIndex, String direction) {
        if (direction.equals("prev")) {
            if (currentIndex == 0) {
                return 6;
            } else {
                return currentIndex - 1;
            }
        } else if (currentIndex == 6) {
            return 0;
        } else {
            return currentIndex + 1;
        }
    }

    private JPanel initTodoList(int index) {
        JPanel todoListPanel = new JPanel();
        todoListPanel.setBackground(Color.blue);
        todoListPanel.setPreferredSize(new Dimension(WIDTH, 350));

        tasksDLM = new DefaultListModel<>();
        TodoList todoList = planner.getTodosPage().getAllTodoLists().get(index);

        for (Task t : todoList.getTaskList()) {
            tasksDLM.addElement((todoList.getTaskList().indexOf(t) + 1) + ". " + t.getTag().getName() + ": "
                    + t.getText() + " (due in " + t.getDueIn() + " days)");
        }

        // test text
        for (int i = 0; i < 20; i++) {
            tasksDLM.addElement(Integer.toString(i));
        }

        taskList = new JList<>(tasksDLM);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setSelectedIndex(0);

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
        dueSoonLabel.setBounds(15, 0, WIDTH - 30, 140);
        dueSoon.add(dueSoonLabel);

        return dueSoon;
    }

    private String printDueSoon() {
        String text = "<html> <h3> Tasks due soon: </h3>";

        for (Task t : planner.getDueSoon().getTaskList()) {
            text += "Due in " + t.getDueIn() + " days: " + t.getText() + "<br>";
        }

        // placeholder
        text += "due in fewf<br>";
        text += "due in wefwefwfwef<br>";
        text += "due in fewf<br>";
        text += "due in fewfw<br>";

        text += "</html>";


        return text;
    }

    private JPanel initTodosBotBar(int currentIndex) {
        JPanel todosBotBar = new JPanel(new FlowLayout());
        todosBotBar.setBackground(Color.orange);
        todosBotBar.setMaximumSize(new Dimension(WIDTH, BAR_HEIGHT));
        todosBotBar.setPreferredSize(new Dimension(WIDTH, BAR_HEIGHT));

        JLabel newTagLabel = new JLabel("Tag:");
        JLabel newDueInLabel = new JLabel("Due in:");
        JLabel newTextLabel = new JLabel("Text:");

        JTextField newTagField = new JTextField(3);
        JTextField newDueInField = new JTextField(2);
        JTextField newTextField = new JTextField(4);

        JButton addButton = new JButton("Add");
        JButton removeButton = new JButton("Remove");

        addButton.addActionListener(new TodosEvents.AddTaskEvent(newTagField, newDueInField, newTextField,
                currentIndex, planner, tasksDLM));
        removeButton.addActionListener(new TodosEvents.RemoveTaskEvent(taskList, currentIndex, planner, tasksDLM));

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

    private JPanel initGroupsPanel() {
        JPanel groupsPanel = new JPanel(null);
        groupsPanel.setBackground(Color.orange);

        JLabel groupsLabel = new JLabel(printGroupsPanel());
        groupsLabel.setBounds(15, 0, WIDTH - 30, 600);
        groupsPanel.add(groupsLabel);

        return groupsPanel;
    }

    private String printGroupsPanel() {
        String text = "<html> <h2> Tasks grouped by tag: </h2>";

        for (TaskGroup tg : planner.getGroupsPage().getTaskGroups()) {
            text += "<h3>" + tg.getTag().getName() + ": </h3>";
            for (Task t : tg.getTaskList()) {
                text += t.getText() + " (due in " + t.getDueIn() + " days) <br>";
            }
        }

        // placeholder
        text += "<h3> tagname 1 </h3>";
        text += "due in wefwefwfwef<br>";
        text += "due in fewf<br>";
        text += "due in fewfw<br>";
        text += "<h3> tagname 1 </h3>";
        text += "due in wefwefwfwef<br>";
        text += "due in fewf<br>";
        text += "due in fewfw<br>";

        text += "</html>";

        return text;
    }

    public JPanel initTagsPanel() {
        JPanel tagsPanel = new JPanel();
        tagsPanel.setLayout(new BoxLayout(tagsPanel, BoxLayout.Y_AXIS));
        tagsPanel.setBackground(Color.black);

        tagsPanel.add(initTagList());
        tagsPanel.add(initTagsBotBar());

        return tagsPanel;
    }

    private JPanel initTagList() {
        JPanel tagListPanel = new JPanel();
        tagListPanel.setBackground(Color.blue);
        tagListPanel.setMaximumSize(new Dimension(WIDTH, HEIGHT - BAR_HEIGHT));
        tagListPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT - BAR_HEIGHT));

        tagsDLM = new DefaultListModel<>();
        for (Tag t : planner.getTagsPage().getTagList()) {
            tagsDLM.addElement(t.getName());
        }

        // placeholder text
        for (int i = 0; i < 30; i++) {
            tagsDLM.addElement(Integer.toString(i));
        }

        tagList = new JList<>(tagsDLM);
        tagList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tagList.setSelectedIndex(0);

        JScrollPane listScrollPane = new JScrollPane(tagList);
        listScrollPane.setMaximumSize(new Dimension(WIDTH - 30, 500));
        listScrollPane.setPreferredSize(new Dimension(WIDTH - 30, 500));
        tagListPanel.add(listScrollPane);

        return tagListPanel;
    }

    private JPanel initTagsBotBar() {
        JPanel tagsBotBar = new JPanel(new FlowLayout());
        tagsBotBar.setBackground(Color.orange);
        tagsBotBar.setMaximumSize(new Dimension(WIDTH, BAR_HEIGHT));
        tagsBotBar.setPreferredSize(new Dimension(WIDTH, BAR_HEIGHT));

        JLabel newNameLabel = new JLabel("New tag name:");
        JTextField newNameField = new JTextField(11);
        JButton addButton = new JButton("Add");
        JButton removeButton = new JButton("Remove");

        addButton.addActionListener(new TagsEvents.AddTagEvent(newNameField, planner, tagsDLM));
        removeButton.addActionListener(new TagsEvents.RemoveTagEvent(tagList, planner, tagsDLM));

        tagsBotBar.add(newNameLabel);
        tagsBotBar.add(newNameField);
        tagsBotBar.add(addButton);
        tagsBotBar.add(removeButton);

        return tagsBotBar;
    }

}
