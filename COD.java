/**
 * Implementasi metode pembayaran Cash On Delivery (COD).
 * Mengimplementasikan antarmuka Pembayaran dan menyediakan informasi metode pembayaran COD.
 * 
 * @author Kelompok 15 
 * @version 12-03-2023
 */
public class COD implements Pembayaran {

    /**
     * Mengembalikan informasi metode pembayaran Cash On Delivery (COD).
     *
     * @return Informasi metode pembayaran COD.
     */
    @Override
    public String bayar() {
        return "COD";
    }
}
