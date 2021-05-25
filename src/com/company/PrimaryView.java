package com.company;

import com.company.debts.DebtsView;
import com.company.inventory.InventoryView;
import com.company.orders.ViewOrders;
import com.company.sales.SalesView;
import com.company.utilities.Utility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PrimaryView {
    private JPanel sidePanel, centerPanel;
    private JButton salesBtn, addProductBtn, debtBtn;
    public JButton orderBtn;
    private final Color blueColor = new Color(0x2ACDC0);
    private final Font font = new Font(Font.DIALOG, Font.BOLD, 28);
    private final Color whiteColor = new Color(0xF5F5F5);
    private final Dimension dimension = new Dimension(200, 300);
    private final Color redColor = new Color(0xA34E21);
    private SalesView salesView;
    private ViewOrders orderView;
    private DebtsView debtsView;
    public static JButton receiptBtn;
    public static JFrame mainFrame;

    public PrimaryView() {
        initComponent();
    }

    private void initComponent() {
        mainFrame = new JFrame();
        Container cp = mainFrame.getContentPane();
        cp.setLayout(new BorderLayout());
        centerPanel = this.getCenterPanel();
        sidePanel = this.getSidePanel();
        salesView = new SalesView();
        orderView = new ViewOrders();
        debtsView = new DebtsView();
        cp.add(sidePanel, BorderLayout.EAST);
        cp.add(centerPanel, BorderLayout.CENTER);
        mainFrame.setSize(950, 650);
        mainFrame.setTitle("مذخر المبيعات");
        mainFrame.setLocation(300, 60);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        receiptBtn = new JButton("وصولات الشخص");
        receiptBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getOrderView();
                DebtsView.newTable();
            }
        });
        mainFrame.setVisible(true);
    }

    /**
     * @return center panel use to recall other panel when any button in side panel that actions
     */
    private JPanel getCenterPanel() {
        centerPanel = new JPanel(new BorderLayout(10, 10));

        return centerPanel;
    }

    /**
     * @return side panel that contains the Buttons
     */
    private JPanel getSidePanel() {
        sidePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        sidePanel.setBackground(blueColor);
        sidePanel.setPreferredSize(dimension);

        sidePanel.add(getSalesBtn());

        sidePanel.add(getAddProductBtn());

        sidePanel.add(getOrderBtn());

        sidePanel.add(getDebtBtn());


        return sidePanel;
    }

    /**
     * @return salesBtn and set attributes and properties
     */
    private JButton getSalesBtn() {
        salesBtn = new JButton("المبيعات");
        Utility.sideButtonMainConfig(salesBtn);
        salesBtn.addActionListener(e -> {
            getSalesView();
        });
        return salesBtn;
    }

    /**
     * @return addProductBtn and set attributes and properties
     */
    private JButton getAddProductBtn() {
        addProductBtn = new JButton("مخزن الادوية");
        Utility.sideButtonMainConfig(addProductBtn);
        addProductBtn.addActionListener(e -> {
            getInventoryView();
        });
        return addProductBtn;
    }

    private JButton getOrderBtn() {
        orderBtn = new JButton("الفواتير");
        Utility.sideButtonMainConfig(orderBtn);
        orderBtn.addActionListener(e -> {
            getOrderView();
        });
        return orderBtn;
    }

    private JButton getDebtBtn() {
        debtBtn = new JButton("الديون");
        Utility.sideButtonMainConfig(debtBtn);
        debtBtn.addActionListener(e -> {
            getDebtView();
        });
        return debtBtn;
    }

    private void getSalesView() {
        centerPanel.removeAll();
        centerPanel.repaint();
        centerPanel.revalidate();
        setSelectBtnSettings(salesBtn);

        resetDefaultAllBtn(addProductBtn, orderBtn, debtBtn);
        centerPanel.add(salesView.getMainPanel());

    }

    private void getInventoryView() {
        centerPanel.removeAll();
        centerPanel.repaint();
        centerPanel.revalidate();
        setSelectBtnSettings(addProductBtn);

        resetDefaultAllBtn(salesBtn, orderBtn, debtBtn);

        centerPanel.add(new InventoryView().getMainPanel());

    }

    public void getOrderView() {
        centerPanel.removeAll();
        centerPanel.repaint();
        centerPanel.revalidate();
        setSelectBtnSettings(orderBtn);
        resetDefaultAllBtn(salesBtn, addProductBtn, debtBtn);
        centerPanel.add(orderView.process());

    }

    private void getDebtView() {
        centerPanel.removeAll();
        centerPanel.repaint();
        centerPanel.revalidate();
        setSelectBtnSettings(debtBtn);
        resetDefaultAllBtn(salesBtn, addProductBtn, orderBtn);
        centerPanel.add(debtsView.process());
    }


    private void resetDefaultAllBtn(JButton... buttons) {
        for (JButton button : buttons) {
            button.setEnabled(true);
            button.setBackground(redColor);
            button.setForeground(whiteColor);
        }
    }

    private void setSelectBtnSettings(JButton button) {
        button.setEnabled(false);
        button.setBackground(whiteColor);
        button.setForeground(redColor);
    }

}
