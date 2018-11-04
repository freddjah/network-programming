package org.kth.id1212.client;

import java.io.IOException;

public class Client {
    private ServerConnection connection;
    private GameHandler ui;

    Client(String serverUrl, int port) throws IOException {
        this.connection = new ServerConnection(this, serverUrl, port);
        this.ui = new GameHandler(this);

        this.ui.start();
        this.connection.start();
    }

    protected void addRequest(String message) throws InterruptedException {
        this.connection.addOutgoingRequest(message);
    }

    protected void addResponse(String message) {
        this.ui.addServerResponse(message);
    }

    public static void main(String args[]) throws IOException {
        new Client("localhost", 3000);
    }
}
