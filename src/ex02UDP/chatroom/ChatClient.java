package ex02UDP.chatroom;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient extends JFrame {
    private static final int PORT = 6000;

    private InetAddress host;
    private int port;

    public ChatClient(InetAddress host, int port) {
        this.host = host;
        this.port = port;
    }

    private void execute() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhap vao ten cua ban: ");
        String name = sc.nextLine();

        //tao socket
        DatagramSocket clientSocket = new DatagramSocket();

        ThreadReadClient readClient = new ThreadReadClient(clientSocket);
        readClient.start();

        ThreadWriteClient writeClient = new ThreadWriteClient(clientSocket, host, port, name);
        writeClient.start();

    }

    public static void main(String[] args) throws IOException {
        ChatClient chatClient = new ChatClient(InetAddress.getLocalHost(), PORT);
        chatClient.execute();
    }
}

class ThreadReadClient extends Thread {
    private DatagramSocket client;

    public ThreadReadClient(DatagramSocket client) {
        this.client = client;
    }

    @Override
    public void run() {
        while (true) {
            //doi du lieu tu server gui ve
            String sms = receiveDataClient(client);
            System.out.println(sms);
        }
    }

    private String receiveDataClient(DatagramSocket client) {
        byte[] receiveData = new byte[4096];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        String result = "";
        try {
            client.receive(receivePacket);
            result = new String(receivePacket.getData()).trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}


class ThreadWriteClient extends Thread {
    private DatagramSocket client;
    private InetAddress inetAddress;
    private int port;
    private String name;

    public ThreadWriteClient(DatagramSocket client, InetAddress inetAddress, int port, String name) {
        this.client = client;
        this.inetAddress = inetAddress;
        this.port = port;
        this.name = name;
    }

    @Override
    public void run() {
        Scanner sc = null;
        sc = new Scanner(System.in);
        while (true) {
            String sms = sc.nextLine();
            //dong goi packet de gui len server
            DatagramPacket sendPacket = sendDataClient(name + ": " + sms);
            try {
                client.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private DatagramPacket sendDataClient(String value) {
        byte[] sendData = new byte[4096];
        sendData = value.getBytes();
        return new DatagramPacket(sendData, sendData.length, inetAddress, port);
    }
}
