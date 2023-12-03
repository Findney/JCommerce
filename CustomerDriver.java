import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Kelas untuk mengelola interaksi pelanggan (Customer) pada sistem toko online.
 * 
 * @author Kelompok 15 
 * @version 12-03-2023
 */
public class CustomerDriver extends Driver {
    private Customer akun;
    private Transaksi transaksi;
    private ListBarang listBarang;
    private static final String transaksiPath = "database/transaksi/";
    private static final String transaksiFile = "transaksi.txt";

    /**
     * Konstruktor tanpa parameter untuk membuat objek CustomerDriver.
     */
    public CustomerDriver() {

    }

    /**
     * Konstruktor dengan parameter untuk membuat objek CustomerDriver berdasarkan username.
     *
     * @param username Nama pengguna (username) pelanggan.
     */
    public CustomerDriver(String username) {
        this.akun = new Customer(username);
        this.listBarang = new ListBarang();
        this.transaksi = new Transaksi(akun);
    }

    /**
     * Metode override untuk melakukan pendaftaran pelanggan (signup).
     *
     * @param FILE_NAME Nama file untuk menyimpan data pengguna.
     */
    @Override
    public void signupAs(String FILE_NAME) {
        akun = new Customer();
        akun.signup(FILE_NAME);
    }

    /**
     * Metode override untuk melakukan login pelanggan.
     *
     * @param username  Nama pengguna (username) yang diinput oleh pengguna.
     * @param password  Kata sandi yang diinput oleh pengguna.
     * @param FILE_NAME Nama file untuk menyimpan data pengguna.
     */
    @Override
    public void login(String username, String password, String FILE_NAME) {
        if (this.akun.login(username, password, FILE_NAME)) {
            boolean running = true;
            getListBarang();
            AdminDriver admin = new AdminDriver();
            Scanner sc = new Scanner(System.in);
            while (running) {
                System.out.println("Silakan Pilih Salah Satu Opsi:");
                System.out.println("1. Lihat Produk");
                System.out.println("2. Tambahkan ke Troli");
                System.out.println("3. Lihat Troli");
                System.out.println("4. Pembayaran");
                System.out.println("5. Lihat Transaksi");
                System.out.println("6. Keluar");

                System.out.print("Masukkan pilihan: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        if (listBarang != null) {
                            listBarang.lihatListBarang();
                        } else {
                            System.out.println("Barang kosong. Tidak dapat melihat produk.");
                        }

                        break;
                    case 2:
                        System.out.println("Tambahkan produk ke troli");
                        System.out.print("Masukkan id produk: ");
                        String id = sc.nextLine();
                        akun.masukkanBarangKeKeranjang(id, listBarang, akun.getid());
                        break;
                    case 3:
                        this.akun.editKeranjang(akun.getid());
                        break;
                    case 4:
                        this.akun.checkout(this.transaksi, admin);
                        break;
                    case 5:
                        getTransaksi();
                        this.akun.cetakInvoice(transaksi, listBarang, admin);
                        break;
                    case 6:
                        running = false;
                        this.akun.logoutAs();
                        break;
                    default:
                        System.out.println("Masukan tidak valid. Silakan coba lagi.");
                        break;
                }
            }
        }
    }

    /**
     * Metode untuk mendapatkan daftar barang dari file dataBarang.txt.
     *
     * @return Objek ListBarang yang berisi daftar barang.
     */
    public ListBarang getListBarang() {
        try (BufferedReader reader = new BufferedReader(new FileReader(barangFileName))) {
            ListBarang newListBarang = new ListBarang();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 4) {
                    String dataId = fields[0];
                    String dataNama = fields[1];
                    int dataHarga = Integer.parseInt(fields[2]);
                    int dataKuantitas = Integer.parseInt(fields[3]);
                    Barang barang = new Barang(dataId, dataNama, dataHarga, dataKuantitas);
                    listBarang.tambahkanBarang(barang);
                }
            }
            return newListBarang;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listBarang;
    }

    /**
     * Metode untuk mendapatkan data transaksi dari file transaksi.txt.
     *
     * @return Objek Transaksi yang berisi data transaksi pelanggan.
     */
    public Transaksi getTransaksi() {
        try (BufferedReader reader = new BufferedReader(new FileReader(transaksiPath + transaksiFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Transaksi transaksi = this.akun.parseTransaksiFromString(line);
                if (transaksi != null) {
                    this.transaksi = transaksi;
                    return transaksi;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
