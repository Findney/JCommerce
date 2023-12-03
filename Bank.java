/**
 * Bank adalah implementasi antarmuka Pembayaran yang menyediakan metode untuk
 * melakukan pembayaran menggunakan metode pembayaran melalui bank.
 * 
 * @author Kelompok 15 
 * @version 12-03-2023
 */
public class Bank implements Pembayaran {

    /**
     * Melakukan pembayaran melalui bank.
     *
     * @return String yang menyatakan metode pembayaran, dalam hal ini "Bank".
     */
    @Override
    public String bayar() {
        // Implementasi metode pembayaran
        String bayar = "Bank";
        return bayar;
    }
}
