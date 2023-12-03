/**
 * Kelas abstrak yang merupakan dasar untuk tipe pengemudi dalam sistem. Menyediakan konstanta
 * untuk nama file barang dan file checkout. Kelas turunan harus memberikan implementasi untuk
 * metode login dan signup sesuai kebutuhan sistem.
 * 
 * @author Kelompok 15 
 * @version 12-03-2023
 */
public abstract class Driver {

    /** Nama file untuk data barang. */
    protected static final String barangFileName = "database/barang/dataBarang.txt";

    /** Nama file untuk data checkout. */
    protected static final String checkoutFile = "database/checkout/checkout.txt";

    /**
     * Konstruktor untuk kelas abstrak Driver.
     */
    public Driver() {
        // Kosong, karena tidak ada logika khusus yang perlu dijalankan di konstruktor.
    }

    /**
     * Metode abstrak untuk login ke sistem dengan menggunakan username dan password.
     *
     * @param username Nama pengguna untuk login.
     * @param password Kata sandi untuk login.
     * @param FILE_NAME Nama file yang digunakan untuk menyimpan data login.
     */
    public abstract void login(String username, String password, String FILE_NAME);

    /**
     * Metode abstrak untuk melakukan pendaftaran atau signup ke sistem.
     *
     * @param FILE_NAME Nama file yang digunakan untuk menyimpan data pendaftaran.
     */
    public abstract void signupAs(String FILE_NAME);
}
