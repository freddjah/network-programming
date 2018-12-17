package org.kth.id1212.server.model;

import org.mindrot.jbcrypt.BCrypt;

public class User {

  private int id;
  private String username;
  private String passwordHash;

  public User(int id, String username, String passwordHash) {

    this.id = id;
    this.username = username;
    this.passwordHash = passwordHash;
  }

  public int getId() {
    return this.id;
  }

  public String getUsername() {
    return this.username;
  }

  public boolean verifyPassword(String password) {
    return BCrypt.checkpw(password, this.passwordHash);
  }

  public static String hashPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt(12));
  }
}
