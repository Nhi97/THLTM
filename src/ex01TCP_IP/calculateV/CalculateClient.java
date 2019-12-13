package ex01TCP_IP.calculateV;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class CalculateClient {
    private static int PORT = 3001;
    private InetAddress host;
    private int port;

    public CalculateClient(InetAddress inetAddress, int port) {
        this.host = inetAddress;
        this.port = port;
    }

    public String input() throws IOException {
        String s = "";
        DataInputStream disC = new DataInputStream(System.in);
        s = disC.readLine();
        return s;
    }

    public void execute() throws IOException {
        System.out.println("Start CalculateClient !!!");

        Socket socket = new Socket(host, port);

        while (true) {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            System.out.print("Moi ban nhap chuoi: ");
            String chuoi = input();
            if (chuoi.equals("exit")) {
                break;
            } else {
                dos.writeUTF(chuoi);
                dos.flush();

                DataInputStream dis = new DataInputStream(socket.getInputStream());
                String msg = dis.readUTF();
                System.out.println("Ket Qua :" + msg);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        CalculateClient client = new CalculateClient(InetAddress.getLocalHost(), PORT);
        client.execute();
    }
}
