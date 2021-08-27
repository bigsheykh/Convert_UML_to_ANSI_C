package com.project.diagramGUI;

import com.project.classBaseUML.ClassDiagram;

import javax.swing.*;

public class GUIDiagram extends ClassDiagram<GUIValueType, GUIAttribute, GUIConstructor, GUIMethod, GUIClass>
        implements Runnable {
    private final JPanel panel, showPanelList;
    private final GUIList<GUIClass> classGUIList;
    private boolean showMode;

    public GUIDiagram() {
        super();
        this.panel = new JPanel();
        this.showPanelList = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        classGUIList = new GUIList<>("classes", getClasses(), showPanelList, new GUIClass());

        panel.add(classGUIList);
        panel.add(showPanelList);

        setShowMode(true);
    }

    public JPanel getPanel() {
        return panel;
    }

    public boolean isShowMode() {
        return showMode;
    }

    public void setShowMode(boolean showMode) {
        if (showMode == this.showMode)
            return;
        this.showMode = showMode;
        if (showMode)
            new Thread(this).start();
    }

    @Override
    public void run() {
        panel.updateUI();
        while (isShowMode()) {
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
