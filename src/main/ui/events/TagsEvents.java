package ui.events;

import exceptions.TagAlreadyExistsException;
import exceptions.TagNotFoundException;
import model.Planner;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TagsEvents {

    public static class AddTagEvent implements ActionListener {
        private JTextField newNameField;
        private Planner planner;
        private DefaultListModel<String> tagsDLM;


        public AddTagEvent(JTextField newNameField, Planner planner, DefaultListModel<String> tagsDLM) {
            this.newNameField = newNameField;
            this.planner = planner;
            this.tagsDLM = tagsDLM;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                planner.createTag(newNameField.getText());
                tagsDLM.addElement(newNameField.getText());
            } catch (TagAlreadyExistsException ignored) {
                ;
            }
        }
    }

    public static class RemoveTagEvent implements ActionListener {
        private final JList<String> tagList;
        private Planner planner;
        private DefaultListModel<String> tagsDLM;

        public RemoveTagEvent(JList<String> tagList, Planner planner, DefaultListModel<String> tagsDLM) {
            this.tagList = tagList;
            this.planner = planner;
            this.tagsDLM = tagsDLM;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                planner.deleteTag(planner.getTagsPage().getTag(tagList.getSelectedValue()));
                tagsDLM.removeElement(tagList.getSelectedValue());
            } catch (TagNotFoundException ignored) {
                ;
            }
        }
    }

}
