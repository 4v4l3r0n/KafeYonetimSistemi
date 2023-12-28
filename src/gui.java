    import javax.swing.*;
    import javax.swing.filechooser.FileNameExtensionFilter;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.io.BufferedReader;
    import java.io.File;
    import java.io.FileReader;
    import java.util.ArrayList;
    import java.util.List;

    public class gui {
        private Siparis siparis = new Siparis();
        private DefaultListModel<String> listModel;
        private static final String MENU_FILE_PATH = "menu.txt";
        private List<Urun> menu = new ArrayList<>();


        private JPanel anaPanel, eklenenUrunler, UrunButonlari, ustPanel, urunButonlari;
        private JButton cheesecakeButton, sandvicButton, smoothieButton, filtreKahveButton, americanoButton, espressoButton, bubbleTeaButton, cappuccinoButton, latteButton, cayButton, macchiatoButton, frappeButton, limonataButton, eklerButton, donutButton, kruvasanButton, corekButton;
        private JList list1;
        private JButton SiparisOlustur, Iptal, Menu, Gecmis;
        private JLabel Urunler, SicakIceceklerLabel, SogukIceceklerLabel, TatlilarLabel, TuzlularLabel, labelTF, ToplamFiyat;


        public gui() {
            listModel = new DefaultListModel<>();
            list1.setModel(listModel);
            readMenuFromFile();


            americanoButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Display a dialog for selecting the size
                    String[] sizes = {"Small", "Medium", "Large"};
                    String selectedSize = (String) JOptionPane.showInputDialog(
                            anaPanel,
                            "Select size:",
                            "Americano Size",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            sizes,
                            sizes[0]);

                    if (selectedSize != null) {
                        // If a size is selected, add the Americano with the selected size to the order
                        String itemName = "Americano - " + selectedSize;
                        selectItem(itemName);
                    }
                }
            });
            Iptal.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removeLastItem();
                }
            });
            SiparisOlustur.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Get the customer name from user input (you can use a JTextField or any other input method)
                    String customerName = JOptionPane.showInputDialog(anaPanel, "Enter customer name:");

                    // Set the customer name in the Siparis object
                    siparis.setCustomerName(customerName);

                    siparis.dosyayaYaz();

                    // Clear the listModel to remove the current items in list1
                    listModel.clear();

                    // Display a message indicating that the order details have been successfully saved
                    JOptionPane.showMessageDialog(anaPanel, "Sipariş detayları başarıyla kaydedildi.");

                    // Update the total price label after clearing the list
                    updateToplamFiyatLabel();
                }
            });
            Gecmis.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showSavedSiparisFiles();
                }
            });

            JFrame frame = new JFrame("gui");
            frame.setContentPane(this.anaPanel);
            frame.setSize(1200,700);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.setVisible(true);

        }


        private void readMenuFromFile() {
            try (BufferedReader reader = new BufferedReader(new FileReader(MENU_FILE_PATH))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        float fiyat = Float.parseFloat(parts[0]);
                        String isim = parts[1];
                        String boy = parts[2];
                        Urun urun = new Urun(fiyat, isim, boy) {};
                        menu.add(urun);
                    }
                }
            } catch (Exception e) {
                System.out.println("Error reading menu from file: " + e.getMessage());
            }
        }

        private void selectItem(String itemName) {
            for (Urun urun : menu) {
                if ((urun.isim + " - " + urun.boy).equals(itemName)) {
                    listModel.addElement(itemName);
                    siparis.urunEkle(urun);
                    updateToplamFiyatLabel();
                    break;  // Stop searching once found
                }
            }
        }

        private void updateToplamFiyatLabel() {
            float toplamFiyat = siparis.hesapla();
            ToplamFiyat.setText("Toplam Fiyat: " + toplamFiyat);
        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new gui();
                }
            });
        }

        private void removeLastItem() {
            int lastIndex = listModel.getSize() - 1;
            if (lastIndex >= 0) {
                // Get the last added item in the list
                String removedItem = listModel.getElementAt(lastIndex);

                // Remove the item from the list
                listModel.removeElementAt(lastIndex);

                // Find the corresponding Urun and remove it from the order
                for (Urun urun : menu) {
                    if (urun.isim.equals(removedItem)) {
                        siparis.urunler.remove(urun);
                        break;
                    }
                }

                // Update the total price label
                updateToplamFiyatLabel();
            }
        }

        private void showSavedSiparisFiles() {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Geçmiş Sipariş Dosyalarını Seç");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
            fileChooser.setCurrentDirectory(new File(".")); // Set the default directory

            int result = fileChooser.showOpenDialog(anaPanel);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                // Open and display the contents of the selected file
                openAndDisplayFile(selectedFile);
            }
        }

        private void openAndDisplayFile(File file) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }

                // Display the contents of the file (you can use JTextArea or any other component)
                JTextArea textArea = new JTextArea(content.toString());
                textArea.setEditable(false);

                // Show the contents in a JOptionPane
                JOptionPane.showMessageDialog(anaPanel, new JScrollPane(textArea), "Sipariş Detayları",
                        JOptionPane.PLAIN_MESSAGE);
            } catch (Exception ex) {
                System.out.println("Error opening file: " + ex.getMessage());
            }
        }
    }