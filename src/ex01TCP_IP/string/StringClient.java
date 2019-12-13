package ex01TCP_IP.string;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class StringClient {
    private static final int PORT = 6001;
    private static final String IP = "localhost";
    private static DataInputStream dis;
    private static DataOutputStream dos;
    private static DataInputStream disC;
    private static String s;


    public static void main(String[] args) {
        connection();
    }

    private static void connection() {
        try {
            Socket socket = new Socket(IP, PORT);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            arrString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void arrString() {
        disC = new DataInputStream(System.in);

        try {
            while (true) {
                System.out.println("Nhap chuoi: ");

                s = disC.readLine();
                dos.writeUTF(s);

                String result = dis.readUTF();
                System.out.println("======================");
                System.out.println(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
