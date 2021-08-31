package com.project.diagramGUI;

import com.project.classBaseUML.ValueType;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class GUIValueType extends ValueType implements GUIListItem<GUIValueType>, Runnable {
    private final JSpinner numberGetter;
    private final JPanel panel;
    private final JTextField typeGetter;
    private boolean ended;

    public GUIValueType() {
        super("type", 0);
        panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));
        numberGetter = new JSpinner();
        numberGetter.addChangeListener(new NumberGetter());
        typeGetter = new JTextField();
        try {
            typeGetter.getDocument().addDocumentListener(new GUITextFieldSetter(
                    GUIValueType.class.getMethod("setTypeName", String.class), typeGetter, this));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        panel.setSize(200, 50);
        panel.add(new JLabel("level of pointer:"));
        panel.add(numberGetter);
        panel.add(new JLabel("value type:"));
        panel.add(typeGetter);
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
    public GUIValueType getNewInstance() {
        return new GUIValueType();
    }

    @Override
    public void getDelete() {
        ended = true;
    }

    @Override
    public void run() {
        panel.updateUI();
        numberGetter.setValue(getNumberOfPointer());
        typeGetter.setText(getTypeName());
        while (!ended) {
            if (!panel.isVisible()) {
                numberGetter.setValue(getNumberOfPointer());
                typeGetter.setText(getTypeName());
            } else
                panel.updateUI();
            try {
                Thread.sleep(399);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class NumberGetter implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            if ((int) numberGetter.getValue() < 0)
                numberGetter.setValue(0);
            setNumberOfPointer((int) numberGetter.getValue());
        }
    }

}
