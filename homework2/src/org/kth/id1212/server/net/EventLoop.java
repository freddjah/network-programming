package org.kth.id1212.server.net;

import java.util.concurrent.LinkedBlockingQueue;

public class EventLoop {

  LinkedBlockingQueue<Runnable> callbacks = new LinkedBlockingQueue<>();
  Runnable wakeup;

  public EventLoop(Runnable wakeup) {
    this.wakeup = wakeup;
  }

  public void dispatch(Function function, Callback callback) {

    new Thread(() -> {
      try {

        Object result = function.run();
        this.callbacks.put(() -> {
          callback.run(result);
        });
        this.wakeup.run();

      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }).start();
  }

  public void run() throws InterruptedException {

    while (!this.callbacks.isEmpty()) {

      Runnable callback = this.callbacks.take();
      callback.run();
    }
  }

  public interface Function {
    Object run();
  }

  public interface Callback {
    void run(Object o);
  }

  /*
  public static void main(String[] args) {

    EventLoop eventLoop = new EventLoop();
    boolean run = true;


    try {
      while (true) {

        eventLoop.run();

        if (run) {

          run = false;

          eventLoop.dispatch(() -> {
            try {
              Thread.sleep(1000);
              return new String("THIS IS WORKING???");
            } catch (InterruptedException e) {
              e.printStackTrace();
            }

            return null;

          }, (Object o) -> {

            String message = (String) o;
            System.out.println("Got message: " + message);
          });
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  */
}
