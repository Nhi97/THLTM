package ex01TCP_IP.calculateH;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private static String SERVER_IP = "localhost";
    public static int PORT = 6001;
    public static Socket socket;
    public static String info;
    public static String calStr;

    public static DataInputStream disC;
    public static DataInputStream disS;
    public static DataOutputStream dos;


    public static void main(String[] args) {
        setUpNetworking();
        //bai1();
        System.out.println("---------------------");
        bai2();
    }

    private static void setUpNetworking() {
        try {
            socket = new Socket(SERVER_IP, PORT);
            System.out.println("================");
            System.out.println(socket);
            System.out.println("================");
            disC = new DataInputStream(System.in);
            disS = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void bai2() {
        System.out.println("bai tap 2 nhap vao chuoi phep tinh:");
        try {
            calStr = disC.readLine();
            dos.writeBytes(calStr + '\n');
            System.out.println("Ket qua cua phep tinh la:");
            double result = Double.parseDouble(disS.readLine());
            System.out.println(result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private static void bai1() {
        System.out.println("Nhap vao chuoi can xu ly:");
        try {
            info = disC.readLine();
            dos.writeBytes(info + '\n');
            System.out.println("Chuoi sau khi in hoa");
            String temp = disS.readLine();
            System.out.println(temp.toUpperCase());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}