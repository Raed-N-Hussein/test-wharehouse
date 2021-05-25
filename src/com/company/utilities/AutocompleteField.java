package com.company.utilities;

import com.company.sales.SalesView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author Mikle Garin
 */

public class AutocompleteField extends JTextField implements FocusListener, DocumentListener, KeyListener{
    /**
     * {@link Function} for text lookup.
     * It simply returns {@link List} of {@link String} for the text we are looking results for.
     */
    private Function<String, List<String>> lookup;

    /**
     * {@link List} of lookup results.
     * It is cached to optimize performance for more complex lookups.
     */
    private List<String> results;

    /**
     * {@link JWindow} used to display offered options.
     */
    private JWindow popup;

    /**
     * Lookup results {@link JList}.
     */
    private JList list;

    /**
     * {@link #list} model.
     */
    private ListModel model;

    /**
     * Constructs {@link AutocompleteField}.
     *
     * @param lookup {@link Function} for text lookup
     */
    public AutocompleteField(Function<String, List<String>> lookup) {
        super();
        initiate(lookup);
    }

    public void initiate(Function<String, List<String>> lookup) {
        this.lookup = lookup;
        this.results = new ArrayList<>();

        Window parent = SwingUtilities.getWindowAncestor(this);

        popup = new JWindow(parent);


        popup.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
            }
        });

        popup.setType(Window.Type.POPUP);
        popup.setFocusableWindowState(true);
        popup.setAlwaysOnTop(true);
        model = new ListModel();
        list = new JList(model);
        list.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
        JScrollPane scrollPane = new JScrollPane(list){
            @Override
            public Dimension getPreferredSize() {
                final Dimension ps = super.getPreferredSize();
                ps.width = AutocompleteField.this.getWidth();
                return ps;
            }
        };
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                SalesView.nameField.setText((String) list.getSelectedValue());
            }
        });

        popup.add(scrollPane);
        list.addListSelectionListener(e -> {
            list.ensureIndexIsVisible(list.getSelectedIndex());
        });

        addFocusListener(this);
        getDocument().addDocumentListener(this);
        addKeyListener(this);
    }

    /**
     * Displays autocomplete popup at the correct location.
     */
    private void showAutocompletePopup() {
        Point los = AutocompleteField.this.getLocationOnScreen();
        popup.setLocation(los.x, los.y + getHeight());
        popup.setVisible(true);
    }

    /**
     * Closes autocomplete popup.
     */
    private void hideAutocompletePopup() {
        popup.setVisible(false);
    }

    @Override
    public void focusGained(FocusEvent e) {
        SwingUtilities.invokeLater(() -> {
            if (results.size() > 0) {
                showAutocompletePopup();
            }
        });
    }

    private void documentChanged() {
        SwingUtilities.invokeLater(() -> {
            // Updating results list

            results.clear();
            if(SalesView.nameField.getText().isEmpty()){
                results.clear();
                hideAutocompletePopup();
            }


            results.addAll(lookup.apply(getText()));
            // Updating list view
            model.updateView();
            list.setVisibleRowCount(Math.min(results.size(), 10));


            // Ensure autocomplete popup has correct size
            popup.pack();

            // Display or hide popup depending on the results
            if (results.size() > 0) {
                list.setSelectedIndex(0);
                showAutocompletePopup();
            } else {
                results.add(SalesView.nameField.getText().trim());
                hideAutocompletePopup();
            }

        });
    }

    @Override
    public void focusLost(FocusEvent e) {
        SwingUtilities.invokeLater(this::hideAutocompletePopup);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            int index = list.getSelectedIndex();
            if (index > 0) {
                list.setSelectedIndex(index - 1);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            int index = list.getSelectedIndex();
            if (index != -1 && list.getModel().getSize() > index + 1) {
                list.setSelectedIndex(index + 1);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER ) {
            try {
                String text = (String) list.getSelectedValue();
                setText(text);
                setCaretPosition(text.length());

            } catch (Exception ex) {
                //TODO
            }

        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            hideAutocompletePopup();
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        documentChanged();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        documentChanged();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        documentChanged();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Do nothing
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Do nothing
    }


    private class ListModel extends AbstractListModel {
        @Override
        public int getSize() {
            return results.size();
        }

        @Override
        public Object getElementAt(int index) {
            if(index >= results.size()){
                return 0;
            }
            return results.get(index);
        }

        /**
         * Properly updates list view.
         */
        public void updateView() {
            super.fireContentsChanged(AutocompleteField.this, 0, getSize());
        }

    }


}
