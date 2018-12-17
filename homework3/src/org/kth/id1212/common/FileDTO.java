package org.kth.id1212.common;

public interface FileDTO {

  public static final int PERMISSION_ALL = 0;
  public static final int PERMISSION_PRIVATE = 1;

  public String getFilename();

  public int getSize();

  public int getReadPermission();

  public int getWritePermission();

}
