package com.company;

import com.company.db.OrderDAO;
import com.company.utils.CustomDebitTableModel;
import com.company.utils.Helpers;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class ViewOrders {
    private JPanel rightPanel, leftPanel;
    private JScrollPane scrollPane;
    private JTextArea textArea;
    private JTextField searchField;
    private Order highlightedOrder;
    private static JTable receiptTable;
    private static DefaultTableModel model;
    private final Font font = new Font(Font.DIALOG, Font.BOLD, 20);
    public static JLabel nameHolder, debitHolder;
    private static ArrayList<Order> orders = new ArrayList<>();
    private JButton searchBtn = new JButton("بحث");
    private ActionListener searchClickListener = new Listener();

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
        textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(300, 300));
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
        textArea.setSize(350, 300);
        textArea.setLineWrap(true);       // wrap line
        textArea.setWrapStyleWord(true);
        scrollPane = new JScrollPane(textArea);
        scrollPane.setSize(new Dimension(350, 300));
        panel.add(scrollPane);
        return panel;
    }

    private class Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                OrderDAO orderDAO = new OrderDAO();
                if (!searchField.getText().trim().isEmpty()) {
                    Order order = new OrderDAO().find(Integer.parseInt(searchField.getText().trim()));
                    System.out.println(order.getTotalDebit());
                    Helpers.prepareOrderDAO(order, textArea);
                    highlightedOrder = order;
                    return;
                }
                Helpers.prepareOrderDAO(orderDAO.find(Integer.parseInt(searchField.getText())), textArea);
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
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 20));
        panel.add(print);
        panel.add(new JLabel(""));
        return panel;
    }

    private JPanel prepareLeftPanel() {
        leftPanel = new JPanel(new BorderLayout(5, 20));
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
        receiptTable = new JTable(prepareModel());
        receiptTable.setFont(font);
        JTableHeader header = receiptTable.getTableHeader();
        header.setFont(font);
        receiptTable.setRowHeight(30);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        receiptTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        receiptTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        receiptTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        int lastIndex = receiptTable.getRowCount() - 1;
        Rectangle rect = receiptTable.getCellRect(lastIndex, 0, false);
        receiptTable.scrollRectToVisible(rect);
        receiptTable.changeSelection(lastIndex, 0, false, false);
        receiptTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        receiptTable.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        receiptTable.setFillsViewportHeight(true);
        receiptTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                int orderId = Integer.parseInt(receiptTable.getModel().getValueAt(receiptTable.getSelectedRow(), 1).toString());
                searchField.setText(orderId + "");
                searchBtn.setEnabled(true);
                try {
                    highlightedOrder = new OrderDAO().find(orderId);
                    Helpers.prepareOrderDAO(highlightedOrder, textArea);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        return new JScrollPane(receiptTable);
    }

    public static void renderReceipt(ArrayList<Order> orderdList) {
        receiptTable.updateUI();
        model.setRowCount(0);
        orders = orderdList;
        int count = 1;
        for (Order order : orderdList) {
            model.addRow(new Object[]{count, order.getId(), order.getDate()});
            count++;
        }

        receiptTable.revalidate();
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
        leftPanel = this.prepareLeftPanel();
        rightPanel = this.prepareRightPanel();
        mainPanel.add(leftPanel, 1, 0);

        mainPanel.add(rightPanel, 2, 1);

        return mainPanel;
    }


}
