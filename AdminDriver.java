import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Kelas {@code AdminDriver} mewakili driver untuk fungsionalitas administrator.
 * Kelas ini memperluas kelas {@code Driver} dan mencakup metode untuk menangani transaksi,
 * mengelola produk, dan melakukan tugas administratif.
 *
 * <p>Kelas ini berinteraksi dengan berbagai kelas lain, seperti {@code Admin}, {@code ListBarang},
 * {@code Transaksi}, dan {@code Invoice}, untuk melakukan fungsionalitas yang ditentukan.
 *
 * @author Kelompok 15 
 * @version 12-03-2023
 */
public class AdminDriver extends Driver {

    private Admin akun;
    private ListBarang listBarang;
    private ArrayList<Transaksi> listTransaksi;
    private Invoice invoice;
    private static final String transaksiPath = "database/transaksi/";
    private static final String transaksiFile = "transaksi.txt";
    private static final String barangFileName = "database/barang.txt";

    /**
     * Konstruktor default untuk kelas {@code AdminDriver}.
     */
    public AdminDriver() {
        this.akun = new Admin();
        this.listBarang = new ListBarang();
        this.listTransaksi = new ArrayList<>();
    }

    /**
     * Menambahkan transaksi ke daftar dan menulis daftar yang diperbarui ke file transaksi.
     *
     * @param transaksi Transaksi yang akan ditambahkan.
     * @param invoice   Faktur yang terkait dengan transaksi.
     */
    public void terimaTransaksi(Transaksi transaksi, Invoice invoice) {
        listTransaksi.add(transaksi);
        tulisListTransaksiKeFile(invoice);
    }

    /**
     * Menulis daftar transaksi yang diperbarui ke file transaksi.
     *
     * @param invoice Faktur yang terkait dengan transaksi.
     */
    private void tulisListTransaksiKeFile(Invoice invoice) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(transaksiPath + transaksiFile))) {
            for (Transaksi transaksi : listTransaksi) {
                writer.write(transaksi.toFileString(invoice));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Membaca daftar transaksi dari file.
     */
    public void bacaListTransaksiDariFile() {
        listTransaksi.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(transaksiPath + transaksiFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Transaksi transaksi = parseTransaksiFromString(line);
                if (transaksi != null) {
                    listTransaksi.add(transaksi);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metode ini membaca sebuah string yang mewakili transaksi dari file dan
     * mengembalikan objek Transaksi yang sesuai.
     *
     * @param line String yang mewakili transaksi.
     * @return Objek Transaksi yang dibaca dari string atau null jika parsing gagal.
     */
    private Transaksi parseTransaksiFromString(String line) {
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
     * Metode ini mendaftarkan seorang admin dengan menginisialisasi objek Admin.
     *
     * @param FILE_NAME Nama file tempat data admin disimpan.
     */ 
    public void signupAs(String FILE_NAME) {
        akun = new Admin();
        akun.signup(FILE_NAME);
    }
    
    /**
     * Metode ini digunakan untuk login sebagai admin dan mengakses fungsionalitas admin.
     *
     * @param username   Nama pengguna admin.
     * @param password   Kata sandi admin.
     * @param FILE_NAME  Nama file tempat data admin disimpan.
     */
    @Override
    public void login(String username, String password, String FILE_NAME) {
        getListBarang();
        if (akun.login(username, password, FILE_NAME)) {
            boolean running = true;
            Scanner sc = new Scanner(System.in);
            bacaListTransaksiDariFile();
            while (running) {
                System.out.println("Silakan pilih salah satu opsi:");
                System.out.println("1. Tambahkan produk");
                System.out.println("2. Hapus produk");
                System.out.println("3. Edit produk");
                System.out.println("4. Lihat Transaksi");
                System.out.println("5. Keluar");

                System.out.print("Masukkan pilihan: ");
                int pilihan = sc.nextInt();

                sc.nextLine();

                switch (pilihan) {
                    case 1:

                        tambahBarang();
                        break;
                    case 2:

                        System.out.print("Masukkan ID produk yang akan dihapus: ");
                        String idToDelete = sc.nextLine();
                        hapusBarang(idToDelete);
                        break;
                    case 3:

                        System.out.println("Edit produk");

                        System.out.print("Masukkan id produk: ");

                        String idToEdit = sc.nextLine();

                        Barang barangToEdit = listBarang.getBarang(idToEdit);
                        if (barangToEdit != null) {

                            System.out.println("Current product details:");
                            System.out.println("ID: " + barangToEdit.getId());
                            System.out.println("Nama: " + barangToEdit.getNama());
                            System.out.println("Harga: " + barangToEdit.getHarga());
                            System.out.println("Kuantitas: " + barangToEdit.getKuantitas());

                            System.out.println("Please choose an option to edit:");
                            System.out.println("1. Nama");
                            System.out.println("2. Harga");
                            System.out.println("3. Kuantitas");
                            System.out.println("4. Batal");
                            System.out.print("Masukkan pilihan edit: ");

                            int pilihanEdit = sc.nextInt();

                            sc.nextLine();

                            switch (pilihanEdit) {
                                case 1:

                                    System.out.print("Masukkan nama produk baru: ");

                                    String namaBaru = sc.nextLine();

                                    barangToEdit.setNama(namaBaru);
                                    System.out.println("Nama produk berhasil diperbarui.");
                                    break;
                                case 2:
                                    System.out.print("Masukkan harga produk baru: ");
                                    int Harga = sc.nextInt();
                                    sc.nextLine();
                                    barangToEdit.setHarga(Harga);
                                    System.out.println("Harga produk berhasil diperbarui.");
                                    break;
                                case 3:
                                    System.out.print("Masukkan jumlah produk baru: ");
                                    int jumlahBaru = sc.nextInt();
                                    sc.nextLine();
                                    barangToEdit.setKuantitas(jumlahBaru);
                                    System.out.println("Kuantitas produk berhasil diperbarui.");
                                    break;
                                case 4:
                                    System.out.println("Pengeditan produk telah dibatalkan.");
                                    break;
                                default:
                                    System.out.println("Masukan tidak valid. Silakan coba lagi.");
                                    break;

                            }

                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(barangFileName))) {
                                for (Barang barang : listBarang.getBarang()) {
                                    writer.write(barang.toFileString());
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println("Id produk tidak ada. Silakan coba lagi.");
                        }
                        break;
                    case 4:
                        if (lihatDaftarTransaksi()) {
                            processTransactionsMenu();

                        }
                        break;
                    case 5:
                        running = false;
                        akun.logoutAs();
                        break;
                    default:
                        System.out.println("Masukan tidak valid. Silakan coba lagi.");
                        break;
                }

            }
        }
    }
     
    /**
     * Metode ini menangani logika penambahan barang baru ke daftar produk.
     */
    public void tambahBarang() {

        try {
            String id;
            Scanner sc = new Scanner(System.in);

            File file = new File(barangFileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.print("Enter new product ID: ");
            id = sc.nextLine();

            try (BufferedReader reader = new BufferedReader(new FileReader(barangFileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length > 0 && parts[0].equals(id)) {
                        System.out.println("Product with ID " + id + " already exists. Please enter a unique ID.");
                        return;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.print("Enter new product name: ");
            String nama = sc.nextLine();
            System.out.print("Enter new product price: ");
            int harga = sc.nextInt();
            System.out.print("Enter new product quantity: ");
            int kuantitas = sc.nextInt();

            Barang newBarang = new Barang(id, nama, harga, kuantitas);

            try (FileWriter writer = new FileWriter(barangFileName, true)) {
                writer.write(newBarang.toFileString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Barang berhasil di tambahkan");
        } catch (Exception e) {
            System.out.println("Barang gagal di tambahkan");
        }

    }

    /**
     * Metode ini menampilkan daftar transaksi jika ada.
     *
     * @return true jika ada transaksi, false jika tidak ada.
     */
    public boolean lihatDaftarTransaksi() {

        if (listTransaksi.isEmpty()) {
            System.out.println("Belum ada transaksi.");
            return false;
        } else {
            System.out.println("Daftar Transaksi:");
            for (Transaksi transaksi : listTransaksi) {
                List<Barang> barangList = transaksi.getBarang();
                if (!barangList.isEmpty()) {
                    System.out.println("Transaction ID: " + transaksi.getId());
                    int totalTransactionPrice = 0;

                    for (Barang barang : barangList) {
                        System.out.println(" - ID: " + barang.getId());
                        System.out.println("   Nama: " + barang.getNama());
                        System.out.println("   Kuantitas: " + barang.getKuantitas());
                        System.out.println("   Harga: " + barang.getHarga());

                        int totalItemPrice = barang.getHarga() * barang.getKuantitas();
                        System.out.println("   Total: " + totalItemPrice);

                        totalTransactionPrice += totalItemPrice;
                    }
                    System.out.println("Total Harga: " + totalTransactionPrice);
                    System.out.println("Status: " + transaksi.getStatus());
                    System.out.println("Metode Pembayaran: " + invoice.getPembayaran().bayar());
                }

                System.out.println("------------------------------");
            }
        }
        return true;
    }

    /**
     * Metode ini menampilkan menu untuk memproses transaksi dengan opsi:
     * 1. Konfirmasi transaksi
     * 2. Menolak transaksi
     * 3. Kembali
     * Pengguna diminta untuk memilih salah satu opsi dan diproses sesuai pilihan.
     */
    public void processTransactionsMenu() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Process transactions menu:");
        System.out.println("1. Confirm transactions");
        System.out.println("2. Reject transactions");
        System.out.println("3. Back");
        System.out.print("Masukkan pilihan: ");
        int processChoice = sc.nextInt();
        sc.nextLine();
        switch (processChoice) {
            case 1:
                confirmTransactionsMenu();
                break;
            case 2:
                rejectTransactionsMenu();
                break;
            case 3:

                break;
            default:
                System.out.println("Invalid input. Please try again.");
                break;
        }
    }

    /**
     * Mengonfirmasi semua transaksi atau transaksi tertentu berdasarkan ID.
     */
    private void confirmTransactionsMenu() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Confirm transactions menu:");
        System.out.println("1. Confirm all transactions");
        System.out.println("2. Confirm transaction by ID");
        System.out.println("3. Cancel");
        System.out.print("Masukkan pilihan: ");
        int confirmChoice = sc.nextInt();
        sc.nextLine();
        switch (confirmChoice) {
            case 1:
                confirmAllTransactions();
                break;
            case 2:
                confirmTransactionById();
                break;
            case 3:

                break;
            default:
                System.out.println("Invalid input. Please try again.");
                break;
        }
    }

    /**
     * Menolak semua transaksi atau transaksi tertentu berdasarkan ID.
     */
    private void rejectTransactionsMenu() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Reject transactions menu:");
        System.out.println("1. Reject all transactions");
        System.out.println("2. Reject transaction by ID");
        System.out.println("3. Cancel");
        System.out.print("Masukkan pilihan: ");
        int rejectChoice = sc.nextInt();
        sc.nextLine();
        switch (rejectChoice) {
            case 1:
                rejectAllTransactions();
                break;
            case 2:
                rejectTransactionById();
                break;
            case 3:

                break;
            default:
                System.out.println("Invalid input. Please try again.");
                break;
        }
    }

    /**
     * Mengonfirmasi semua transaksi.
     */
    private void confirmAllTransactions() {
        for (Transaksi transaksi : listTransaksi) {
            if (transaksi.getStatus().equals("Menunggu Persetujuan")) {
                transaksi.setStatus("Selesai");
                System.out.println("Transaction ID " + transaksi.getId() + " approved successfully.");
                tulisKeInvoice();
            }
        }
    }

    /**
     * Mengonfirmasi transaksi tertentu berdasarkan ID.
     */
    private void confirmTransactionById() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter transaction ID to confirm: ");
        int transactionId = sc.nextInt();
        for (Transaksi transaksi : listTransaksi) {
            if (transaksi.getId() == transactionId && transaksi.getStatus().equals("Menunggu Persetujuan")) {
                transaksi.setStatus("Selesai");
                System.out.println("Transaction ID " + transaksi.getId() + " approved successfully.");
                tulisKeInvoice();
                return;
            }
        }
        System.out.println("Transaction ID " + transactionId + " not found or already approved.");
    }

    /**
     * Menolak semua transaksi.
     */
    private void rejectAllTransactions() {
        for (Transaksi transaksi : listTransaksi) {
            if (transaksi.getStatus().equals("Menunggu Persetujuan")) {
                transaksi.setStatus("Ditolak");
                System.out.println("Transaction ID " + transaksi.getId() + " rejected.");
                tulisKeInvoice();
            }
        }
    }

    /**
     * Menolak transaksi tertentu berdasarkan ID.
     */
    private void rejectTransactionById() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter transaction ID to reject: ");
        int transactionId = sc.nextInt();
        for (Transaksi transaksi : listTransaksi) {
            if (transaksi.getId() == transactionId && transaksi.getStatus().equals("Menunggu Persetujuan")) {
                transaksi.setStatus("Ditolak");
                System.out.println("Transaction ID " + transaksi.getId() + " rejected.");
                tulisKeInvoice();
                return;
            }
        }
        System.out.println("Transaction ID " + transactionId + " not found or already rejected.");
    }

    /**
     * Menghapus produk berdasarkan ID.
     *
     * @param id ID produk yang akan dihapus.
     */
    public void hapusBarang(String id) {

        File tempFile = new File("tempFile.txt");
        try {
            tempFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean idFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(barangFileName));
                FileWriter writer = new FileWriter(tempFile)) {
            String line;
            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",");

                if (parts.length > 0 && parts[0].equals(id)) {
                    idFound = true;
                    continue;
                }
                writer.write(line + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!idFound) {
            System.out.println("ID " + id + " tidak ditemukan di daftar barang yang tersedia.");
        } else {
            System.out.println("Barang berhasil di hapus");
        }

        File originalFile = new File(barangFileName);
        originalFile.delete();
        tempFile.renameTo(originalFile);

        listBarang.hapusBarang(id);
    }

    /**
     * Mengedit produk.
     *
     * @param barang Produk yang akan diedit.
     */
    public void editBarang(Barang barang) {
        listBarang.editBarang(barang);
    }

    /**
     * Mendapatkan daftar transaksi.
     *
     * @return Daftar transaksi.
     */
    public ArrayList<Transaksi> getListTransaksi() {
        return listTransaksi;
    }

    /**
     * Mendapatkan daftar produk.
     *
     * @return Daftar produk.
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

                    // Corrected instantiation of Barang
                    Barang barang = new Barang(dataId, dataNama, dataHarga, dataKuantitas);

                    // Corrected addition to ListBarang
                    newListBarang.tambahkanBarang(barang);
                }
            }
            return newListBarang;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // Return null in case of an exception
    }

    /**
     * Metode ini menulis informasi transaksi ke dalam file invoice.
     * Setiap transaksi dalam daftar transaksi akan diubah menjadi representasi
     * string dan ditulis ke file invoice. File invoice akan diperbarui dengan
     * transaksi terbaru setelah pemanggilan metode ini.
     *
     * @throws IOException Jika terjadi kesalahan dalam menulis ke file.
     */
    private void tulisKeInvoice() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(transaksiPath + transaksiFile))) {
            for (Transaksi transaksi : listTransaksi) {
                writer.write(transaksi.toFileString(invoice));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
