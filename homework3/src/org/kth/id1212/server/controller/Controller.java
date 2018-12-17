package org.kth.id1212.server.controller;

import org.kth.id1212.common.*;
import org.kth.id1212.server.integration.FileDAO;
import org.kth.id1212.server.integration.UserDAO;
import org.kth.id1212.server.model.File;
import org.kth.id1212.server.model.Session;
import org.kth.id1212.server.model.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Controller extends UnicastRemoteObject implements FileCatalog {

  private HashMap<String, User> sessions = new HashMap<>();
  private HashMap<Integer, ArrayList<String>> messagesToUser = new HashMap<>();

  private FileDAO fileDb;
  private UserDAO userDb;

  public Controller(Connection dbConnection) throws RemoteException {
    super();

    this.fileDb = new FileDAO(dbConnection);
    this.userDb = new UserDAO(dbConnection);
  }

  @Override
  public synchronized SessionDTO login(String username, String password) throws FileCatalogException, UserLoginException {

    try {
      User user = userDb.findByUsername(username);

      if (user == null) {
        throw new UserLoginException("User not found");
      }

      if (!user.verifyPassword(password)) {
        throw new UserLoginException("Username and password does not match");
      }

      Session session = new Session();
      sessions.put(session.getId(), user);

      return session;
    } catch (SQLException e) {
      throw new FileCatalogException("Database error");
    }
  }

  @Override
  public SessionDTO register(String username, String password) throws FileCatalogException, UserRegisterException {

    try {
      User existingUser = this.userDb.findByUsername(username);

      if (existingUser != null) {
        throw new UserRegisterException("Username already exists");
      }

      this.userDb.register(username, User.hashPassword(password));

      return this.login(username, password);

    } catch (SQLException e) {
      e.printStackTrace();
      throw new FileCatalogException("Database error");
    } catch (UserLoginException e) {
      throw new UserRegisterException(e.getMessage());
    }
  }


  @Override
  public synchronized void logout(SessionDTO session) {
    this.sessions.remove(session.getId());
  }

  @Override
  public synchronized List<? extends FileDTO> listFiles(SessionDTO session) throws FileCatalogException {

    this.validateSession(session);

    try {
      return this.fileDb.getAll();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FileCatalogException("Database error");
    }
  }

  @Override
  public synchronized void uploadFile(SessionDTO session, String filename, int size, int readPermission, int writePermission) throws FileCatalogException {

    User user = this.validateSession(session);

    try {

      File existingFile = this.fileDb.findByFilename(filename);
      if (existingFile != null) {

        if (!existingFile.hasWriteAccess(user)) {
          throw new FileCatalogException("You don't have write access to this file");
        }

        if (existingFile.getUserId() != user.getId()) {
          sendMessageToUser(existingFile.getUserId(), "User " + user.getUsername() + " wrote to your file " + existingFile.getFilename());
        }
      }




      this.fileDb.store(filename, size, user.getId(), readPermission, writePermission);
    } catch (SQLException e) {
      // @todo catch specific exception
      e.printStackTrace();
      throw new FileCatalogException("Database error");
    }
  }

  private void sendMessageToUser(int userId, String message) {

    ArrayList<String> messages = this.messagesToUser.getOrDefault(userId, new ArrayList<>());
    messages.add(message);
    this.messagesToUser.put(userId, messages);
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

      if (existingFile.getUserId() != user.getId()) {
        sendMessageToUser(existingFile.getUserId(), "User " + user.getUsername() + " deleted your file " + existingFile.getFilename());
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

      if (existingFile.getUserId() != user.getId()) {
        sendMessageToUser(existingFile.getUserId(), "User " + user.getUsername() + " downloaded your file " + existingFile.getFilename());
      }

      return "file content";
    } catch (SQLException e) {
      throw new FileCatalogException("Database error");
    }
  }

  @Override
  public List<String> getUnreadUserMessages(SessionDTO session) throws FileCatalogException {

    User user = this.validateSession(session);

    return this.messagesToUser.remove(user.getId());
  }

  private User validateSession(SessionDTO session) throws FileCatalogException {

    User user = this.sessions.get(session.getId());
    if (user == null) {
      throw new FileCatalogException("Your session is invalid");
    }

    return user;
  }
}
