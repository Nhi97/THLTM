package ex02UDP.chatroom;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChatServer {
    private static final int PORT = 6000;

    private int port;
    private InetAddress clientIp;
    private int clientPort;
    public static List<DatagramPacket> listSocket;

    public ChatServer(int port) {
        this.port = port;
    }

    private void execute() throws IOException {
        DatagramSocket serverSocket = new DatagramSocket(port);

        //server viet du lieu de gui cho cac client
        ThreadWriteServer writeServer = new ThreadWriteServer(serverSocket);
        writeServer.start();
        System.out.println("ChatServer is listening.........");

        //lang nghe yeu cau cua client
        while (true) {
            String sms = receiveDataServer(serverSocket);
            for (DatagramPacket item : listSocket) {
                if (!(item.getAddress().equals(clientIp) && item.getPort() == clientPort)) {
                    sendDataPacket(sms, serverSocket, item.getAddress(), item.getPort());
                }
            }
            System.out.println(sms);
        }

    }

    private String receiveDataServer(DatagramSocket server) {
        byte[] receiveData = new byte[4096];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        try {
            server.receive(receivePacket);
            clientIp = receivePacket.getAddress();
            clientPort = receivePacket.getPort();
            checkDuplicate(receivePacket);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(receivePacket.getData()).trim();
    }

    private void checkDuplicate(DatagramPacket clientPacket) {
        for (DatagramPacket item : listSocket) {
            if (item.getAddress().equals(clientPacket.getAddress()) && item.getPort() == clientPacket.getPort()) {
                return;
            }
        }
        listSocket.add(clientPacket);
    }

    public static void sendDataPacket(String value, DatagramSocket server, InetAddress clientIP, int clientPort) {
        byte[] sendData = new byte[4096];
        sendData = value.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientIP, clientPort);
        try {
            server.send(sendPacket);
        } catch (IOException e) {

        }
    }

    public static void main(String[] args) throws IOException {
        ChatServer chatServer = new ChatServer(PORT);
        ChatServer.listSocket = new ArrayList<>();
        chatServer.execute();
    }
}


class ThreadWriteServer extends Thread {

    private DatagramSocket server;

    public ThreadWriteServer(DatagramSocket server) {
        this.server = server;
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            //server tu minh nhap du lieu
            String sms = sc.nextLine();  // dang doi server nhap
            for (DatagramPacket item : ChatServer.listSocket) {
                ChatServer.sendDataPacket("server: " + sms, server, item.getAddress(), item.getPort());
            }
        }
    }

}
