package org.kth.id1212.server.controller;

import org.kth.id1212.common.FileCatalog;
import org.kth.id1212.common.FileCatalogException;
import org.kth.id1212.common.FileDTO;
import org.kth.id1212.common.SessionDTO;
import org.kth.id1212.server.integration.FileDAO;
import org.kth.id1212.server.integration.UserDAO;
import org.kth.id1212.server.model.File;
import org.kth.id1212.server.model.Session;
import org.kth.id1212.server.model.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class Controller extends UnicastRemoteObject implements FileCatalog {

  private HashMap<SessionDTO, User> sessions = new HashMap<>();
  private FileDAO fileDb;
  private UserDAO userDb;

  public Controller(Connection dbConnection) throws RemoteException {
    super();

    this.fileDb = new FileDAO(dbConnection);
    this.userDb = new UserDAO(dbConnection);
  }

  @Override
  public synchronized SessionDTO login(String username, String password) throws FileCatalogException {

    try {
      User user = userDb.findByUsername(username);

      if (user == null) {
        throw new FileCatalogException("User not found");
      }

      if (!user.verifyPassword(password)) {
        throw new FileCatalogException("Username and password does not match");
      }

      Session session = new Session();
      sessions.put(session, user);

      return session;
    } catch (SQLException e) {
      throw new FileCatalogException("Database error");
    }
  }

  @Override
  public synchronized void logout(SessionDTO session) {
    this.sessions.remove(session);
  }

  @Override
  public synchronized List<? extends FileDTO> listFiles(SessionDTO session) throws FileCatalogException {

    this.validateSession(session);

    try {
      return this.fileDb.getAll();
    } catch (SQLException e) {
      throw new FileCatalogException("Database error");
    }
  }

  @Override
  public synchronized void uploadFile(SessionDTO session, String filename, int size, int readPermission, int writePermission) throws FileCatalogException {

    User user = this.validateSession(session);

    try {

      File existingFile = this.fileDb.findByFilename(filename);
      if (existingFile != null && !existingFile.hasWriteAccess(user)) {
        throw new FileCatalogException("You don't have write access to this file");
      }

      this.fileDb.store(filename, size, user.getId(), readPermission, writePermission);
    } catch (Exception e) {
      // @todo catch specific exception
      throw new FileCatalogException("Database error");
    }
  }

  @Override
  public synchronized void deleteFile(SessionDTO session, String filename) throws FileCatalogException {

    User user = this.validateSession(session);

    try {

      File existingFile = this.fileDb.findByFilename(filename);

      if (existingFile == null) {
        throw new FileCatalogException("File does not exist");
      }

      if (!existingFile.hasWriteAccess(user)) {
        throw new FileCatalogException("You don't have write access to this file");
      }

      this.fileDb.delete(filename);
    } catch (SQLException e) {
      throw new FileCatalogException("Database error");
    }
  }

  @Override
  public synchronized String downloadFile(SessionDTO session, String filename) throws FileCatalogException {

    User user = this.validateSession(session);

    try {

      File existingFile = this.fileDb.findByFilename(filename);

      if (existingFile == null) {
        throw new FileCatalogException("File does not exist");
      }

      if (!existingFile.hasReadAccess(user)) {
        throw new FileCatalogException("You don't have read access to this file");
      }

      return "file content";
    } catch (SQLException e) {
      throw new FileCatalogException("Database error");
    }
  }

  private User validateSession(SessionDTO session) throws FileCatalogException {

    User user = this.sessions.get(session);
    if (user == null) {
      throw new FileCatalogException("Your session is invalid");
    }

    return user;
  }
}
