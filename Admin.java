import java.util.Map;

/**
 * Kelas {@code Admin} mewakili akun administrator, yang memperluas kelas {@code Akun}.
 * Ini menyediakan fungsionalitas khusus untuk operasi administrator seperti mendaftar, logout,
 * dan menghapus akun pengguna.
 * 
 * @author Kelompok 15 
 * @version 12-03-2023
 */
public class Admin extends Akun {

    /**
     * Konstruktor default untuk kelas {@code Admin}.
     */
    public Admin() {
    }

    /**
     * Mencoba mendaftar sebagai administrator, memeriksa apakah sudah ada akun admin yang ada.
     *
     * @param FILE_NAME Nama file dari mana kredensial pengguna dibaca.
     */
    public void signup(String FILE_NAME) {
        System.out.println("Mendaftar sebagai admin");
        Map<String, String> kredensialPengguna = readUserCredentials(FILE_NAME);
        if (!kredensialPengguna.isEmpty()) {
            System.out.println("Tidak bisa membuat akun Admin karena Akun Admin sudah ada!");
        } else {
            this.signUp(FILE_NAME);
        }
    }

    /**
     * Keluar dari akun administrator.
     */
    public void logoutAs() {
        System.out.println("Keluar sebagai admin");
    }

    /**
     * Menghapus akun pengguna. Metode ini memanggil operasi hapus pengguna dari superclass.
     */
    public void deleteUser() {
        this.deleteUser();
    }
}
