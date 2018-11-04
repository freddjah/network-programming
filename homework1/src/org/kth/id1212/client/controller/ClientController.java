package org.kth.id1212.client.controller;

import java.io.IOException;
import org.kth.id1212.client.net.*;

public class ClientController {
    private ServerConnection connection;
    private GameHandler ui;

    public ClientController(String serverUrl, int port) throws IOException {
        this.connection = new ServerConnection(this, serverUrl, port);
        this.ui = new GameHandler(this);

        this.ui.start();
        this.connection.start();
    }

    public void addRequest(String message) throws InterruptedException {
        this.connection.addOutgoingRequest(message);
    }

    public void addResponse(String message) {
        this.ui.addServerResponse(message);
    }
}
