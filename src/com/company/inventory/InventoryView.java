package com.company.inventory;

import com.company.Product;
import com.company.dataBase.ProductDAO;
import com.company.utilities.HeaderRenderer;
import com.company.utilities.Utility;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Inventory {
    private JTextField nameField, cumulativePriceField, singlePriceField, cumulativeQuantityField,
            singleQuantityField, remainQuantityField, expireDateField, barcodeField;
    private JButton clearBtn, addBtn;
    private JTable table;
    private Color background = new Color(0x0E395C);
    private Color foreground = new Color(0xECECEC);
    private Dimension dimension = new Dimension(200, 40);
    private final Font font = new Font("Dialog", Font.BOLD, 18);
    public ArrayList<Product> products;
    private TableModel model;


    private static final String[] columnNames = {"التسلسل", "اسم المنتج", "تأريخ الانتهاء", "السعر بالجملة",
            "السعر بالمفرد", "الكمية بالجملة", "الكمية بالمفرد", "الكمية المتبقية", "الباركود"};

    public Inventory() {
        products = ProductDAO.getProducts();
    }

    public JPanel getMainPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        Font myFont = new Font("SansSerif", Font.PLAIN, 20);
        Color myColor = Color.RED;
        panel.setBorder(BorderFactory.createTitledBorder(null, "مخزن الادوية",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.CENTER, myFont, myColor));

        panel.add(getTable(), BorderLayout.CENTER);
        panel.add(footerPanel(), BorderLayout.SOUTH);

        return panel;
    }

    private JScrollPane getTable() {
        model = new TableModel(columnNames);

        table = new JTable(model);
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer(table));


        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        table.setRowHeight(25);
        table.setFont(font);
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        table.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        table.getColumnModel().getColumn(6).setCellRenderer(centerRender);
        table.getColumnModel().getColumn(7).setCellRenderer(centerRender);
        table.getColumnModel().getColumn(8).setCellRenderer(centerRender);

        header.setFont(font);
        renderTable(products);
        table.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        return new JScrollPane(table);
    }

    private void addRow(Product product, int count) {
        model.addRow(new Object[]{count, product.getName(), product.getExpireDate(), product.getCumulativePrice(),
                product.getSinglePrice(), product.getCumulativeQuantity(),
                product.getSingleQuantity(), product.getRemainQuantity(), product.getBarcode()});
    }

    private void renderTable(ArrayList<Product> products) {
        int count = 1;
        for (Product product : products) {
            addRow(product, count);
            count++;
        }
    }

    public void addNewProduct(Product product) {
        new ProductDAO().insert(product);
    }

    private JPanel footerPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 5, 20, 10));
        panel.setBorder(BorderFactory.createTitledBorder(""));
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);


        ActionListener listener = new Listener();
        barcodeField = new Utility().field(font);
        Utility.preFocus(barcodeField);
        barcodeField.addActionListener(e -> {
            if (!barcodeField.getText().isEmpty()) {
                nameField.requestFocus();
            }
        });
        barcodeField.addActionListener(listener);


        nameField = new Utility().field(font);
        nameField.addActionListener(e -> {
            if (!nameField.getText().isEmpty()) {
                cumulativePriceField.requestFocus();
            }
        });

        cumulativePriceField = new Utility().field(font);
        cumulativePriceField.addActionListener(e -> {
            if (!cumulativePriceField.getText().isEmpty()) {
                singlePriceField.requestFocus();
            }
        });

        singlePriceField = new Utility().field(font);
        singlePriceField.addActionListener(e -> {
            if (!singlePriceField.getText().isEmpty()) {
                cumulativeQuantityField.requestFocus();
            }
        });


        cumulativeQuantityField = new Utility().field(font);
        cumulativeQuantityField.addActionListener(listener);

        singleQuantityField = new Utility().field(font);
        singleQuantityField.addActionListener(listener);

        remainQuantityField = new Utility().field(font);
        remainQuantityField.addActionListener(listener);

        expireDateField = new Utility().field(font);
        expireDateField.addActionListener(listener);

        clearBtn = new Utility().button("تفريغ الحقول", font, background, foreground, dimension);
        clearBtn.addActionListener(e -> {clearFields(); });

        addBtn = new Utility().button("اضافة منتج", font, background, foreground, dimension);
        addBtn.addActionListener(listener);


        panel.add(Utility.label("الباركود", font));
        panel.add(Utility.label("اسم المنتج", font));
        panel.add(Utility.label("سعر الجملة", font));
        panel.add(Utility.label("سعر المفرد", font));
        panel.add(Utility.label("", font));


        panel.add(barcodeField);
        panel.add(nameField);
        panel.add(cumulativePriceField);
        panel.add(singlePriceField);
        panel.add(clearBtn);

        panel.add(Utility.label("الكمية بالجملة", font));
        panel.add(Utility.label("الكمية بالمفرد", font));
        panel.add(Utility.label(" الكمية المتبقية", font));
        panel.add(Utility.label("تأريخ الانتهاء", font));
        panel.add(Utility.label("", font));

        panel.add(cumulativeQuantityField);
        panel.add(singleQuantityField);
        panel.add(remainQuantityField);
        panel.add(expireDateField);
        panel.add(addBtn);


        return panel;
    }

    private void clearFields() {
        barcodeField.setText("");
        nameField.setText("");
        cumulativePriceField.setText("");
        singlePriceField.setText("");
        cumulativeQuantityField.setText("");
        singleQuantityField.setText("");
        remainQuantityField.setText("");
        expireDateField.setText("");
        barcodeField.requestFocusInWindow();
    }

    private void updateTable() {
        for (int i = products.size() - 1; i >= 0; i--) {
            model.removeRow(i);
        }
        table.updateUI();
    }

    /**
     * method to check barcode and add new product it if not existed
     */
    private class Listener implements ActionListener {

        /**
         * Invoked when an action occurs.
         *
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                String barcode = barcodeField.getText().trim();
                if (ProductDAO.checkBarcode(barcode)) {
                    JOptionPane.showMessageDialog(null, Utility.label("هذا المنتج موجود سابقا..\n قم بإدخال منتج جديد", font),
                            "تنبيه", JOptionPane.WARNING_MESSAGE);
                    automateMoveSelectToExistProduct(barcode);
                    clearFields();


                }

                if (!barcode.isEmpty() && !nameField.getText().isEmpty() && !cumulativePriceField.getText().isEmpty()
                        && !singlePriceField.getText().isEmpty() && !cumulativePriceField.getText().isEmpty()&&
                        !singleQuantityField.getText().isEmpty()&&!remainQuantityField.getText().isEmpty()) {
                    String name = nameField.getText().trim();
                    String expireDate = expireDateField.getText().trim();
                    int cumPrice = Integer.parseInt(cumulativePriceField.getText().trim());
                    int singlePrice = Integer.parseInt(singlePriceField.getText().trim());
                    int cumQuantity = Integer.parseInt(cumulativeQuantityField.getText().trim());
                    int singQuantity = Integer.parseInt(singleQuantityField.getText().trim());
                    int remainQuantity = Integer.parseInt(remainQuantityField.getText().trim());
                    Product product = new Product(name, expireDate, cumPrice, singlePrice, cumQuantity, singQuantity,
                            remainQuantity, barcode);
                    addNewProduct(product);
                    clearFields();
//                    model.removeRow();
                    updateTable();
                    barcodeField.requestFocusInWindow();
                    products = ProductDAO.getProducts();
                    renderTable(products);
                    automateMoveSelectLastProduct();

                }

            }
            catch (Exception exception) {
                //Todo
            }


        }
    }

    private void automateMoveSelectLastProduct() {
        table.scrollRectToVisible(table.getCellRect(table.getRowCount() - 1, 0, true));
        table.changeSelection(table.getRowCount() - 1, 0, true, false);
        table.updateUI();
    }

    private void automateMoveSelectToExistProduct(String barcode) {
        int indexRow = returnProductIndex(barcode);
        table.scrollRectToVisible(table.getCellRect(indexRow, 0, true));
        table.changeSelection(indexRow, 0, false, false);

    }

    /***
     *
     * @param barcode
     * @return product index in table to exist product or to new product that's added
     */
    private int returnProductIndex(String barcode) {
        for (Product prod : products) {
            if (prod.getBarcode().equals(barcode.trim())) {
                return products.indexOf(prod);
            }
        }
        return 0;
    }

}
