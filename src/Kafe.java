    import javax.swing.*;
    import javax.swing.event.ListSelectionEvent;
    import javax.swing.event.ListSelectionListener;
    import javax.swing.filechooser.FileNameExtensionFilter;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.io.BufferedReader;
    import java.io.File;
    import java.io.FileReader;
    import java.util.ArrayList;
    import java.util.List;

    public class Kafe {
        private Siparis siparis = new Siparis();
        private DefaultListModel<String> siparisList;
        private DefaultListModel<String> sicakIceceklerList;
        private DefaultListModel<String> sogukIceceklerList;
        private DefaultListModel<String> tatlilarList;
        private DefaultListModel<String> tuzlularList;
        private static final String SICAK_FILE = "sicakIcecek.txt";
        private static final String SOGUK_FILE = "sogukIcecek.txt";
        private static final String TATLILAR_FILE = "tatli.txt";
        private static final String TUZLULAR_FILE = "tuzlu.txt";
        private List<Urun> menu = new ArrayList<>();
        private List<Urun> sicaklar = new ArrayList<>();
        private List<Urun> soguklar = new ArrayList<>();
        private List<Urun> tatlilar = new ArrayList<>();
        private List<Urun> tuzlular = new ArrayList<>();


        private JPanel anaPanel, eklenenUrunler, UrunButonlari, ustPanel, urunButonlari;
        private JList list1;
        private JButton SiparisOlustur, Iptal, Menu, Gecmis;
        private JLabel Urunler, SicakIceceklerLabel, SogukIceceklerLabel, TatlilarLabel, TuzlularLabel, labelTF, ToplamFiyat;
        private JList SicakIcecekler;
        private JList SogukIcecekler;
        private JList Tatlilar;
        private JList Tuzlular;

        public Kafe() {
            siparisList = new DefaultListModel<>();
            sicakIceceklerList = new DefaultListModel<>();
            sogukIceceklerList = new DefaultListModel<>();
            tatlilarList = new DefaultListModel<>();
            tuzlularList = new DefaultListModel<>();
            list1.setModel(siparisList);
            SicakIcecekler.setModel(sicakIceceklerList);
            SogukIcecekler.setModel(sogukIceceklerList);
            Tatlilar.setModel(tatlilarList);
            Tuzlular.setModel(tuzlularList);

            readMenuFromFile(SICAK_FILE,sicaklar,sicakIceceklerList);
            readMenuFromFile(SOGUK_FILE,soguklar,sogukIceceklerList);
            readMenuFromFile(TATLILAR_FILE,tatlilar,tatlilarList);
            readMenuFromFile(TUZLULAR_FILE,tuzlular,tuzlularList);

            SicakIcecekler.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    addSelectedItemToSiparisList(SicakIcecekler);
                }
            });

            SogukIcecekler.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    addSelectedItemToSiparisList(SogukIcecekler);
                }
            });

            Tatlilar.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    addSelectedItemToSiparisList(Tatlilar);
                }
            });

            Tuzlular.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    addSelectedItemToSiparisList(Tuzlular);
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
                    siparisList.clear();

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


        private void readMenuFromFile(String path, List<Urun> list, DefaultListModel<String> listModel) {
            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        float fiyat = Float.parseFloat(parts[0]);
                        String isim = parts[1];
                        String boy = parts[2];
                        Icecek icecek = new Icecek(fiyat, isim, boy);
                        list.add(icecek);
                        listModel.addElement(isim + " - " + boy); // Add the item to the listModel
                        menu.add(icecek);
                    } else if (parts.length == 2) {
                        float fiyat = Float.parseFloat(parts[0]);
                        String isim = parts[1];
                        Yiyecek yiyecek = new Yiyecek(fiyat, isim);
                        list.add(yiyecek);
                        listModel.addElement(isim); // Add the item to the listModel
                        menu.add(yiyecek);
                    }
                }
            } catch (Exception e) {
                System.out.println("Error reading menu from file: " + e.getMessage());
            }
        }


        private void selectItem(String itemName) {
            for (Urun urun : menu) {
                if (urun instanceof Icecek && (urun.isim + " - " + ((Icecek) urun).boy).equals(itemName)) {
                    siparisList.addElement(itemName);
                    siparis.urunEkle((Icecek) urun);
                    updateToplamFiyatLabel();
                    break;
                } else if (urun instanceof Yiyecek && urun.isim.equals(itemName)) {
                    siparisList.addElement(itemName);
                    siparis.urunEkle((Yiyecek) urun);
                    updateToplamFiyatLabel();
                    break;
                }
            }
        }
        private String promptForSize(String itemName, String[] sizes) {
            return (String) JOptionPane.showInputDialog(
                    anaPanel,
                    "Select size:",
                    itemName,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    sizes,
                    sizes[0]
            );
        }


        private void updateToplamFiyatLabel() {
            float toplamFiyat = siparis.hesapla();
            ToplamFiyat.setText("Toplam Fiyat: " + toplamFiyat);
        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new Kafe();
                }
            });
        }

        private void removeLastItem() {
            int lastIndex = siparisList.getSize() - 1;
            if (lastIndex >= 0) {
                // Get the last added item in the list
                String removedItem = siparisList.getElementAt(lastIndex);

                // Remove the item from the list
                siparisList.removeElementAt(lastIndex);

                // Find the corresponding Urun and remove it from the order
                for (Urun urun : menu) {
                    if ((urun.isim + (urun instanceof Icecek ? " - " + ((Icecek) urun).boy : "")).equals(removedItem)) {
                        siparis.urunler.remove(urun);
                        break;
                    }
                }
            }

            // Update the total price label
            updateToplamFiyatLabel();
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
        private void addSelectedItemToSiparisList(JList list) {
            Object selectedValue = list.getSelectedValue();
            if (selectedValue != null) {
                selectItem(selectedValue.toString());
                updateToplamFiyatLabel();
                list.clearSelection();  // Clear the selection after adding the item
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