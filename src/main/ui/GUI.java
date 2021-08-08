package ui;

import model.Planner;
import model.Task;
import model.groupspage.TaskGroup;
import model.tagspage.Tag;
import model.todospage.TodoList;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.events.MenuEvents;
import ui.events.TagsEvents;
import ui.events.TodosEvents;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Calendar;
import java.util.Date;

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
    private int currentIndex;
    private JLabel dueSoonLabel;
    private JLabel groupsLabel;

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String JSON_STORE = "./data/planner.json";
    public static final String BUTTON_PRESS_SOUND = "./data/buttonPress.wav";

    // getters
    public Planner getPlanner() {
        return planner;
    }

    public JTabbedPane getPanelContainer() {
        return panelContainer;
    }

    public JList<String> getTaskList() {
        return taskList;
    }

    public JList<String> getTagList() {
        return tagList;
    }

    public DefaultListModel<String> getTasksDLM() {
        return tasksDLM;
    }

    public DefaultListModel<String> getTagsDLM() {
        return tagsDLM;
    }

    public JsonWriter getJsonWriter() {
        return jsonWriter;
    }

    public JsonReader getJsonReader() {
        return jsonReader;
    }

    public String getJsonStore() {
        return JSON_STORE;
    }

    // setters
    public void setPlanner(Planner planner) {
        this.planner = planner;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public GUI() {
        planner = new Planner();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        checkUpdate();
        setupFrame();
        initPanels();
        setVisible(true);
    }

    public void checkUpdate() {
        Date today = Calendar.getInstance().getTime();
        while (!planner.formatDate(planner.getCurrentDate()).equals(planner.formatDate(today))) {
            planner.updateDay();
        }
    }

    private void setupFrame() {
        setSize(WIDTH, HEIGHT);
        setTitle("Planner");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void initPanels() {
        panelContainer = new JTabbedPane();
        currentIndex = 0;
        groupsLabel = new JLabel(); // initialize for actionListener

        panelContainer.add(initMenuPanel(), "Menu");
        panelContainer.add(initTodosPanel(), "Todos");
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
        saveButton.addActionListener(new MenuEvents.SaveEvent(this));
        loadButton.addActionListener(new MenuEvents.LoadEvent(this));

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

    public JPanel initTodosPanel() {
        JPanel todosPanel = new JPanel();
        todosPanel.setLayout(new BoxLayout(todosPanel, BoxLayout.Y_AXIS));
        todosPanel.setBackground(Color.black);

        todosPanel.add(initTodosTopBar());
        todosPanel.add(initTodoList());
        todosPanel.add(initDueSoon());
        todosPanel.add(initTodosBotBar());

        return todosPanel;
    }

    public void refreshTodosPanel() {
        panelContainer.remove(1);
        panelContainer.add(initTodosPanel(), "Todos", 1);
    }

    private JPanel initTodosTopBar() {
        JPanel todosTopBar = new JPanel(new FlowLayout());
        todosTopBar.setBackground(Color.green);
        todosTopBar.setPreferredSize(new Dimension(WIDTH, BAR_HEIGHT));

        JButton prevListButton = new JButton("<");
        JLabel dayLabel = new JLabel(planner.getTodosPage().getAllTodoLists().get(currentIndex).getDayOfWeek());
        JButton nextListButton = new JButton(">");

        todosTopBar.add(prevListButton);
        todosTopBar.add(dayLabel);
        todosTopBar.add(nextListButton);

        prevListButton.addActionListener(new TodosEvents.ScrollListEvent(processScrollList(currentIndex, "prev"),
                this));
        nextListButton.addActionListener(new TodosEvents.ScrollListEvent(processScrollList(currentIndex, "next"),
                this));

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

    private JPanel initTodoList() {
        JPanel todoListPanel = new JPanel();
        todoListPanel.setBackground(Color.blue);
        todoListPanel.setPreferredSize(new Dimension(WIDTH, 350));

        tasksDLM = new DefaultListModel<>();
        TodoList todoList = planner.getTodosPage().getAllTodoLists().get(currentIndex);

        for (Task t : todoList.getTaskList()) {
            tasksDLM.addElement((todoList.getTaskList().indexOf(t) + 1) + ". " + t.getTag().getName() + ": "
                    + t.getText() + " (due in " + t.getDueIn() + " days)");
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

        dueSoonLabel = new JLabel(printDueSoonLabel());
        dueSoonLabel.setBounds(15, 0, WIDTH - 30, 140);
        dueSoon.add(dueSoonLabel);

        return dueSoon;
    }

    public String printDueSoonLabel() {
        String text = "<html> <h3> Tasks due soon: </h3>";

        for (Task t : planner.getDueSoon().getTaskList()) {
            text += "Due in " + t.getDueIn() + " days: " + t.getText() + "<br>";
        }
        text += "</html>";

        return text;
    }

    public void refreshDueSoonLabel() {
        dueSoonLabel.setText(printDueSoonLabel());
    }

    private JPanel initTodosBotBar() {
        JPanel todosBotBar = new JPanel(new FlowLayout());
        todosBotBar.setBackground(Color.orange);
        todosBotBar.setMaximumSize(new Dimension(WIDTH, BAR_HEIGHT));
        todosBotBar.setPreferredSize(new Dimension(WIDTH, BAR_HEIGHT));

        JLabel newTagLabel = new JLabel("Tag:");
        JLabel newDueInLabel = new JLabel("Due in:");
        JLabel newTextLabel = new JLabel("Text:");

        JTextField tagField = new JTextField(3);
        JTextField dueInField = new JTextField(2);
        JTextField textField = new JTextField(4);

        JButton addButton = new JButton("Add");
        JButton removeButton = new JButton("Remove");

        addButton.addActionListener(new TodosEvents.AddTaskEvent(tagField, dueInField, textField, currentIndex, this));
        removeButton.addActionListener(new TodosEvents.RemoveTaskEvent(currentIndex, this));

        todosBotBar.add(newTagLabel);
        todosBotBar.add(tagField);
        todosBotBar.add(newDueInLabel);
        todosBotBar.add(dueInField);
        todosBotBar.add(newTextLabel);
        todosBotBar.add(textField);

        todosBotBar.add(addButton);
        todosBotBar.add(removeButton);

        return todosBotBar;
    }

    private JPanel initGroupsPanel() {
        JPanel groupsPanel = new JPanel(null);
        groupsPanel.setBackground(Color.orange);

        groupsLabel.setText(printGroupsLabel());
        groupsLabel.setBounds(15, 0, WIDTH - 30, 600);
        groupsPanel.add(groupsLabel);

        return groupsPanel;
    }

    public String printGroupsLabel() {
        String text = "<html> <h2> Tasks grouped by tag: </h2>";

        for (TaskGroup tg : planner.getGroupsPage().getTaskGroups()) {
            text += "<h3>" + tg.getTag().getName() + ": </h3>";
            for (Task t : tg.getTaskList()) {
                text += t.getText() + " (due in " + t.getDueIn() + " days) <br>";
            }
        }
        text += "</html>";
        System.out.println(text);
        return text;
    }

    public void refreshGroupsLabel() {
        groupsLabel.setText(printGroupsLabel());
        System.out.println("r groups");
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

        addButton.addActionListener(new TagsEvents.AddTagEvent(newNameField, this));
        removeButton.addActionListener(new TagsEvents.RemoveTagEvent(this));

        tagsBotBar.add(newNameLabel);
        tagsBotBar.add(newNameField);
        tagsBotBar.add(addButton);
        tagsBotBar.add(removeButton);

        return tagsBotBar;
    }

    public void refreshTagsPanel() {
        tagsDLM.clear();
        for (Tag t : planner.getTagsPage().getTagList()) {
            tagsDLM.addElement(t.getName());
        }
    }

    // method was sourced from http://suavesnippets.blogspot.com/2011/06/add-sound-on-jbutton-click-in-java.html
    public static void playSound(String soundFile) {
        try {
            AudioInputStream audioInputStream = AudioSystem
                    .getAudioInputStream(new File(soundFile).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            System.out.println("Error with playing sound.");
        }
    }

}
