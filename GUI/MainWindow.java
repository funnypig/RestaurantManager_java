package GUI;

import javax.swing.*;
import java.awt.*;

public class MainWindow {

    public MainWindow(){
        JFrame jf = new JFrame();
        jf.setTitle("Restaurant manager");

        // menu
        JTabbedPane tabs = new JTabbedPane();

        JPanel storageWindow = new StorageWindow();
        JPanel recipeWindow = new RecipeWindow();

        // Add tabs and fonts
        tabs.addTab("Storage", StorageWindow.icon, storageWindow);
        tabs.addTab("Recipes", RecipeWindow.icon, recipeWindow);

        tabs.setOpaque(true);

        tabs.setFont(new Font("Calibri", Font.BOLD, 20));

        // Make frame stuff
        jf.setContentPane(tabs);

        jf.pack();
        jf.setSize(600, 400);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }
}
