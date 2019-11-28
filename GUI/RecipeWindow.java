package GUI;

import Model.Recipe;
import Model.Storage;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class RecipeWindow extends JPanel {

    static ImageIcon icon = new ImageIcon(
            new ImageIcon("./icons/recipe-book.png")
                    .getImage()
                    .getScaledInstance(30, 30, Image.SCALE_SMOOTH));

    List<String> curProducts;
    List<Double> curAmounts;
    int curRecipeRows = 0;
    Recipe curRecipeEdit;
    Storage storage;
    JTable recipeTable;

    public RecipeWindow(){
        this.setFont(new Font("Calibri", Font.PLAIN, 22));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Create recipe:"));

        storage = new Storage();
        curProducts = new ArrayList<String>();
        curAmounts = new ArrayList<Double>();

        recipeTable = new JTable(new javax.swing.table.TableModel(){

            String[] colNames ={
                    "Product Name",
                    "Amount"
            };

            @Override
            public int getRowCount() {
                return curRecipeRows;
            }

            @Override
            public int getColumnCount() {
                return 2;
            }

            @Override
            public String getColumnName(int i) {
                return colNames[i];
            }

            @Override
            public Class<?> getColumnClass(int i) {
                if (i==0)
                    return String.class;
                return Double.class;
            }

            @Override
            public boolean isCellEditable(int i, int i1) {
                return true;
            }

            @Override
            public Object getValueAt(int i, int i1) {
                if (i1 == 0){
                    if (i>=curProducts.size())
                        return "";
                    return curProducts.get(i);
                }else{
                    if (i>=curAmounts.size())
                        return 0.0;
                    return curAmounts.get(i);
                }
            }

            @Override
            public void setValueAt(Object o, int i, int i1) {
                if (i1 == 0)
                    curProducts.set(i, (String)o);
                else
                    try{
                        curAmounts.set(i, (Double)o);
                    }catch (NumberFormatException e){}
            }

            @Override
            public void addTableModelListener(TableModelListener tableModelListener) {

            }

            @Override
            public void removeTableModelListener(TableModelListener tableModelListener) {

            }
        });
        recipeTable.setRowHeight(24);
        recipeTable.setFont(new Font("Calibri", Font.PLAIN, 22));

        add(new JScrollPane(recipeTable));
        //------------------------------------------------------------------------


        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout());

        JButton addProd = new JButton("Add product");
        addProd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                curRecipeRows+=1;
                curProducts.add("");
                curAmounts.add(0.0);
                recipeTable.updateUI();
            }
        });

        JButton getCost = new JButton("Calculate cost");
        getCost.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                StringBuilder notProd = new StringBuilder("Not products:\n");
                int notProds = 0;
                Recipe r = new Recipe();
                try{
                    for (int i = 0; i<curRecipeRows; i++){
                        String name = (String) recipeTable.getValueAt(i, 0);
                        Double amount = (Double) recipeTable.getValueAt(i, 1);

                        if (storage.isProduct(name))
                            r.addProduct(storage.getProduct(name), amount);
                        else {
                            notProd.append(name).append("\n");
                            notProds++;
                        }
                    }

                    JOptionPane.showMessageDialog(recipeTable,
                            String.format("Current cost: %f", r.getCost()),
                            "Cost calculation",
                            JOptionPane.INFORMATION_MESSAGE);

                    if (notProds>0)
                    JOptionPane.showMessageDialog(recipeTable,
                            notProd,
                            "Not products",
                            JOptionPane.WARNING_MESSAGE);
                }catch(NumberFormatException e){
                    JOptionPane.showMessageDialog(recipeTable, "Input correct amounts!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton getRecipe = new JButton("Get this recipe");
        getRecipe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                StringBuilder notProd = new StringBuilder("Not products:\n");
                int notProds = 0;
                Recipe r = new Recipe();
                try{
                    for (int i = 0; i<curRecipeRows; i++){
                        String name = (String) recipeTable.getValueAt(i, 0);
                        Double amount = (Double) recipeTable.getValueAt(i, 1);

                        if (storage.isProduct(name))
                            storage.get(name, amount);
                        else {
                            notProd.append(name).append("\n");
                            notProds++;
                        }
                    }

                    if (notProds>0)
                    JOptionPane.showMessageDialog(recipeTable,
                            notProd,
                            "Not products",
                            JOptionPane.WARNING_MESSAGE);
                }catch(NumberFormatException e){
                    JOptionPane.showMessageDialog(recipeTable, "Input correct amounts!", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        buttonsPanel.add(addProd);
        buttonsPanel.add(getCost);
        buttonsPanel.add(getRecipe);
        add(buttonsPanel);
    }
}
