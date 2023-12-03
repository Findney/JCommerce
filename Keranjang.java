import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Kelas untuk merepresentasikan keranjang belanja pelanggan.
 * 
 * @author Kelompok 15 
 * @version 12-03-2023
 */
public class Keranjang {

    private ArrayList<Barang> barang;
    private static final String pathName = "database/cart/";
    private static final String cartFileName = "cart.txt";

    /**
     * Konstruktor untuk membuat objek Keranjang.
     */
    public Keranjang() {
        this.barang = new ArrayList<>();
    }

    /**
     * Metode untuk memasukkan barang ke dalam keranjang.
     *
     * @param id         ID barang yang ingin dimasukkan.
     * @param listBarang Daftar barang yang tersedia.
     * @param namaUser   Nama pengguna (username) pelanggan.
     */
    public void masukkanBarang(String id, ListBarang listBarang, String namaUser) {
        Barang barang = listBarang.getBarang(id);

        if (barang != null) {
            Scanner input = new Scanner(System.in);
            System.out.print("Masukkan jumlah kuantitas: ");
            int quantity = input.nextInt();

            if (quantity > 0 && quantity <= barang.getKuantitas()) {
                Barang newBarang = new Barang(barang.getId(), barang.getNama(), barang.getHarga(), quantity);

                if (this.barang.add(newBarang)) {
                    System.out.println("Barang berhasil dimasukkan ke cart.");

                    try (BufferedWriter writer = new BufferedWriter(
                            new FileWriter(pathName + namaUser + "_" + cartFileName, true))) {
                        writer.write(newBarang.toFileString());
                    } catch (IOException e) {
                        System.out.println("Gagal menulis database.");
                        e.printStackTrace();
                    }

                } else {
                    System.out.println("Gagal memasukkan barang ke keranjang.");
                }
            } else {
                System.out.println("Jumlah quantity tidak valid!");
            }
        } else {
            System.out.println("Barang tidak ditemukan!");
        }
    }

    /**
     * Metode untuk melihat isi keranjang belanja.
     *
     * @param username Nama pengguna (username) pelanggan.
     */
    public void lihatKeranjang(String username) {
        System.out.println("\nIsi Keranjang Belanja:");

        for (Barang barang : getBarang(username)) {
            System.out.println("ID: " + barang.getId() + ", Nama: " + barang.getNama() + ", Harga: " + barang.getHarga()
                    + " Kuantitas: " + barang.getKuantitas());
        }
    }

    /**
     * Metode untuk mengedit keranjang belanja.
     *
     * @param username Nama pengguna (username) pelanggan.
     */
    public void editKeranjang(String username) {
        if (isEmptyCartFile(username)) {
            System.out.println("Keranjang belanja kosong.");
            return;
        }

        Scanner input = new Scanner(System.in);
        lihatKeranjang(username);
        System.out.print("Apakah Anda ingin mengubah atau menghapus barang dari keranjang? (y/n): ");
        String pilihan = input.nextLine();
        if (pilihan.equalsIgnoreCase("y")) {
            System.out.print("Masukkan ID barang yang ingin diubah atau dihapus: ");
            String id = input.nextLine();
            boolean found = false;
            for (Barang barang : this.barang) {
                if (barang.getId().equals(id)) {
                    found = true;
                    System.out.print("Apakah Anda ingin mengubah kuantitas atau menghapus barang ini? (u/h): ");
                    String ubah = input.nextLine();
                    if (ubah.equalsIgnoreCase("u")) {
                        System.out.print("Masukkan kuantitas baru: ");
                        int quantity = input.nextInt();
                        if (quantity > 0) {
                            barang.setKuantitas(quantity);
                            System.out.println("Kuantitas barang berhasil diubah.");
                        } else {
                            System.out.println("Kuantitas tidak valid!");
                        }
                    } else if (ubah.equalsIgnoreCase("h")) {
                        hapusBarang(barang);
                        System.out.println("Barang berhasil dihapus dari keranjang.");
                    } else {
                        System.out.println("Pilihan tidak valid!");
                    }
                    break;
                }
            }
            if (!found) {
                System.out.println("Barang dengan ID tersebut tidak ada di keranjang!");
            }

            try (BufferedWriter writer = new BufferedWriter(
                    new FileWriter(pathName + username + "_" + cartFileName, false))) {
                for (Barang barang : this.barang) {
                    writer.write(barang.toFileString());
                }
            } catch (IOException e) {
                System.out.println("Gagal menulis database.");
                e.printStackTrace();
            }
        } else if (pilihan.equalsIgnoreCase("n")) {

        } else {
            System.out.println("Pilihan tidak valid!");
        }
    }

    /**
     * Metode untuk mendapatkan daftar barang dari file keranjang belanja.
     *
     * @param username Nama pengguna (username) pelanggan.
     * @return Daftar barang pada keranjang belanja.
     */
    public ArrayList<Barang> getBarang(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(pathName + username + "_" + cartFileName))) {
            this.barang.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                Barang barang = Barang.fromString(line);
                this.barang.add(barang);
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca database.");
            e.printStackTrace();
        }
        return this.barang;
    }

    /**
     * Metode untuk menghapus barang dari keranjang belanja.
     *
     * @param barang Barang yang akan dihapus.
     */
    public void hapusBarang(Barang barang) {
        this.barang.remove(barang);
    }

    /**
     * Metode untuk mengosongkan keranjang belanja.
     *
     * @param username Nama pengguna (username) pelanggan.
     * @return True jika keranjang berhasil dikosongkan, false jika tidak.
     */
    public boolean kosongkanKeranjang(String username) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathName + username + "_" + cartFileName));
            
            if (reader.readLine() != null) {
                reader.close();
    
                BufferedWriter writer = new BufferedWriter(new FileWriter(pathName + username + "_" + cartFileName, false));
                writer.close();
                System.out.println("Barang di keranjang telah berhasil di checkout");
                
                return true;
            } else {
                reader.close();
                System.out.println("Keranjang sudah kosong");

                return false;
            }
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
        }

        return false;
    }

    private boolean isEmptyCartFile(String username) {
        try (BufferedReader reader = new BufferedReader(
                new FileReader(pathName + username + "_" + cartFileName))) {
            return reader.readLine() == null;
        } catch (IOException e) {
            System.out.println("Gagal membaca file keranjang.");
            e.printStackTrace();
            return true;
        }
    }
}
