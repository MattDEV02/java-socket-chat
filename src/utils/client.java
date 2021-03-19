package utils;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Description <h1>Socket Client Class </h1>
 * @version 1.0 from 14/03/2021
 * @author Matteo Lambertucci
 * @see clientThread
 * @since 1.0
 */

public final class client extends Object {

    private Socket clientSocket = null;
    private Scanner keyboardInput = null;
    private PrintStream clientOutput = null;
    private String username = "";

    public client(final InetAddress HOST, final int PORT, final String username) {
        super();
        final String HOSTAddress = HOST.getHostAddress();
        final boolean cond = utils.index.isValidSocket(HOSTAddress, PORT);
        if(cond) {
            try {
                this.clientSocket = new Socket(HOST, PORT);
                this.username = index.capitalize(username);
                this.initClientOutput();
                this.keyboardInput = new Scanner(System.in);
                this.joinChat();
                final String serverProcess = utils.index.serverProcess(HOSTAddress, PORT);
                System.out.println("Client Connected to Server:  | " + serverProcess + " |");
            } catch(final IOException e) {
                utils.index.handleException(e);
            }
        } else {
            System.err.println("Check inserted HOST.");
            utils.index.handlePORT_ERR();
        }
    }

    public client(final client c) {
        if(c != null)  {
            this.setClientSocket(c.clientSocket);
            this.setUsername(c.username);
            this.setClientOutput(c.clientOutput);
            this.setKeyboardInput(c.keyboardInput);
        } else {
            final String className = this.getClass().toString();
            utils.index.handleCopy_Construct_ERR(className);
        }
    }

    @Override
    public final String toString() {
        final String clientStr = utils.index.clientProcess(this.clientSocket, true);
        final String s = String.format("Client: %s \nUsername: %s", clientStr, this.username);
        return s;
    }

    public final Socket getClientSocket() { return this.clientSocket; }

    public final String getUsername() { return this.username; }

    public final PrintStream getClientOutput() { return this.clientOutput; }

    public final Scanner getKeyboardInput() { return this.keyboardInput; }

    public final void setClientSocket(final Socket client) { this.clientSocket = client; }

    public final void setUsername(final String username) { this.username = username; }

    public final void setClientOutput(final PrintStream clientOutput) { this.clientOutput = clientOutput; }

    public void setKeyboardInput(final Scanner keyboardInput) { this.keyboardInput = keyboardInput; }

    public final void initClientOutput() throws IOException {
        try{
            final OutputStream clientOutputStream = this.clientSocket.getOutputStream();
            final PrintStream clientOutput = new PrintStream(clientOutputStream);
            this.setClientOutput(clientOutput);
        } catch (final IOException e) {
            utils.index.handleException(e);
        }
    }

    public final void joinChat() {
        final String keyWord = index.strJoin;
        this.clientOutput.println(keyWord);
    }

    public final void send(String msg) {
        try {
            final String keyWord = index.strLeave;
            if(msg.equals(keyWord)) {
                this.clientOutput.println(keyWord);
                this.close();
            }
            msg =  utils.index.capitalize(msg);
            final String now = utils.index.getDate();
            final String s = (this.username + ": " + msg + now);
            this.clientOutput.println(s);
        } catch (IOException e) {
            utils.index.handleException(e);
        }
    }

    public final synchronized void close() throws IOException {
        try {
            final String clientProcess = utils.index.clientProcess(this.clientSocket, true);
            System.out.println("Client | " + clientProcess +" | shutting down...");
            this.clientOutput.close();
            this.keyboardInput.close();
            this.clientSocket.close();
            final boolean condition = this.clientSocket.isClosed();
            if (condition) {
                System.out.println("Client Closed" + utils.index.getDate());
                System.exit(1);
            }
            else
                System.out.println("Client still running...");
        } catch (final IOException e) {
            utils.index.handleException(e);
        }
    }
}
