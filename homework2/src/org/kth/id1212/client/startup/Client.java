package org.kth.id1212.client.startup;

import org.kth.id1212.client.net.ServerHandler;
import org.kth.id1212.client.controller.GameController;
import org.kth.id1212.client.net.ServerOutputThread;
import org.kth.id1212.client.net.UserInputThread;
import org.kth.id1212.client.view.InputTerminalView;
import org.kth.id1212.client.view.OutputTerminalView;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;

/**
 * Client
 */
public class Client {

    public static void main(String[] args) {
        try {
            ServerHandler server = new ServerHandler("localhost", 3000);
            GameController controller = new GameController(server);

            initiateIO(controller);

            server.start();
        } catch (Exception e) {
            System.out.println("Could not boot program");
        }
    }

    public static void initiateIO(GameController controller) {
        new ServerOutputThread(controller).start();
        new UserInputThread(controller).start();
    }
}
