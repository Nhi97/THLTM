package ex02UDP.string;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class StringClient {
    private static DataInputStream disC;

    public static void main(String[] args) throws IOException {
        DatagramSocket clientSocket = new DatagramSocket(); // gan cong
        InetAddress ipAddress = InetAddress.getByName("localhost");
        byte[] sendData = new byte[4096];
        byte[] receiveData = new byte[4096];
        System.out.print("Nhap chuoi: ");
        disC = new DataInputStream(System.in);
        String s = disC.readLine();
        sendData = s.getBytes();

        //tao datagram co noi dung yeu cau loai du lieu de gui cho server
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, 9876);

        clientSocket.send(sendPacket); // gui du lieu cho server

        //tao datagram rong de nhan du lieu
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        //nhan du lieu tu server
        clientSocket.receive(receivePacket);

        //lay du lieu tu packet nhan duoc
        String result = new String(receivePacket.getData());
        System.out.println(result);


    }

}
