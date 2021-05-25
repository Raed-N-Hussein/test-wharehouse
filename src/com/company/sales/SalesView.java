package com.company.sales;

import com.company.PrimaryView;
import com.company.Product;
import com.company.account.Account;
import com.company.dataBase.DebitDAO;
import com.company.dataBase.OrderDAO;
import com.company.dataBase.ProductDAO;
import com.company.debts.DebtsView;
import com.company.inventory.InventoryView;
import com.company.orders.Order;
import com.company.utilities.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SalesView {
    private JTextField barcodeField;
    public static JTextField nameField, amountField;
    private JButton checkBtn, printBtn;
    private JTable table;
    private TableModel model;
    private final JLabel priceLabel = new JLabel("0");
    private JTextArea textArea;
    private final Font font = new Font("Dialog", Font.BOLD, 18);

    private static final String[] columnNames = {"التسلسل", "اسم المنتج", "السعر بالجملة",
            "السعر بالمفرد", "الكمية", "جملة؟"};
    private ArrayList<Product> products;
    private int count = 1;
    private int quantity = 0;
    private InventoryView inventory;

    private DebitDAO dao;


    public SalesView() {
        products = new ArrayList<>();
        inventory = new InventoryView();
        dao = new DebitDAO();
    }

    public JPanel getMainPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 20));
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        Font myFont = new Font("SansSerif", Font.PLAIN, 20);
        Color myColor = Color.RED;
        panel.setBorder(BorderFactory.createTitledBorder(null, "المبيعات",
                TitledBorder.CENTER,
                TitledBorder.CENTER, myFont, myColor));

        panel.add(new JLabel(), BorderLayout.NORTH);
        panel.add(getTable(), BorderLayout.CENTER);
        panel.add(footerPanel(), BorderLayout.SOUTH);
        panel.add(leftPanel(), BorderLayout.WEST);

        return panel;
    }

    private JScrollPane getTable() {
        model = new TableModel(columnNames);
        model.addTableModelListener(new ModelChangeValueListener());
        table = new JTable(model);
        JTableHeader header = table.getTableHeader();

        header.setDefaultRenderer(new HeaderRenderer(table));
        header.setFont(font);
        table.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();

        table.setRowHeight(25);
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        table.setFont(font);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRender);

        renderTable();
        return new JScrollPane(table);
    }

    private class ModelChangeValueListener implements TableModelListener {

        @Override
        public void tableChanged(TableModelEvent e) {
            if (e.getColumn() < 0) return;
            int row = e.getFirstRow();
            int column = e.getColumn();
            Product product = products.get(row);
            if (column == 5) {
                Boolean check = (Boolean) model.getValueAt(row, column);
                product.setCumulative(check);
            } else if (column == 4) {
                product.setQuantity((Integer) model.getValueAt(row, column));
//                    checkQuantity(product);
            } else if (column == 3) {
                product.setSinglePrice((Integer) model.getValueAt(row, column));
            } else if (column == 2) {
                product.setCumulativePrice((Integer) model.getValueAt(row, column));
            }
            priceLabel.setText(String.format("%,d", sumTotalPrice(products)));
        }
    }

    public int sumTotalPrice(ArrayList<Product> products) {
        int sum = 0;
        for (Product prod : products) {
            sum += prod.getPrice();
        }
        return sum;
    }


    private JPanel footerPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 20));
        panel.setBorder(BorderFactory.createTitledBorder(""));
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        Font font = new Font(Font.DIALOG, Font.BOLD, 22);
        barcodeField = new JTextField(13);
        barcodeField.addActionListener(new ListenerCheckBarcode());
        Utility.preFocus(barcodeField);
        barcodeField.setFont(font);

        checkBtn = new JButton("تنظيف القائمة");
        checkBtn.addActionListener(new ListenerUpdateQuantity());
        checkBtn.setFocusPainted(true);
        checkBtn.setFont(font);

        printBtn = new JButton("طباعة فاتورة");
        printBtn.setFont(font);
        printBtn.addActionListener(e -> {
            createTableOrder();
        });
        Utility.mouseHandCourser(checkBtn);
        Utility.mouseHandCourser(printBtn);

        panel.add(barcodeField);
        panel.add(checkBtn);
        panel.add(printBtn);
        return panel;
    }

    private void createTableOrder() {
        displayOrderPreview();
        Helpers.setBarcodeFocus(barcodeField);
    }


    private JPanel leftPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 20, 20));
        panel.setPreferredSize(new Dimension(400, 600));
        panel.setBorder(BorderFactory.createTitledBorder(""));

        panel.add(getTextArea());


        panel.add(getPricePanel());
        return panel;
    }

    private JScrollPane getTextArea() {
        textArea = new JTextArea();
        textArea.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
        textArea.setLineWrap(true);       // wrap line
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        return new JScrollPane(textArea);
    }

    private JPanel getPricePanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 20));
        Font myFont = new Font("SansSerif", Font.PLAIN, 24);
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        Color myColor = Color.RED;
        panel.setBorder(BorderFactory.createTitledBorder(null, "المبلغ الاجمالي",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.CENTER, myFont, myColor));
        priceLabel.setFont(myFont);
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(new JLabel(""));
        panel.add(priceLabel);
        JLabel label = new JLabel("د.ع");
        label.setFont(font);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label);
        return panel;
    }

    private class ListenerCheckBarcode implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (!barcodeField.getText().isEmpty()) {
                for (Product prod : products) {
                    if (barcodeField.getText().trim().equals(prod.getBarcode())) {
                        int quantity = prod.getQuantity() + 1;
                        prod.setQuantity(quantity);
                        renderTable();
                        clearBarcodeField();
                        return;
                    }

                }
                for (Product product : InventoryView.products) {
                    if (barcodeField.getText().trim().equals(product.getBarcode())) {
                        Product prod = new Product(product.getId(), product.getName(), product.getExpireDate(),
                                product.getCumulativePrice(),
                                product.getSinglePrice(), product.getCumulativeQuantity(),
                                product.getSingleQuantity(), product.getRemainQuantity(), product.getBarcode());

                        products.add(prod);
                        renderTable();
                        clearBarcodeField();
                        return;
                    }
                }
                clearBarcodeField();

            }
        }
    }

    private void addRow(Product product) {
        model.addRow(new Object[]{model.getRowCount() + 1, product.getName(), product.getCumulativePrice(), product.getSinglePrice(),
                product.getQuantity()});
    }

    private void clearBarcodeField() {
        barcodeField.setText("");
        barcodeField.requestFocusInWindow();
        priceLabel.setText(String.format("%,d", sumTotalPrice(products)));
    }

    private void renderTable() {
        model.setRowCount(0);
        for (Product product : products) {
            addRow(product);
        }

    }

    /**
     * high performance quality in work of below algorithm
     * to render and check all quantities of inventory products
     */
    private void updateQuantityOfProductInInventory() {
        boolean isMoreThan = true;
        for (Product product : products) {
            if (product.isCumulative()) {
                int q = product.getCumulativeQuantity() - product.getQuantity();
                product.setCumulativeQuantity(Math.max(q, 0));

            } else if (!product.isCumulative()) {
                while (isMoreThan) {
                    if (product.getQuantity() < product.getRemainQuantity()) {
                        product.setRemainQuantity(product.getRemainQuantity() - product.getQuantity());
                        isMoreThan = false;
                    } else if (product.getQuantity() > product.getRemainQuantity()) {
                        product.setQuantity(product.getQuantity() - product.getRemainQuantity());
                        product.setCumulativeQuantity(product.getCumulativeQuantity() - 1);
                        product.setRemainQuantity(product.getSingleQuantity());
                    } else if (product.getQuantity() == product.getRemainQuantity() || product.getRemainQuantity() == 0) {
                        product.setCumulativeQuantity(product.getCumulativeQuantity() - 1);
                        product.setRemainQuantity(product.getSingleQuantity());
                        isMoreThan = false;
                    }
                }
            }
            ProductDAO.updateProduct(product);
        }
    }

    private class ListenerUpdateQuantity implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            checkQuantityAndClearAllFields();
        }
    }

    private void checkQuantityAndClearAllFields() {
        updateQuantityOfProductInInventory();
        priceLabel.setText("0");
        model.setRowCount(0);
        products.clear();
        textArea.removeAll();
        textArea.setText("");
        barcodeField.requestFocusInWindow();
    }


    public void displayOrderPreview() {
        if (!textArea.getText().isEmpty()) {
            return;
        }
        if (products.size() > 0) {
            Component c = panelClientAndAmount();
            Object[] inputField = new Object[]{"الاسم:", nameField,
                    "المبلغ المدفوع:", amountField};

            JOptionPane optionPane = new JOptionPane(inputField,
                    JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
            optionPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            JDialog dialog = optionPane.createDialog(PrimaryView.mainFrame, "نافذة الاسم والمبلغ المدفوع");
            dialog.setModal(false);
            dialog.setVisible(true);

            dialog.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentHidden(ComponentEvent e) {
                    if (optionPane.getValue() == null || (int) optionPane.getValue() != JOptionPane.YES_OPTION) {
                        return;
                    }
                    if ((int) optionPane.getValue() == JOptionPane.YES_OPTION) {
                        try {

                            if (!nameField.getText().trim().isEmpty() && !amountField.getText().trim().isEmpty()) {
                                String textField = nameField.getText().trim();
                                Order order = new Order(products, Helpers.currentDateTime(), textField,
                                        " ", Integer.parseInt(amountField.getText().trim()));

                                if (dao.checkAccount(textField)) {
                                    order.setClientName(dao.find(textField).getName());
                                    dao.updateAccountByNameOrId(order.getClientName(), order.getRemain(), order.getDate());
                                } else if (!dao.checkAccount(textField) && order.getRemain() >= 0) {
                                    int conf = JOptionPane.showConfirmDialog(null,
                                            Helpers.placeholderLabels(new JLabel("هذا الزبون غير موجود في قائمة الديون, هل تريد إضافته؟"), font),
                                            "تأكيد", JOptionPane.OK_CANCEL_OPTION);
                                    if (conf == 0) {
                                        dao.insertData(textField, order.getRemain(), Helpers.currentDateTime());
                                        DebtsView.accounts = new DebitDAO().getDebits();

                                    }
                                }
                                order.setTotalDebit(dao.returnTotalAmount(order.getClientName()));
                                OrderDAO.insertData(order);
                                System.out.println(order.getTotalDebit());
                                order.setId(OrderDAO.getOrderId());
                                order = OrderDAO.find((int) order.getId());
                                Helpers.prepareOrderDAO(order, textArea);
//                                Helpers.printBtn(Helpers.printBtnOrder(order));

                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null,
                                    Helpers.placeholderLabels(new JLabel(ex.getMessage()), font),
                                    "تحذير", JOptionPane.WARNING_MESSAGE);
                            displayOrderPreview();

                        }
                    }
                }


            });

        } else {
            JOptionPane.showMessageDialog(null,
                    Helpers.placeholderLabels(new JLabel("لا يوجد شئ لطباعته....!"), font),
                    "تحذير", JOptionPane.WARNING_MESSAGE);
            Helpers.setBarcodeFocus(barcodeField);
        }

    }

    private JPanel panelClientAndAmount() {

        nameField = new AutocompleteField(refreshNames());
        nameField.addHierarchyListener(e -> {
            if (e.getComponent().isShowing() && (e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0)
                SwingUtilities.invokeLater(e.getComponent()::requestFocusInWindow);
        });

        nameField.setHorizontalAlignment(SwingConstants.CENTER);

        nameField.setFont(font);
        nameField.addActionListener(e -> {
            if (!nameField.getText().trim().isEmpty())
                nameField.transferFocus();
        });
        amountField = new JTextField(15);
        amountField.setText("0");
        amountField.setHorizontalAlignment(SwingConstants.CENTER);
        amountField.setFont(font);
        amountField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!((c >= '0') && (c <= '9') ||
                        (c == KeyEvent.VK_BACK_SPACE) ||
                        (c == KeyEvent.VK_DELETE))) {
                    e.consume();
                }
            }
        });
        // Simple lookup based on our data list

        // Autocomplete field itself
        JPanel panel = new JPanel(new GridLayout(2, 2, 6, 25));
        panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
//        panel.setFocusable(true);
        panel.add(nameField);
        panel.add(amountField);
        return panel;
    }

    private Function<String, java.util.List<String>> refreshNames() {

        ArrayList<String> values = new ArrayList<>();
        for (Account account : DebtsView.accounts) {
            values.add(account.getName());
        }
        Function<String, List<String>> lookup = text -> values.stream()
                .filter(v -> !text.isEmpty() && v.contains(text) && !v.equals(text))
                .collect(Collectors.toList());
        return lookup;
    }


}
