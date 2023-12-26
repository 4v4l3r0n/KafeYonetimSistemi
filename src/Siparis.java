import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Siparis {
    List<Urun> urunler = new ArrayList<>();
    float toplamFiyat;

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
}
