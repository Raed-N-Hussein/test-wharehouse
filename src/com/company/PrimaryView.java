package com.company;

import com.company.inventory.Inventory;
import com.company.sales.SalesView;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PrimaryView {
    private JFrame mainFrame;
    private JPanel sidePanel, centerPanel;
    private JButton salesBtn, addProductBtn;
    private final Color color = new Color(0x235D9A);
    private final Font font = new Font(Font.DIALOG, Font.BOLD, 28);
    private final Color whiteColor = new Color(0xF5F5F5);
    private final Dimension dimension = new Dimension(200, 300);
    private final Color redColor = new Color(0x3E051A);
    private SalesView salesView;

    public PrimaryView()
    {
        initComponent();
    }

    private void initComponent() {
        mainFrame = new JFrame();
        Container cp = mainFrame.getContentPane();
        cp.setLayout(new BorderLayout());
        centerPanel = this.getCenterPanel();
        sidePanel = this.getSidePanel();
        salesView = new SalesView();
        cp.add(sidePanel, BorderLayout.EAST);
        cp.add(centerPanel, BorderLayout.CENTER);
        mainFrame.setSize(950, 650);
        mainFrame.setTitle("مذخر المبيعات");
        mainFrame.setLocation(300, 60);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    /**
     *
     * @return center panel use to recall other panel when any button in side panel that actions
     *
     */
    private JPanel getCenterPanel() {
        centerPanel = new JPanel(new BorderLayout(10, 10));

        return centerPanel;
    }

    /**
     *
     * @return side panel that contains the Buttons
     */
    private JPanel getSidePanel() {
        sidePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 70));
        sidePanel.setBackground(color);
        sidePanel.setPreferredSize(dimension);

        sidePanel.add(getSalesBtn());

        sidePanel.add(getAddProductBtn());
        return sidePanel;
    }

    /**
     *
     * @return salesBtn and set attributes and properties
     */
    private JButton getSalesBtn() {
        salesBtn = new JButton("المبيعات");
        salesBtn.setFont(font);
        dimension.setSize(200, 50);
        salesBtn.setPreferredSize(dimension);
        salesBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                salesBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                salesBtn.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
        salesBtn.setFocusPainted(false);
        salesBtn.setBorder(null);
        salesBtn.setForeground(whiteColor);
        salesBtn.setBackground(new Color(0x3E051A));
        salesBtn.addActionListener(e->{
            getSalesView();
        });
        return salesBtn;
    }

    /**
     *
     * @return addProductBtn and set attributes and properties
     */
    private JButton getAddProductBtn() {
        addProductBtn = new JButton("مخزن الادوية");
        addProductBtn.setFont(font);
        dimension.setSize(200, 50);
        addProductBtn.setPreferredSize(dimension);
        addProductBtn.setFocusPainted(false);
        addProductBtn.setBorder(null);
        addProductBtn.setForeground(whiteColor);
        addProductBtn.setBackground(redColor);
        addProductBtn.setFocusCycleRoot(false);
        addProductBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                addProductBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                addProductBtn.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
        addProductBtn.addActionListener(e->{
            getInventoryView();
        });
        return addProductBtn;
    }

    private void getSalesView() {
        centerPanel.removeAll();
        centerPanel.repaint();
        centerPanel.revalidate();
        salesBtn.setBackground(whiteColor);
        salesBtn.setForeground(redColor);


        addProductBtn.setBackground(redColor);
        addProductBtn.setForeground(whiteColor);

        centerPanel.add(salesView.getMainPanel());

    }

    private void getInventoryView() {
        centerPanel.removeAll();
        centerPanel.repaint();
        centerPanel.revalidate();
        salesBtn.setBackground(redColor);
        salesBtn.setForeground(whiteColor);

        addProductBtn.setBackground(whiteColor);
        addProductBtn.setForeground(redColor);

        centerPanel.add(new Inventory().getMainPanel());

    }
}
