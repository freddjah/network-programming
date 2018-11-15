package org.kth.id1212.client.startup;

import org.kth.id1212.client.net.ServerHandler;
import org.kth.id1212.client.controller.GameController;
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

            boolean connected = false;

            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("localhost", 3000));

            ByteBuffer msgFromServer = ByteBuffer.allocateDirect(1024);

            connected = true;

            Selector selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_CONNECT);

            while (connected) {

                selector.select();
                for (SelectionKey key : selector.selectedKeys()) {

                    selector.selectedKeys().remove(key);

                    if (!key.isValid()) {
                        continue;
                    }

                    if (key.isConnectable()) {

                        socketChannel.finishConnect();
                        key.interestOps(SelectionKey.OP_WRITE);

                    } else if (key.isReadable()) {

                        msgFromServer.clear();
                        int numOfReadBytes = socketChannel.read(msgFromServer);
                        if (numOfReadBytes == -1) {
                            throw new Exception("Could not read");
                        }

                        String message = messageFromBuffer(msgFromServer);
                        System.out.println("Got message: " + message);
                        Thread.sleep(1000);
                        key.interestOps(SelectionKey.OP_WRITE);

                    } else if (key.isWritable()) {

                        System.out.println("Sending message: yolo");
                        ByteBuffer message = ByteBuffer.wrap("yolo".getBytes());
                        socketChannel.write(message);
                        key.interestOps(SelectionKey.OP_READ);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

      /*
        ServerHandler server = new ServerHandler("localhost", 3000);
        GameController gameController = new GameController(server);
        */
    }

    private static String messageFromBuffer(ByteBuffer buffer) {
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return new String(bytes);
    }
}
