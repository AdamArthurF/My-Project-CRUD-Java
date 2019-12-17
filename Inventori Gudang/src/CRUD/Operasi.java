package CRUD;

import javax.swing.*;
import java.util.*;
import java.io.*;

public class Operasi {

    public static void listBarang() throws IOException{
        FileReader fileInput;
        BufferedReader bufferInput;

        // Kita cek file database nya (inventory.txt) ada atau tidak
        try {
            fileInput = new FileReader("inventory.txt");
            bufferInput = new BufferedReader(fileInput);
        } catch (Exception ex){
            JOptionPane.showMessageDialog(null,"Database tidak ditemukan!!!","Wah error nih",JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(null,"Silakan tambah data terlebih dahulu","Pemberitahuan Penting",JOptionPane.INFORMATION_MESSAGE);
            barangMasuk();
            return;
        }

        // Kita buat header nya secara manual
        System.out.print("________________________________________________________________________________________________");
        System.out.println("\n| No |\tTahun |\tJenis Barang\t|      Merk     |             Seri Barang            |  Stok  |");
        System.out.println("------------------------------------------------------------------------------------------------");

        String data = bufferInput.readLine(); // Akan memulai pembacaaan file di baris pertama
        int nomor = 0;

        while (data != null){ // Jika data tidak kosong, maka kita baca isi nya
            nomor++;
            StringTokenizer masukan = new StringTokenizer(data, ","); // Membaca per kata di baris pertama

            masukan.nextToken(); // Kita skip bagian primary keys nya
            masukan.nextToken(); // Kita skip bagian supplier nya
            String nomer = String.format("| %2d ",nomor); // Kita tambahkan nomor secara manual
            String stok = String.format("|   %-5s|",masukan.nextToken()); // Bagian stok barang
            String tahun = String.format("|\t%4s  ",masukan.nextToken()); // Bagian tahun
            String jenisBarang = String.format("|\t%-16s",masukan.nextToken()); // Bagian jenis barang
            String Merk = String.format("|     %-10s",masukan.nextToken()); // Bagian merk barang
            String Seri = String.format("| %-35s",masukan.nextToken()); // Bagian seri barang
            System.out.println(nomer + tahun + jenisBarang + Merk + Seri + stok); // Mencetak data keseluruhan

            data = bufferInput.readLine(); // Akan memulai pembacaan file di baris selanjutnya
        }
        System.out.println("------------------------------------------------------------------------------------------------");
    }

    public static void searchData() throws IOException{
        Scanner inputUser = new Scanner(System.in);

        // Mengecek database kita (inventory.txt) ada atau tidak
        try {
            File file = new File("inventory.txt"); // Untuk mengecek file kita ada atau tidak
        } catch (Exception ex){
            JOptionPane.showMessageDialog(null,"Database tidak ditemukan!!!","Wah error nih!",JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(null,"Silakan tambah data terlebih dahulu","Pemberitahuan",JOptionPane.INFORMATION_MESSAGE);
            barangMasuk();
            return;
        }

        // Kita ambil inputan keyword dari user
        System.out.print("Masukkan kata kunci untuk mencari barang : ");
        String cariString = inputUser.nextLine();
        String[] kataKunci = cariString.split("\\s+"); // Kita ubah menjadi Array  dengan tipe data String

        // Kita cek keyword di database
        Utility.cekBarangDiDatabase(kataKunci,true);
    }

    public static void barangMasuk() throws IOException{
        FileWriter fileOutput = new FileWriter("inventory.txt",true);
        BufferedWriter bufferOutput = new BufferedWriter(fileOutput);

        // Mengambil input dari user untuk menambah data
        Scanner inputUser = new Scanner(System.in);
        String jawab, supplier, pilihJenis, jenis = null, merk, seri, tahun;
        int stok;

        System.out.println("A. Tambah barang baru");
        System.out.println("B. Tambah stok barang");
        System.out.print("--> ");
        jawab = inputUser.nextLine();
        while (!jawab.equalsIgnoreCase("A") && !jawab.equalsIgnoreCase("B")) {
            System.out.println("Maaf anda salah input! Silahkan coba lagi!");
            JOptionPane.showMessageDialog(null,"Maaf anda salah input! Silahkan coba lagi!","Error",JOptionPane.ERROR_MESSAGE);
            System.out.print("--> ");
            jawab = inputUser.nextLine();
        }

        // Menulis barang di database (inventory.txt)
        if (jawab.equalsIgnoreCase("A")){
            System.out.print("Masukkan supplier : ");
            supplier = inputUser.nextLine();
            System.out.println("Jenis Barang : ");
            System.out.println("1. Elektronik");
            System.out.println("2. Transportasi");
            System.out.print("Masukkan jenis barang : ");
            pilihJenis = inputUser.nextLine();
            if (pilihJenis.equals("1")) {
                jenis = "Elektronik";
            } else if (pilihJenis.equals("2")) {
                jenis = "Transportasi";
            }

            System.out.print("Masukan merk barang : ");
            merk = inputUser.nextLine();
            System.out.print("Masukan seri barang : ");
            seri = inputUser.nextLine();
            System.out.print("Masukan tahun barang (YYYY) : ");
            tahun = Utility.ambilTahun();
            System.out.print("Masukkan banyak nya barang : ");
            stok = inputUser.nextInt(); inputUser.nextLine();

            long nomorEntry = Utility.ambilEntry(merk, tahun) + 1; // Menciptakan nomorEntry

            String merkTanpaSpasi = merk.replaceAll("\\s+",""); // Kita hapus semua spasi
            String primaryKey = merkTanpaSpasi + "_" + tahun + "_" + nomorEntry;
            System.out.println("\n---- Data yang akan anda masukkan : ----");
            System.out.println("----------------------------------------");
            System.out.println("Primary key      : " + primaryKey);
            System.out.println("Supplier         : " + supplier);
            System.out.println("Tahun Barang     : " + tahun);
            System.out.println("Jenis Barang     : " + jenis);
            System.out.println("Merk Barang      : " + merk);
            System.out.println("Seri Barang      : " + seri);
            System.out.println("Banyaknya Barang : " + stok);

            boolean isTambah = Utility.GET_YES_OR_NO("Apakah anda ingin menambahkan data tersebut? "); // Sebagai konfirmasi
            if(isTambah){
                bufferOutput.write(primaryKey + "," + supplier + "," + stok + "," + tahun + "," + jenis + "," + merk + "," + seri);
                bufferOutput.newLine(); // Menciptakan baris baru (enter)
                bufferOutput.flush(); // Menuliskan di database (inventory.txt)
                System.out.println("Data barang berhasil ditambahkan!");
                JOptionPane.showMessageDialog(null,"Data barang berhasil ditambahkan!","Pemberitahuan",JOptionPane.INFORMATION_MESSAGE);
                listBarang();
            }
        } else if (jawab.equalsIgnoreCase("B")){
            File database = new File("inventory.txt");
            FileReader fileInput = new FileReader(database);
            BufferedReader bufferInput = new BufferedReader(fileInput);

            // Tampilkan data terlebih dahulu
            listBarang();
            System.out.print("Masukkan nomor barang yang akan ditambah stoknya : ");
            int nomor = inputUser.nextInt();

            String data = bufferInput.readLine();
            String tahun2 = null,jenis2 = null,merk2 = null,seri2 = null;
            int nomorData = 0;
            while (data != null) {
                nomorData++;
                if (nomorData == nomor) {
                    StringTokenizer masukan = new StringTokenizer(data, ",");
                    masukan.nextToken(); // Primary keys
                    masukan.nextToken(); // Supplier
                    masukan.nextToken(); // Stok
                    tahun2 = masukan.nextToken();
                    jenis2 = masukan.nextToken();
                    merk2 = masukan.nextToken();
                    seri2 = masukan.nextToken();
                }
                data = bufferInput.readLine();
            }
            if (nomor > nomorData) {
                JOptionPane.showMessageDialog(null,"Barang tidak ditemukan!","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            System.out.print("Berapa jumlah yang akan ditambahkan : ");
            int stok2 = inputUser.nextInt();

            String[] keywords = {tahun2 + "," + jenis2 + "," + merk2 + "," + seri2};
            Utility.tambahStok(keywords,stok2);
            System.out.println("Stok barang berhasil ditambahkan!");
            JOptionPane.showMessageDialog(null,"Stok barang berhasil ditambahkan!","Pemberitahuan",JOptionPane.INFORMATION_MESSAGE);
            listBarang();
        }

        // Jangan lupa untuk menutup file
        bufferOutput.close(); // Menutup file
    }

    public static void barangKeluar() throws IOException{
        // Kita ambil file database original (inventory.txt)
        File database = new File("inventory.txt");
        FileReader fileInput = new FileReader(database);
        BufferedReader bufferInput = new BufferedReader(fileInput);

        // Kita buat file database sementara (temporary.txt);
        File temporary = new File("temporary.txt");
        FileWriter fileOutput = new FileWriter(temporary);
        BufferedWriter bufferOutput = new BufferedWriter(fileOutput);

        // Tampilkan data terlebih dahulu
        System.out.println("-_-_-_-_- LIST BARANG -_-_-_-_-");
        listBarang();

        // Kita ambil input dari user
        Scanner inputUser = new Scanner(System.in);
        System.out.print("Masukkan nama peminjam : ");
        String peminjam = inputUser.nextLine();

        System.out.print("Masukkan nomor barang yang akan dipinjamkan : ");
        int nomorPinjam = inputUser.nextInt();

        // Tampilkan data yang ingin diupdate
        String data = bufferInput.readLine();
        int entryCounts = 0;
        int stokAwal = 0;

        while (data != null) {
            entryCounts++;
            StringTokenizer masukan = new StringTokenizer(data, ",");

            // Tampilkan data entrycounts = nomorPinjam
            if (nomorPinjam == entryCounts) {
                System.out.print("Jumlah yang akan dipinjamkan : ");
                int jumlahPinjam = inputUser.nextInt();

                String[] fieldData = {"supplier", "stok", "tahun", "jenis", "merk", "seri"};
                String[] tempData = new String[6];

                masukan = new StringTokenizer(data, ",");
                String originalData = masukan.nextToken();
                for (int i = 0;i < fieldData.length;i++) {
                    originalData = masukan.nextToken();
                    if (i == 1) { // stok
                        stokAwal = Integer.parseInt(originalData);
                        tempData[i] = String.valueOf(stokAwal - jumlahPinjam);
                    } else {
                        tempData[i] = originalData;
                    }
                }

                if (stokAwal - jumlahPinjam <= 0) {
                    JOptionPane.showMessageDialog(null,"Stok tidak mencukupi! \nProses peminjaman dibatalkan!","Error",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Utility.tambahPeminjam(peminjam);

                // Tampilkan data ke layar
                masukan = new StringTokenizer(data, ",");
                System.out.println("\n---- Barang yang akan anda pinjamkan : ----");
                System.out.println("-------------------------------------------");
                System.out.println("Primary key       : " + masukan.nextToken());
                System.out.println("Supplier          : " + masukan.nextToken());
                masukan.nextToken(); // Kita skip bagian stok nya
                System.out.println("Tahun Barang      : " + masukan.nextToken());
                System.out.println("Jenis Barang      : " + masukan.nextToken());
                System.out.println("Merk Barang       : " + masukan.nextToken());
                System.out.println("Seri Barang       : " + masukan.nextToken());
                System.out.println("Stok awal         : " + stokAwal);
                System.out.println("Dipinjamkan       : " + jumlahPinjam);
                System.out.println("Sisa stok         : " + tempData[1]);

                boolean isPinjam = Utility.GET_YES_OR_NO("Apakah anda ingin meminjamkan barang tersebut?");
                if (isPinjam) {
                    // Format data baru ke dalam database
                    String supplier = tempData[0];
                    String stokBaru = tempData[1];
                    String tahun = tempData[2];
                    String jenis = tempData[3];
                    String merk = tempData[4];
                    String seri = tempData[5];

                    // Kita bikin primary keys nya
                    long nomorEntry = Utility.ambilEntry(merk, tahun);
                    String merkTanpaSpasi = merk.replaceAll("\\s+", "");
                    String primaryKey = merkTanpaSpasi + "_" + tahun + "_" + nomorEntry;

                    // Tulis data kedalam database sementara (temporary.txt)
                    bufferOutput.write(primaryKey + "," + supplier + "," + stokBaru + "," + tahun + "," + jenis + "," + merk + "," + seri);
                    JOptionPane.showMessageDialog(null,"Barang berhasil dipinjamkan!","Pemberitahuan",JOptionPane.INFORMATION_MESSAGE);
                    listBarang();
                } else {
                    // Copy data
                    bufferOutput.write(data);
                }
            } else {
                // Copy data
                bufferOutput.write(data);
            }

            bufferOutput.newLine();
            data = bufferInput.readLine();
        }
        if (nomorPinjam > entryCounts) {
            JOptionPane.showMessageDialog(null,"Barang tidak ditemukan! \nProses peminjaman dibatalkan!","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Menulis data kedalam file temporary database (temporary.txt)
        bufferOutput.flush();

        // Kita delete original database (inventory.txt)
        database.delete();

        // Rename file temporary.txt menjadi inventory.txt
        temporary.renameTo(database);
    }

    public static void dataSupplier() throws IOException{
        // Kita ambil file database original (inventory.txt)
        File database = new File("inventory.txt");
        FileReader fileInput = new FileReader(database);
        BufferedReader bufferInput = new BufferedReader(fileInput);

        // Kita buat file baru untuk data supplier (supplyInventory.txt)
        File dataSupplier = new File ("supplyInventory.txt");
        FileWriter fileOutput = new FileWriter(dataSupplier);
        BufferedWriter bufferOutput = new BufferedWriter(fileOutput);

        ArrayList<String> supplierList = new ArrayList<>();
        int indeks = 0;
        String data = bufferInput.readLine();
        Scanner dataScanner;
        while (data != null) {
            dataScanner = new Scanner(data);
            dataScanner.useDelimiter(",");
            dataScanner.next();
            supplierList.add(indeks,dataScanner.next());
            indeks++;
            data = bufferInput.readLine();
        }
        Collections.sort(supplierList);
        for (int i = 0;i < supplierList.size();i++) {
            for (int j = 0; j < supplierList.size() - 1; j++) {
                if (supplierList.get(j).equals(supplierList.get(j + 1))) {
                    supplierList.remove(j);
                }
            }
        }
        indeks = 0;
        supplierList.trimToSize();
        while (indeks < supplierList.size()) {
            bufferOutput.write(String.valueOf(supplierList.get(indeks)));
            bufferOutput.newLine();
            indeks++;
        }
        bufferOutput.flush();
        bufferOutput.close();
        bufferInput.close();

        // Kita ambil file dataSupplier nya (dataSupplier.txt)
        File file = new File("supplyInventory.txt");
        FileReader fileInput2 = new FileReader(file);
        BufferedReader bufferInput2 = new BufferedReader(fileInput2);

        String data2 = bufferInput2.readLine();
        Scanner dataScanner2;
        int nomorData = 0;
        while (data2 != null) {
            nomorData++;
            dataScanner2 = new Scanner(data2);
            String nomor = String.format("%d.",nomorData);
            String supplier = String.format(" %-30s",dataScanner2.nextLine());
            System.out.println(nomor + supplier);
            data2 = bufferInput2.readLine();
        }
    }

    public static void dataPeminjaman() throws IOException{
        // Kita ambil file data peminjam (pinjamInventory.txt)
        File dataPeminjam = new File("pinjamInventory.txt");
        FileReader fileInput = new FileReader(dataPeminjam);
        BufferedReader bufferInput = new BufferedReader(fileInput);

        String data = bufferInput.readLine();
        Scanner dataScanner;
        int nomorData = 0;
        while (data != null) {
            nomorData++;
            dataScanner = new Scanner(data);
            String nomor = String.format("%d.",nomorData);
            String peminjam = String.format(" %-30s",dataScanner.nextLine());
            System.out.println(nomor + peminjam);
            data = bufferInput.readLine();
        }
    }

    public static void updateBarang() throws IOException{
        // Kita ambil file database original (inventory.txt)
        File database = new File("inventory.txt"); // Mengecek file kita ada atau tidak
        FileReader fileInput = new FileReader(database);
        BufferedReader bufferedInput = new BufferedReader(fileInput);

        // Kita buat file database sementara (temporary.txt)
        File temporary = new File("temporary.txt"); // Mengecek file kita ada atau tidak
        FileWriter fileOutput = new FileWriter(temporary);
        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

        // Tampilkan data
        System.out.println("-_-_-_-_- LIST BARANG -_-_-_-_-");
        listBarang();

        // Kita ambil input dari user
        Scanner inputUser = new Scanner(System.in);
        System.out.print("\nMasukan nomor barang yang akan diupdate : ");
        int updateNum = inputUser.nextInt();

        // Tampilkan data yang ingin diupdate
        String data = bufferedInput.readLine();
        int entryCounts = 0;

        while (data != null){
            entryCounts++;
            StringTokenizer masukan = new StringTokenizer(data,","); // Memulai pembacaan file per kata

            // Tampilkan jika entrycounts == updateNum
            if (updateNum == entryCounts){
                System.out.println("\n------ Data yang akan anda update adalah : ------");
                System.out.println("--------------------------------------------------");
                System.out.println("Primary Keys   : " + masukan.nextToken()); // Bagian primary keys
                System.out.println("Supplier       : " + masukan.nextToken()); // Bagian supplier
                System.out.println("Stok Barang    : " + masukan.nextToken()); // Bagian stok barang
                System.out.println("Tahun Barang   : " + masukan.nextToken()); // Bagian tahun barang
                System.out.println("Jenis Barang   : " + masukan.nextToken()); // Bagian jenis barang
                System.out.println("Merk Barang    : " + masukan.nextToken()); // Bagian merk barang
                System.out.println("Seri Barang    : " + masukan.nextToken()); // Bagian seri barang

                // Update data dengan cara mengambil input dari user
                String[] fieldData = {"supplier","stok","tahun","jenis","merk","seri"};
                String[] tempData = new String[6];

                masukan = new StringTokenizer(data,","); // Kita refresh data
                String originalData = masukan.nextToken();
                for (int i = 0; i < fieldData.length; i++) {
                    originalData = masukan.nextToken();
                    if (i == 1) { // Kita lewati bagian stok nya
                        tempData[i] = originalData;
                        continue;
                    }

                    boolean isUpdate = Utility.GET_YES_OR_NO("Apakah anda ingin mengubah " + fieldData[i] + "?");
                    if (isUpdate) {
                        // Mengambil input dari user
                        if (fieldData[i].equalsIgnoreCase("tahun")) {
                            System.out.print("Masukkan tahun barang, format = (YYYY) : ");
                            tempData[i] = Utility.ambilTahun();
                        } else {
                            inputUser = new Scanner(System.in);
                            System.out.print("\nMasukkan " + fieldData[i] + " baru : ");
                            tempData[i] = inputUser.nextLine();
                        }
                    } else {
                        tempData[i] = originalData;
                    }
                }

                // Tampilkan data baru ke layar
                masukan = new StringTokenizer(data,",");
                masukan.nextToken(); // Kita skip bagian primary keys nya
                System.out.println("\n------- Data baru anda adalah : -----");
                System.out.println("-------------------------------------");
                System.out.println("Supplier Barang  : " + masukan.nextToken() + " --> " + tempData[0]);
                masukan.nextToken(); // Kita lompati bagian stok nya
                System.out.println("Tahun Barang     : " + masukan.nextToken() + " --> " + tempData[2]);
                System.out.println("Jenis Barang     : " + masukan.nextToken() + " --> " + tempData[3]);
                System.out.println("Merk Barang      : " + masukan.nextToken() + " --> " + tempData[4]);
                System.out.println("Seri Barang      : " + masukan.nextToken() + " --> " + tempData[5]);

                boolean isUpdate = Utility.GET_YES_OR_NO("Apakah anda yakin ingin mengupdate data tersebut");
                if (isUpdate){
                    // Cek data baru di database
                    boolean isExist = Utility.cekBarangDiDatabase(tempData,false);
                    if(isExist){
                        System.err.println("Data barang sudah ada di database, proses update dibatalkan, \nsilahkan delete data yang bersangkutan");
                        // Keseluruhan data tetap kita copy ke dalam temporary database (temporary.txt)
                        bufferedOutput.write(data);
                    } else {
                        // Format data baru ke dalam database
                        String supplier = tempData[0];
                        String stok = tempData[1];
                        String tahun = tempData[2];
                        String jenis = tempData[3];
                        String merk = tempData[4];
                        String seri = tempData[5];

                        // Kita bikin primary key lagi
                        long nomorEntry = Utility.ambilEntry(merk, tahun) + 1;
                        String merkTanpaSpasi = merk.replaceAll("\\s+","");
                        String primaryKey = merkTanpaSpasi + "_" + tahun + "_" + nomorEntry;

                        // Tulis data kedalam database sementara (temporary.txt)
                        bufferedOutput.write(primaryKey + "," + supplier + "," + stok + "," + tahun + "," + jenis + "," + merk + "," + seri);
                        System.out.println("Data barang berhasil diupdate!");
                        JOptionPane.showMessageDialog(null,"Data barang berhasil diupdate!","Pemberitahuan",JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    // Copy data
                    bufferedOutput.write(data);
                }
            } else {
                // Copy data
                bufferedOutput.write(data);
            }
            bufferedOutput.newLine();
            data = bufferedInput.readLine(); // Memulai pembacaan file per kata di baris selanjutnya
        }

        // Menulis data kedalam file temporary database (temporary.txt)
        bufferedOutput.flush();

        // Kita delete original database (inventory.txt)
        database.delete();

        // Rename file temporary.txt menjadi inventory.txt
        temporary.renameTo(database);
    }

    public static void deleteBarang() throws IOException{
        // Kita ambil database original (inventory.txt)
        File database = new File("inventory.txt"); // Mengecek file kita ada atau tidak
        FileReader fileInput = new FileReader(database);
        BufferedReader bufferedInput = new BufferedReader(fileInput);

        // Kita buat temporary database (temporary.txt)
        File temporary = new File("temporary.txt"); // Mengecek file kita ada atau tidak
        FileWriter fileOutput = new FileWriter(temporary);
        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

        // Tampilkan data
        System.out.println("-_-_-_-_- LIST BARANG -_-_-_-_-");
        listBarang();

        // Kita ambil input dari user untuk menghapus data
        Scanner inputUser = new Scanner(System.in);
        System.out.print("\nMasukkan nomor barang yang akan dihapus : ");
        int deleteNum = inputUser.nextInt();

        // Looping untuk membaca data tiap baris dan skip data yang akan dihapus
        boolean isFound = false;
        int entryCounts = 0;
        String data = bufferedInput.readLine(); // Akan memulai pembacaan file di baris pertama

        while (data != null){
            entryCounts++;
            boolean isDelete = false;

            StringTokenizer masukan = new StringTokenizer(data,","); // Memulai pembacaan file per kata
            // Tampilkan terlebih dahulu data yang ingin di hapus
            if (deleteNum == entryCounts){
                System.out.println("\n------ Data yang ingin anda delete adalah : ------");
                System.out.println("--------------------------------------------------");
                System.out.println("Primary Keys   : " + masukan.nextToken()); // Bagian primary keys
                System.out.println("Supplier       : " + masukan.nextToken()); // Bagian supplier
                System.out.println("Stok Barang    : " + masukan.nextToken()); // Bagian stok barang
                System.out.println("Tahun Barang   : " + masukan.nextToken()); // Bagian tahun barang
                System.out.println("Jenis Barang   : " + masukan.nextToken()); // Bagian jenis barang
                System.out.println("Merk Barang    : " + masukan.nextToken()); // Bagian merk barang
                System.out.println("Seri Barang    : " + masukan.nextToken()); // Bagian seri barang

                isDelete = Utility.GET_YES_OR_NO("Apakah anda yakin untuk menghapus?");
                isFound = true;
            }
            if(isDelete){
                /* Data yang akan kita hapus tetap berada di database original (inventory.txt),
                 * sedangkan data yang lainnya kita pindahkan ke database sementara (temporary.txt) */
                System.out.println("Data barang berhasil dihapus!");
                JOptionPane.showMessageDialog(null,"Data barang berhasil dihapus!","Pemberitahuan",JOptionPane.INFORMATION_MESSAGE);
            } else {
                /* kita pindahkan semua data dari database original (inventory.txt)
                 * ke database sementara (temporary.txt) */
                bufferedOutput.write(data);
                bufferedOutput.newLine();
            }
            data = bufferedInput.readLine();
        }
        if(!isFound){
            System.err.println("Barang tidak ditemukan");
        }

        // Menuliskan data ke file database sementara (temporary.txt)
        bufferedOutput.flush();

        // Delete original file database (inventory.txt)
        database.delete();

        // Rename file temporary (temporary.txt) menjadi file database asli (inventory.txt)
        temporary.renameTo(database);
    }
}
