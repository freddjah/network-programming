package org.kth.id1212.common;

import java.io.Serializable;

public interface FileDTO extends Serializable {

  public static final int PERMISSION_ALL = 0;
  public static final int PERMISSION_PRIVATE = 1;

  String getFilename();

  int getSize();
}
