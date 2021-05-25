package com.company.orders;

import com.company.dataBase.OrderDAO;
import com.company.utilities.CustomDebitTableModel;
import com.company.utilities.HeaderRenderer;
import com.company.utilities.Helpers;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;


public class ViewOrders {
    private JPanel rightPanel, leftPanel;
    private JScrollPane scrollPane;
    private JTextArea textArea;
    private JTextField searchField;
    private Order highlightedOrder;
    private static JTable table;
    private static DefaultTableModel model;
    private final Font font = new Font(Font.DIALOG, Font.BOLD, 20);
    public static JLabel nameHolder, debitHolder;
    private static ArrayList<Order> orders = new ArrayList<>();
    private JButton searchBtn = new JButton("بحث");
    private ActionListener searchClickListener = new Listener();
    private final Color myColor = Color.RED;

    public ViewOrders() {
        nameHolder = new JLabel("");
        nameHolder.setFont(font);
        nameHolder.setHorizontalAlignment(SwingConstants.LEADING);
        debitHolder = new JLabel("");
        debitHolder.setFont(font);
        debitHolder.setHorizontalAlignment(SwingConstants.LEADING);
    }

    private JPanel prepareLeftScrollPane() {
        JPanel panel = new JPanel(new GridLayout(1, 1, 5, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(300, 300));
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
        textArea.setLineWrap(true);       // wrap line
        textArea.setWrapStyleWord(true);
        scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane);
        return panel;
    }

    private class Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (!searchField.getText().trim().isEmpty()) {
                    Order order = new OrderDAO().find(Integer.parseInt(searchField.getText().trim()));
                    System.out.println(order.getTotalDebit());
                    Helpers.prepareOrderDAO(order, textArea);
                    highlightedOrder = order;
                    return;
                }
                Helpers.prepareOrderDAO(OrderDAO.find(Integer.parseInt(searchField.getText())), textArea);
            } catch (Exception ex) {
                textArea.setText("");
                JOptionPane.showMessageDialog(null,
                        Helpers.placeholderLabels(new JLabel("رقم الوصل خطأ او غير موجود ..!"), font)
                        , "تنبيه!", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private JPanel prepareSearchField() {
        searchField = new JTextField(15);
        searchField.setFont(font);
        searchField.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        Helpers.setBarcodeFocus(searchField);
        searchField.addActionListener(searchClickListener);
        searchBtn.setFont(font);
        searchBtn.setEnabled(false);
        searchBtn.addActionListener(searchClickListener);

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (searchField.getText().isEmpty()) {
                    textArea.setText("");
                    searchBtn.setEnabled(false);
                    return;
                }
                searchBtn.setEnabled(!searchField.getText().isEmpty());
            }
        });
        JPanel panel = new JPanel(new GridLayout(1, 2, 5, 20));
        JPanel holdPanel = new JPanel(new GridLayout(1, 2, 15, 20));
        holdPanel.add(searchBtn);
        holdPanel.add(searchField);
        panel.add(holdPanel);
        panel.add(Helpers.placeholderLabels(new JLabel("بحث بالرقم"), font));
        return panel;
    }

    private JPanel printPanel() {
        JButton print = new JButton("طباعة الوصل");
        print.setFont(font);
        print.addActionListener(e -> {
            if (!textArea.getText().isEmpty() && highlightedOrder != null) {
                Helpers.print(Helpers.printOrder(highlightedOrder));
            }
        });
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 10));
        panel.add(new JLabel(""));
        panel.add(print);

        return panel;
    }

    private JPanel prepareLeftPanel() {
        leftPanel = new JPanel(new BorderLayout(30, 20));
        leftPanel.add(prepareSearchField(), BorderLayout.NORTH);
        leftPanel.add(prepareLeftScrollPane(), BorderLayout.CENTER);
        leftPanel.add(printPanel(), BorderLayout.SOUTH);
        return leftPanel;
    }

    private DefaultTableModel prepareModel() {
        model = new CustomDebitTableModel(new String[]{"التسلسل", "رقم الوصل", "التأريخ"});
        return model;
    }

    private JScrollPane prepareTable() {
        table = new JTable(prepareModel());
        table.setFont(font);
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer(table));
        header.setFont(font);
        table.setRowHeight(30);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        int lastIndex = table.getRowCount() - 1;
        Rectangle rect = table.getCellRect(lastIndex, 0, false);
        table.scrollRectToVisible(rect);
        table.changeSelection(lastIndex, 0, false, false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        table.setFillsViewportHeight(true);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                int orderId = Integer.parseInt(table.getModel().getValueAt(table.getSelectedRow(), 1).toString());
                searchField.setText(orderId + "");
                searchBtn.setEnabled(true);
                try {
                    new OrderDAO();
                    highlightedOrder =  OrderDAO.find(orderId);
                    Helpers.prepareOrderDAO(highlightedOrder, textArea);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                System.out.println("wel");
            }

        });
        return new JScrollPane(table);
    }

    public static void renderReceipt(ArrayList<Order> orderdList) {
        table.updateUI();
        model.setRowCount(0);
        orders = orderdList;
        int count = 1;
        for (Order order : orderdList) {
            model.addRow(new Object[]{count, order.getId(), order.getDate()});
            count++;
        }

        table.revalidate();
    }


    private JPanel prepareRightPanel() {
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setSize(500, 300);
        JPanel topPanel = new JPanel(new GridLayout(3, 2, 10, 50));

        topPanel.add(nameHolder);
        topPanel.add(Helpers.placeholderLabels(new JLabel("اسم الزبون: "), font));
        topPanel.add(debitHolder);
        topPanel.add(Helpers.placeholderLabels(new JLabel("الديون: "), font));

        rightPanel.add(topPanel, BorderLayout.NORTH);
        rightPanel.add(this.prepareTable(), BorderLayout.CENTER);
        return rightPanel;
    }

    public JPanel process() {
        nameHolder.setText("");
        debitHolder.setText("");
        JPanel mainPanel = new JPanel(new GridLayout(1, 3, 50, 10));
        mainPanel.setBorder(BorderFactory.createTitledBorder(null, "الفواتير",
                TitledBorder.CENTER,
                TitledBorder.CENTER, font, myColor));
        leftPanel = this.prepareLeftPanel();
        rightPanel = this.prepareRightPanel();
        mainPanel.add(leftPanel, 1, 0);

        mainPanel.add(rightPanel, 2, 1);

        return mainPanel;
    }


}
