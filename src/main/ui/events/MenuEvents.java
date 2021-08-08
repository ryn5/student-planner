package ui.events;

import exceptions.TagAlreadyExistsException;
import model.Planner;
import ui.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

public class MenuEvents {

    public static class TabEvent implements ActionListener {
        private JTabbedPane panelContainer;
        private int index;

        public TabEvent(JTabbedPane panelContainer, int index) {
            this.panelContainer = panelContainer;
            this.index = index;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            panelContainer.setSelectedIndex(index);
        }

    }

    public static class SaveEvent implements ActionListener {
        private GUI gui;

        public SaveEvent(GUI gui) {
            this.gui = gui;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                gui.getJsonWriter().open();
                gui.getJsonWriter().write(gui.getPlanner());
                gui.getJsonWriter().close();
                System.out.println("Successfully saved Planner to" + gui.getJsonStore());
            } catch (FileNotFoundException ignored) {
                ;
            }
        }
    }

    public static class LoadEvent implements ActionListener {
        private GUI gui;

        public LoadEvent(GUI gui) {
            this.gui = gui;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                gui.setPlanner(gui.getJsonReader().read());
                gui.checkUpdate();
                gui.refreshTodosPanel();
                gui.refreshGroupsLabel();
                gui.refreshTagsPanel();
            } catch (Exception ignored) {
                ;
            }
        }
    }

}
