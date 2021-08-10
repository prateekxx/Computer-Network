import java.net.*;
import java.io.*;

public class Server {
   private ServerSocket serverSocket;

   public Server(int port) throws IOException {
      System.out.println("Server Initialized");
      serverSocket = new ServerSocket(port);
      // serverSocket.setSoTimeout(1000000);
   }

   public static void main(String[] args) {
      int port = 1234;
      try {
         Server s = new Server(port);
         s.run();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void run() {
      int service;

      int input1 = 0;
      int input2 = 0;
      while (true) {

         try {

            System.out.println("Waiting for client on port " + serverSocket.getLocalPort());

            Socket server = serverSocket.accept();

            System.out.println("Just connected to " + server.getRemoteSocketAddress());

            DataInputStream input = new DataInputStream(server.getInputStream());
            DataOutputStream output = new DataOutputStream(server.getOutputStream());

            // initial interaction ;send menu
            output.writeUTF("\n\nSERVER\n" + menu());

            service = input.readInt();
            if (service > 1 && service < 6) {
               input1 = input.readInt();
               input2 = input.readInt();
            }

            do {
               System.out.println("Client Selected " + service);
               System.out.println("Gaining Input from Client");
               System.out.println("Processing...");
               // ** evaluate request

               if (service == 0) {
                  break; // from do while
               } else if (service == 1) {

                  output.writeUTF("\n\nSERVER Responded\n" + menu());

               } else if (service == 2) {

                  output.writeUTF("SERVER Responded\nAnswer : " + add(input1, input2));

               } else if (service == 3) {

                  output.writeUTF("SERVER Responded\nAnswer : " + diff(input1, input2));

               } else if (service == 4) {

                  output.writeUTF("SERVER Responded\nAnswer : " + mult(input1, input2));

               } else if (service == 5) {
                  double temp = qout(input1, input2);
                  if (temp == -1)
                     output.writeUTF("SERVER Responded\nError: Division by 0 not allowed");
                  else
                     output.writeUTF("SERVER Responded\nAnswer : " + temp);

               } else {
                  output.writeUTF("\nSERVER Responded\n" + "invalid choice");
               }
               // ** end of evaluate request
               System.out.println("Respond Sent to the Client");
               service = input.readInt();
               if (service > 1 && service < 6) {
                  input1 = input.readInt();
                  input2 = input.readInt();
               }

            } while (service != 0);

            output.writeUTF("Client Disconnect\n\t " + "Thank you for connecting to " + server.getLocalSocketAddress());

            server.close();

         } catch (IOException e) {

            e.printStackTrace();
            break;

         }
      }
   }

   private String menu() {
      return "\tMath Server\n***************************\nchoose a number for the coresponding service\nthen send response in this format\n\n\tservice: (int)\n\tinput1: (int)\n\tinput2: (int)\n\n 0. Quit\n 1. Print this help message\n 2. Addition\n 3. Subtraction\n 4. Multiplication\n 5. Division";
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
}