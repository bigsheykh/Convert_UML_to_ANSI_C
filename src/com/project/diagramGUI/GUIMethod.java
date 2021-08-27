package com.project.diagramGUI;

import com.project.classBaseUML.ClassMethod;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class GUIMethod extends ClassMethod<GUIValueType, GUIAttribute> implements GUIListItem<GUIMethod>, Runnable {
    private final JPanel panel, showPanelList, attributePanel;
    private final GUIList<GUIAttribute> attributeGUIList;
    private final JTextField nameGetter;
    private boolean ended;

    public GUIMethod() {
        super("method", new GUIValueType());
        setParams(new Vector<>());
        ended = false;
        showPanelList = new JPanel();
        attributeGUIList = new GUIList<>("method", getParams(), showPanelList, new GUIAttribute());
        panel = new JPanel();
        panel.setLayout(new BoxLayout(getShowPanel(), BoxLayout.X_AXIS));

        attributePanel = new JPanel();
        attributePanel.setLayout(new BoxLayout(attributePanel, BoxLayout.Y_AXIS));
        JPanel namePanel = new JPanel(new GridLayout(1, 2));
        nameGetter = new JTextField();
        try {
            nameGetter.getDocument().addDocumentListener(new GUITextFieldSetter(
                    GUIMethod.class.getMethod("setName", String.class), nameGetter, this));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        namePanel.setSize(200, 25);
        namePanel.add(new JLabel("method name:"));
        namePanel.add(nameGetter);
        attributePanel.add(namePanel);
        setReturnValueType(new GUIValueType());
        attributePanel.add(getReturnValueType().getShowPanel());

        panel.add(attributePanel);
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
    public GUIMethod getNewInstance() {
        return new GUIMethod();
    }

    @Override
    public void getDelete() {
        attributeGUIList.getDelete();
        getReturnValueType().getDelete();
        ended = true;
    }

    @Override
    public void run() {
        nameGetter.setText(getName());
        panel.updateUI();
        attributePanel.updateUI();
        while (!ended) {
            getReturnValueType().getShowPanel().setVisible(panel.isVisible());
            if (!panel.isVisible())
                nameGetter.setText(getName());
            else {
                panel.updateUI();
                attributePanel.updateUI();
            }
            try {
                Thread.sleep(399);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
