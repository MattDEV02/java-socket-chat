package main;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import main.Server;
import org.jetbrains.annotations.NotNull;
import utils.index;
import utils.thread.*;


/**
 * <h1>Socket Server Class </h1>
 * @version 1.0 from 14/03/2021
 * @author Matteo Lambertucci
 * @see serverThread
 * @since 1.0
 */

public final class Server extends Object {

  // start attributes
   private ArrayList<Socket> clientSockets = null;
   private ServerSocket serverSocket = null;
  // end attributes

   /**
    * <h2>Server Constructor</h2>
    * @param port the port that server will listen.
    * @return new instance of Server class.
    * @throws IOException Input-Output Exception
    */

   public Server(final int port) {
      if(index.isValidSocketPORT(port)) {
         try {
            this.serverSocket = new ServerSocket(port);
            this.clientSockets = new ArrayList<Socket>();
            final String serverProcess = index.serverProcess(this.serverSocket);
            System.out.println("Server running on:  | " + serverProcess + " |");
         } catch(final IOException e) {
            index.handleException(e);
         }
      } else
         index.handlePORT_ERR();
   }

   public Server(Server s) {
      if (s != null) {
         this.setClients(s.clientSockets);
         this.setServerSocket(s.serverSocket);
      } else {
         final String className = this.getClass().toString();
         index.handleCopy_Construct_ERR(className);
      }
   }
  // start methods

   public final String toString() {
      StringBuilder clientSocketStr = new StringBuilder("");
      for(Socket clientSocket : this.clientSockets) {
         final String clientProcess = index.clientProcess(clientSocket, false);
         clientSocketStr.append(clientProcess);
      }
      final String serverSocketStr = index.serverProcess(this.serverSocket);
      final String s = String.format("ServerSocket: %s \nClientSockets: %s", serverSocketStr, clientSocketStr.toString());
      return s;
   }

   public final ArrayList<Socket> getClientSockets() { return this.clientSockets; }

   public final ServerSocket getServerSocket() { return this.serverSocket; }

   public final void setClients(final ArrayList<Socket> clients) { this.clientSockets = clients; }

   public final void setServerSocket(final ServerSocket serverSocket) { this.serverSocket = serverSocket; }

   public final void comunication()   {
      try {
         for(;;) {
            final Socket client = serverSocket.accept();
            new serverThread(this, client).start(); // start new Thread
         }
      } catch (final IOException e) {
         index.handleException(e);
      }
   }

   public final void send(@NotNull final Socket clientSocket, final String msg) throws IOException {
      final OutputStream clientOutputStream = clientSocket.getOutputStream();
      final PrintStream serverOutput = new PrintStream(clientOutputStream);
      serverOutput.println(msg);
   }

   public final void broadcastMessages(final String msg) {
      try {
         for (final Socket clientSocket : this.clientSockets)
            this.send(clientSocket, msg);
      } catch (final IOException e) {
         index.handleException(e);
      }
   }

   public final String addClient(final Socket clientSocket) {
      this.clientSockets.add(clientSocket);
      final int numClients = this.clientSockets.size();
      index.serverLog(this.clientSockets, true);
      final String msg = index.serverMessage(clientSocket, numClients, true);
      return msg;
   }

   public final String removeClient(final Socket clientSocket) {
      String msg = "";
      try {
         clientSocket.close();
         if (clientSocket.isClosed()) {
            this.clientSockets.remove(clientSocket);
            final int numClients = this.clientSockets.size();
            index.serverLog(clientSocket, numClients, false);
            msg = index.serverMessage(clientSocket, numClients, false);
         } else
            System.out.println("Client still connected...");
      } catch(final IOException e) {
         return index.handleException(e);
      } finally {
         return msg;
      }
   }

   public final synchronized void close() {
      try {
         final String serverProcess = index.serverProcess(this.serverSocket);
         System.out.println("Server | " + serverProcess +" | shutting down...");
         this.serverSocket.close();
         final boolean isClosed = this.serverSocket.isClosed();
         if (isClosed) {
            System.out.println("Server closed "+index.getDate());
            System.exit(1);
         } else
            System.out.println("Server still running...");
      } catch (final IOException e) {
         index.handleException(e);
      }
   }


   public final static void main(final String[] args) {
      index.PORT = args.length > 0 ? Integer.parseInt(args[0]) : index.PORT;
      final Server s = new Server(index.PORT);
      s.comunication();
   }
  // end methods
}
