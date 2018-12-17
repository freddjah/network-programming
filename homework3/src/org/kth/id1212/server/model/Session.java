package org.kth.id1212.server.model;

import org.kth.id1212.common.SessionDTO;

import java.util.UUID;

public class Session implements SessionDTO {
  private String id;

  public Session() {
    this.id = UUID.randomUUID().toString();
  }

  public String getId() {
    return id;
  }
}
