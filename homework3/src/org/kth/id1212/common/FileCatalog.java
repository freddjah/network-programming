package org.kth.id1212.common;

import java.rmi.Remote;
import java.util.List;

public interface FileCatalog extends Remote {

  public static final String REGISTRY_NAME = "filecatalog";

  public SessionDTO login(String username, String password) throws FileCatalogException;

  public void logout(SessionDTO session);

  public List<? extends FileDTO> listFiles(SessionDTO session) throws FileCatalogException;

  public void uploadFile(SessionDTO session, String filename, int size, int readPermission, int writePermission) throws FileCatalogException;

  public void deleteFile(SessionDTO session, String filename) throws FileCatalogException;

  public String downloadFile(SessionDTO session, String filename) throws FileCatalogException;
}
