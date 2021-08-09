package ui.events;

import exceptions.TagAlreadyExistsException;
import exceptions.TagNotFoundException;
import model.Planner;
import model.tagspage.Tag;
import ui.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Action event classes for the tags panel
public class TagsEvents {

    public static class AddTagEvent implements ActionListener {
        private JTextField newNameField;
        private GUI gui;

        public AddTagEvent(JTextField newNameField, GUI gui) {
            this.newNameField = newNameField;
            this.gui = gui;
        }

        // MODIFIES: gui, planner
        // EFFECT: creates tag in planner and updates gui to display it
        @Override
        public void actionPerformed(ActionEvent e) {
            GUI.playSound(GUI.BUTTON_PRESS_SOUND);
            if (!newNameField.getText().equals("")) {
                try {
                    gui.getPlanner().createTag(newNameField.getText());
                    gui.getTagsDLM().addElement(newNameField.getText());
                    gui.refreshGroupsLabel();
                } catch (TagAlreadyExistsException | TagNotFoundException ignored) {
                    ;
                }
            }
        }
    }

    public static class RemoveTagEvent implements ActionListener {
        private GUI gui;

        public RemoveTagEvent(GUI gui) {
            this.gui = gui;
        }

        // MODIFIES: gui, planner
        // EFFECT: deletes tag from planner and updates gui to display it
        @Override
        public void actionPerformed(ActionEvent e) {
            GUI.playSound(GUI.BUTTON_PRESS_SOUND);
            try {
                gui.getPlanner().deleteTag(gui.getPlanner().getTagsPage().getTag(gui.getTagList().getSelectedValue()));
                gui.getTagsDLM().removeElement(gui.getTagList().getSelectedValue());

                gui.refreshGroupsLabel();
                gui.refreshTodosPanel();
            } catch (TagNotFoundException ignored) {
                ;
            }
        }
    }

}
