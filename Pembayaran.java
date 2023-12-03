/**
 * Antarmuka Pembayaran mendefinisikan metode pembayaran yang harus diimplementasi
 * oleh kelas-kelas yang menerapkannya.
 * 
 * @author Kelompok 15 
 * @version 12-03-2023
 */
public interface Pembayaran {

    /**
     * Mengembalikan representasi string dari metode pembayaran.
     *
     * @return String yang mewakili metode pembayaran.
     */
    String bayar();
}
