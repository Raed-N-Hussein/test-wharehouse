package com.company.inventory;

import com.company.Product;
import com.company.dataBase.ProductDAO;
import com.company.debts.DebtsView;
import com.company.utilities.*;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.demo.TableEditorsDemo;
import com.github.lgooddatepicker.optionalusertools.DateChangeListener;
import com.github.lgooddatepicker.tableeditors.DateTableEditor;
import com.github.lgooddatepicker.tableeditors.DateTimeTableEditor;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;
import org.jdatepicker.JDatePicker;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class InventoryView {
    private JTextField nameField, cumulativePriceField, singlePriceField, cumulativeQuantityField,
            singleQuantityField, remainQuantityField, barcodeField;
    private JButton clearBtn, addBtn;
    private JTable table;
    private Color background = new Color(0x0E395C);
    private Color foreground = new Color(0xECECEC);
    private Dimension dimension = new Dimension(200, 40);
    private final Font font = new Font("Dialog", Font.BOLD, 18);
    public static ArrayList<Product> products;
    private TableModel model;
    private DatePicker expireDate;
    private final Color myColor = Color.RED;


    private static final String[] columnNames = {"التسلسل", "اسم المنتج", "تأريخ الانتهاء", "السعر بالجملة",
            "السعر بالمفرد", "الكمية بالجملة", "الكمية بالمفرد", "الكمية المتبقية", "الباركود"};

    public InventoryView() {
        products = ProductDAO.getProducts();
    }

    public JPanel getMainPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(null, "مخزن الادوية",
                TitledBorder.LEFT,
                TitledBorder.CENTER, font, myColor));
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        panel.add(getSearchFieldPanel(), BorderLayout.NORTH);
        panel.add(getTable(), BorderLayout.CENTER);
        panel.add(footerPanel(), BorderLayout.SOUTH);

        return panel;
    }

    private JScrollPane getTable() {
        model = new TableModel(columnNames);
        table = new PrepareTable(model);

        TableModelListener modelListener = new ModelListener();
        model.addTableModelListener(modelListener);
        table.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                table.getSelectionModel().clearSelection();
            }
        });
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTableHeader header = table.getTableHeader();

        header.setDefaultRenderer(new HeaderRenderer(table));
        DateTableEditor dateTableEditor = new DateTableEditor();
        dateTableEditor.getDatePickerSettings().setFormatForDatesCommonEra("yyyy-MM-dd");
        table.setDefaultEditor(LocalDateTime.class, dateTableEditor);
        table.setDefaultRenderer(LocalDateTime.class, dateTableEditor);
        table.getColumnModel().getColumn(2).setCellEditor(table.getDefaultEditor(LocalDateTime.class));
        table.getColumnModel().getColumn(2).setCellRenderer(table.getDefaultRenderer(LocalDateTime.class));

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        table.setRowHeight(35);
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

    private JPanel getSearchFieldPanel() {
        JTextField searchField = new JTextField(20);
        searchField.setHorizontalAlignment(SwingConstants.CENTER);
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                searchField.setForeground(new Color(0x0A0A0A));
                searchField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                searchField.setForeground(new Color(0x777778));
                searchField.setText("بحث");
            }
        });
        searchField.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        Font font = new Font(Font.DIALOG, Font.PLAIN, 20);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                renderTable(searchProduct(searchField.getText()));

            }

        });
        Helpers.setBarcodeFocus(searchField);
        searchField.setFont(font);
        searchField.setPreferredSize(new Dimension(10, 30));
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 20));
        panel.add(new JLabel(""));
        panel.add(searchField);
        panel.add(Helpers.placeholderLabels(new JLabel("بحث :"), font));
        return panel;
    }

    /**
     *
     * @param keyword to search by name or barcode of product
     * @return array list as products
     */
    private ArrayList<Product> searchProduct(String keyword) {
        ArrayList<Product> searchResults = new ArrayList<>();
        if (keyword.trim().length()<1) {
            return products;
        }
        for (Product product : products) {
            if (product.getName().contains(keyword) || product.getBarcode().contains(keyword)) {
                searchResults.add(product);
            }

        }
        return searchResults;
    }


    /**
     * model listener if any change on table that's happened
     */
    private class ModelListener implements TableModelListener {

        @Override
        public void tableChanged(TableModelEvent e) {
            try {
                if (e.getColumn() < 0) return;
                Product product = new Product();
                product.setName((String) table.getValueAt(e.getFirstRow(), 1));
                product.setExpireDate(table.getValueAt(e.getFirstRow(), 2).toString());
                product.setCumulativePrice(Integer.parseInt(table.getValueAt(e.getFirstRow(), 3)+""));
                product.setSinglePrice(Integer.parseInt(table.getValueAt(e.getFirstRow(), 4)+""));
                product.setCumulativeQuantity(Integer.parseInt(table.getValueAt(e.getFirstRow(), 5)+""));
                product.setSingleQuantity(Integer.parseInt(table.getValueAt(e.getFirstRow(), 6)+""));
                product.setRemainQuantity(Integer.parseInt(table.getValueAt(e.getFirstRow(), 7)+""));
                product.setBarcode((String)table.getValueAt(e.getFirstRow(), 8));
                ProductDAO.updateProduct(product);
                invokeDataChange();
                automateMoveSelectToExistProduct(product.getBarcode());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private void invokeDataChange() {
        this.renderTable(ProductDAO.getProducts());
    }

    private void addRow(Product product, int count) {
        model.addRow(new Object[]{count, product.getName(), product.getExpireDate(), product.getCumulativePrice(),
                product.getSinglePrice(), product.getCumulativeQuantity(),
                product.getSingleQuantity(), product.getRemainQuantity(), product.getBarcode()});
    }

    private void renderTable(ArrayList<Product> products) {
        model.setRowCount(0);
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
        panel.setBorder(BorderFactory.createTitledBorder(null, "إضافة منتج",
                TitledBorder.LEFT,
                TitledBorder.CENTER, font, myColor));
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


        expireDate = getDatePicker();

        expireDate.setName("E");// must be as model alone


        clearBtn = new Utility().button("تفريغ الحقول", font, background, foreground, dimension);
        clearBtn.addActionListener(e -> {
            clearFields();
        });
        addBtn = new Utility().button("أضف", font, background, foreground, dimension);
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
        panel.add(Utility.label("تأريخ الانتهاء", font));
        panel.add(Utility.label("", font));
        panel.add(Utility.label("", font));

        panel.add(cumulativeQuantityField);
        panel.add(singleQuantityField);


        panel.add(expireDate);
        panel.add(Utility.label("", font));
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

        barcodeField.requestFocusInWindow();
    }

    private void updateTable() {
        table.removeAll();
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
                        && !singlePriceField.getText().isEmpty() && !cumulativeQuantityField.getText().isEmpty() &&
                        !singleQuantityField.getText().isEmpty() && !expireDate.getText().isEmpty()) {

                    String name = nameField.getText().trim();
                    String expire = expireDate.getText();
                    int cumPrice = Integer.parseInt(cumulativePriceField.getText().trim());
                    int singlePrice = Integer.parseInt(singlePriceField.getText().trim());
                    int cumQuantity = Integer.parseInt(cumulativeQuantityField.getText().trim());
                    int singQuantity = Integer.parseInt(singleQuantityField.getText().trim());
                    Product product = new Product(name, expire, cumPrice,
                            singlePrice, cumQuantity, singQuantity,
                            singQuantity, barcode);
                    addNewProduct(product);
                    clearFields();
//
                    products = ProductDAO.getProducts();
                    updateTable();
                    barcodeField.requestFocusInWindow();

                    renderTable(products);
                    automateMoveSelectLastProduct();
                }

            } catch (Exception exception) {
                exception.printStackTrace();
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

    private DatePicker getDatePicker() {
        expireDate = new DatePicker();
        expireDate.getSettings().setColor(DatePickerSettings.DateArea.TextFieldBackgroundValidDate, Color.RED);
        expireDate.getSettings().setColor(DatePickerSettings.DateArea.DatePickerTextValidDate, Color.white);
        expireDate.getSettings().setFontValidDate(new Font(Font.DIALOG, Font.BOLD, 18));

        expireDate.setDateToToday();
        expireDate.getSettings().setFormatForDatesCommonEra("yyyy-MM-dd");
        expireDate.getComponentDateTextField().setEditable(false);
        expireDate.addDateChangeListener(new DateChangeListener() {
            @Override
            public void dateChanged(DateChangeEvent event) {
                SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                date.setLenient(false);

                Date expiry = null;
                try {
                    expiry = date.parse(expireDate.getDate().toString());

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                assert expiry != null;
                boolean expired = expiry.before(new Date()) || expiry.equals(new Date());
                if (expired) {
                    expireDate.getSettings().setColor(DatePickerSettings.DateArea.TextFieldBackgroundValidDate, Color.RED);
                    expireDate.getSettings().setColor(DatePickerSettings.DateArea.DatePickerTextValidDate, Color.white);
                } else {
                    expireDate.getSettings().setColor(DatePickerSettings.DateArea.TextFieldBackgroundValidDate, Color.white);
                    expireDate.getSettings().setColor(DatePickerSettings.DateArea.DatePickerTextValidDate, Color.green);
                }
            }
        });
        return expireDate;
    }
    

}
