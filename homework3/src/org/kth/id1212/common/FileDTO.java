package org.kth.id1212.common;

import java.io.Serializable;

public interface FileDTO extends Serializable {

  int PERMISSION_ALL = 0;
  int PERMISSION_PRIVATE = 1;

  String getFilename();

  int getSize();

  boolean isOwner(int userId);

  int getReadPermission();

  int getWritePermission();
}
