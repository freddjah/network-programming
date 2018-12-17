package org.kth.id1212.server.model;

import org.kth.id1212.common.SessionDTO;

public class Session implements SessionDTO {
  private String id;

  public Session() {
    this.id = "123abc";
  }

  public String getId() {
    return id;
  }
}
