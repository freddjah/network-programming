package org.kth.id1212.server.integration;

import org.kth.id1212.server.model.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileDAO {

  private PreparedStatement storeStatement;
  private PreparedStatement deleteStatement;
  private PreparedStatement findByFilenameStatement;
  private PreparedStatement getAllStatement;

  public FileDAO(Connection dbConnection) {

    try {
      this.prepareStatements(dbConnection);
    } catch (SQLException e) {
      // @todo fix this
      e.printStackTrace();
    }
  }

  private void prepareStatements(Connection dbConnection) throws SQLException {

    this.storeStatement = dbConnection.prepareStatement("REPLACE INTO files (filename, `size`, user_id, read_permission, write_permission) VALUES (?, ?, ?, ?, ?)");
    this.deleteStatement = dbConnection.prepareStatement("DELETE FROM files WHERE filename = ? LIMIT 1");
    this.findByFilenameStatement = dbConnection.prepareStatement("SELECT * FROM files WHERE filename = ? LIMIT 1");
    this.getAllStatement = dbConnection.prepareStatement("SELECT * FROM files ORDER BY filename");
  }

  public File store(String filename, int size, int userId, int readPermission, int writePermission) throws SQLException {

    this.storeStatement.setString(1, filename);
    this.storeStatement.setInt(2, size);
    this.storeStatement.setInt(3, userId);
    this.storeStatement.setInt(4, readPermission);
    this.storeStatement.setInt(5, writePermission);

    this.storeStatement.executeUpdate();

    return new File(filename, size, userId, readPermission, writePermission);
  }

  public void delete(String filename) throws SQLException {

    this.deleteStatement.setString(1, filename);
    this.deleteStatement.executeUpdate();
  }

  public File findByFilename(String filename) throws SQLException {

    this.findByFilenameStatement.setString(1, filename);

    ResultSet result = this.findByFilenameStatement.executeQuery();

    if (result.next()) {
      return this.createFromResult(result);
    }

    return null;
  }

  public List<File> getAll() throws SQLException {

    ResultSet result = this.getAllStatement.executeQuery();
    List<File> files = new ArrayList<>();

    while (result.next()) {
      files.add(createFromResult(result));
    }

    return files;
  }

  private File createFromResult(ResultSet result) throws SQLException {

    return new File(
      result.getString("filename"),
      result.getInt("size"),
      result.getInt("user_id"),
      result.getInt("read_permission"),
      result.getInt("write_permission")
    );
  }

}
