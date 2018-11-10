package org.kth.id1212.client.startup;

import org.kth.id1212.client.net.ServerHandler;
import org.kth.id1212.client.controller.GameController;
import org.kth.id1212.client.view.InputTerminalView;
import org.kth.id1212.client.view.OutputTerminalView;
import java.io.IOException;

/**
 * Client
 */
public class Client {
    public static void main(String[] args) throws IOException {
        ServerHandler server = new ServerHandler("localhost", 3000);
        GameController gameController = new GameController(server);
    }
}
