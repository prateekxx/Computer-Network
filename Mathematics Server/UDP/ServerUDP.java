import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

class ServerUDP {
    private DatagramSocket socket;

    // int port = 17;
    public ServerUDP(int port) throws SocketException {
        System.out.println("Server Initialized");
        socket = new DatagramSocket(port);
    }

    public static void main(String[] args) {

        int port = 17;

        try {
            ServerUDP server = new ServerUDP(port);
            server.service();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void service() throws IOException, InterruptedException {
        byte[] buff = new byte[1024];
        DatagramPacket request = new DatagramPacket(buff, buff.length);// requested by client
        socket.receive(request);

        byte[] buffe = menu().getBytes();
        InetAddress clientAddress = request.getAddress();
        int clientPort = request.getPort();

        DatagramPacket response = new DatagramPacket(buffe, buffe.length, clientAddress, clientPort);
        socket.send(response);// send menu to client

        while (true) {
            System.out.println("\nServer Status: Idle\nWaiting to Recieve Data Packets");
            buff = new byte[1024];
            request = new DatagramPacket(buff, buff.length);
            socket.receive(request);

            int req[] = convert(buff);

            System.out.println("Data Packets Received Successfully\nProcessing...\n");

            if (req[0] == 1)
                System.out.println("Menu Displayed");
            else
                System.out.println("User Has Requested : " + req[0] + "\nInputs : " + req[1] + " " + req[2]);

            String ans = "";
            int temp = 0;
            switch (req[0]) {
                case 0:
                    break;
                case 1:
                    ans = menu();
                    break;
                case 2:
                    temp = add(req[1], req[2]);
                    ans = Integer.toString(temp);
                    break;
                case 3:
                    temp = diff(req[1], req[2]);
                    ans = Integer.toString(temp);
                    break;
                case 4:
                    temp = mult(req[1], req[2]);
                    ans = Integer.toString(temp);
                    break;

                case 5:
                    double temp2 = qout(req[1], req[2]);
                    ans = Double.toString(temp2);
                    break;
                default:
                    break;
            }
            if (req[0] != 1) {
                System.out.println("\nGenerating Output....");
                TimeUnit.SECONDS.sleep(2);//stopping for 2 seconds
                byte[] buffer = ans.getBytes();
                System.out.println("\nOutput Generated Successfully\nSending Data Packets");
                TimeUnit.SECONDS.sleep(2);
                clientAddress = request.getAddress();
                clientPort = request.getPort();

                response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
                socket.send(response);
                System.out.println("Data Packets Sent Successfully\n");
            } else {
                byte[] buffer = ans.getBytes();
                clientAddress = request.getAddress();
                clientPort = request.getPort();
                response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
                socket.send(response);
            }

        }
    }

    private String menu() {
        return "\tMath Server\n***********\nchoose a number for the coresponding service\nthen send response in this format\n\n\tservice: (int)\n\tinput1: (int)\n\tinput2: (int)\n\n 0. Quit\n 1. Print this help message\n 2. Addition\n 3. Subtraction\n 4. Multiplication\n 5. Division";
    }

    private int add(int a, int b) {
        return a + b;
    }

    private int diff(int a, int b) {
        return a - b;
    }

    private int mult(int a, int b) {
        return a * b;
    }

    private double qout(int a, int b) {
        if (b == 0) {
            System.out.println("Error : Division by 0 \n");
            return -1;
        }
        return (double) a / b;
    }

    public int[] convert(byte buf[]) {
        int intArr[] = new int[buf.length / 4];
        int offset = 0;
        for (int i = 0; i < intArr.length; i++) {
            intArr[i] = (buf[3 + offset] & 0xFF) | ((buf[2 + offset] & 0xFF) << 8) | ((buf[1 + offset] & 0xFF) << 16)
                    | ((buf[0 + offset] & 0xFF) << 24);
            offset += 4;
        }
        return intArr;
    }
}