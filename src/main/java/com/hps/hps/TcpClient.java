package com.hps.hps;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.packager.GenericPackager;

import java.io.InputStream;
import java.net.Socket;

public class TcpClient {

    public static void main(String[] args) {
        try {
            // Connect to the server (localhost:10008)
            Socket socket = new Socket("localhost", 10008);
            System.out.println("Connected to server at localhost:10008");

            // Load the ISO8583 packager from XML file (same one used by the server)
            ISOPackager packager = new GenericPackager("src/main/resources/iso93binary.xml");

            // Get the input stream from the socket
            InputStream inputStream = socket.getInputStream();

            // Buffer to hold incoming data
            byte[] buffer = new byte[1024];  // Adjust size if needed
            int bytesRead = inputStream.read(buffer);
            System.out.println("Received " + bytesRead + " bytes from the server");

            // Create a new ISOMsg and set the packager
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);

            // Unpack the message received from the server
            isoMsg.unpack(buffer);

            // Display the fields of the ISO message
            System.out.println("ISO8583 Message received:");
            for (int i = 0; i <= isoMsg.getMaxField(); i++) {
                if (isoMsg.hasField(i)) {
                    System.out.println("Field " + i + ": " + isoMsg.getString(i));
                }
            }

            // Close the connection
            socket.close();
            System.out.println("Connection closed");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
