package com.company.sales;

import com.company.Product;
import com.company.dataBase.DataBase;
import com.company.inventory.Inventory;
import com.company.utilities.Utility;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SalesView {
    private JTextField barcodeField;
    private JButton checkBtn;
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
    private Inventory inventory;
    public SalesView() {
        products = new ArrayList<>();
        inventory = new Inventory();
    }

    public JPanel getMainPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        Font myFont = new Font("SansSerif", Font.PLAIN, 20);
        Color myColor = Color.RED;
        panel.setBorder(BorderFactory.createTitledBorder(null, "واجهة المبيعات",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.CENTER, myFont, myColor));

        panel.add(getTable(), BorderLayout.CENTER);
        panel.add(footerPanel(), BorderLayout.SOUTH);
        panel.add(leftPanel(), BorderLayout.WEST);

        return panel;
    }

    private JScrollPane getTable() {
        model = new TableModel(columnNames);
        table = new JTable(model);
        JTableHeader header = table.getTableHeader();
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
        table.getModel().addTableModelListener(new TableModelListener() { //// to change the select value
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                Product product = products.get(row);
                if (column == 5) {
                    Boolean check = (Boolean) model.getValueAt(row, column);
                    product.setCumulative(check);

                }else if(column==4){
                    product.setQuantity((Integer) model.getValueAt(row, column));
//                    checkQuantity(product);
                }
                priceLabel.setText(sumTotalPrice(products) + "");

            }
        });

        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {

            }
        });
        renderTable();
        return new JScrollPane(table);
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

        barcodeField = new JTextField(13);
        barcodeField.addActionListener(new ListenerCheckBarcode());
        Utility.preFocus(barcodeField);
        barcodeField.setFont(font);

        checkBtn = new JButton("تنظيف القائمة");
        checkBtn.addActionListener(new ListenerUpdateQuantity());
        checkBtn.setFocusPainted(false);

        checkBtn.setBackground(new Color(0x0E395C));
        checkBtn.setForeground(new Color(0xECECEC));
        checkBtn.setFont(font);
        checkBtn.setPreferredSize(new Dimension(200, 40));

        panel.add(barcodeField);
        panel.add(checkBtn);
        return panel;
    }


    private JPanel leftPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 20));
        panel.setPreferredSize(new Dimension(350, 500));
        panel.setBorder(BorderFactory.createTitledBorder(""));

        panel.add(getTextArea());
        panel.add(getPricePanel());
        return panel;
    }

    private JScrollPane getTextArea() {
        textArea = new JTextArea();

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

            if(!barcodeField.getText().isEmpty()){
                for(Product prod : products) {
                    if(barcodeField.getText().trim().equals(prod.getBarcode())){
                        int quantity = prod.getQuantity()+1;
                        prod.setQuantity(quantity);
                        renderTable();
                        clearBarcodeField();
                        return;
                    }

                }
                for(Product product : inventory.products) {
                    if(barcodeField.getText().trim().equals(product.getBarcode())) {
                        Product prod = new Product(product.getName(), product.getCumulativePrice(), product.getSinglePrice());

                        products.add(prod);
                        renderTable();
                        clearBarcodeField();
                        return;
                    }
                }
            }
        }
    }

    private void addRow(Product product) {
        model.addRow(new Object[]{products.indexOf(product)+1, product.getName(), product.getCumulativePrice(), product.getSinglePrice(),
                product.getQuantity()});
    }

    private void clearBarcodeField() {
        barcodeField.setText("");
        barcodeField.requestFocusInWindow();
        priceLabel.setText(sumTotalPrice(products) + "");
    }

    private void renderTable() {
        for(int i = model.getRowCount() -1;i>=0; i--) {
            model.removeRow(i);
        }
        for(Product product: products) {
            addRow(product);
        }

    }

    private void updateQuantityOfProductInInventory(Product product) {
        for(Product prod: products) {
          if(prod.getBarcode().equals(product.getBarcode())){
              if(prod.isCumulative()&&product.getCumulativeQuantity()>prod.getQuantity()) {
                  product.setCumulativeQuantity(product.getCumulativeQuantity()-prod.getQuantity());
                  DataBase.updateProduct(product);

                  return;
              }

          }
        }
    }

    private class ListenerUpdateQuantity implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            for (Product product : inventory.products){
                updateQuantityOfProductInInventory(product);
            }
        }
    }



}
