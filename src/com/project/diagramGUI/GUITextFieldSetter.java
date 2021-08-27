package com.project.diagramGUI;

import com.project.Phase2CodeGeneration.LexicalAnalyzer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GUITextFieldSetter implements DocumentListener {
    final Method textSetter;
    final JTextField textField;
    final Object theClass;

    public GUITextFieldSetter(Method textSetter, JTextField textField, Object theClass) {
        this.textSetter = textSetter;
        this.textField = textField;
        this.theClass = theClass;
    }

    @Override
    public void insertUpdate(DocumentEvent documentEvent) {
        changedUpdate(documentEvent);
    }

    @Override
    public void removeUpdate(DocumentEvent documentEvent) {
        changedUpdate(documentEvent);
    }

    void inputChecker() {
        if (LexicalAnalyzer.isNameOkayInC(textField.getText()))
            textField.setBackground(Color.GREEN);
        else if (LexicalAnalyzer.isTypeOkayInC(textField.getText()))
            textField.setBackground(Color.getHSBColor((float) 0.75, (float) 0.5, (float) 100.0));
        else
            textField.setBackground(Color.RED);
    }

    @Override
    public void changedUpdate(DocumentEvent documentEvent) {
        inputChecker();
        try {
            textSetter.invoke(theClass, textField.getText());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
