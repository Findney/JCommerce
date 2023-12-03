import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Kelas Transaksi merepresentasikan suatu transaksi penjualan yang melibatkan
 * pelanggan, barang-barang, dan status transaksi.
 * 
 * @author Kelompok 15 
 * @version 12-03-2023
 */
public class Transaksi {

    private static final String invoiceFileName = "database/invoice/invoice.txt";
    private int counter = 0;
    private int id;
    private Customer akun;
    private ArrayList<Barang> barang;
    private String status;

    /**
     * Konstruktor untuk membuat objek Transaksi dengan parameter tertentu.
     *
     * @param id          ID transaksi.
     * @param barangList  Daftar barang yang terlibat dalam transaksi.
     * @param pelanggan   Pelanggan yang melakukan transaksi.
     * @param status      Status transaksi.
     */
    public Transaksi(int id, ArrayList<Barang> barangList, Customer pelanggan, String status) {
        this.id = id;
        this.barang = barangList;
        this.akun = pelanggan;
        this.status = status;
    }

    /**
     * Konstruktor untuk membuat objek Transaksi dengan pelanggan tertentu.
     *
     * @param akun Pelanggan yang melakukan transaksi.
     */
    public Transaksi(Customer akun) {
        this.id = ++counter;
        this.akun = akun;
        this.barang = new ArrayList<>();
    }

    // Metode getter dan setter

    /**
     * Mengatur daftar barang dalam transaksi.
     *
     * @param barang Daftar barang yang terlibat dalam transaksi.
     */
    public void setBarang(ArrayList<Barang> barang) {
        this.barang = barang;
    }

    /**
     * Mendapatkan daftar barang dalam transaksi.
     *
     * @return Daftar barang dalam transaksi.
     */
    public ArrayList<Barang> getBarang() {
        return this.barang;
    }

    /**
     * Mendapatkan ID transaksi.
     *
     * @return ID transaksi.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Mengatur ID transaksi.
     *
     * @param id ID transaksi.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Mendapatkan status transaksi.
     *
     * @return Status transaksi.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Mengatur status transaksi.
     *
     * @param status Status transaksi.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Mendapatkan pelanggan yang melakukan transaksi.
     *
     * @return Objek pelanggan yang melakukan transaksi.
     */
    public Customer getPelanggan() {
        return this.akun;
    }

    /**
     * Membuat objek Invoice berdasarkan transaksi dan memilih metode pembayaran.
     *
     * @return Objek Invoice yang dibuat.
     */
    public Invoice buatInvoice() {
        ArrayList<Barang> keranjangItems = getPelanggan().getKeranjang().getBarang(getPelanggan().getid());

        this.barang.addAll(keranjangItems);

        if (akun.getKeranjang().kosongkanKeranjang(getPelanggan().getid())) {

            Invoice pembayaran = new Invoice();

            Invoice invoice = new Invoice(this, this.akun.pilihPembayaran(pembayaran));

            this.setStatus("Menunggu Persetujuan");

            akun.saveChekoutToFile(this, invoice);

            return invoice;
        } else {
            System.out.println("Barang di keranjang tidak ada");
        }
        return null;
    }

    /**
     * Menyimpan informasi invoice ke dalam file.
     *
     * @param transaksi Transaksi yang berisi informasi.
     * @param invoice   Objek Invoice yang berisi informasi pembayaran.
     */
    public void saveInvoiceToFile(Transaksi transaksi, Invoice invoice) {
        try (FileWriter writer = new FileWriter(invoiceFileName, true)) {
            writer.write("Transaction ID: " + transaksi.getId() + "\n");
            int totalTransactionPrice = 0;
            for (Barang barang : transaksi.getBarang()) {
                writer.write(" - ID: " + barang.getId() + "\n");
                writer.write(" Nama: " + barang.getNama() + "\n");
                writer.write(" Kuantitas: " + barang.getKuantitas() + "\n");
                writer.write(" Harga: " + barang.getHarga() + "\n");

                int totalItemPrice = barang.getHarga() * barang.getKuantitas();
                writer.write(" Total: " + totalItemPrice + "\n");
                totalTransactionPrice += totalItemPrice;
            }

            writer.write("Total Harga: " + totalTransactionPrice + "\n");
            writer.write("Status: " + transaksi.getStatus() + "\n");
            writer.write("Metode Pembayaran: " + invoice.getPembayaran().bayar() + "\n");
            writer.write("------------------------------\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mengembalikan representasi string dari objek Transaksi untuk penulisan ke file.
     *
     * @param invoice Objek Invoice yang berisi informasi pembayaran.
     * @return String yang merepresentasikan objek Transaksi.
     */
    public String toFileString(Invoice invoice) {
        StringBuilder fileString = new StringBuilder();
        fileString.append(this.id).append(",");

        int totalTransactionPrice = 0;
        for (Barang barang : this.barang) {
            fileString.append(barang.getId()).append(",");
            fileString.append(barang.getNama()).append(",");
            fileString.append(barang.getKuantitas()).append(",");
            fileString.append(barang.getHarga()).append(",");

            int totalItemPrice = barang.getHarga() * barang.getKuantitas();
            totalTransactionPrice += totalItemPrice;
        }

        fileString.append(totalTransactionPrice).append(",");
        fileString.append(this.status).append(",");
        fileString.append(invoice.getPembayaran().bayar());

        return fileString.toString();
    }
}
