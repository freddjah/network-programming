package org.kth.id1212.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface FileCatalog extends Remote {

  public static final String REGISTRY_NAME = "filecatalog";

  public SessionDTO login(String username, String password, Client client) throws FileCatalogException, RemoteException, UserLoginException;

  public SessionDTO register(String username, String password, Client client) throws FileCatalogException, RemoteException, UserRegisterException;

  public void logout(SessionDTO session) throws RemoteException;

  public List<? extends FileDTO> listFiles(SessionDTO session) throws FileCatalogException, RemoteException;

  public void uploadFile(SessionDTO session, String filename, int size, int readPermission, int writePermission) throws FileCatalogException, RemoteException;

  public void deleteFile(SessionDTO session, String filename) throws FileCatalogException, RemoteException;

  public String downloadFile(SessionDTO session, String filename) throws FileCatalogException, RemoteException;
}
