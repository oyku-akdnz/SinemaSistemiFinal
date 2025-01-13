import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

// Temel Sınıf: BaseEntity
abstract class BaseEntity {
    private int id;
    private String ad;

    public BaseEntity(int id, String ad) {
        this.id = id;
        this.ad = ad;
    }

    public int getId() {
        return id;
    }

    public String getAd() {
        return ad;
    }

    // Polymorphism için temel metot
    public abstract void BilgiGoster();
}

// Film Sınıfı
class Film extends BaseEntity {
    private int sure;
    private String tur;

    public Film(int id, String ad, int sure, String tur) {
        super(id, ad);
        this.sure = sure;
        this.tur = tur;
    }

    @Override
    public void BilgiGoster() {
        System.out.println("Film Bilgileri:");
        System.out.println("ID: " + getId());
        System.out.println("Ad: " + getAd());
        System.out.println("Süre: " + sure + " dakika");
        System.out.println("Tür: " + tur);
    }

    public int getSure() {
        return sure;
    }

    public String getTur() {
        return tur;
    }

    // Film'i JSON formatında döndür
    public String toJSON() {
        return String.format("{\"id\":%d,\"ad\":\"%s\",\"sure\":%d,\"tur\":\"%s\"}",
                getId(), getAd(), sure, tur);
    }
}

// Müşteri Sınıfı
class Musteri extends BaseEntity {
    private String telNo;

    public Musteri(int id, String ad, String telNo) {
        super(id, ad);
        this.telNo = telNo;
    }

    @Override
    public void BilgiGoster() {
        System.out.println("Müşteri Bilgileri:");
        System.out.println("ID: " + getId());
        System.out.println("Ad: " + getAd());
        System.out.println("Telefon No: " + telNo);
    }

    // Müşteri'yi JSON formatında döndür
    public String toJSON() {
        return String.format("{\"id\":%d,\"ad\":\"%s\",\"telNo\":\"%s\"}",
                getId(), getAd(), telNo);
    }
}

// Ana Uygulama Sınıfı
public class SinemaSistemi {
    private static List<Musteri> musteriler = new ArrayList<>();
    private static List<Film> filmler = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            menuGoster();
            int secim = scanner.nextInt();
            scanner.nextLine(); // Boş satırı temizle

            switch (secim) {
                case 1 -> musteriEkle();
                case 2 -> filmEkle();
                case 3 -> bilgileriListele();
                case 0 -> {
                    System.out.println("Sistemden çıkılıyor...");
                    return;
                }
                default -> System.out.println("Geçersiz seçim!");
            }
        }
    }

    private static void menuGoster() {
        System.out.println("\n--- SİNEMA SİSTEMİ ---");
        System.out.println("1. Müşteri Ekle");
        System.out.println("2. Film Ekle");
        System.out.println("3. Bilgileri Listele");
        System.out.println("0. Çıkış");
        System.out.print("Seçiminizi yapın: ");
    }

    private static void musteriEkle() {
        System.out.print("Müşteri ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Müşteri Adı: ");
        String ad = scanner.nextLine();
        System.out.print("Telefon No: ");
        String telNo = scanner.nextLine();

        Musteri musteri = new Musteri(id, ad, telNo);
        musteriler.add(musteri);
        saveToFile("Musteri.json", musteriler);
        System.out.println("Müşteri başarıyla eklendi.");
    }

    private static void filmEkle() {
        System.out.print("Film ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Film Adı: ");
        String ad = scanner.nextLine();
        System.out.print("Film Süresi (dakika): ");
        int sure = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Film Türü: ");
        String tur = scanner.nextLine();

        Film film = new Film(id, ad, sure, tur);
        filmler.add(film);
        saveToFile("Film.json", filmler);
        System.out.println("Film başarıyla eklendi.");
    }

    private static void bilgileriListele() {
        System.out.println("\n--- TÜM BİLGİLER ---");

        System.out.println("MÜŞTERİLER:");
        for (Musteri musteri : musteriler) {
            musteri.BilgiGoster();
        }

        System.out.println("\nFİLMLER:");
        for (Film film : filmler) {
            film.BilgiGoster();
        }
    }

    // JSON Dosyasına Kaydetme Fonksiyonu
    private static <T> void saveToFile(String fileName, List<T> dataList) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write("[");
            for (int i = 0; i < dataList.size(); i++) {
                T item = dataList.get(i);
                if (item instanceof Musteri) {
                    fileWriter.write(((Musteri) item).toJSON());
                } else if (item instanceof Film) {
                    fileWriter.write(((Film) item).toJSON());
                }
                if (i < dataList.size() - 1) {
                    fileWriter.write(",");
                }
            }
            fileWriter.write("]");
            System.out.println(fileName + " dosyasına kaydedildi.");
        } catch (IOException e) {
            System.out.println("Dosyaya kaydetme hatası: " + e.getMessage());
        }
    }

    // JSON Dosyasından Okuma Fonksiyonu
    private static String readFromFile(String fileName) {
        try {
            return new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            System.out.println("Dosyadan okuma hatası: " + e.getMessage());
            return null;
        }
    }
}
