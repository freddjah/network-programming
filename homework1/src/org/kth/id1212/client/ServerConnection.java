package org.kth.id1212.client;

import org.kth.id1212.InvalidCommandException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerConnection extends Thread {
    private Client controller;

    private Socket connection;
    private DataOutputStream requestDataStream;
    private BufferedReader responseDataReader;

    private LinkedBlockingQueue<String> outgoingRequests = new LinkedBlockingQueue<>();

    public ServerConnection(Client controller, String serverUrl, int serverPort) throws IOException {
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
            String read = this.read(6);
            System.out.println("What should be integers is " + read);
            contentLength = Integer.parseInt(read);
            System.out.println("Content length is: " + contentLength);
        } catch (NumberFormatException e) {
            throw new InvalidCommandException("Could not parse command.");
        }

        String content = this.read(contentLength);
        System.out.println("Received content:" + content);
        return content;
    }

    public synchronized void addOutgoingRequest(String message) throws InterruptedException {
        String responseLength = String.format("%06d", message.length());
        this.outgoingRequests.add(responseLength + message);
        System.out.println("Outgoing request added: " + this.outgoingRequests.peek());
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
