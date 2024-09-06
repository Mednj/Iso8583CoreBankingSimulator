package com.hps.hps;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOField;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.packager.GenericPackager;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class TcpServer {

    private ServerSocket serverSocket;
    private boolean running = true;
    private ISOPackager packager;

    @PostConstruct
    public void startServer() {
        runServer();
    }

    private void runServer() {
        try {
            // Load the ISO8583 packager from XML file (placed in resources folder)
            packager = new GenericPackager("src/main/resources/iso93binary.xml");

            serverSocket = new ServerSocket(10008); // Port 10008
            System.out.println("TCP Server started on port 10008");

            while (running) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Create an ISO8583 message
                ISOMsg isoMsg = new ISOMsg();
                isoMsg.setPackager(packager); // Set the custom packager loaded from XML
                isoMsg.setMTI("0800"); // Set the MTI
                isoMsg.set(new ISOField(3, "000000")); // Processing code
                isoMsg.set(new ISOField(11, "000001")); // System trace number
                isoMsg.set(new ISOField(41, "29110001")); // Terminal ID
                isoMsg.set(new ISOField(60, "jPOS 6")); // Field 60
                isoMsg.set(new ISOField(70, "301")); // Network management code

                // Pack the ISOMsg into a byte array
                byte[] message = isoMsg.pack();
                System.out.println("Sending ISO8583 message: " + new String(message));

                // Send the ISO8583 message to the client
                OutputStream outputStream = clientSocket.getOutputStream();
                outputStream.write(message);
                outputStream.flush();

                // Adding a delay to ensure the message is fully sent before closing the connection
                Thread.sleep(100);

                clientSocket.close(); // Close the client connection
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void stopServer() {
        running = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
