import java.io.*;
import java.net.*;
import java.util.Scanner;

class ClientUDP {

    public static void main(String[] args) {

        String hostname = "localhost";
        int port = 17;
        Scanner sc = new Scanner(System.in);

        try {
            InetAddress address = InetAddress.getByName(hostname);
            DatagramSocket socket = new DatagramSocket();

            int req[] = new int[3];
            req[0] = 1;// choice
            req[1] = 2;// first number
            req[2] = 3;// second number


            byte[] buffer = integersToBytes(req);
            DatagramPacket request = new DatagramPacket(buffer, buffer.length, address, port);
            socket.send(request);// send to server

            byte[] buff = new byte[1024];//to store data received from server
            DatagramPacket response = new DatagramPacket(buff, buff.length);
            socket.receive(response);
            // int received = ByteBuffer.wrap(buff).getInt();
            String received = new String(buff, 0, response.getLength());

            System.out.println(received);//first time menu

            do {
                System.out.print("\nservice: ");
                req[0] = sc.nextInt();// choice
                if (req[0] == 0) {
                    break;
                }
                if (req[0] != 1) {
                    System.out.print("input1 : ");
                    req[1] = sc.nextInt();
                    System.out.print("input2 : ");
                    req[2] = sc.nextInt();
                }
                buffer = integersToBytes(req);
                request = new DatagramPacket(buffer, buffer.length, address, port);
                try {
                    socket.send(request);
                    System.out.println("Request Send Successfully\nYour Response is being Generated....\n");
                } catch (IOException ioe) {
                    System.out.println("Request Send Failed\n" + ioe.getStackTrace());
                }
                buff = new byte[512];
                response = new DatagramPacket(buff, buff.length);
                socket.receive(response);

                // int received = ByteBuffer.wrap(buff).getInt();
                received = new String(buff, 0, response.getLength());
                if (req[0] != 1)
                    System.out.println("Answer : " + received);
                else
                    System.out.println(received);

            } while (req[0] != 0);

            sc.close();
            socket.close();// teardown
            
        } catch (Exception ex) {
            System.out.println("Timeout error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    static byte[] integersToBytes(int[] values) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        for (int i = 0; i < values.length; ++i) {
            dos.writeInt(values[i]);
        }

        return baos.toByteArray();
    }
}