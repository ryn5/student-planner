package ui.events;

import model.Planner;
import ui.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TodosEvents {

    public static class ScrollListEvent implements ActionListener {
        private JTabbedPane panelContainer;
        private int newIndex;
        private GUI gui;

        public ScrollListEvent(JTabbedPane panelContainer, int newIndex, GUI gui) {
            this.panelContainer = panelContainer;
            this.newIndex = newIndex;
            this.gui = gui;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            panelContainer.remove(1);
            panelContainer.add(gui.initTodosPanel(newIndex), "Todos", 1);
            panelContainer.setSelectedIndex(1);
        }
    }

    public static class AddTaskEvent implements ActionListener {
        private JTextField newTagField;
        private JTextField newDueInField;
        private JTextField newTextField;
        private int currentIndex;
        private Planner planner;
        private DefaultListModel<String> tasksDLM;

        public AddTaskEvent(JTextField newTagField, JTextField newDueInField, JTextField newTextField,
                            int currentIndex, Planner planner, DefaultListModel<String> tasksDLM) {
            this.newTagField = newTagField;
            this.newDueInField = newDueInField;
            this.newTextField = newTextField;
            this.currentIndex = currentIndex;
            this.planner = planner;
            this.tasksDLM = tasksDLM;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String dayOfWeek = planner.getTodosPage().getAllTodoLists().get(currentIndex).getDayOfWeek();
            planner.createTask(newTagField.getText(), Integer.parseInt(newDueInField.getText()),
                    newTextField.getText(), dayOfWeek);
            tasksDLM.addElement((tasksDLM.size() + 1) + ". " + newTagField.getText() + ": " + newTextField.getText()
                    + " (due in " + newDueInField.getText() + " days)");
        }
    }

    public static class RemoveTaskEvent implements ActionListener {
        private JList<String> taskList;
        private int currentIndex;
        private Planner planner;
        private DefaultListModel<String> tasksDLM;

        public RemoveTaskEvent(JList<String> taskList, int currentIndex, Planner planner,
                               DefaultListModel<String> tasksDLM) {
            this.taskList = taskList;
            this.currentIndex = currentIndex;
            this.planner = planner;
            this.tasksDLM = tasksDLM;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int taskIndex = taskList.getSelectedIndex();
            planner.deleteTask(planner.getTodosPage().getAllTodoLists().get(currentIndex).getTask(taskIndex));
            tasksDLM.remove(taskList.getSelectedIndex());
        }
    }
}
