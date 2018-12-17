package org.kth.id1212.server.model;

import org.kth.id1212.common.FileCatalogException;
import org.kth.id1212.common.FileDTO;

public class File implements FileDTO {

  private String filename;
  private int size;
  private int readPermission;
  private int writePermission;
  private int userId;

  public File(String filename, int size, int userId, int readPermission, int writePermission) {
    this.filename = filename;
    this.size = size;
    this.userId = userId;
    this.readPermission = readPermission;
    this.writePermission = writePermission;
  }

  @Override
  public String getFilename() {
    return this.filename;
  }

  @Override
  public int getSize() {
    return this.size;
  }

  @Override
  public int getReadPermission() {
    return this.readPermission;
  }

  @Override
  public int getWritePermission() {
    return this.writePermission;
  }

  public int getUserId() {
    return this.userId;
  }

  public boolean hasReadAccess(User user) {
    return this.userId == user.getId() || this.readPermission == FileDTO.PERMISSION_ALL;
  }

  public boolean hasWriteAccess(User user) {
    return this.userId == user.getId() || this.writePermission == FileDTO.PERMISSION_ALL;
  }
}
