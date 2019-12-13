package ex01TCP_IP.calculateV;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Stack;

public class CalculateServer {
    private static int PORT = 3001;
    private int port;
    private static ServerSocket serverSocket;

    public CalculateServer(int port) {
        this.port = port;
    }

    private void execute() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Start CalculateServer !!!");

        Socket socket = serverSocket.accept();

        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        while (true) {
            String msg = dis.readUTF();

            String s = String.valueOf(result(msg));
            dos.writeUTF(s);
        }
    }


    public static void main(String[] args) throws IOException {
        CalculateServer server = new CalculateServer(PORT);
        server.execute();
    }

    public int priority(char c) {
        if (c == '+' || c == '-') {
            return 1;

        } else if (c == '*' || c == '/') {
            return 2;

        } else {
            return 0;
        }
    }

    public boolean isOperation(char c) {
        if (c != '+' && c != '-' && c != '*' && c != '/' && c != '(' && c != ')') {
            return false;

        } else {
            return true;
        }
    }

    public String[] processString(String sMath) {
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

    public String[] postfix(String[] elementMath) {
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

    public double result(String s) {
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