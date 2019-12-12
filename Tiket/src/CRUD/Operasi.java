package CRUD;

import java.util.*;
import java.io.*;
import javax.swing.*;

public class Operasi {

    public static void listTiket() throws IOException{
        FileReader fileInput;
        BufferedReader bufferInput;

        try {
            fileInput = new FileReader("tiket.txt");
            bufferInput = new BufferedReader(fileInput);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"Database tidak ditemukan!!!","Wah error nih",JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(null,"Silakan tambah data terlebih dahulu","Pemberitahuan Penting",JOptionPane.INFORMATION_MESSAGE);
            tambahTiket();
            return;
        }

        // Kita buat header nya secara manual
        System.out.print("____________________________________________________________________________________________");
        System.out.println("\n| No |      Kereta      |   Kelas   | Berangkat |  Tiba  |\t Harga");
        System.out.println("--------------------------------------------------------------------------------------------");

        String data = bufferInput.readLine(); // Akan memulai pembacaan file di baris pertama
        int nomor = 0;
        while (data != null) {
            nomor++;
            StringTokenizer masukan = new StringTokenizer(data, ",");

            masukan.nextToken(); // Kita skip bagian primary keys nya
            String nomer = String.format("| %2d ",nomor); // Kita tambahkan nomor secara manual
            String kereta = String.format("| %s",masukan.nextToken()); // Bagian nama kereta
            String kelas = String.format("| %s",masukan.nextToken()); // Bagian kelas kereta
            String berangkat = String.format("|%s",masukan.nextToken()); // Bagian waktu saat keberangkatan
            String tiba = String.format("|%s",masukan.nextToken()); // Bagian waktu saat tiba
            String harga = String.format("|%s",masukan.nextToken()); // Bagian harga
            System.out.println(nomer + kereta + kelas + berangkat + tiba + harga); // Mencetak data keseluruhan

            data = bufferInput.readLine(); // Akan memulai pembacaan file di baris selanjutnya
        }
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    public static void cariTiket() throws IOException{}

    public static void tambahTiket() throws IOException{}

    public static void updateTiket() throws IOException{}

    public static void hapusTiket() throws IOException{}
}
