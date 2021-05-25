package com.company.debts;

import com.company.PrimaryView;
import com.company.account.Account;
import com.company.dataBase.DebitDAO;
import com.company.dataBase.OrderDAO;
import com.company.dataBase.DebitDAO;
import com.company.dataBase.OrderDAO;
import com.company.orders.Order;
import com.company.orders.ViewOrders;
import com.company.utilities.*;
import com.company.utilities.CustomDebitTableModel;
import com.company.utilities.Helpers;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/*****
 *
 *  Class of DebitsView
 */
public class DebtsView {
    private JTextField nameField, amountField;
    private JButton updateNameBtn, receiptBtn, discountAmountDebitBtn, addAmountDebitBtn;
    private DebitDAO debitDAO;
    private JTable table;
    private DefaultTableModel model;
    public static ArrayList<Account> accounts = new ArrayList<>();
    private static JLabel nameLabel, idLabel, totalDebitLabel, dateTimeLabel;
    private Account account;
    private JLabel statsLabel;
    private final Font font = new Font(Font.DIALOG, Font.BOLD, 16);
    private static ArrayList<Order> orders;
    private final Color myColor = Color.RED;

    /***
     *  constructor of DebitsView
     */
    public DebtsView() {
        debitDAO = new DebitDAO();
        accounts = debitDAO.getDebits();
        account = new Account();
        orders = new ArrayList<>();
    }

    /***
     *
     * @return panel contain the all information about customer inside JLabels components
     */

    private JPanel customerInformation() {
        JPanel panel = new JPanel(new GridLayout(4, 3, 5, 10));

        panel.setBorder(BorderFactory.createTitledBorder(null, "الزبون",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.CENTER, font, myColor));
        Font font = new Font(Font.DIALOG, Font.BOLD, 16);
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        updateNameBtn = new JButton("تعديل الاسم");
        updateNameBtn.setEnabled(false);
        updateNameBtn.setFont(font);
        updateNameBtn.addActionListener(e -> updateCustomerName());
        panel.add(Helpers.placeholderLabels(new JLabel("الاسم :"), font));
        panel.add(Helpers.placeholderLabels(nameLabel, font));
        panel.add(updateNameBtn);
        panel.add(Helpers.placeholderLabels(new JLabel("رقم الحساب :"), font));
        panel.add(Helpers.placeholderLabels(idLabel, font));

        receiptBtn = PrimaryView.receiptBtn;
        receiptBtn.setFont(font);

        receiptBtn.setEnabled(false);

        panel.add(receiptBtn);

        panel.add(Helpers.placeholderLabels(new JLabel("مبلغ الدين :"), font));
        panel.add(Helpers.placeholderLabels(totalDebitLabel, font));
        addAmountDebitBtn = new JButton("إضافة مبلغ للدين");
        addAmountDebitBtn.setEnabled(false);
        addAmountDebitBtn.setFont(font);
        addAmountDebitBtn.setFocusPainted(false);
        addAmountDebitBtn.addActionListener(e -> addAmount());//Action listener
        panel.add(addAmountDebitBtn);
        panel.add(Helpers.placeholderLabels(new JLabel("تأريخ اخر إضافة او خصم : "), font));
        dateTimeLabel.setFont(font);
        panel.add(dateTimeLabel);
        discountAmountDebitBtn = new JButton("خصم مبلغ من الدين");
        discountAmountDebitBtn.setEnabled(false);
        discountAmountDebitBtn.setFont(font);
        discountAmountDebitBtn.setFocusPainted(false);
        discountAmountDebitBtn.addActionListener(e -> discountAmount());//Action listener
        panel.add(discountAmountDebitBtn);
        return panel;
    }
    public static void newTable() {
        ViewOrders.nameHolder.setText(nameLabel.getText());
        ViewOrders.debitHolder.setText(totalDebitLabel.getText());
        OrderDAO orderDAO = new OrderDAO();
        orders = orderDAO.getOrdersToClient(nameLabel.getText());
        ViewOrders.renderReceipt(orders);
    }
    /***
     *
     * @return panel that's contain the component of add new customer it's locate inside leftPanel
     */
    private JPanel addCustomerPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 3, 10, 40));
        panel.setBorder(BorderFactory.createTitledBorder(null, "إضافة زبائن الى قائمة الديون",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.CENTER, font, myColor));
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        nameField = new JTextField(30);
        nameField.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        nameField.setFont(font);
        nameField.addActionListener(e -> {
            if (checkName(nameField.getText())) {
                nameField.transferFocus();
            }
        });
        amountField = new JTextField(30);
        amountField.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        amountField.setFont(font);
        amountField.addActionListener(new Listener());
        JButton addBtn = new JButton("اضف");
        addBtn.setFont(font);
        addBtn.addActionListener(new Listener());

        panel.add(Helpers.placeholderLabels(new JLabel("الاسم : "), font));
        panel.add(nameField);
        panel.add(new JLabel());
        panel.add(Helpers.placeholderLabels(new JLabel("مبلغ الدين : "), font));
        panel.add(amountField);
        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(addBtn);
        panel.add(new JLabel());

        return panel;
    }

    private JPanel stats() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 10, 15));
        panel.setBorder(BorderFactory.createTitledBorder(null, "إجمالي الديون",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.CENTER, font, myColor));
        JLabel debits = new JLabel("مجموع الديون : ");
        JLabel currency = new JLabel("د.ع");
        Font font = new Font(Font.DIALOG, Font.BOLD, 20);
        currency.setFont(font);
        debits.setFont(font);
        statsLabel.setFont(font);
        statsLabel.setText(NumberFormat.getNumberInstance(Locale.US).format(totalDebits()));
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        JButton printBtn = new JButton("طباعة الديون");
        printBtn.setFont(font);
        printBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Helpers.print(Helpers.printDebits(accounts));
            }
        });
        JLabel label = new JLabel("  ");
        panel.add(debits);
        panel.add(statsLabel);
        panel.add(currency);
        panel.add(label);
        panel.add(printBtn);
        panel.add(label);

        return panel;
    }

    /***
     *** private nested class to implement action listener
     * check name and amount of entries
     */
    private class Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!nameField.getText().isEmpty() && !amountField.getText().isEmpty() && checkName(nameField.getText())) {
                JLabel label = new JLabel(" تمت إضافة ( " + nameField.getText().trim() + " ) إلى قائمة الديون ");
                label.setFont(new Font("Arial", Font.BOLD, 18));
                try {
                    int amount = Integer.parseInt(amountField.getText().trim());
                    debitDAO.insertData(nameField.getText().trim(), amount, Helpers.currentDateTime());
                    automateScrollToLastCustomer();
                    JOptionPane.showMessageDialog(null, label,
                            "إضافة", JOptionPane.WARNING_MESSAGE);
                    statsLabel.setText(NumberFormat.getNumberInstance(Locale.US).format(totalDebits()));

                } catch (Exception exc) {
                    label.setText("يجب ان يكون المبلغ رقما...!");
                    JOptionPane.showMessageDialog(null,label,
                            "إضافة", JOptionPane.ERROR_MESSAGE);
                    amountField.setText("");
                }
            }
        }
    }

    /***
     *  function to automate the scrollPane and selection in table after add new customer
     */
    private void automateScrollToLastCustomer() {
        nameField.setText("");
        amountField.setText("");
        loseCellFocus();
        refreshTable();
        int lastIndex = table.getRowCount() - 1;
        Rectangle rect = table.getCellRect(lastIndex, 0, false);
        table.scrollRectToVisible(rect);
        table.changeSelection(lastIndex, 0, false, false);
        populateDetails(lastIndex + 1);
        Helpers.setBarcodeFocus(nameField);
    }

    private void automateScrollToExitCustomer(String barcode) {
        int specificIndex = returnProductIndex(barcode);
        table.scrollRectToVisible(table.getCellRect(specificIndex, 2, true));
        table.changeSelection(specificIndex, 2, false, false);
        populateDetails(specificIndex + 1);
    }

    /**
     * *
     * refresh table when any change on table
     */
    public void refreshTable() {
        accounts = debitDAO.getDebits();
        renderAccounts(accounts);
    }

    private int returnProductIndex(String name) {
        for (Account account : accounts) {
            if (account.getName().equals(name.trim())) {
                return accounts.indexOf(account);
            }
        }
        return 0;
    }

    /**
     * check name if  is existed or not
     */
    private boolean checkName(String name) {
        if (debitDAO.checkName(name.trim())) {
            JLabel label = new JLabel("هذا الاسم موجود في قائمة الديون");
            label.setFont(new Font("Arial", Font.BOLD, 18));
            JOptionPane.showMessageDialog(null, label
                    , "إنتبه", JOptionPane.WARNING_MESSAGE);

            amountField.setText("");
            nameField.setText("");
            automateScrollToExitCustomer(name);
            Helpers.setBarcodeFocus(nameField);
            return false;
        }
        return true;
    }

    /***
     *
     * @return main left panel that's contain three panels info, add, and summation all debits
     *
     ***/
    private JPanel leftPanel() {
        this.nameLabel = new JLabel("", SwingConstants.CENTER);
        this.idLabel = new JLabel("", SwingConstants.CENTER);
        this.totalDebitLabel = new JLabel("", SwingConstants.CENTER);
        this.dateTimeLabel = new JLabel("", SwingConstants.CENTER);
        this.statsLabel = new JLabel();
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 20));
        panel.setBorder(BorderFactory.createTitledBorder(""));
        panel.add(customerInformation());
        panel.add(addCustomerPanel());
        panel.add(stats());
        return panel;
    }

    /***
     *
     * @return the right panel that's contain the table or table
     */
    private JPanel rightPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 30));
        JTextField accountSearch = new JTextField(10);
        accountSearch.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                accountSearch.setForeground(new Color(0x0A0A0A));
                accountSearch.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                accountSearch.setForeground(new Color(0x777778));
                accountSearch.setText("بحث");
            }
        });
        accountSearch.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        Font font = new Font(Font.DIALOG, Font.PLAIN, 20);
        accountSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                renderAccounts(searchAccount(accountSearch.getText()));

            }

        });
        Helpers.setBarcodeFocus(accountSearch);
        accountSearch.setFont(font);
        accountSearch.setPreferredSize(new Dimension(10, 30));
        panel.add(accountSearch, BorderLayout.NORTH);
        panel.add(this.prepareTable(), BorderLayout.CENTER);
        return panel;
    }

    /***
     *
     * @return center panel contain left and right panel
     */
    private JPanel centerPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder());
        panel.add(leftPanel());
        panel.add(rightPanel());
        return panel;
    }

    /***
     *
     * @return Default model to the table
     */
    private DefaultTableModel prepareModel() {
        model = new CustomDebitTableModel(new String[]{"التسلسل", "الاسم", "رقم الحساب", "مبلغ الدين"});
        return model;
    }


    /***
     *
     * @return scrollPane contain the table component
     */
    private JScrollPane prepareTable() {
        table = new JTable(prepareModel());
        table.setFont(font);
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer(table));
        header.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
        table.setRowHeight(30);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        table.setFillsViewportHeight(true);

        renderAccounts(accounts);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                int index = (Integer) table.getModel().getValueAt(table.getSelectedRow(), 2);
                updateNameBtn.setEnabled(true);
                receiptBtn.setEnabled(true);
                addAmountDebitBtn.setEnabled(true);
                discountAmountDebitBtn.setEnabled(true);
                populateDetails(index);

            }

        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                table.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
        JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JScrollBar bar = scrollPane.getVerticalScrollBar();
        bar.setPreferredSize(new Dimension(18, 0));

        return scrollPane;
    }

    /***
     * to remove selection from row in the table
     */
    public void loseCellFocus() {
        table.getSelectionModel().clearSelection();

    }

    /**
     * function to create data inside table
     *
     * @param accounts object of array list contain all the account of customers in DebitDAO
     */
    public void renderAccounts(ArrayList<Account> accounts) {
        model.setRowCount(0);
        int count = 1;
        for (Account account : accounts) {
            this.model.addRow(new Object[]{count, account.getName(), account.getID(), account.getTotalDebit()});
            count++;
        }
    }



    /***
     *
     * @return main panel that's contain all the component of debits
     */
    public JPanel process() {
        JPanel panel = new JPanel(new BorderLayout(10, 20));
        panel.setBorder(BorderFactory.createTitledBorder(null, "سجل الديون",
                TitledBorder.CENTER,
                TitledBorder.CENTER, font, myColor));
        panel.add(new JLabel(), BorderLayout.NORTH);
        panel.add(centerPanel(), BorderLayout.CENTER);
        refreshTable();
        return panel;
    }

    /***
     * function to update label
     * @param accountId the id of customer in table
     */
    private void populateDetails(int accountId) {
        account = debitDAO.find(accountId);
        accounts = debitDAO.getDebits();
        nameLabel.setText(account.getName());
        idLabel.setText(account.getID() + "");
        totalDebitLabel.setText(NumberFormat.getNumberInstance(Locale.US).format(account.getTotalDebit()));
        dateTimeLabel.setText(account.getCreationDate());
        statsLabel.setText(NumberFormat.getNumberInstance(Locale.US).format(totalDebits()));

    }

    /**
     *
     */
    private void updateCustomerName() {
        String val = nameLabel.getText();
        String strValue = (String) JOptionPane.showInputDialog(null,
                Helpers.placeholderLabels(new JLabel("ادخل الاسم الجديد"), font),
                "تعديل الإسم", JOptionPane.PLAIN_MESSAGE, null, null, val);
        if (strValue != null && account.getName() != null && account.getCreationDate() != null) {
            try {
                debitDAO.updateAccountName(Integer.parseInt(idLabel.getText()), strValue);
                table.getModel().setValueAt(strValue, table.getSelectedRow(), 1);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                table.updateUI();
                populateDetails(Integer.parseInt(idLabel.getText()));
            }
        }
    }

    /***
     *  function to add amount to customer
     */
    private void addAmount() {
        JLabel label = new JLabel("ادخل المبلغ الذي تريد إضافته");
        String strValue = JOptionPane.showInputDialog(null, Helpers.placeholderLabels(label, font),
                "إضافة دين", JOptionPane.PLAIN_MESSAGE);

        if (strValue != null && account.getName() != null && account.getCreationDate() != null) {
            try {
                int total = account.getTotalDebit() + Integer.parseInt(strValue.trim());
                updateTableAndLabels(account.getID(), total, Helpers.currentDateTime());
            } catch (Exception ex) {
                label.setText("يجب ان يكون السعر رقما ...!");
                JOptionPane.showMessageDialog(null, Helpers.placeholderLabels(label, font),
                        "تحذير", JOptionPane.WARNING_MESSAGE);
                addAmount();
            } finally {
                table.updateUI();
                populateDetails(Integer.parseInt(idLabel.getText()));
            }
            printReceipt(account.getID());
        }
    }

    private void printReceipt(int accountId){
        int conf = JOptionPane.showConfirmDialog(null,
                Helpers.placeholderLabels(new JLabel("طباعه وصل؟"), font),
                "تأكيد", JOptionPane.OK_CANCEL_OPTION);
        if (conf == 0) {
            Helpers.print(Helpers.printAccount(debitDAO.find(account.getID())));
        }
    }
    /***
     *  function to discount amount from customer
     *
     */
    private void discountAmount() {
        JLabel label = new JLabel("ادخل المبلغ الذي تريد ان تخصمه");
        String strValue = JOptionPane.showInputDialog(null, Helpers.placeholderLabels(label, font),
                "خصم من دين", JOptionPane.PLAIN_MESSAGE);

        if (strValue != null && account.getName() != null && account.getCreationDate() != null) {
            try {
                int total = account.getTotalDebit() - Integer.parseInt(strValue.trim());
                updateTableAndLabels(account.getID(), total, Helpers.currentDateTime());
            } catch (Exception ex) {
                label.setText("يجب ان يكون السعر رقما ...!");
                JOptionPane.showMessageDialog(null, Helpers.placeholderLabels(label, font),
                        "تحذير", JOptionPane.WARNING_MESSAGE);
                discountAmount();
            } finally {
                table.updateUI();
                populateDetails(Integer.parseInt(idLabel.getText()));
            }
            printReceipt(account.getID());
        }
    }

    /***
     *  function to update table and label that's after change in the amount and date
     *
     * @param id to the customer in DebitDAO
     * @param totalAmount total amount to the customer
     * @param dateTime creation DateTime to (add or discount) amount
     */
    private void updateTableAndLabels(int id, double totalAmount, String dateTime) {
        try {
            debitDAO.updateAccount(id, totalAmount, dateTime);
            account = debitDAO.find(id);
            totalDebitLabel.setText(NumberFormat.getNumberInstance(Locale.US).format(account.getTotalDebit()));
            table.getModel().setValueAt(account.getTotalDebit(), table.getSelectedRow(), 3);
            this.statsLabel.setText(NumberFormat.getNumberInstance(Locale.US).format(totalDebits()));
        } catch (Exception e) {
            //TODO
        }
    }


    /**
     * Search through listed accounts
     */
    private ArrayList<Account> searchAccount(String keyword) {
        ArrayList<Account> searchResults = new ArrayList<>();
        if (keyword.isEmpty()) {
            return accounts;
        }
        for (Account account : accounts) {

            if (account.getName().contains(keyword) || String.valueOf(account.getID()).contains(keyword)) {
                searchResults.add(account);

            }

        }
        return searchResults;
    }


    private int totalDebits() {
        int sum = 0;
        for (Account account : accounts) {
            sum += account.getTotalDebit();
        }
        return sum;
    }

}

