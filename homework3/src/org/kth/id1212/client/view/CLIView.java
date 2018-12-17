package org.kth.id1212.client.view;

import org.kth.id1212.common.*;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Scanner;

public class CLIView {

  private FileCatalog fileCatalog;
  transient private Scanner input = new Scanner(System.in);
  MessageClient messageClient;
  SessionDTO session;

  public CLIView(FileCatalog fileCatalog) {
    this.fileCatalog = fileCatalog;
  }

  public void start() {

    try {

      this.messageClient = new MessageClient();
      this.session = this.authenticate();

      while (true) {
        try {

          this.handleAction();

        } catch (FileCatalogException e) {
          System.out.println("Error: " + e.getMessage());
        }
      }

    } catch (FileCatalogException | RemoteException e) {
      e.printStackTrace();
    }
  }

  private SessionDTO authenticate() throws RemoteException, FileCatalogException {

    System.out.println("Press 1 to register");
    System.out.println("Press 2 to login");

    switch (input.nextInt()) {

      case 1:
        return this.register();

      case 2:
        return this.login();

      default:
        System.out.println("Error: You have to choose between choices 1 or 2.");
        return authenticate();
    }
  }

  private SessionDTO login() throws RemoteException, FileCatalogException {

    System.out.print("\nEnter your username: ");
    String username = this.input.next();

    System.out.print("Enter your password: ");
    String password = this.input.next();

    try {
      return this.fileCatalog.login(username, password, this.messageClient);
    } catch (UserLoginException e) {
      System.out.println("Error: " + e.getMessage());
      return login();
    }
  }

  private SessionDTO register() throws RemoteException, FileCatalogException {

    System.out.print("\nEnter your username: ");
    String username = this.input.next();

    System.out.print("Enter your password: ");
    String password = this.input.next();

    try {
      return this.fileCatalog.register(username, password, this.messageClient);
    } catch (UserRegisterException e) {
      System.out.println("Error: " + e.getMessage());
      return register();
    }
  }

  private void handleAction() throws RemoteException, FileCatalogException {

    System.out.println("\nChoose one of the following actions:");
    System.out.println("1. List files");
    System.out.println("2. Download file");
    System.out.println("3. Upload file");
    System.out.println("4. Delete file");
    System.out.println("5. Logout");

    switch (input.nextInt()) {

      case 1:
        this.listFiles();
        break;

      case 2:
        this.downloadFile();
        break;

      case 3:
        this.uploadFile();
        break;

      case 4:
        this.deleteFile();
        break;

      case 5:
        this.logout();
        break;

      default:
        System.out.println("Error: Choose options 1 to 5");
        this.handleAction();
        break;
    }
  }

  private void listFiles() throws RemoteException, FileCatalogException {

    List<? extends FileDTO> files = this.fileCatalog.listFiles(this.session);
    System.out.println();
    printListColumn("Filename", 30);
    printListColumn("Size", 10);
    printListColumn("Is Owner", 15);
    printListColumn("Read Perm", 15);
    printListColumn("Write Perm", 15);
    System.out.println();

    for (FileDTO file : files) {
      printListColumn(file.getFilename(), 30);
      printListColumn(file.getSize() + " KB", 10);
      printListColumn(file.isOwner(this.session.getUserId()) ? "YES" : "NO", 15);
      printListColumn(file.getReadPermission() == FileDTO.PERMISSION_PRIVATE ? "PRIVATE" : "PUBLIC", 15);
      printListColumn(file.getWritePermission() == FileDTO.PERMISSION_PRIVATE ? "PRIVATE" : "PUBLIC", 15);
      System.out.println();
    }
  }

  private static void printListColumn(String s, int n) {
    System.out.print(String.format("%1$-" + n + "s", s));
  }

  private void downloadFile() throws RemoteException, FileCatalogException {

    System.out.print("\nEnter filename: ");
    String filename = input.next();

    String fileContent = this.fileCatalog.downloadFile(this.session, filename);
    System.out.println("Content: " + fileContent);
  }

  private void uploadFile() throws RemoteException, FileCatalogException {

    System.out.print("\nEnter local filepath: ");
    String localFilepath = input.next();

    File localFile = new File(localFilepath);
    if (!localFile.exists()) {

      System.out.println("Error: Local file does not exist");
      this.uploadFile();
      return;
    }

    int size = Math.round(localFile.length() / 1000);
    if (size == 0) {
      size = 1;
    }

    System.out.print("Enter destination filename: ");
    String filename = input.next();

    boolean readPublic = this.handleBooleanInput("Do you want to make it readable for all? [yes|no] ");
    boolean writePublic = this.handleBooleanInput("Do you want to make it writable for all? [yes|no] ");
    int readPermission = readPublic ? FileDTO.PERMISSION_ALL : FileDTO.PERMISSION_PRIVATE;
    int writePermission = writePublic ? FileDTO.PERMISSION_ALL : FileDTO.PERMISSION_PRIVATE;

    this.fileCatalog.uploadFile(this.session, filename, size, readPermission, writePermission);
  }

  private void deleteFile() throws RemoteException, FileCatalogException {

    System.out.print("\nEnter filename: ");
    String filename = input.next();

    this.fileCatalog.deleteFile(this.session, filename);
    System.out.println("File was deleted");
  }

  private void logout() throws RemoteException {

    this.fileCatalog.logout(this.session);
    System.out.println("Bye bye");
    System.exit(0);
  }

  private boolean handleBooleanInput(String question) {

    System.out.print(question);
    String choice = input.next();

    if (choice.toLowerCase().equals("yes")) {
      return true;
    } else if (choice.toLowerCase().equals("no")) {
      return false;
    }

    return handleBooleanInput(question);
  }
}
