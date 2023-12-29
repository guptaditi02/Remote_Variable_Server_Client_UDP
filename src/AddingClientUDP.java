//Aditi Gupta - argupta@andrew.cmu.edu - Project2Task3
// Used code from EchoClientUDP.java from Coulouris textbook to make the changes
//Used code from Lab 5 for separation of concerns

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class AddingClientUDP {
    // Variable to store the server's port number
    private static int serverPort;

    public static void main(String args[]) {
        try {
            // Announce that the client is running
            System.out.println("The client is running.");

            // Create a BufferedReader to read input from the user
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            // Prompt the user for the server side port number
            System.out.print("Enter the server side port number (e.g., 6789): ");
            serverPort = Integer.parseInt(reader.readLine());

            // Display the client's main menu and capture the chosen option
            String nextLine, value, id, total;
            BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
            nextLine = menu();

            while (nextLine != null) {
                if (nextLine.trim().equalsIgnoreCase("1")) {
                    // Option 1: Add a value to the sum
                    System.out.println("Enter a value to add to your sum:");
                    value = typed.readLine();
                    System.out.println("Enter your ID:");
                    id = typed.readLine();
                    String add = "add";
                    total = id + "," + value + "," + add;
                    add(total); // Send the request to the server
                    nextLine = menu(); // Show the menu again
                } else if (nextLine.trim().equalsIgnoreCase("2")) {
                    // Option 2: Subtract a value from the sum
                    System.out.println("Enter a value to subtract from your sum:");
                    value = typed.readLine();
                    System.out.println("Enter your ID:");
                    id = typed.readLine();
                    String diff = "diff";
                    total = id + "," + value + "," + diff;
                    add(total); // Send the request to the server
                    nextLine = menu(); // Show the menu again
                } else if (nextLine.trim().equalsIgnoreCase("3")) {
                    // Option 3: Get the current sum
                    int num = 0;
                    System.out.println("Enter your ID:");
                    id = typed.readLine();
                    String get = "get";
                    total = id + "," + num + "," + get;
                    add(total); // Send the request to the server
                    nextLine = menu(); // Show the menu again
                } else if (nextLine.trim().equalsIgnoreCase("4")) {
                    // Option 4: Exit the client
                    System.out.println("Client side quitting. The remote variable server is still running.");
                    break;
                } else {
                    System.out.println("Invalid option. Please choose a valid option (1-4).");
                    nextLine = menu(); // Show the menu again
                }
            }
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
    }

    // Method to send a request to the server
    // Used Lab 5 for help for this method
    public static void add(String i) {
        DatagramSocket socket = null;
        try {
            // Change the server address to "localhost"
            InetAddress host = InetAddress.getByName("localhost");

            // Create a DatagramSocket for sending and receiving UDP packets
            socket = new DatagramSocket();

            // Convert the data to a byte array and send to the server
            byte[] m = i.getBytes();

            // Send the byte array to the server
            DatagramPacket request = new DatagramPacket(m, m.length, host, serverPort);
            socket.send(request);

            // Receive the reply from the server (containing the updated sum)
            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            socket.receive(reply);

            // Display the server's reply
            System.out.println("Reply from server: " + new String(reply.getData(), 0, reply.getLength()));

        } catch (IOException e) {
            System.out.println("Error in add method: " + e.getMessage());
        } finally {
            if (socket != null)
                socket.close();
        }
    }

    /**
     * Displays the menu of available operations to the user and captures their input.
     *
     * @return The user's menu choice.
     * @throws IOException If there's an error reading the user's input.
     */
    // Method to display the client menu and get user input
    public static String menu() throws IOException {
        System.out.println("1. Add a value to your sum.");
        System.out.println("2. Subtract a value from your sum.");
        System.out.println("3. Get your sum.");
        System.out.println("4. Exit client.");
        BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
        String nextLine = typed.readLine();
        return nextLine;
    }
}
