package org.kth.id1212;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Command {

  private HashMap<String, String> map = new HashMap<String, String>();

  Command(String type) {
    map.put("type", type);
  }

  Command(String type, HashMap<String, String> arguments) {

    this.map = arguments;
    this.map.put("type", type);
  }

  void set(String key, String value) {
    this.map.put(key, value);
  }

  public String get(String key) {
    return this.map.get(key);
  }

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

  public static Command createFromString(String command) {

    String type = "";
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

    return new Command(type, arguments);
  }
}