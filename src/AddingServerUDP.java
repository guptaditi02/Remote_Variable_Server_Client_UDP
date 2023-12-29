//Aditi Gupta - argupta@andrew.cmu.edu - Project2Task3
// Used code from EchoServerUDP.java from Coulouris textbook to make the changes

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.TreeMap;

public class AddingServerUDP {
    private static int sum = 0, diff=0; // Variable to store the sum/ difference of values

    // Treemap to store the shared variable for each client identified by a unique ID
    private static TreeMap<Integer, Integer> map = new TreeMap<>();
    public static void main(String args[]) {
        DatagramSocket aSocket = null; // UDP socket for communication
        byte[] buffer = new byte[2046]; // Buffer for receiving incoming UDP packets

        try {
            // Announce that the server is running
            System.out.println("Server started");

            // Create a BufferedReader to read input from the user
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            // Prompt the user for the port number to listen on
            System.out.print("Enter the port number for the server to listen on (e.g., 6789): ");
            int serverPort = Integer.parseInt(reader.readLine());

            // Create a DatagramSocket to listen for incoming UDP packets
            aSocket = new DatagramSocket(serverPort);
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);

            while (true) {
                // Receive an incoming UDP packet (request) from a client
                aSocket.receive(request);

                // Create a new byte array with length equal to the message length
                byte[] data = new byte[request.getLength()];

                // Code taken from this site:
                // https://stackoverflow.com/questions/5690954/java-how-to-read-an-unknown-number-of-bytes-from-an-inputstream-socket-socke
                // Copy the message from request to data array
                System.arraycopy(request.getData(), request.getOffset(), data, 0, request.getLength());

                // Split the received data into components: ID, value, and operation
                // Used ChatGPT for this line
                String[] elements = new String(data).split(",");
                int id = Integer.valueOf(elements[0]);

                // Initialize the shared variable for new clients
                if (!map.containsKey(id)) {
                    map.put(id, 0);
                }

                int value = Integer.valueOf(elements[1]);
                String operation = elements[2];
                System.out.println("Visitor's ID: " + id );
                System.out.println("Operation Requested: " + operation);

                // Perform the requested operation (addition or subtraction)
                if (operation.equalsIgnoreCase("add")|| operation.equalsIgnoreCase("get")) {
                    sum = add(map.get(id), value);
                } else {
                    sum = diff(map.get(id), value);
                }

                // Update the value associated with the client ID in the map
                map.put(id, sum);

                // Print the updated value associated with the client ID
                System.out.println("Value associated with ID " + id + ": " + map.get(id));

                // Convert the sum to a byte array (assuming sum is an int)
                // Used ChatGPT to get this line of code
                byte[] sumBytes = String.valueOf(sum).getBytes();

                // Create a DatagramPacket with the sumBytes, client's address, and port
                DatagramPacket reply = new DatagramPacket(sumBytes, sumBytes.length, request.getAddress(), request.getPort());

                // Send the DatagramPacket back to the client
                aSocket.send(reply);

                // Print the new sum
                System.out.println("The result is " + sum);
            }
        } catch (SocketException e) {
            System.out.println("Socket Exception: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        } finally {
            if (aSocket != null)
                aSocket.close();
        }
    }

    /**
     * Adds the given value to the initial value and returns the sum.
     *
     * @param i Initial value.
     * @param value Value to be added.
     * @return Resultant sum.
     */
    public static int add(int i, int value) {
        sum = i+value;
        return sum;
    }

    /**
     * Subtracts the given value from the initial value and returns the difference.
     *
     * @param i Initial value.
     * @param value Value to be subtracted.
     * @return Resultant difference.
     */
    public static int diff(int i, int value) {
        diff = i-value;
        return diff;
    }

}
