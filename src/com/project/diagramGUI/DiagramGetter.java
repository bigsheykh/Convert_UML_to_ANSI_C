package com.project.diagramGUI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class DiagramGetter {
    private final JPanel panel;
    private JFrame f;
    private double zoom = 1.0;

    public DiagramGetter() {
        panel = new _JPanel();
    }

    public void init() {
        JButton b = new JButton();
        b.setBackground(Color.red);
        b.setBorder(new LineBorder(Color.black, 2));
        b.setPreferredSize(new Dimension(600, 10));
        panel.add(b);
        f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(panel, "Center");
        f.add(getCheckBoxPanel(), "South");
        f.setLocation(200, 200);
        f.setSize(400, 400);
        f.validate();
        f.setVisible(true);
        Graphics graphics = new DebugGraphics();

    }

    private JPanel getCheckBoxPanel() {
        JButton zoomIn = new JButton("Zoom in");
        zoomIn.addActionListener(e -> zoomAndRepaint(0.1));
        JButton zoomOut = new JButton("Zoom out");
        zoomOut.addActionListener(e -> zoomAndRepaint(-0.1));
        JPanel panel2 = new JPanel();
        panel2.add(zoomIn);
        panel2.add(zoomOut);
        return panel2;
    }

    /**
     * zoomAndRepaint
     */
    protected void zoomAndRepaint(double d) {
        zoom += d;
        f.repaint();
    }

    private class _JPanel extends JPanel {

        public _JPanel() {
            super(null);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setFont(new Font("Arial", Font.PLAIN, (int) (zoom * 10 + 2)));
            for (int x = 0; x < 100; x++)
                for (int y = 0; y < 100; y++) {
                    g.drawString("Hello " + x + "," + y, (int) (x * 60 * zoom), (int) (y * 10 * zoom));
                    g.drawString("Hello " + x + "," + y, (int) (x * 60 * zoom), (int) (y * 10 * zoom));

                    g.drawRect((int) (x * 60 * zoom) - 1, (int) (y * 10 * zoom) - 1, (int) (70 * zoom), (int) (20 * zoom));
                }

        }
    }
}
