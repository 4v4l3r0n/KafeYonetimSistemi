import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CafeManagementGUI {
    private JFrame frame;
    private JTextField customerNameField;
    private JTextArea orderTextArea;
    private Siparis cafeOrder;
    private List<String> beveragesMenu;
    private List<String> foodsMenu;

    public CafeManagementGUI() {
        frame = new JFrame("Cafe Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        cafeOrder = new Siparis();
        beveragesMenu = new ArrayList<>();
        foodsMenu = new ArrayList<>();

        loadMenuItemsFromFile("KafeYonetimSistemi-master/beverages.txt", beveragesMenu);
        loadMenuItemsFromFile("KafeYonetimSistemi-master/foods.txt", foodsMenu);

        JPanel mainPanel = new JPanel(new GridLayout(3, 1));

        // Customer Name Panel
        JPanel customerPanel = new JPanel();
        JLabel customerLabel = new JLabel("Customer Name:");
        customerNameField = new JTextField(15);
        JButton setCustomerButton = new JButton("Set Customer");
        setCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cafeOrder.setCustomerName(customerNameField.getText());
            }
        });

        customerPanel.add(customerLabel);
        customerPanel.add(customerNameField);
        customerPanel.add(setCustomerButton);

        // Order Panel
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderTextArea = new JTextArea();
        orderTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(orderTextArea);
        orderPanel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton addBeveragesButton = new JButton("Add Beverages");
        JButton addFoodsButton = new JButton("Add Foods");
        JButton calculateButton = new JButton("Calculate Total");
        JButton saveOrderButton = new JButton("Save Order");
        JButton cancelOrderButton = new JButton("Cancel Order");
        JButton orderHistoryButton = new JButton("Order History");
        JButton updateMenuButton = new JButton("Update Menu");

        addBeveragesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBeveragesMenuDialog();
            }
        });

        addFoodsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFoodsMenuDialog();
            }
        });

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Total Price: " + cafeOrder.hesapla());
            }
        });

        saveOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cafeOrder.dosyayaYaz();
            }
        });

        cancelOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelLastItem();
            }
        });

        orderHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showOrderHistoryDialog();
            }
        });

        updateMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMenuItems();
            }
        });

        buttonPanel.add(addBeveragesButton);
        buttonPanel.add(addFoodsButton);
        buttonPanel.add(calculateButton);
        buttonPanel.add(saveOrderButton);
        buttonPanel.add(cancelOrderButton);
        buttonPanel.add(orderHistoryButton);
        buttonPanel.add(updateMenuButton);

        // Add panels to the main panel
        mainPanel.add(customerPanel);
        mainPanel.add(orderPanel);
        mainPanel.add(buttonPanel);

        // Add main panel to the frame
        frame.add(mainPanel);

        // Make the frame visible
        frame.setVisible(true);
    }

    private void loadMenuItemsFromFile(String filePath, List<String> menuList) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                menuList.add(line.trim());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error reading menu items from file: " + e.getMessage());
        }
    }

    private void showBeveragesMenuDialog() {
        String[] beverageOptions = beveragesMenu.toArray(new String[0]);
        String selectedBeverage = (String) JOptionPane.showInputDialog(frame, "Select Beverage",
                "Beverages Menu", JOptionPane.QUESTION_MESSAGE, null, beverageOptions, beverageOptions[0]);

        if (selectedBeverage != null) {
            float price = getBeveragePrice(selectedBeverage);
            cafeOrder.urunEkle(new Icecek(price, selectedBeverage, "Regular"));
            updateOrderTextArea();
        }
    }

    private void showFoodsMenuDialog() {
        String[] foodOptions = foodsMenu.toArray(new String[0]);
        String selectedFood = (String) JOptionPane.showInputDialog(frame, "Select Food",
                "Foods Menu", JOptionPane.QUESTION_MESSAGE, null, foodOptions, foodOptions[0]);

        if (selectedFood != null) {
            float price = getFoodPrice(selectedFood);
            cafeOrder.urunEkle(new Yiyecek(price, selectedFood));
            updateOrderTextArea();
        }
    }

    private float getBeveragePrice(String beverage) {
        // You can customize this method to retrieve the actual prices for each beverage
        // For simplicity, we'll return a default price for each beverage
        switch (beverage) {
            case "Cola":
                return 2.5f;
            case "Coffee":
                return 3.0f;
            case "Tea":
                return 2.0f;
            default:
                return 0.0f;
        }
    }

    private float getFoodPrice(String food) {
        // You can customize this method to retrieve the actual prices for each food item
        // For simplicity, we'll return a default price for each food item
        switch (food) {
            case "Burger":
                return 5.0f;
            case "Pizza":
                return 8.0f;
            case "Sandwich":
                return 4.5f;
            default:
                return 0.0f;
        }
    }

    private void updateOrderTextArea() {
        orderTextArea.setText("");
        for (Urun urun : cafeOrder.urunler) {
            if (urun instanceof Icecek) {
                orderTextArea.append(stringYaz((Icecek) urun) + "\n");
            } else if (urun instanceof Yiyecek) {
                orderTextArea.append(stringYaz((Yiyecek) urun) + "\n");
            }
        }
    }

    private void cancelLastItem() {
        int lastIndex = cafeOrder.urunler.size() - 1;
        if (lastIndex >= 0) {
            cafeOrder.urunler.remove(lastIndex);
            updateOrderTextArea();
        } else {
            JOptionPane.showMessageDialog(frame, "No items to cancel.");
        }
    }

    private void showOrderHistoryDialog() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            loadOrderHistory(filePath);
        }
    }

    private void loadOrderHistory(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            StringBuilder historyText = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                historyText.append(line).append("\n");
            }

            orderTextArea.setText(historyText.toString());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error reading order history file: " + e.getMessage());
        }
    }

    private void updateMenuItems() {
        loadMenuItemsFromFile("beverages.txt", beveragesMenu);
        loadMenuItemsFromFile("foods.txt", foodsMenu);

        JOptionPane.showMessageDialog(frame, "Menu items updated successfully!");
    }

    private String stringYaz(Icecek x) {
        return x.isim + " - " + x.boy + " - " + x.fiyat;
    }

    private String stringYaz(Yiyecek x) {
        return x.isim + " - " + x.fiyat;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CafeManagementGUI();
            }
        });
    }
}
