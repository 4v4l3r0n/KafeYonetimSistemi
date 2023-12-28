import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class YemekUygulamasi {
    private DefaultListModel<String> siparisListModel;
    private DefaultListModel<String> menuListModel;

    private List<String> yiyecekListesi;
    private List<String> icecekListesi;

    public YemekUygulamasi() {
        // Ana frame
        JFrame frame = new JFrame("Yemek Uygulaması");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Sol panel: Order List ve Sipariş İşlemleri
        JPanel solPanel = new JPanel();
        solPanel.setLayout(new BorderLayout());

        // Sipariş oluştur ve iptal et butonları
        JButton siparisOlusturButton = new JButton("Sipariş Oluştur");
        JButton iptalEtButton = new JButton("İptal Et");

        // Butonları sol panele ekle
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
        buttonPanel.add(siparisOlusturButton);
        buttonPanel.add(iptalEtButton);

        solPanel.add(buttonPanel, BorderLayout.NORTH);

        // Sipariş listesi için model
        siparisListModel = new DefaultListModel<>();
        JList<String> siparisList = new JList<>(siparisListModel);
        JScrollPane siparisScrollPane = new JScrollPane(siparisList);

        // Sipariş listesi etiketi
        JLabel siparisLabel = new JLabel("Sipariş Listesi");
        siparisLabel.setHorizontalAlignment(JLabel.CENTER);
        siparisLabel.setPreferredSize(new Dimension(100, 20)); // Etiket boyutunu ayarla

        solPanel.add(siparisLabel, BorderLayout.SOUTH); // Etiketi alt panele yerleştir
        solPanel.add(siparisScrollPane, BorderLayout.CENTER);

        // Sağ panel: Menü butonları ve Menü İçeriği
        JPanel sagPanel = new JPanel();
        sagPanel.setLayout(new BorderLayout());

        // Menü butonları
        JPanel menuButtonPanel = new JPanel();
        menuButtonPanel.setLayout(new GridLayout(1, 3));

        JButton yiyeceklerButon = new JButton("Yiyecekler");
        JButton iceceklerButon = new JButton("İçecekler");
        JButton menuGuncelleButton = new JButton("Menü Güncelle");

        // Butonları küçültme
        Font buttonFont = new Font(yiyeceklerButon.getFont().getName(), Font.PLAIN, 12);
        yiyeceklerButon.setFont(buttonFont);
        iceceklerButon.setFont(buttonFont);
        menuGuncelleButton.setFont(buttonFont);

        menuButtonPanel.add(yiyeceklerButon);
        menuButtonPanel.add(iceceklerButon);
        menuButtonPanel.add(menuGuncelleButton);

        // Menü içeriği için liste ve model
        menuListModel = new DefaultListModel<>();
        JList<String> menuList = new JList<>(menuListModel);
        JScrollPane menuScrollPane = new JScrollPane(menuList);

        // Sağ paneli düzenle
        sagPanel.add(menuButtonPanel, BorderLayout.NORTH);
        sagPanel.add(menuScrollPane, BorderLayout.CENTER);

        // Menü güncelle butonuna tıklama olayı
        menuGuncelleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Kullanıcıdan yiyecek veya içecek adını al
                String yeniUrun = JOptionPane.showInputDialog(frame, "Yeni Ürün Adı:");

                // Eğer kullanıcı bir şey girmişse ve "Cancel" düğmesine basmamışsa ekleyin
                if (yeniUrun != null && !yeniUrun.isEmpty()) {
                    // Kullanıcıdan yiyecek veya içecek seçimini al
                    String[] secenekler = {"Yiyecek", "İçecek"};
                    int secim = JOptionPane.showOptionDialog(
                            frame,
                            "Seçim Yapınız",
                            "Yiyecek veya İçecek Seçimi",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            secenekler,
                            secenekler[0]
                    );

                    // Eğer kullanıcı bir seçim yapmışsa, seçilen listeye ürünü ekle
                    if (secim != JOptionPane.CLOSED_OPTION) {
                        if (secim == 0) { // "Yiyecek" seçildiyse
                            yiyecekListesi.add(yeniUrun);
                        } else if (secim == 1) { // "İçecek" seçildiyse
                            icecekListesi.add(yeniUrun);
                        }

                        // Menüyü güncelle
                        guncelleMenu();
                    }
                }
            }
        });


        // Yiyecekler butonuna tıklama olayı
        yiyeceklerButon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                temizleVeEkle(yiyecekListesi.toArray(new String[0]));
            }
        });

        // İçecekler butonuna tıklama olayı
        iceceklerButon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                temizleVeEkle(icecekListesi.toArray(new String[0]));
            }
        });

        // Menu listesine tıklama olayı
        menuList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JList<String> list = (JList<String>) evt.getSource();
                if (evt.getClickCount() == 1) {
                    // Sipariş listesine ekle
                    int index = list.locationToIndex(evt.getPoint());
                    String secilenUrun = menuListModel.getElementAt(index);
                    siparisListModel.addElement(secilenUrun);
                }
            }
        });

        // Sipariş oluştur butonuna tıklama olayı
        siparisOlusturButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Sipariş oluşturma işlemleri
                // Bu kısmı isteğinize göre doldurun
                JOptionPane.showMessageDialog(frame, "Sipariş Oluşturuldu!");
            }
        });

        // İptal et butonuna tıklama olayı
        iptalEtButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Son eklenen ürünü iptal et
                int lastIndex = siparisListModel.getSize() - 1;
                if (lastIndex >= 0) {
                    siparisListModel.removeElementAt(lastIndex);
                }
            }
        });

        // Ana frame'e panelleri ekle
        frame.add(solPanel, BorderLayout.WEST);
        frame.add(sagPanel, BorderLayout.CENTER);

        // Frame'i görünür yap
        frame.setVisible(true);

        // Yiyecek ve içecek listelerini başlat
        yiyecekListesi = new ArrayList<>();
        icecekListesi = new ArrayList<>();

        // Başlangıçta menüyü güncelle
        guncelleMenu();
    }

    private void temizleVeEkle(String... urunler) {
        menuListModel.clear();
        for (String urun : urunler) {
            menuListModel.addElement(urun);
        }
    }

    private void guncelleMenu() {
        // Menüyü güncelleme işlemleri
        menuListModel.clear();
        menuListModel.addElement("Yiyecekler:");
        for (String yiyecek : yiyecekListesi) {
            menuListModel.addElement(yiyecek);
        }
        menuListModel.addElement(""); // Bir boşluk ekleyerek içecekler ve yiyecekler arasına bir boşluk bırakabilirsiniz
        menuListModel.addElement("İçecekler:");
        for (String icecek : icecekListesi) {
            menuListModel.addElement(icecek);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new YemekUygulamasi();
            }
        });
    }
}
