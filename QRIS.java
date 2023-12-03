/**
 * Implementasi metode pembayaran menggunakan QRIS.
 * Kelas ini mengimplementasikan antarmuka Pembayaran.
 * 
 * @author Kelompok 15 
 * @version 12-03-2023
 */
public class QRIS implements Pembayaran {

    /**
     * Mengembalikan representasi string dari metode pembayaran QRIS.
     *
     * @return String "QRIS".
     */
    @Override
    public String bayar() {
        return "QRIS";
    }
}
