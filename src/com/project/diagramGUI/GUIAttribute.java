package com.project.diagramGUI;

import com.project.classBaseUML.ClassAttribute;

import javax.swing.*;
import java.awt.*;

public class GUIAttribute extends ClassAttribute<GUIValueType> implements GUIListItem<GUIAttribute>, Runnable {
    private final JPanel panel;
    private final JTextField nameGetter;
    private boolean ended;

    public GUIAttribute() {
        setName("name");
        ended = false;
        panel = new JPanel();
        panel.setLayout(new BoxLayout(getShowPanel(), BoxLayout.Y_AXIS));
        JPanel namePanel = new JPanel(new GridLayout(1, 2));
        nameGetter = new JTextField();
        try {
            nameGetter.getDocument().addDocumentListener(new GUITextFieldSetter(
                    GUIAttribute.class.getMethod("setName", String.class), nameGetter, this));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        namePanel.setSize(200, 25);
        namePanel.add(new JLabel("name:"));
        namePanel.add(nameGetter);
        panel.add(namePanel);
        setValueType(new GUIValueType());
        panel.add(getValueType().getShowPanel());
        new Thread(this).start();
    }

    @Override
    public String toString() {
        return getShowName();
    }

    @Override
    public JPanel getShowPanel() {
        panel.setVisible(true);
        return panel;
    }

    @Override
    public GUIAttribute getNewInstance() {
        return new GUIAttribute();
    }

    @Override
    public void getDelete() {
        getValueType().getDelete();
        ended = true;
    }

    @Override
    public void run() {
        nameGetter.setText(getName());
        panel.updateUI();

        while (!ended) {
            getValueType().getShowPanel().setVisible(panel.isVisible());
            if (!panel.isVisible())
                nameGetter.setText(getName());
            else
                panel.updateUI();
            try {
                Thread.sleep(399);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
