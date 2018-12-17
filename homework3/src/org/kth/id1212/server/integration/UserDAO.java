package org.kth.id1212.server.integration;

import org.kth.id1212.server.model.User;

import java.sql.*;

public class UserDAO {

  private PreparedStatement registerStatement;
  private PreparedStatement findByUsernameStatement;

  public UserDAO(Connection dbConnection) {
    try {
      this.prepareStatements(dbConnection);
    } catch (SQLException e) {
      // @todo fix this
      e.printStackTrace();
    }
  }

  private void prepareStatements(Connection dbConnection) throws SQLException {
    this.registerStatement = dbConnection.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
    this.findByUsernameStatement = dbConnection.prepareStatement("SELECT * FROM users WHERE username = ? LIMIT 1");
  }

  public User register(String username, String password) throws SQLException {

    this.registerStatement.setString(1, username);
    this.registerStatement.setString(2, password);

    if (this.registerStatement.executeUpdate() != 1) {
      throw new SQLException("Could not execute query");
    }

    ResultSet result = this.registerStatement.getGeneratedKeys();

    if (!result.next()) {
      throw new SQLException("Something went wrong");
    }

    return new User(result.getInt(1), username, password);
  }

  public User findByUsername(String username) throws SQLException {
    this.findByUsernameStatement.setString(1, username);

    ResultSet result = this.findByUsernameStatement.executeQuery();

    if (result.next()) {
      return new User(result.getInt("id"), result.getString("username"), result.getString("password"));
    }

    return null;
  }
}
