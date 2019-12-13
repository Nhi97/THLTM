package ex02UDP.string;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class StringServer {
    private static int port = 9876;
    private static DatagramPacket sendPacket;

    public static void main(String[] args) throws Exception {

        //Gan cong 9876 cho chuong trinh
        DatagramSocket serverDatagramSocket = new DatagramSocket(port);

        //Tao cac mang byte de chua di va nhan
        System.out.println("CalculateServer is started");
        byte[] receiveData = new byte[4096];
        byte[] sendData = new byte[4096];

        while (true) {


            //Tao goi rong de nhan du lieu tu client
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            //Nhan du lieu tu client
            serverDatagramSocket.receive(receivePacket);

            //Lay dia chi IP cua may client
            InetAddress ipInetAddress = receivePacket.getAddress();

            //Lay port cua chuong trinh client
            int port = receivePacket.getPort();

            //Lay cac chuoi de gui nguoc lai client
            String request = new String(receivePacket.getData());


            String result = "reverseString: " + doReverseString(request).trim() + "\n" + "upcaseString: " + doUpcaseString(request) + "\n" +
                    "normalString: " + doNormalString(request) + "\n" + "upcaseNormalString: " + doUpcaseNormalString(request).trim() + "\n" +
                    "naString: " + doNAString(request) + "\n" + "numberOfString: " + numberOfString(request) + "";

            if (!request.trim().equals(" ")) {
                sendData = result.getBytes();

            } else {
                sendData = "CalculateServer not know what you want ? ".getBytes();
            }

            sendPacket = new DatagramPacket(sendData, sendData.length, ipInetAddress, port);

            //Gui lai cho client
            serverDatagramSocket.send(sendPacket);

        }

    }

    private static String doReverseString(String s) {
        String s1 = "";
        for (int i = s.length() - 1; i >= 0; i--) {
            s1 = s1 + s.charAt(i);
        }
        return s1;
    }

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
}
