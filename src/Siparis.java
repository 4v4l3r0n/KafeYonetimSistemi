import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class Siparis {
    List<Urun> urunler = new ArrayList<>();
    private String customerName; // New field for customer name
    void setCustomerName(String customerName) {
        this.customerName = customerName;
    }


    void urunEkle(Urun x){
        urunler.add(x);
    }
    float hesapla(){
        float a=0;
        for(Urun x:urunler) {
            a+=x.fiyat;
        }
        return a;
    }
    String stringYaz(Urun x){
        return x.isim+" - "+x.fiyat;
    }
    void dosyayaYaz() {
        try {
            // Generate a unique file name based on the current timestamp
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String dosyaAdi = "siparis_detay_" + customerName + "_" + timeStamp + ".txt";

            try (BufferedWriter yazici = new BufferedWriter(new FileWriter(dosyaAdi))) {
                yazici.write("Müşteri Adı: " + customerName);
                yazici.newLine();

                for (Urun urun : urunler) {
                    yazici.write(stringYaz(urun));
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