package main;

import java.io.*;
import java.net.*;
import java.util.*;

import org.jetbrains.annotations.NotNull;
import utils.*;


/**
 * Description <h1>Socket Server Class </h1>
 * @version 1.0 from 14/03/2021
 * @author Matteo Lambertucci
 * @see serverThread
 */

public final class Server extends Object {

   private Vector<Socket> clients = null;
   private ServerSocket serverSocket = null;

   public Server(int port) {
      if(utils.index.isValidSocketPORT(port)) {
         try {
            this.serverSocket = new ServerSocket(port);
            this.clients = new Vector<Socket>();
            final String serverProcess = utils.index.serverProcess(this.serverSocket);
            System.out.println("Server running on:  | " + serverProcess + " |");
         } catch(final IOException e) {
            utils.index.handleException(e);
         }
      } else
         utils.index.handlePORT_ERR();
   }

   public Server(Server s) {
      if (s != null) {
         this.setClients(s.clients);
         this.setServerSocket(s.serverSocket);
      } else {
         final String className = this.getClass().toString();
         utils.index.handleCopy_Construct_ERR(className);
      }
   }

   public final String toString() {
      StringBuilder client = new StringBuilder("");
      for(Socket c : this.clients)
         client.append(c.toString());
      final String server = utils.index.serverProcess(this.serverSocket);
      final String s = String.format("Server: %s \nClients: %s", server, client.toString());
      return s;
   }

   public final Vector<Socket> getClients() { return this.clients; }

   public final ServerSocket getServerSocket() { return this.serverSocket; }

   public final void setClients(final Vector<Socket> clients) { this.clients = clients; }

   public final void setServerSocket(final ServerSocket serverSocket) { this.serverSocket = serverSocket; }

   public final void comunication()   {
      try {
         for(;;) {
            final Socket client = serverSocket.accept();
            new serverThread(this, client).start(); // start new Thread
         }
      } catch (final IOException e) {
         utils.index.handleException(e);
      }
   }

   public final void send(@NotNull final Socket clientSocket, final String msg) throws IOException {
      final OutputStream clientOutputStream = clientSocket.getOutputStream();
      final PrintStream serverOutput = new PrintStream(clientOutputStream);
      serverOutput.println(msg);
   }

   public final void broadcastMessages(final String msg) {
      try {
         for (final Socket client : this.clients)
            this.send(client, msg);
      } catch (IOException e) {
         utils.index.handleException(e);
      }
   }

   public final String addClient(final Socket client) {
      this.clients.add(client);
      utils.index.serverLog(this.clients, true);
      final String msg = utils.index.serverMessage(this.clients, true);
      return msg;
   }

   public final String removeClient(final Socket client) {
      String msg = "";
      try {
         client.close();
         if (client.isClosed()) {
            this.clients.remove(client);
            final int numClients = this.clients.size();
            utils.index.serverLog(client, numClients, false);
            msg = utils.index.serverMessage(this.clients, false);
         } else
            System.out.println("Client still connected...");
      } catch(final IOException e) {
         return utils.index.handleException(e);
      } finally {
         return msg;
      }
   }

   public final synchronized void close() {
      try {
         final String serverProcess = utils.index.serverProcess(this.serverSocket);
         System.out.println("Server | " + serverProcess +" | shutting down...");
         this.serverSocket.close();
         final boolean isClosed = this.serverSocket.isClosed();
         if (isClosed) {
            System.out.println("Server closed "+utils.index.getDate());
            System.exit(1);
         }
         else
            System.out.println("Server still running...");
      } catch (IOException e) {
         utils.index.handleException(e);
      }
   }


   public final static void main(final String[] args) {
      utils.index.PORT = args.length > 0 ? Integer.parseInt(args[0]) : utils.index.PORT;
      final Server s = new Server(utils.index.PORT);
      s.comunication();
   }
}