package ex01TCP_IP.string;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class StringServer {
    private static final int PORT = 6001;
    private static Socket socket;

    public static void main(String[] args) throws IOException {
        connection();
    }

    private static void connection() throws IOException {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (true) {
                socket = serverSocket.accept();
                ThreadReadServer threadReadServer = new ThreadReadServer(socket);
                threadReadServer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }

    //dao chuoi
    private static String doReverseString(String s) {
        String s1 = "";
        for (int i = s.length() - 1; i >= 0; i--) {
            s1 = s1 + s.charAt(i);
        }
        return s1;
    }

    //chuoi hoa
    private static String doUpcaseString(String st) {
        String s = "";
        for (int i = 0; i < st.length(); i++) {
            if (st.charAt(i) >= 'A' && st.charAt(i) <= 'Z') {
                s = s + (char) (st.charAt(i));
            } else if (st.charAt(i) >= 'a' && st.charAt(i) <= 'z') {
                s = s + (char) (st.charAt(i) - 32);
            } else if (st.charAt(i) == ' ') {
                s = s + (char) (st.charAt(i));
            }
        }
        return s;
    }

    //chuoi thuong
    private static String doNormalString(String st) {
        String s = "";
        for (int i = 0; i < st.length(); i++) {
            if (st.charAt(i) >= 'A' && st.charAt(i) <= 'Z') {
                s = s + (char) (st.charAt(i) + 32);
            } else if (st.charAt(i) >= 'a' && st.charAt(i) <= 'z') {
                s = s + (char) (st.charAt(i));
            } else if (st.charAt(i) == ' ') {
                s = s + (char) (st.charAt(i));
            }
        }
        return s;
    }

    //chuoi hoa thuong
    private static String doUpcaseNormalString(String st) {
        String s = "";
        for (int i = 0; i < st.length(); i++) {
            char c = st.charAt(i);
            if (c >= 'a' && c <= 'z') {
                c = (char) (c - 32);
            } else if (c >= 'A' && c <= 'Z') {
                c = (char) (c + 32);
            }
            s = s + c;
        }
        return s;

    }

    //nguyen am
    private static String doNAString(String st) {
        String nguyenAm = "ueoaiUEOAI";
        String result = "";
        for (int i = 0; i < st.length(); i++) {
            if (nguyenAm.indexOf(st.charAt(i)) >= 0 && result.indexOf(st.charAt(i)) < 0) {
                result += st.charAt(i);
            }
        }
        return result;
    }

    //so luong ky tu cua chuoi
    private static int numberOfString(String st) {
        String result = st.trim().replace("[ ]+", " ");
        int count = 1;
        for (int i = 0; i < result.length(); i++) {
            if (result.charAt(i) == ' ') {
                count++;
            }
        }
        return count;
    }

    public static class ThreadReadServer extends Thread {
        private Socket client;
        DataOutputStream dos = null;

        public ThreadReadServer(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            DataInputStream dis = null;
            try {
                dis = new DataInputStream(client.getInputStream());
                while (true) {
                    //
                    String sms = dis.readUTF();
                    System.out.println(sms);

                    if (sms.contains("exit")) {
                        System.out.println("Da ngat ket noi voi " + client);
                        dis.close();
                        client.close();
                        continue;  // Ngat ket noi client
                    }

                    arrString(sms);
                }
            } catch (IOException e) {
                try {
                    dis.close();
                    client.close();
                } catch (IOException ex) {
                }
            }
        }

        private void arrString(String sms) {
            try {
                String result = "reverseString: " + doReverseString(sms).trim() + "\n" + "upcaseString: " + doUpcaseString(sms) + "\n" +
                        "normalString: " + doNormalString(sms) + "\n" + "upcaseNormalString: " + doUpcaseNormalString(sms).trim() + "\n" +
                        "naString: " + doNAString(sms) + "\n" + "numberOfString: " + numberOfString(sms) + "";
                dos = new DataOutputStream(client.getOutputStream());
                dos.writeUTF(result);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
