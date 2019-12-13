package ex02UDP.calculate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class CalculateClient {
    private static int PORT = 9876;

    public static String input() throws IOException {
        String s = "";
        DataInputStream disC = new DataInputStream(System.in);
        s = disC.readLine();
        return s;
    }

    public static void execute() throws IOException {
        System.out.println("Start CalculateClient !!!");

        DatagramSocket clientSocket = new DatagramSocket(); // gan cong
        InetAddress ipAddress = InetAddress.getByName("localhost");
        byte[] sendData = new byte[4096];
        byte[] receiveData = new byte[4096];


        while (true) {

            System.out.print("Moi ban nhap chuoi: ");
            String chuoi = input();
            if (chuoi.equals("exit")) {
                break;
            } else {
                sendData = chuoi.getBytes();

                //tao datagram co noi dung yeu cau loai du lieu de gui cho server
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, PORT);

                clientSocket.send(sendPacket); // gui du lieu cho server

                //tao datagram rong de nhan du lieu
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                //nhan du lieu tu server
                clientSocket.receive(receivePacket);
                //lay du lieu tu packet nhan duoc
                String result = new String(receivePacket.getData());
                System.out.println("result: " + result);
            }

        }

    }

    public static void main(String[] args) throws IOException {
        execute();
    }
}
