package com.company.utilities;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Utility {
    public static JLabel label(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    public JTextField field(Font font) {
        JTextField field = new JTextField(13);
        field.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        field.setFont(font);
        return field;
    }

    public JButton button(String name, Font font, Color background, Color foreground, Dimension dimension) {
        JButton button = new JButton(name);
        button.setFont(font);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setPreferredSize(dimension);
        button.setFocusPainted(false);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                button.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });


        return button;

    }

    public static void preFocus(JTextField field) {
        EventQueue.invokeLater(() -> {
            field.grabFocus();
            field.requestFocus();//or inWindow
        });
    }
}
