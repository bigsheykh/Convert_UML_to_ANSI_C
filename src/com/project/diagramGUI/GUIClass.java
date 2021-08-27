package com.project.diagramGUI;

import com.project.Main;
import com.project.classBaseUML.ClassStructure;

import javax.swing.*;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.StringWriter;

public class GUIClass extends ClassStructure<GUIValueType, GUIAttribute, GUIConstructor, GUIMethod>
        implements GUIListItem<GUIClass>, Runnable {
    private final JPanel panel, showPanelList, basicInfoPanel;
    private final GUIList<GUIAttribute> attributeGUIList;
    private final GUIList<GUIMethod> methodGUIList;
    private final GUIList<GUIConstructor> constructorGUIList;
    private final JTextField nameGetter, superGetter;
    private final JCheckBox destructorBox, superBox;
    private boolean ended;

    public GUIClass() {
        super("className");
        ended = false;
        panel = new JPanel();
        panel.setLayout(new BoxLayout(getShowPanel(), BoxLayout.X_AXIS));
        showPanelList = new JPanel();
        attributeGUIList = new GUIList<>("attributes", getAttributes(), showPanelList, new GUIAttribute());
        methodGUIList = new GUIList<>("methods", getMethods(), showPanelList, new GUIMethod());
        constructorGUIList = new GUIList<>("constructors", getConstructors(), showPanelList, new GUIConstructor());

        basicInfoPanel = new JPanel(new GridLayout(6, 1));
        nameGetter = new JTextField();
        try {
            nameGetter.getDocument().addDocumentListener(new GUITextFieldSetter(
                    ClassStructure.class.getMethod("setName", String.class), nameGetter, this));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        destructorBox = new JCheckBox("have destructor", false);
        destructorBox.addItemListener(new DestructorListener());

        superGetter = new JTextField();
        try {
            superGetter.getDocument().addDocumentListener(new GUITextFieldSetter(
                    ClassStructure.class.getMethod("setSuperClass", String.class), superGetter, this));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        superGetter.setEditable(false);

        superBox = new JCheckBox("super class", false);
        superBox.addItemListener(new superItemListener());

        basicInfoPanel.add(new JLabel("class name:"));
        basicInfoPanel.add(nameGetter);
        basicInfoPanel.add(destructorBox);
        basicInfoPanel.add(superBox);
        basicInfoPanel.add(new JLabel("super class name:"));
        basicInfoPanel.add(superGetter);

        panel.add(basicInfoPanel);
        panel.add(attributeGUIList);
        panel.add(constructorGUIList);
        panel.add(methodGUIList);
        panel.add(showPanelList);
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
    public GUIClass getNewInstance() {
        return new GUIClass();
    }

    @Override
    public void getDelete() {
        ended = true;
        attributeGUIList.getDelete();
        constructorGUIList.getDelete();
        methodGUIList.getDelete();
    }

    @Override
    public void run() {
        nameGetter.setText(getName());
        superGetter.setText(getSuperClass());
        panel.updateUI();
        basicInfoPanel.updateUI();
        while (!ended) {
            if (!panel.isVisible()) {
                nameGetter.setText(getName());
                superGetter.setText(getSuperClass());
            } else {
                panel.updateUI();
                basicInfoPanel.updateUI();
            }
            try {
                Thread.sleep(399);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private class superItemListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent itemEvent) {
            Main.document = Main.documentBuilder.newDocument();
            Main.document.appendChild(getElementDocument());
            DOMSource domSource = new DOMSource(Main.document);
            StringWriter writer = new StringWriter();
            StreamResult streamResult = new StreamResult(writer);
            try {
                Main.transformer.transform(domSource, streamResult);
            } catch (TransformerException e) {
                e.printStackTrace();
                return;
            }

            if (itemEvent.getStateChange() == ItemEvent.SELECTED)
                superGetter.setEditable(true);
            else {
                superGetter.setEditable(false);
                setSuperClass("null");
                superGetter.setText("null");
            }

        }
    }

    private class DestructorListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent itemEvent) {

            setHavingDestructor(itemEvent.getStateChange() == ItemEvent.SELECTED);

        }
    }

}
