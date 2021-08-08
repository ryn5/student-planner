package ui.events;

import exceptions.TagNotFoundException;
import model.Planner;
import ui.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TodosEvents {

    public static class ScrollListEvent implements ActionListener {
        private int newIndex;
        private GUI gui;

        public ScrollListEvent(int newIndex, GUI gui) {
            this.newIndex = newIndex;
            this.gui = gui;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            gui.setCurrentIndex(newIndex);
            gui.getPanelContainer().remove(1);
            gui.getPanelContainer().add(gui.initTodosPanel(), "Todos", 1);
            gui.getPanelContainer().setSelectedIndex(1);
        }
    }

    public static class AddTaskEvent implements ActionListener {
        private JTextField newTagField;
        private JTextField newDueInField;
        private JTextField newTextField;
        private int currentIndex;
        private GUI gui;

        public AddTaskEvent(JTextField newTagField, JTextField newDueInField, JTextField newTextField,
                            int currentIndex, GUI gui) {
            this.newTagField = newTagField;
            this.newDueInField = newDueInField;
            this.newTextField = newTextField;
            this.currentIndex = currentIndex;
            this.gui = gui;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String dayOfWeek = gui.getPlanner().getTodosPage().getAllTodoLists().get(currentIndex).getDayOfWeek();
            try {
                gui.getPlanner().createTask(newTagField.getText(), Integer.parseInt(newDueInField.getText()),
                        newTextField.getText(), dayOfWeek);
                gui.getTasksDLM().addElement((gui.getTasksDLM().size() + 1) + ". " + newTagField.getText() + ": "
                        + newTextField.getText() + " (due in " + newDueInField.getText() + " days)");

                gui.refreshDueSoonLabel();
                gui.refreshGroupsLabel();
            } catch (TagNotFoundException ignored) {
                ;
            }
        }
    }

    public static class RemoveTaskEvent implements ActionListener {
        private GUI gui;
        private int currentIndex;

        public RemoveTaskEvent(int currentIndex, GUI gui) {
            this.currentIndex = currentIndex;
            this.gui = gui;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int taskIndex = gui.getTaskList().getSelectedIndex();

            gui.getPlanner().deleteTask(gui.getPlanner().getTodosPage().getAllTodoLists().get(currentIndex)
                    .getTask(taskIndex));
            gui.getTasksDLM().remove(gui.getTaskList().getSelectedIndex());

            gui.refreshDueSoonLabel();
            gui.refreshGroupsLabel();
        }
    }
}

