package com.project.diagramGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class GUIList<T extends GUIListItem<T>> extends JPanel implements Runnable {
    final Vector<T> list;
    final JPanel showPanel, listPanel;
    final JButton addItem, deleteItem;
    final String title;
    final JList<T> jList;
    final T oneSample;
    JScrollPane scrollPane;
    boolean ended;

    public GUIList(String title, Vector<T> list, JPanel showPanel, T oneSample) {
        this.title = title;
        this.list = list;
        this.showPanel = showPanel;
        this.oneSample = oneSample;
        ended = false;

        jList = new JList<>(list);
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList.addMouseListener(new MyMouseAdapter());
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(jList);
        jList.setLayoutOrientation(JList.VERTICAL);
        jList.setVisibleRowCount(20);

        listPanel = new JPanel(new BorderLayout());
        listPanel.add(jList);
        listPanel.add(scrollPane);
        listPanel.setMaximumSize(new Dimension(230, 750));

        addItem = new JButton("add");
        addItem.addActionListener(new addActionListener());
        addItem.setMinimumSize(new Dimension(230, 25));
        addItem.setPreferredSize(new Dimension(230, 25));
        addItem.revalidate();

        deleteItem = new JButton("delete");
        deleteItem.addActionListener(new removeActionListener());
        deleteItem.setMinimumSize(new Dimension(230, 25));
        deleteItem.setPreferredSize(new Dimension(230, 25));
        deleteItem.revalidate();

        add(new JLabel(title));
        add(listPanel);
        add(addItem);
        add(deleteItem);
        setMinimumSize(new Dimension(250, 790));

        new Thread(this).start();
    }

    public void setLayout() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public void getDelete() {
        ended = true;
        for (T item : list) {
            item.getDelete();
        }
    }

    @Override
    public void run() {
        setLayout();
        setVisible(true);
        while (!ended) {
            updateUI();
            try {
                Thread.sleep(499);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            listPanel.remove(scrollPane);
            scrollPane = new JScrollPane(jList);
            listPanel.add(scrollPane);
            listPanel.revalidate();
            listPanel.repaint();
            showPanel.updateUI();
        }
    }

    private class MyMouseAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent eve) {
            // Double-click detected
            if (eve.getClickCount() > 0) {
                int index = jList.locationToIndex(eve.getPoint());
                if (index < 0 || index >= list.size())
                    return;
                while (showPanel.getComponentCount() > 0) {
                    showPanel.getComponent(0).setVisible(false);
                    showPanel.remove(0);
                }
                showPanel.setLayout(new GridLayout(1, 1));
                list.get(index).getShowPanel().setVisible(true);
                showPanel.add(list.get(index).getShowPanel());
                updateUI();
            }
        }
    }

    private class addActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            list.add(oneSample.getNewInstance());
            jList.updateUI();
            updateUI();
        }
    }

    private class removeActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            T selected = jList.getSelectedValue();
            selected.getDelete();
            list.remove(selected);
            jList.updateUI();
            updateUI();
        }
    }

}
