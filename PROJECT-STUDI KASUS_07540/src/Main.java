import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class Kendaraan {
    protected String platNomor;
    protected Date jamMasuk;

    public Kendaraan(String platNomor, Date jamMasuk) {
        this.platNomor = platNomor;
        this.jamMasuk = jamMasuk;
    }
}

class Mobil extends Kendaraan {
    public Mobil(String platNomor, Date jamMasuk) {
        super(platNomor, jamMasuk);
    }
}

class ParkirMobil {
    private static Map<String, ArrayList<String>> parkedCars = new HashMap<>();
    private static boolean isAdminLoggedIn = false;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        loginAdmin(scanner);

        while (true) {
            if (isAdminLoggedIn) {
                System.out.println("1. Check-in Kendaraan");
                System.out.println("2. Check-out Kendaraan");
                System.out.println("3. Mencetak Detail Struk");
                System.out.println("4. Melihat Daftar Mobil Sedang Parkir");
                System.out.println("5. Melihat Semua Daftar Mobil yang Pernah Parkir");
                System.out.println("6. Log Out");

                System.out.print("Pilih opsi (1-6): ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        checkInKendaraan();
                        break;
                    case 2:
                        checkOutKendaraan();
                        break;
                    case 3:
                        cetakDetailStruk();
                        break;
                    case 4:
                        lihatDaftarMobilParkir();
                        break;
                    case 5:
                        lihatSemuaDaftarMobil();
                        break;
                    case 6:
                        System.out.println("Terima kasih. Program berakhir.");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Pilihan tidak valid. Silakan coba lagi.");
                }
            } else {
                System.out.println("Login Admin belum berhasil. Program berakhir.");
                System.exit(0);
            }
        }
    }

    private static void loginAdmin(Scanner scanner) {
        System.out.println("=== Login Admin ===");
        System.out.print("Username: ");
        String username = scanner.next();
        System.out.print("Password: ");
        String password = scanner.next();

        // Validasi username dan password
        if (username.equals("admin") && password.equals("123")) {
            System.out.println("Login Admin berhasil.");
            isAdminLoggedIn = true;
        } else {
            System.out.println("Login Admin gagal. Program berakhir.");
            System.exit(0);
        }
    }

    private static void checkInKendaraan() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Masukkan Plat Nomor Kendaraan: ");
        String platNomor = scanner.nextLine();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss");
        Date jamMasuk = new Date();
        try {
            System.out.print("Tanggal dan Jam Masuk (dd/mm/yyyy hh:mm:ss): ");
            String tanggalJamMasukStr = scanner.nextLine();
            jamMasuk = dateFormat.parse(tanggalJamMasukStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Mobil mobil = new Mobil(platNomor, jamMasuk);
        ArrayList<String> details = new ArrayList<>();
        details.add(dateFormat.format(mobil.jamMasuk));
        parkedCars.put(mobil.platNomor, details);

        System.out.println("Check-in berhasil.");
    }


    private static void checkOutKendaraan() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Masukkan Plat Nomor Kendaraan: ");
        String platNomor = scanner.nextLine();

        if (parkedCars.containsKey(platNomor)) {
            ArrayList<String> details = parkedCars.get(platNomor);
            String jamMasuk = details.get(0);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss");
            Date jamKeluar = new Date();
            try {
                System.out.print("Tanggal dan Jam Keluar (dd/mm/yyyy hh:mm:ss): ");
                String tanggalJamKeluarStr = scanner.nextLine();
                jamKeluar = dateFormat.parse(tanggalJamKeluarStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            double biayaParkir = hitungBiayaParkir(jamMasuk, jamKeluar);
            System.out.println("Biaya Parkir: Rp" + biayaParkir);

            parkedCars.remove(platNomor);
            System.out.println("Check-out berhasil.");
        } else {
            System.out.println("Kendaraan dengan plat nomor tersebut tidak ditemukan.");
        }
    }

    private static double hitungBiayaParkir(String jamMasuk, Date jamKeluar) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss");

        try {
            Date jamMasukDate = dateFormat.parse(jamMasuk);
            long selisihWaktuMillis = jamKeluar.getTime() - jamMasukDate.getTime();
            long selisihMenit = selisihWaktuMillis / (60 * 1000);


            double biayaParkir = 0;
            if (selisihMenit <= 10) {
                biayaParkir = 0;
            } else if (selisihMenit <= (24 * 60)) {
                biayaParkir = 10000;
            } else {
                long selisihJam = selisihMenit / 60;
                biayaParkir = 10000 + (selisihJam - 24) + 5000;
            }
            return biayaParkir;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }


    private static void cetakDetailStruk() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Masukkan Plat Nomor Kendaraan: ");
        String platNomor = scanner.nextLine();

        if (parkedCars.containsKey(platNomor)) {
            ArrayList<String> details = parkedCars.get(platNomor);
            String jamMasuk = details.get(0);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date jamKeluar = new Date();
            try {
                System.out.print("Tanggal dan Jam Keluar (dd/MM/yyyy HH:mm:ss): ");
                String tanggalJamKeluarStr = scanner.nextLine();
                jamKeluar = dateFormat.parse(tanggalJamKeluarStr);
            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println("Format tanggal dan jam keluar tidak valid. Program berakhir.");
                System.exit(0);
            }

            double biayaParkir = hitungBiayaParkir(jamMasuk, jamKeluar);

            System.out.println("Detail Struk:");
            System.out.println("Plat Nomor: " + platNomor);
            System.out.println("Jam Masuk: " + jamMasuk);
            System.out.println("Jam Keluar: " + dateFormat.format(jamKeluar));
            System.out.println("Biaya Parkir: Rp" + biayaParkir);
        } else {
            System.out.println("Kendaraan dengan plat nomor tersebut tidak ditemukan.");
        }
    }



    private static void lihatDaftarMobilParkir() {
        System.out.println("Daftar Mobil Sedang Parkir:");

        if (parkedCars.isEmpty()) {
            System.out.println("Tidak ada mobil yang sedang parkir.");
        } else {
            for (Map.Entry<String, ArrayList<String>> entry : parkedCars.entrySet()) {
                System.out.println("Plat Nomor: " + entry.getKey() + ", Jam Masuk: " + entry.getValue().get(0));
            }
        }
    }


    private static void lihatSemuaDaftarMobil() {
        System.out.println("Semua Daftar Mobil yang Pernah Parkir:");
        for (Map.Entry<String, ArrayList<String>> entry : parkedCars.entrySet()) {
            String platNomor = entry.getKey();
            ArrayList<String> details = entry.getValue();
            String jamMasuk = details.get(0);

            System.out.println("Plat Nomor: " + platNomor + ", Jam Masuk: " + jamMasuk);
        }
    }
}
