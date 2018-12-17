package org.kth.id1212.server.model;

import org.kth.id1212.common.SessionDTO;

import java.util.UUID;

public class Session implements SessionDTO {
  private String id;
  private int userId;

  public Session(int userId) {
    this.id = UUID.randomUUID().toString();
    this.userId = userId;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public int getUserId() {
    return this.userId;
  }
}
