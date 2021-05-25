package com.company.utils;

import com.company.Account;
import com.company.Order;
import com.company.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Helpers {
    public static void setBarcodeFocus(JTextField elm) {
        EventQueue.invokeLater(elm::grabFocus);
    }

    public static void printOrder(JTextArea textArea) {
        if (textArea.getText().isEmpty()) {
            return;
        }
        try {
            textArea.print();
        } catch (Exception ex) {
            //TODO
        }
    }

    public static String currentDateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd   hh:mm a");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static void prepareOrderDAO(Order order, JTextArea textArea) {
        final String line = "-----------------------------------------\n";
        String billHeader = "" + "[ اسواق وبراد الزاب \\ بإدارة مظهر عبد الغني]" + "" + "\n";
        billHeader += "---------------" + "فاتوره دفع" + "----------------" + "\n";
        billHeader += order.getDate() + "\t" + ":تاريخ" + "\n";
        billHeader += order.getId() + "\t" + ":رقم الفاتوره" + "\n";
        billHeader += line;
        String s = String.format("%.6s\t %2s %20.15s\n", "السعر" , "المادة", "الكميه");
        String billFooter = line;
        billFooter += order.getTotal() + "\t" + ":الاجمالي" + "\n";
        billFooter += order.getPaid() + "\t" + ":المدفوع" + "\n";
        billFooter += order.getRemain() + "\t" + ":المتبقي" + "\n";
        billFooter += order.getTotalDebit() + "\t" + ":الديون" + "\n";
        billFooter += String.format("الزبون: %s \t\n", order.getClientName());
        billFooter += line;
        billFooter += "شكرا لتسوقكم معنا\n";
        billFooter += line;
        billFooter += "07827626713 \\ M&G برمجة \n";
        StringBuilder output = new StringBuilder(billHeader + s + line);
        for (Product product : order.getItems()) {
            output.append(formatProduct(product));
        }
        output.append(billFooter);
        textArea.setText(String.valueOf(output));
    }

    public static JLabel placeholderLabels(JLabel label, Font font) {
        label.setFont(font);
        return label;
    }

    public static JLabel placeholderCenterLabels(JLabel label, Font font) {
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(font);
        return label;
    }

    public static String formatQuantity(float quantity) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(quantity);
    }

    public static String printDebits(ArrayList<Account> accounts) {
        final String line = "-----------------------------------------\n";
        String billHeader ="";
        billHeader += String.format(currentDateTime() + "\t" + ":تاريخ" + "\n");
        billHeader += line;
        String s = String.format("مبلغ الدين" + "\t"  + "الاسم" + "\n");
        String billFooter = line;

        String output = billHeader + s + line;
        for (Account account : accounts) {
            output += String.format(account.getTotalDebit() + "\t" + account.getName() + "\n");
        }
        output += billFooter;
        return output;
    }

    public static void print(String content){
        JTextPane billHolder = new JTextPane();
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        PageFormat pageFormat = printerJob.defaultPage();
        Paper paper = new Paper();
        paper.setSize(300.0, (paper.getHeight() + content.split("\n").length * 10.0));
        paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());
        pageFormat.setPaper(paper);
        pageFormat.setOrientation(PageFormat.PORTRAIT);
        printerJob.setPrintable(billHolder.getPrintable(null, null), pageFormat);
        billHolder.setText(content);
//        JOptionPane.showMessageDialog(null,billHolder);
//        try {TODO
////            printerJob.print();
//        } catch (PrinterException e) {
//            e.printStackTrace();
//        }
    }

    public static String printAccount(Account account){
        final String line = "-----------------------------------------\n";
        String billHeader = "" + "[ اسواق وبراد الزاب \\ بإدارة مظهر عبد الغني]" + "" + "\n";
        billHeader += "---------------" + "وصل ديون" + "----------------" + "\n";
        String s = account.getName() + "\t" + "الاسم" + "\n";
        s+= account.getID() + "\t" + "رقم الحساب" + "\n";
        s+= account.getTotalDebit() + "\t" + "مبلغ الدين" + "\n";
        billHeader+=s;
        billHeader+=line;
        billHeader += "07827626713 \\ M&G برمجة \n";
        return billHeader;
    }

    public static String printOrder(Order order) {
        final String line = "-----------------------------------------\n";
        String billHeader = "" + "[ اسواق وبراد الزاب \\ بإدارة مظهر عبد الغني]" + "" + "\n";
        billHeader += "---------------" + "فاتوره دفع" + "----------------" + "\n";
        billHeader += order.getDate() + "\t" + ":تاريخ" + "\n";
        billHeader += order.getId() + "\t" + ":رقم الفاتوره" + "\n";
        billHeader += line;
        String s = String.format("%.6s\t %2s %20.15s\n", "السعر" ,  "الكميه", "المادة");
        String billFooter = line;
        billFooter += order.getTotal() + "\t" + ":الاجمالي" + "\n";
        billFooter += order.getPaid() + "\t" + ":المدفوع" + "\n";
        billFooter += order.getRemain() + "\t" + ":المتبقي" + "\n";
        billFooter += order.getTotalDebit() + "\t" + ":الديون" + "\n";
        billFooter += String.format("الزبون: %s \t\n", order.getClientName());
        billFooter += line;
        billFooter += "شكرا لتسوقكم معنا\n";
        billFooter += line;
        billFooter += "07827626713 \\ M&G برمجة \n";
        StringBuilder output = new StringBuilder(billHeader + s + line);
        for (Product product : order.getItems()) {
            output.append(formatProduct(product));
        }
        output.append(billFooter);
        return output.toString();
    }

    private static String formatProduct(Product product) {
        return String.format("%.6s\t %2s %20.15s\n", (int) product.getPrice(), formatQty(product.getQuantity()), product.getName());
    }

    private static String formatQty(Float qty) {
        String[] parts = qty.toString().split(".");
        if (parts.length > 1) {
            if (parts[1] == "0") {
                return parts[0];
            }
        }
        return formatQuantity(qty);
    }
}
