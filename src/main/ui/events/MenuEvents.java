package ui.events;

import model.Planner;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        private Planner planner;

        public SaveEvent(Planner planner) {
            this.planner = planner;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ;
        }
    }

    public static class LoadEvent implements ActionListener {
        private Planner planner;

        public LoadEvent(Planner planner) {
            this.planner = planner;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ;
        }
    }

}
