import java.io.IOException;
import java.util.Scanner;

/**
 * Kelas utama yang berfungsi sebagai program utama untuk menjalankan aplikasi.
 * 
 * @author Kelompok 15 
 * @version 12-03-2023
 */
public class Main {
    private final String adminFile = "database/login/adminCredentials.dat";
    private final String customerFile = "database/login/customerCredentials.dat";
    private static Scanner input = new Scanner(System.in);
    private Akun akun;
    private Driver driverAkun;

    /**
     * Metode utama yang berisi loop untuk menampilkan menu utama aplikasi.
     *
     * @param args Argumen baris perintah (command line arguments).
     * @throws ClassNotFoundException Jika kelas tidak ditemukan.
     * @throws IOException            Jika terjadi kesalahan IO.
     */
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        Main main = new Main();

        while (true) {
            main.displayMainMenu();
            int choice = input.nextInt();

            switch (choice) {
                case 1:
                    main.login();
                    break;
                case 2:
                    main.signup();
                    break;
                case 3:
                    main.exitProgram();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Metode untuk menampilkan menu utama aplikasi.
     */
    private void displayMainMenu() {
        System.out.println("Pilih menu: ");
        System.out.println("1. Login");
        System.out.println("2. Sign Up");
        System.out.println("3. Keluar");
        System.out.print("Masukkan pilihan: ");
    }

    /**
     * Metode untuk menampilkan menu pilihan user.
     */
    private void displayUserMenu() {
        System.out.println("Pilih User: ");
        System.out.println("1. Admin");
        System.out.println("2. Customer");
        System.out.println("3. Kembali");
        System.out.print("Masukkan pilihan: ");
    }

    /**
     * Metode untuk menjalankan proses login.
     *
     * @throws ClassNotFoundException Jika kelas tidak ditemukan.
     * @throws IOException            Jika terjadi kesalahan IO.
     */
    public void login() throws ClassNotFoundException, IOException {
        System.out.println("Pilih Login: ");
        System.out.println("1. Admin");
        System.out.println("2. Customer");
        System.out.println("3. Forgot Password");
        System.out.println("4. Reset Password");
        System.out.println("5. Kembali");
        System.out.print("Masukkan pilihan: ");
        int loginChoice = input.nextInt();
        input.nextLine();

        switch (loginChoice) {
            case 1:
                System.out.println("Login as admin");
                System.out.print("Masukkan username: ");
                String adminId = input.nextLine();
                System.out.print("Masukkan password: ");
                String adminPass = input.nextLine();
                driverAkun = new AdminDriver();
                driverAkun.login(adminId, adminPass, adminFile);
                break;
            case 2:
                System.out.println("Login as Customer");
                System.out.print("Masukkan username: ");
                String cusId = input.nextLine();
                System.out.print("Masukkan password: ");
                String cussPass = input.nextLine();
                driverAkun = new CustomerDriver(cusId);
                driverAkun.login(cusId, cussPass, customerFile);
                break;
            case 3:
                displayUserMenu();
                int ForgotPassChoice = input.nextInt();

                switch (ForgotPassChoice) {
                    case 1:
                        akun = new Admin();
                        akun.forgotPassword(adminFile);
                        break;
                    case 2:
                        akun = new Customer();
                        akun.forgotPassword(customerFile);
                    case 3:
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
                break;
            case 4:
                displayUserMenu();
                int resetPassChoice = input.nextInt();

                switch (resetPassChoice) {
                    case 1:
                        akun = new Admin();
                        akun.resetPassword(adminFile);
                        break;
                    case 2:
                        akun = new Customer();
                        akun.resetPassword(customerFile);
                        break;
                    case 3:
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
                break;
            case 5:
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    /**
     * Metode untuk menjalankan proses sign up.
     */
    public void signup() {
        displayUserMenu();
        int signupChoice = input.nextInt();
        switch (signupChoice) {
            case 1:
                driverAkun = new AdminDriver();
                driverAkun.signupAs(adminFile);
                break;
            case 2:
                driverAkun = new CustomerDriver();
                driverAkun.signupAs(customerFile);
                break;
            case 3:
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    /**
     * Metode untuk keluar dari program.
     */
    public void exitProgram() {
        System.out.println("Terima Kasih Telah Menggunakan aplikasi kami.");
        System.exit(0);
    }
}
