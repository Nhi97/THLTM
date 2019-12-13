package ex01TCP_IP.chatroom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatServer {
    private int port;
    public static ArrayList<Socket> listSocket;

    private static final int PORT = 6000;

    public ChatServer(int port) {
        this.port = port;
    }

    private void execute() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);

        //server viet du lieu de gui cho cac client
        ThreadWriteServer writeServer = new ThreadWriteServer();
        writeServer.start();
        System.out.println("CalculateServer is listening.........");

        //lang nghe yeu cau cua client
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Da ket noi: " + socket);
            listSocket.add(socket);
            ThreadReadServer readServer = new ThreadReadServer(socket);
            readServer.start();
        }

    }

    public static void main(String[] args) throws IOException {
        ChatServer chatServer = new ChatServer(PORT);
        ChatServer.listSocket = new ArrayList<>();
        chatServer.execute();
    }
}

class ThreadReadServer extends Thread {
    private Socket client;

    public ThreadReadServer(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(client.getInputStream());
            while (true) {
                //doi du lieu tu client gui len(doc du lieu client gui len)
                String sms = dis.readUTF();

                if (sms.contains("exit")) {
                    ChatServer.listSocket.remove(client);
                    System.out.println("Da ngat ket noi voi " + client);
                    dis.close();
                    client.close();
                    continue;  // Ngat ket noi client
                }

                //gui du lieu den tat ca cac client
                for (Socket item : ChatServer.listSocket) {
                    if (item.getPort() != client.getPort()) {
                        DataOutputStream dos = new DataOutputStream(item.getOutputStream());
                        dos.writeUTF(sms);
                    }
                }
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

class ThreadWriteServer extends Thread {
    @Override
    public void run() {
        DataOutputStream dos = null;
        Scanner sc = new Scanner(System.in);
        while (true) {
            //server tu minh nhap du lieu
            String sms = sc.nextLine();  // dang doi server nhap
            for (Socket item : ChatServer.listSocket) {
                try {
                    dos = new DataOutputStream(item.getOutputStream());
                    dos.writeUTF("CalculateServer: " + sms);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
