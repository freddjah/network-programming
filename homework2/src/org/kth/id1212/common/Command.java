package org.kth.id1212.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Command {

  private HashMap<String, String> map = new HashMap<String, String>();

  public Command(String type) {
    map.put("type", type);
  }

  public Command(String type, HashMap<String, String> arguments) {

    this.map = arguments;
    this.map.put("type", type);
  }

  public void set(String key, String value) {
    this.map.put(key, value);
  }

  public String get(String key) {
    return this.map.get(key);
  }

  /*
   * Generate string command to send over TCP
   */
  public String toString() {

    String out = "";
    Iterator it = this.map.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry pair = (Map.Entry) it.next();
      out += pair.getKey() + "=" + pair.getValue() + ";";
      it.remove();
    }

    return out;
  }

  /*
   * Create command from string
   */
  public static Command createFromString(String command) throws Exception {

    try {

      String type = null;
      HashMap<String,String> arguments = new HashMap();

      String[] commands = command.split(";");
      for (String cmd : commands) {

        String[] cmdSplit = cmd.split("=");

        if (cmdSplit[0].equals("type")) {
          type = cmdSplit[1];
        } else {
          arguments.put(cmdSplit[0], cmdSplit[1]);
        }
      }

      if (type == null) {
        throw new Exception("Invalid command: Must include type");
      }

      return new Command(type, arguments);
    } catch (Exception e) {
      throw new InvalidCommandException("Invalid command");
    }
  }
}
