package main;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import utils.*;

/**
 * Description <h1>Socket Client Class </h1>
 * @version 1.0 from 14/03/2021
 * @author Matteo Lambertucci
 * @see clientThread
 */

public final class Client extends Object {

    private Socket clientSocket = null;
    private Scanner keyboardInput = null;
    private PrintStream clientOutput = null;
    private String username = "";

    public Client(final InetAddress HOST, final int PORT) {
        super();
        final String HOSTAddress = HOST.getHostAddress();
        if(utils.index.isValidSocket(HOSTAddress, PORT)) {
            try {
                this.clientSocket = new Socket(HOST, PORT);
                this.initClientOutput();
                this.keyboardInput = new Scanner(System.in);
                final String serverProcess = utils.index.serverProcess(HOSTAddress, PORT);
                System.out.println("Client Connected to Server:  | " + serverProcess + " |"); //
            } catch (final UnknownHostException e){
                utils.index.handleException(e);
            } catch(final IOException e) {
                utils.index.handleException(e);
            }
        } else {
            System.err.println("Check inserted HOST.");
            utils.index.handlePORT_ERR();
        }
    }

    public Client(final Client c) {
        if(c != null)  {
            this.setClientSocket(c.clientSocket);
            this.setUsername(c.username);
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

    public final void comunication() {
        try {
            new clientThread(this.clientSocket).start(); // start new Thread
            this.enterUsername();
            this.sendMsg();
            this.close();
        } catch(final IOException e) {
            utils.index.handleException(e);
        }
    }

    public final void enterUsername() {
        System.out.print("Enter your Username: ");
        String username = this.keyboardInput.nextLine();
        if(username.length() <= 0)
            username = utils.index.randomString(5);
        this.setUsername(username);
        this.clientOutput.println(utils.index.strJoin);
    }

    public final void sendMsg() {
        System.out.println("\n" + this.username + ", enter your messages (leave to exit) :  \n");
        final String keyWord = utils.index.strLeave;
        do {
            String msg = this.keyboardInput.nextLine();
            if(msg.length() <= 0)
                msg = utils.index.randomString(5);
            else if(msg.equals(keyWord)) {
                this.clientOutput.println(keyWord);
                break;
            }
            final String s = (this.username + ": " + msg + utils.index.getDate());
            this.clientOutput.println(s);
        } while (keyboardInput.hasNextLine());
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

    public final static void main(final String[] args) {
        final InetAddress HOST = utils.index.HOST;
        final int PORT = utils.index.PORT;
        final Client c = new Client(HOST, PORT);
        c.comunication();
    }
}
