import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Kelas Customer merupakan turunan dari kelas Akun dan merepresentasikan pelanggan
 * yang dapat melakukan transaksi, melihat keranjang, dan lain sebagainya.
 * 
 * @author Kelompok 15 
 * @version 12-03-2023
 */
public class Customer extends Akun {

    private static final String checkoutPath = "database/checkout/";
    private static final String checkoutFile = "checkout.txt";
    private static final String barangFileName = "database/barang/dataBarang.txt";
    private Invoice invoice;
    private Keranjang keranjang;
    private ArrayList<Invoice> invoiceSelesai;

    /**
     * Konstruktor tanpa parameter untuk membuat objek Customer.
     */
    public Customer() {

    }  

    /**
     * Konstruktor untuk membuat objek Customer dengan ID tertentu.
     *
     * @param id ID pelanggan.
     */
    public Customer(String id) {
        super(id);
        this.keranjang = new Keranjang();
        invoiceSelesai = new ArrayList<>();
    }

    /**
     * Metode untuk melakukan proses signup sebagai pelanggan.
     *
     * @param FILE_NAME Nama file untuk menyimpan data pelanggan.
     */
    public void signup(String FILE_NAME) {
        System.out.println("Signup as customer");
        this.signUp(FILE_NAME);
    }

    /**
     * Metode untuk melakukan logout sebagai pelanggan.
     */
    public void logoutAs() {
        System.out.println("Logout as customer");
    }

    /**
     * Metode untuk melihat isi keranjang belanja pelanggan.
     *
     * @param username Username pelanggan.
     */
    public void lihatKeranjang(String username) {
        this.keranjang.lihatKeranjang(username);
    }

    /**
     * Metode untuk mengedit isi keranjang belanja pelanggan.
     *
     * @param username Username pelanggan.
     */
    public void editKeranjang(String username) {
        this.keranjang.editKeranjang(username);
    }

    /**
     * Metode untuk memasukkan barang ke dalam keranjang belanja pelanggan.
     *
     * @param id         ID barang yang akan dimasukkan.
     * @param listBarang Daftar barang yang tersedia.
     * @param username   Username pelanggan.
     */
    public void masukkanBarangKeKeranjang(String id, ListBarang listBarang, String username) {
        this.keranjang.masukkanBarang(id, listBarang, username);
    }

    /**
     * Metode untuk mendapatkan daftar barang dalam keranjang pelanggan.
     *
     * @return Daftar barang dalam keranjang pelanggan.
     */
    public ArrayList<Barang> getBarangdiKeranjang() {
        return this.keranjang.getBarang(this.getid());
    }

    /**
     * Metode untuk mengosongkan keranjang belanja pelanggan.
     *
     * @param username Username pelanggan.
     */
    public void kosongkanKeranjang(String username) {
        this.keranjang.kosongkanKeranjang(username);
    }

    /**
     * Metode untuk mendapatkan objek Keranjang pelanggan.
     *
     * @return Objek Keranjang pelanggan.
     */
    public Keranjang getKeranjang() {
        return this.keranjang;
    }

    /**
     * Metode untuk mendapatkan daftar transaksi yang telah selesai.
     *
     * @return Daftar transaksi yang telah selesai.
     */
    public ArrayList<Invoice> getTransaksiList() {
        return invoiceSelesai;
    }

    /**
     * Metode untuk memilih metode pembayaran berdasarkan objek Invoice.
     *
     * @param invoice Objek Invoice yang berisi informasi pembayaran.
     * @return Objek Pembayaran yang dipilih pelanggan.
     */
    public Pembayaran pilihPembayaran(Invoice invoice) {
        return invoice.pilihPembayaran();
    }

    /**
     * Metode untuk melakukan proses checkout, membuat invoice, dan menyimpan data transaksi.
     *
     * @param transaksi Objek Transaksi yang akan di-checkout.
     * @param admin     Objek AdminDriver yang akan menerima transaksi.
     */
    public void checkout(Transaksi transaksi, AdminDriver admin) {
        Invoice invoice = transaksi.buatInvoice();
        this.invoiceSelesai.add(invoice);
        if (invoice != null) {
            admin.terimaTransaksi(transaksi, invoice);
        }
    }

    /**
     * Metode untuk mengubah string representasi transaksi menjadi objek Transaksi.
     *
     * @param line String representasi transaksi.
     * @return Objek Transaksi yang dibuat dari string.
     */
    public Transaksi parseTransaksiFromString(String line) {
        String[] fields = line.split(",");
        if (fields.length >= 5) {
            int id = Integer.parseInt(fields[0].trim());
            String status = fields[fields.length - 2];
            String paymentMethod = fields[fields.length - 1];

            ArrayList<Barang> barangList = new ArrayList<>();
            for (int i = 1; i < fields.length - 4; i += 4) {
                String barangId = fields[i];
                String nama = fields[i + 1];
                int kuantitas = Integer.parseInt(fields[i + 2]);
                int harga = Integer.parseInt(fields[i + 3]);
            
                barangList.add(new Barang(barangId, nama, harga, kuantitas));
            }

            Customer pelanggan = new Customer();

            Transaksi transaksi = new Transaksi(id, barangList, pelanggan, status);

            if (paymentMethod.equalsIgnoreCase("QRIS")) {
                invoice = new Invoice(new QRIS());
            } else if (paymentMethod.equalsIgnoreCase("COD")) {
                invoice = new Invoice(new COD());
            } else {
                invoice = new Invoice(new Bank());
            }

            return transaksi;
        }

        return null;
    }

    /**
     * Metode untuk mencetak invoice dari transaksi yang telah selesai.
     *
     * @param transaksi   Objek Transaksi yang telah selesai.
     * @param listBarang  Daftar barang yang tersedia.
     * @param admin       Objek AdminDriver yang melakukan proses.
     */
    public void cetakInvoice(Transaksi transaksi, ListBarang listBarang, AdminDriver admin) {
        if (transaksi.getStatus() != null) {
            if (transaksi.getStatus().equals("Selesai")) {

                List<Barang> barangList = transaksi.getBarang();

                if (!barangList.isEmpty()) {

                    System.out.println("Transaction History:");

                    System.out.println("Transaction ID: " + transaksi.getId());
                    int totalTransactionPrice = 0;

                    for (Barang barang : barangList) {
                        System.out.println(" - ID: " + barang.getId());
                        System.out.println(" Nama: " + barang.getNama());
                        System.out.println(" Kuantitas: " + barang.getKuantitas());
                        System.out.println(" Harga: " + barang.getHarga());

                        int totalItemPrice = barang.getHarga() * barang.getKuantitas();
                        System.out.println(" Total: " + totalItemPrice);

                        Barang availableBarang = listBarang.getBarang(barang.getId());
                        int remainingQuantity = availableBarang.getKuantitas() - barang.getKuantitas();
                        availableBarang.setKuantitas(remainingQuantity);
                        if (availableBarang.getKuantitas() == 0) {
                            listBarang.hapusBarang(barang.getId());
                            admin.hapusBarang(barang.getId());
                        }
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(barangFileName))) {
                            for (Barang newbarang : listBarang.getBarang()) {
                                writer.write(newbarang.toFileString());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        totalTransactionPrice += totalItemPrice;
                    }
                    System.out.println("Total Harga: " + totalTransactionPrice);
                    System.out.println("Status: " + transaksi.getStatus());

                    System.out.println("Metode Pembayaran: " + invoice.getPembayaran().bayar());
                    System.out.println("------------------------------");

                    transaksi.saveInvoiceToFile(transaksi, invoice);
                } else {
                    System.out.println("Tidak ada item transaksi.");
                }
            }
        }else{
            System.out.println("Transaksi Anda belum diterima oleh Admin");
        }
    }
    

    /**
     * Metode untuk menyimpan informasi checkout ke dalam file.
     *
     * @param transaksi Objek Transaksi yang berisi informasi checkout.
     * @param invoice   Objek Invoice yang berisi informasi pembayaran.
     */
    public void saveChekoutToFile(Transaksi transaksi, Invoice invoice) {
        try (FileWriter writer = new FileWriter(checkoutPath + getid() + "_" + checkoutFile, true)) {

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

            System.out.println("Checkout successful. Invoice details saved to file.");
        } catch (IOException e) {
            System.out.println("Error writing to the file: " + e.getMessage());
        }
    }
}
