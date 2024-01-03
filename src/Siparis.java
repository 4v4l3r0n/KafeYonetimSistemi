import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class Siparis {
    List<Urun> urunler = new ArrayList<>();
    private String customerName;
    void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    void urunEkle(Icecek x){
        urunler.add(x);
    }
    void urunEkle(Yiyecek x) {
        urunler.add(x);
    }

    float hesapla() {
        float total = 0;
        for (Urun urun : urunler) {
            if (urun instanceof Icecek) {
                total += ((Icecek) urun).fiyat;
            } else if (urun instanceof Yiyecek) {
                total += ((Yiyecek) urun).fiyat;
            }
        }
        return total;
    }
    String stringYaz(Icecek x){
        return x.isim+" - "+x.boy+" - "+x.fiyat;
    }
    String stringYaz(Yiyecek x){
        return x.isim+" - "+x.fiyat;
    }
    void dosyayaYaz() {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String dosyaAdi = "siparis_" + customerName + "_" + timeStamp + ".txt";

            try (BufferedWriter yazici = new BufferedWriter(new FileWriter(dosyaAdi))) {
                yazici.write("Müşteri Adı: " + customerName);
                yazici.newLine();

                for (Urun urun : urunler) {
                    if (urun instanceof Icecek) {
                        yazici.write(stringYaz((Icecek) urun));
                    } else if (urun instanceof Yiyecek) {
                        yazici.write(stringYaz((Yiyecek) urun));
                    }
                    yazici.newLine();
                }
                yazici.write("Toplam Fiyat: " + hesapla());
            }

            System.out.println("Sipariş detayları başarıyla kaydedildi. Dosya adı: " + dosyaAdi);

            urunler.clear();
        } catch (Exception e) {
            System.out.println("Dosyaya yazma sırasında hata oluştu: " + e.getMessage());
        }
    }

}