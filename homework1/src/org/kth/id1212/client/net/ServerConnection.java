package org.kth.id1212.client.net;

import org.kth.id1212.common.InvalidCommandException;
import org.kth.id1212.client.controller.ClientController;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerConnection extends Thread {
    private ClientController controller;

    private Socket connection;
    private DataOutputStream requestDataStream;
    private BufferedReader responseDataReader;

    private LinkedBlockingQueue<String> outgoingRequests = new LinkedBlockingQueue<>();

    public ServerConnection(ClientController controller, String serverUrl, int serverPort) throws IOException {
        this.controller = controller;
        this.connection = new Socket(serverUrl, serverPort);
        this.requestDataStream = new DataOutputStream(this.connection.getOutputStream());
        this.responseDataReader = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
    }

    private synchronized void sendOutgoingRequests() throws InterruptedException, IOException {
        while (outgoingRequests.isEmpty()) wait();

        String message = outgoingRequests.take();
        this.requestDataStream.writeBytes(message);
    }

    private String receiveIncomingResponse() throws IOException, InvalidCommandException {
        int contentLength;

        try {
            contentLength = Integer.parseInt(this.read(6));
        } catch (NumberFormatException e) {
            throw new InvalidCommandException("Could not parse command.");
        }

        return this.read(contentLength);
    }

    public synchronized void addOutgoingRequest(String message) throws InterruptedException {
        String responseLength = String.format("%06d", message.length());
        this.outgoingRequests.add(responseLength + message);
        notify();
    }

    private String read(int bytes) throws IOException {

        char[] cbuf = new char[bytes];
        this.responseDataReader.read(cbuf, 0, bytes);

        return String.copyValueOf(cbuf);
    }

    public void run() {
        while(true) {
            try {
                this.sendOutgoingRequests();
                String response = this.receiveIncomingResponse();
                controller.addResponse(response);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
