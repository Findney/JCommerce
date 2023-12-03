import java.util.ArrayList;
import java.util.Iterator;

/**
 * Kelas untuk merepresentasikan daftar barang yang tersedia.
 * 
 * @author Kelompok 15 
 * @version 12-03-2023
 */
public class ListBarang {

    private ArrayList<Barang> barang;

    /**
     * Konstruktor untuk membuat objek ListBarang.
     */
    public ListBarang() {
        this.barang = new ArrayList<>();
    }

    /**
     * Metode untuk menambahkan barang ke daftar.
     *
     * @param barang Barang yang akan ditambahkan.
     */
    public void tambahkanBarang(Barang barang) {
        this.barang.add(barang);
    }

    /**
     * Metode untuk menambahkan barang ke daftar pada indeks tertentu.
     *
     * @param barang Barang yang akan ditambahkan.
     * @param index  Indeks tempat barang akan ditambahkan.
     */
    public void tambahkanBarang(Barang barang, int index) {
        this.barang.add(index, barang);
    }

    /**
     * Metode untuk mendapatkan barang dari daftar berdasarkan indeks.
     *
     * @param index Indeks barang yang akan diambil.
     * @return Barang pada indeks yang diberikan.
     */
    public Barang getBarang(int index) {
        return this.barang.get(index);
    }

    /**
     * Metode untuk mendapatkan jumlah barang dalam daftar.
     *
     * @return Jumlah barang dalam daftar.
     */
    public int getSize() {
        return this.barang.size();
    }

    /**
     * Metode untuk menghapus barang dari daftar berdasarkan ID.
     *
     * @param id ID barang yang akan dihapus.
     */
    public void hapusBarang(String id) {
        Iterator<Barang> iterator = this.barang.iterator();
        while (iterator.hasNext()) {
            Barang barang = iterator.next();
            if (barang.getId().equals(id)) {
                iterator.remove();
                break;
            }
        }
    }

    /**
     * Metode untuk mendapatkan seluruh daftar barang.
     *
     * @return ArrayList berisi seluruh barang dalam daftar.
     */
    public ArrayList<Barang> getBarang() {
        return this.barang;
    }

    /**
     * Metode untuk mendapatkan barang dari daftar berdasarkan ID.
     *
     * @param id ID barang yang akan diambil.
     * @return Barang dengan ID yang diberikan atau null jika tidak ditemukan.
     */
    public Barang getBarang(String id) {
        for (Barang barang : this.barang) {
            if (barang.getId().equals(id)) {
                return barang;
            }
        }
        return null;
    }

    /**
     * Metode untuk menampilkan seluruh daftar barang.
     */
    public void lihatListBarang() {
        if (this.barang.isEmpty()) {
            System.out.println("Tidak ada produk yang tersedia.");
        } else {
            System.out.println("Daftar produk yang tersedia:");
            for (Barang barang : this.barang) {
                System.out.println("ID: " + barang.getId());
                System.out.println("Nama: " + barang.getNama());
                System.out.println("Harga: " + barang.getHarga());
                System.out.println("Kuantitas: " + barang.getKuantitas());
                System.out.println("------------------------------");
            }
        }
    }

    /**
     * Metode untuk mengedit informasi barang dalam daftar.
     *
     * @param barangToUpdate Barang yang akan diupdate.
     */
    public void editBarang(Barang barangToUpdate) {
        for (int i = 0; i < this.barang.size(); i++) {
            Barang barang = this.barang.get(i);
            if (barang.getId().equals(barangToUpdate.getId())) {
                barang.setNama(barangToUpdate.getNama());
                barang.setHarga(barangToUpdate.getHarga());
                barang.setKuantitas(barangToUpdate.getKuantitas());
                break;
            }
        }
    }
}
