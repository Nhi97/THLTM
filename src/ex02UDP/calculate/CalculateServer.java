package ex02UDP.calculate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Stack;

public class CalculateServer {
    private static int PORT = 9876;

    private static void execute() throws IOException {
        //Gan cong 9876 cho chuong trinh
        DatagramSocket serverDatagramSocket = new DatagramSocket(PORT);

        //Tao cac mang byte de chua di va nhan
        System.out.println("CalculateServer is started");
        byte[] receiveData = new byte[4096];
        byte[] sendData = new byte[4096];

        while (true) {
            //tao packet rong de nhan du lieu tu client
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            //nhan du lieu tu client
            serverDatagramSocket.receive(receivePacket);

            //lay dia chi ip cua client
            InetAddress inetAddress = receivePacket.getAddress();

            //lay cong port cua client
            int port = receivePacket.getPort();

            //lay ket qua tra lai cho client
            String request = new String(receivePacket.getData());
            String result = String .valueOf(resultCalculate(request));

            if (!result.trim().equals(" ")) {
                sendData = result.getBytes();

            } else {
                sendData = "CalculateServer not know what you want ? ".getBytes();
            }

            //server gui tra lai ket qua cho client
            DatagramPacket sendDataPacket = new DatagramPacket(sendData, sendData.length, inetAddress, port);
            serverDatagramSocket.send(sendDataPacket);
        }
    }

    public static void main(String[] args) throws IOException {
        execute();
    }

    public static int priority(char c) {
        if (c == '+' || c == '-') {
            return 1;

        } else if (c == '*' || c == '/') {
            return 2;

        } else {
            return 0;
        }
    }

    public static boolean isOperation(char c) {
        if (c != '+' && c != '-' && c != '*' && c != '/' && c != '(' && c != ')') {
            return false;

        } else {
            return true;
        }
    }

    public static String[] processString(String sMath) {
        String s1 = "";
        String elementString[] = null;
        sMath = sMath.trim();
        for (int i = 0; i < sMath.length(); i++) {
            char c = sMath.charAt(i);
            if (!isOperation(c))
                s1 += c;
            else
                s1 += " " + c + " ";
        }
        s1 = s1.trim();
        elementString = s1.split(" ");
        String s = "";
        for (String string : elementString) {
            if (!string.equals("")) {
                s += string + " ";
            }
        }
        return s.split(" ");
    }

    public static String[] postfix(String[] elementMath) {
        String s1 = "";
        Stack<String> s = new Stack<>();
        for (int i = 0; i < elementMath.length; i++) {
            char c = elementMath[i].charAt(0);
            if (!isOperation(c)) {
                s1 = s1 + " " + elementMath[i];
            } else {
                if (c == '(') {
                    s.push(elementMath[i]);
                } else {
                    if (c == ')') {
                        char c1;
                        do {
                            if (s.isEmpty())
                                System.out.println("ho");
                            c1 = s.peek().charAt(0);
                            if (c1 != '(')
                                s1 = s1 + ' ' + s.peek();
                            s.pop();
                        } while (c1 != '(');
                    } else {
                        while (!s.isEmpty() && priority(s.peek().charAt(0)) >= priority(c)) {
                            s1 = s1 + " " + s.peek();
                            s.pop();
                        }
                        s.push(elementMath[i]);
                    }
                }
            }
        }
        while (!s.isEmpty()) {
            s1 = s1 + " " + s.peek();
            s.pop();
        }
        return s1.trim().split(" ");
    }

    public static double resultCalculate(String s) {
        String element[] = postfix(processString(s));
        Stack<Double> stack = new Stack<>();
        for (int i = 0; i < element.length; i++) {
            if (!isOperation(element[i].charAt(0))) {
                stack.push(Double.parseDouble(element[i]));
            } else {
                double a = stack.pop();
                double b = stack.pop();
                switch (element[i].charAt(0)) {
                    case '+':
                        stack.push(a + b);
                        break;
                    case '-':
                        stack.push(b - a);
                        break;
                    case '*':
                        stack.push(b * a);
                        break;
                    case '/':
                        stack.push(b / a);
                        break;
                }
            }
        }
        return stack.pop();
    }
}