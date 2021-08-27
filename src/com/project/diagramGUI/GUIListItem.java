package com.project.diagramGUI;

import javax.swing.*;

public interface GUIListItem<T extends GUIListItem> {
    JPanel getShowPanel();

    T getNewInstance();

    void getDelete();
}
