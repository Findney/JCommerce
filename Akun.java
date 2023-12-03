import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Kelas Akun digunakan untuk manajemen akun pengguna, seperti signup, login,
 * reset password, dan forgot password.
 * 
 * @author Kelompok 15 
 * @version 12-03-2023
 */
public class Akun {
    private static Scanner scanner;
    private String id;

    /**
     * Konstruktor default untuk kelas Akun.
     */
    public Akun() {

    }

    /**
     * Konstruktor dengan parameter untuk kelas Akun.
     * 
     * @param id ID akun yang akan diatur.
     */
    public Akun(String id) {
        this.id = id;
    }

    /**
     * Mengatur ID akun.
     * 
     * @param id ID akun yang akan diatur.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Mendapatkan ID akun.
     * 
     * @return ID akun.
     */
    public String getid() {
        return this.id;
    }

    /**
     * Melakukan proses signup, yaitu pendaftaran pengguna dengan username dan
     * password.
     * 
     * @param FILE_NAME Nama file untuk menyimpan data pengguna.
     */
    public void signUp(String FILE_NAME) {
        try {
            Map<String, String> userCredentials = readUserCredentials(FILE_NAME);

            scanner = new Scanner(System.in);

            System.out.println("=== Signup ===");
            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            if (userCredentials.containsKey(username)) {
                System.out.println("Username already exists. Please choose a different username.");
            } else {
                System.out.print("Enter password: ");
                String password = scanner.nextLine();

                System.out.print("Enter reset code: ");
                String reset_code = scanner.nextLine();

                userCredentials.put(username, password);
                userCredentials.put(username + ".", reset_code);

                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
                    oos.writeObject(userCredentials);
                    System.out.println("Signup successful.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Melakukan penghapusan akun pengguna.
     */
    public void deleteUser() {
        
    }

    /**
     * Membaca data pengguna dari file dan mengembalikan dalam bentuk peta.
     * 
     * @param FILE_NAME Nama file yang berisi data pengguna.
     * @return Peta yang berisi data pengguna.
     */
    public static Map<String, String> readUserCredentials(String FILE_NAME) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            @SuppressWarnings("unchecked")
            Map<String, String> userCredentials = (Map<String, String>) ois.readObject();
            return userCredentials;
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    /**
     * Melakukan proses login dengan memeriksa username dan password.
     * 
     * @param username  Username yang dimasukkan pengguna.
     * @param password  Password yang dimasukkan pengguna.
     * @param FILE_NAME Nama file yang berisi data pengguna.
     * @return True jika login berhasil, False jika tidak.
     */
    public boolean login(String username, String password, String FILE_NAME) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            @SuppressWarnings("unchecked")
            Map<String, String> userCredentials = (Map<String, String>) ois.readObject();

            if (userCredentials.containsKey(username) && userCredentials.get(username).equals(password)) {
                System.out.println("Login successful.");
                return true;

            } else {
                System.out.println("Invalid username or password.");
                return false;

            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("User data not found. Please signup first.");
        }
        return false;
    }

    /**
     * Melakukan reset password dengan memverifikasi username dan reset code.
     * 
     * @param FILE_NAME Nama file yang berisi data pengguna.
     */
    public void resetPassword(String FILE_NAME) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            scanner = new Scanner(System.in);

            System.out.println("=== Change Password ===");
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter reset_code: ");
            String reset_code = scanner.nextLine();

            @SuppressWarnings("unchecked")
            Map<String, String> userCredentials = (Map<String, String>) ois.readObject();

            if (userCredentials.containsKey(username) && userCredentials.get(username + ".").equals(reset_code)) {
                System.out.print("Enter new password: ");
                String newPassword = scanner.nextLine();

                userCredentials.put(username, newPassword);

                ois.close();

                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
                    oos.writeObject(userCredentials);
                    System.out.println("Password changed successfully.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Username/Reset code not found.");
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("User data not found. Please signup first.");
        }
    }

    /**
     * Melakukan proses forgot password dengan memverifikasi username dan reset
     * code, lalu menampilkan password yang terkait.
     * 
     * @param FILE_NAME Nama file yang berisi data pengguna.
     */
    public void forgotPassword(String FILE_NAME) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            scanner = new Scanner(System.in);

            System.out.println("=== Forgot Password ===");
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter reset_code: ");
            String reset_code = scanner.nextLine();
            @SuppressWarnings("unchecked")
            Map<String, String> userCredentials = (Map<String, String>) ois.readObject();

            if (userCredentials.containsKey(username) && userCredentials.get(username + ".").equals(reset_code)) {
                System.out.println("Your password is: " + userCredentials.get(username));
            } else {
                System.out.println("Username/Reset code not found.");
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("User data not found. Please signup first.");
        }
    }
}
