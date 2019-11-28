package GUI;

import Model.AmountUnit;
import Model.Product;
import Model.Storage;
import javafx.geometry.VerticalDirection;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;

class TableModel implements javax.swing.table.TableModel {

    private Storage storage;

    public TableModel(Storage s){
        storage = s;
    }

    @Override
    public int getRowCount() {
        return storage.getProductCount();
    }

    @Override
    public int getColumnCount() {
        return Storage.Fields.length;
    }

    @Override
    public String getColumnName(int i) {
        return Storage.Fields[i];
    }

    @Override
    public Class<?> getColumnClass(int i) {
        switch (Storage.Fields[i]){
            case "id" : return Integer.class;
            case "name": return String.class;
            case "amountUnit": return String.class;
            case "amount": return Double.class;
            case "cost": return Double.class;
            case "icon": return ImageIcon.class;
            default: return String.class;
        }
    }

    @Override
    public boolean isCellEditable(int i, int i1) {
        return false;
    }

    @Override
    public Object getValueAt(int i, int i1) {
        Object val = storage.getProductField(storage.getProductAt(i), Storage.Fields[i1]);
        if (Storage.Fields[i1].equals("icon")){
            val = new ImageIcon(
                    new ImageIcon("./icons/"+val)
                            .getImage()
                            .getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        }
        return val;
    }

    @Override
    public void setValueAt(Object o, int i, int i1) {

    }

    @Override
    public void addTableModelListener(TableModelListener tableModelListener) {

    }

    @Override
    public void removeTableModelListener(TableModelListener tableModelListener) {

    }
}

public class StorageWindow extends JPanel {

    static ImageIcon icon = new ImageIcon(
            new ImageIcon("./icons/warehouse.png")
                    .getImage()
                    .getScaledInstance(25, 25, Image.SCALE_SMOOTH));

    Storage storage;
    JTable table;

    static String iconPath = "";

    public StorageWindow(){
        storage = new Storage();

        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(3,3,3,3));

        // First line of window
        JPanel add_product = new JPanel();
        add_product.setLayout(new GridLayout());

        JTextField name = new JTextField("Name");

        JComboBox amountUnit = new JComboBox();

        for (AmountUnit s : AmountUnit.values()){
            amountUnit.addItem(s);
        }

        JTextField amount = new JTextField("amount");
        JTextField cost = new JTextField("cost");
        JFileChooser iconChooser = new JFileChooser();
        iconChooser.setFileFilter(new FileNameExtensionFilter("Image", "png", "jpg", "ico"));

        JButton showFileChooser = new JButton("Choose icon");
        showFileChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (JFileChooser.APPROVE_OPTION == iconChooser.showDialog((Component) actionEvent.getSource(), "Choose")){
                    iconPath = iconChooser.getSelectedFile().getName();
                }
            }
        });

        add_product.add(name);
        add_product.add(amountUnit);
        add_product.add(amount);
        add_product.add(cost);
        add_product.add(showFileChooser);

        add(add_product);

        // Buttons
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                table.updateUI();
            }
        });

        JButton addProductButton = new JButton("Add product");
        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try{
                    Double _amount = Double.parseDouble(amount.getText());
                    String _name = name.getText();
                    String _amountUnit = amountUnit.getSelectedItem().toString();
                    Double _cost = Double.parseDouble(cost.getText());

                    storage.addProduct(_name, _amountUnit, _amount, _cost, iconPath);
                    iconPath = "";
                }catch (Exception e){
                    JOptionPane.showMessageDialog((Component) actionEvent.getSource(),
                            "Check input data!\nIf you are sure it's ok - talk to developer, it's his fault",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        });

        JPanel buttonGrid = new JPanel();
        buttonGrid.setLayout(new GridLayout());

        buttonGrid.add(addProductButton);
        buttonGrid.add(updateButton);

        add(buttonGrid);

        // Table
        table = new JTable(new TableModel(storage));

        table.setFont(new Font("Calibri", Font.PLAIN, 20));
        table.setAutoResizeMode(1);
        table.setRowHeight(30);

        add(new JScrollPane(table));
    }
}
