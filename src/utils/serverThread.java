package utils;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import main.Server;
import utils.index;


public final class serverThread extends Thread {

    private Server server = null;
    private Socket clientSocket = null;

    public serverThread(final Server server, final Socket clientSocket) {
        super();
        final String client_HOST = clientSocket.getInetAddress().getHostAddress();
        final int client_PORT= clientSocket.getPort();
        final boolean cond = ((server != null) && (utils.index.isValidSocket(client_HOST,client_PORT)));
        if(cond) {
            this.server = server;
            this.clientSocket = clientSocket;
        } else {
            final String className = this.getClass().toString();
            utils.index.handleConstruct_ERR(className);
        }
    }

    public serverThread(final serverThread s) {
        if(s != null) {
            this.setServer(s.server);
            this.setClientSocket(s.clientSocket);
        } else {
            final String className = this.getClass().toString();
            utils.index.handleCopy_Construct_ERR(className);
        }
    }

    @Override
    public final String toString() {
        final String strServer = this.server.toString();
        final String strClient = this.clientSocket.toString();
        final String s = String.format("Server:  %s \nClient: %s", strServer, strClient);
        return s;
    }

    public final Server getServer() { return this.server; }

    public final Socket getClientSocket() { return this.clientSocket; }

    public final void setServer(Server server) { this.server = server; }

    public final void setClientSocket(Socket client) { this.clientSocket = client; }

    @Override
    public final void run() {
        try {
            final InputStream clientInputStream = this.clientSocket.getInputStream();
            final Scanner serverInput = new Scanner(clientInputStream);
            String msg = "";
            do {
                final String clientProcess = utils.index.clientProcess(this.clientSocket, false);
                msg = serverInput.nextLine();
                msg = msg.equals(index.strLeave) ?
                        server.removeClient(this.clientSocket) : msg.equals(index.strJoin) ?
                        server.addClient(this.clientSocket) : msg;
                server.broadcastMessages(msg);
            } while (serverInput.hasNextLine());
            serverInput.close();
        } catch (IOException e) {
            utils.index.handleException(e);
        }
    }
}