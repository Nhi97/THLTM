package ex01TCP_IP.chatroom;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
        //Phan bo sung
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhap vao ten cua ban: ");
        String name = sc.nextLine();

        Socket client = new Socket(host, port);

        ThreadReadClient readClient = new ThreadReadClient(client);
        readClient.start();

        ThreadWriteClient writeClient = new ThreadWriteClient(client, name);
        writeClient.start();

    }

    public static void main(String[] args) throws IOException {
        ChatClient chatClient = new ChatClient(InetAddress.getLocalHost(), PORT);
        chatClient.execute();
    }
}

class ThreadReadClient extends Thread {
    private Socket client;

    public ThreadReadClient(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(client.getInputStream());
            while (true) {
                //doi du lieu tu server gui ve
                String sms = dis.readUTF();
                System.out.println(sms);
            }
        } catch (IOException e) {
            try {
                dis.close();
                client.close();
            } catch (IOException ex) {
                System.out.println("Ngat ket noi voi server");
            }
        }
    }
}

class ThreadWriteClient extends Thread {
    private Socket client;
    private String name;

    public ThreadWriteClient(Socket client, String name) {
        this.client = client;
        this.name = name;
    }

    @Override
    public void run() {
        DataOutputStream dos = null;
        Scanner sc = null;
        try {
            dos = new DataOutputStream(client.getOutputStream());
            sc = new Scanner(System.in);
            while (true) {
                String sms = sc.nextLine();
                dos.writeUTF(name + ": " + sms);
            }
        } catch (IOException e) {
            try {
                dos.close();
                client.close();
            } catch (IOException ex) {
                System.out.println("Ngat ket noi voi server");
            }
        }
    }
}
