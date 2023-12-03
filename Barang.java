import java.util.StringTokenizer;

/**
 * Barang adalah representasi objek barang dalam sistem, termasuk informasi
 * seperti ID, nama, harga, dan kuantitas.
 * 
 * @author Kelompok 15 
 * @version 12-03-2023
 */
public class Barang {

    private String id;
    private String nama;
    private int harga;
    private int kuantitas;

    /**
     * Konstruktor default untuk objek Barang.
     */
    public Barang() {

    }

    /**
     * Konstruktor untuk membuat objek Barang dengan parameter ID, nama, harga,
     * dan kuantitas.
     *
     * @param id        ID barang.
     * @param nama      Nama barang.
     * @param harga     Harga barang.
     * @param kuantitas Kuantitas barang.
     */
    public Barang(String id, String nama, int harga, int kuantitas) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.kuantitas = kuantitas;
    }

    /**
     * Mendapatkan ID barang.
     *
     * @return ID barang.
     */
    public String getId() {
        return id;
    }

    /**
     * Mengatur ID barang.
     *
     * @param id ID barang yang akan diatur.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Mendapatkan nama barang.
     *
     * @return Nama barang.
     */
    public String getNama() {
        return nama;
    }

    /**
     * Mengatur nama barang.
     *
     * @param nama Nama barang yang akan diatur.
     */
    public void setNama(String nama) {
        this.nama = nama;
    }

    /**
     * Mendapatkan harga barang.
     *
     * @return Harga barang.
     */
    public int getHarga() {
        return harga;
    }

    /**
     * Mengatur harga barang.
     *
     * @param harga Harga barang yang akan diatur.
     */
    public void setHarga(int harga) {
        this.harga = harga;
    }

    /**
     * Mendapatkan kuantitas barang.
     *
     * @return Kuantitas barang.
     */
    public int getKuantitas() {
        return kuantitas;
    }

    /**
     * Mengatur kuantitas barang.
     *
     * @param kuantitas Kuantitas barang yang akan diatur.
     */
    public void setKuantitas(int kuantitas) {
        this.kuantitas = kuantitas;
    }

    /**
     * Mengembalikan representasi string dari objek Barang yang siap ditulis ke file.
     *
     * @return String representasi file dari objek Barang.
     */
    public String toFileString() {
        return id + "," + nama + "," + harga + "," + kuantitas + "\n";
    }

    /**
     * Mengembalikan objek Barang dari string yang diambil dari file.
     *
     * @param line String dari file yang akan diubah menjadi objek Barang.
     * @return Objek Barang.
     */
    public static Barang fromString(String line) {
        StringTokenizer tokenizer = new StringTokenizer(line, ",");
        String id = tokenizer.nextToken();
        String nama = tokenizer.nextToken();
        int harga = (int) Double.parseDouble(tokenizer.nextToken());
        int kuantitas = Integer.parseInt(tokenizer.nextToken());

        return new Barang(id, nama, harga, kuantitas);
    }
}
