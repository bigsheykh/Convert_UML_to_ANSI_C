package com.project.diagramGUI;

import com.project.classBaseUML.ClassConstructor;

import javax.swing.*;
import java.util.Vector;

public class GUIConstructor extends ClassConstructor<GUIValueType, GUIAttribute>
        implements GUIListItem<GUIConstructor>, Runnable {

    private final JPanel panel, showPanelList;
    private final GUIList<GUIAttribute> attributeGUIList;
    private boolean ended;

    public GUIConstructor() {
        super(new Vector<>());
        ended = false;
        showPanelList = new JPanel();
        attributeGUIList = new GUIList<>("constructor", getParams(), showPanelList, new GUIAttribute());
        panel = new JPanel();
        panel.setLayout(new BoxLayout(getShowPanel(), BoxLayout.X_AXIS));

        panel.add(attributeGUIList);
        panel.add(showPanelList);

        new Thread(this).start();
    }


    @Override
    public String toString() {
        return getShowName();
    }

    @Override
    public JPanel getShowPanel() {
        return panel;
    }

    @Override
    public GUIConstructor getNewInstance() {
        return new GUIConstructor();
    }

    @Override
    public void getDelete() {
        ended = true;
        attributeGUIList.getDelete();
    }

    @Override
    public void run() {
        panel.updateUI();
        while (!ended) {
            if (panel.isVisible())
                panel.updateUI();
            try {
                Thread.sleep(399);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
