package com.company;

import com.company.dataBase.Connect;
import com.company.dataBase.DebitDAO;
import com.company.dataBase.OrderProduct;
import com.company.dataBase.ProductDAO;
import com.formdev.flatlaf.intellijthemes.*;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneLightContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneLightIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDeepOceanContrastIJTheme;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
//                if (ComputerIdentifier.getUUID().equals("32444335-3331-5334-3759-80C16E5CED1B")) {
                FlatCyanLightIJTheme.install();
                new PrimaryView();
//                        return;

//                }
//                throw new Exception("Invalid UUID");
            } catch (Exception e) {

                JOptionPane.showMessageDialog(null,
                        "حدث خطأ في تشغيل البرنامج ....!"+e.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
            }

        });

    }
}
