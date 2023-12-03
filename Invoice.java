import java.util.Scanner;

/**
 * Kelas yang merepresentasikan invoice untuk transaksi yang dilakukan.
 * 
 * @author Kelompok 15 
 * @version 12-03-2023
 */
public class Invoice {

    /** Transaksi yang terkait dengan invoice. */
    private Transaksi transaksi;

    /** Metode pembayaran yang digunakan dalam invoice. */
    private Pembayaran pembayaran;

    /**
     * Konstruktor default untuk Invoice.
     */
    public Invoice() {
        // Kosong, karena tidak ada inisialisasi khusus yang diperlukan.
    }

    /**
     * Konstruktor dengan parameter untuk Invoice.
     *
     * @param pembayaran Metode pembayaran yang digunakan dalam invoice.
     */
    public Invoice(Pembayaran pembayaran) {
        this.pembayaran = pembayaran;
    }

    /**
     * Konstruktor dengan parameter untuk Invoice.
     *
     * @param transaksi Transaksi yang terkait dengan invoice.
     * @param pembayaran Metode pembayaran yang digunakan dalam invoice.
     */
    public Invoice(Transaksi transaksi, Pembayaran pembayaran) {
        this.transaksi = transaksi;
        this.pembayaran = pembayaran;
    }

    /**
     * Mendapatkan transaksi yang terkait dengan invoice.
     *
     * @return Objek Transaksi yang terkait dengan invoice.
     */
    public Transaksi getTransaksi() {
        return transaksi;
    }

    /**
     * Menetapkan metode pembayaran pada invoice.
     *
     * @param pembayaran Metode pembayaran yang ingin ditetapkan.
     * @return Objek Pembayaran yang telah ditetapkan.
     */
    public Pembayaran setPembayaran(Pembayaran pembayaran) {
        return this.pembayaran = pembayaran;
    }

    /**
     * Mendapatkan metode pembayaran pada invoice.
     *
     * @return Objek Pembayaran yang terkait dengan invoice.
     */
    public Pembayaran getPembayaran() {
        return pembayaran;
    }

    /**
     * Meminta pengguna untuk memilih metode pembayaran dan mengembalikan objek Pembayaran yang dipilih.
     *
     * @return Objek Pembayaran yang dipilih oleh pengguna.
     */
    public Pembayaran pilihPembayaran() {
        System.out.println("Pilih metode pembayaran: ");
        System.out.println("1. QRIS");
        System.out.println("2. Bank");
        System.out.println("3. COD");
        Scanner input = new Scanner(System.in);
        System.out.print("Masukkan pilihan pembayaran: ");
        int pilihan = input.nextInt();

        switch (pilihan) {
            case 1:
                return new QRIS();

            case 2:
                return new Bank();

            case 3:
                return new COD();

            default:
                System.out.println("Pilihan tidak valid!");
                return null;
        }
    }
}
