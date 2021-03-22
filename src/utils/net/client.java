package utils.net;


import java.io.*;
import java.net.*;
import java.util.Scanner;
import utils.thread.clientThread;
import utils.index;

/**
 * Description <h1>Socket Client Class </h1>
 * @version 1.0 from 14/03/2021
 * @author Matteo Lambertucci
 * @see clientThread
 * @since 1.0
 */

public final class client extends Object {

  // start attributes
    private Socket clientSocket = null;
    private PrintStream clientOutput = null;
    private Scanner clientInput = null;
    private Scanner keyboardInput = null;
    private String username = "";
  // end attributes

    public client(final InetAddress HOST, final int PORT, final String username) {
        super();
        final String HOSTAddress = HOST.getHostAddress();
        final boolean cond = index.isValidSocket(HOSTAddress, PORT);
        if(cond) {
            try {
                this.clientSocket = new Socket(HOST, PORT);
                this.initClientOutput();
                this.initClientInput();
                this.username = index.capitalize(username);
                this.keyboardInput = new Scanner(System.in);
                this.joinChat();
                final String serverProcess = index.serverProcess(HOSTAddress, PORT);
                this.printResult(serverProcess);
            } catch(final IOException e) {
                index.handleException(e);
            }
        } else {
            System.err.println("Check inserted HOST.");
            index.handlePORT_ERR();
        }
    }

    public client(final client c) {
        if(c != null)  {
            this.setClientSocket(c.clientSocket);
            this.setClientOutput(c.clientOutput);
            this.setClientInput(c.clientInput);
            this.setUsername(c.username);
            this.setKeyboardInput(c.keyboardInput);
        } else {
            final String className = this.getClass().toString();
            index.handleCopy_Construct_ERR(className);
        }
    }
  // start methods

    @Override
    public final String toString() {
        final String clientStr = index.clientProcess(this.clientSocket, true);
        final String s = String.format("Client: %s \nUsername: %s", clientStr, this.username);
        return s;
    }

    public final Socket getClientSocket() { return this.clientSocket; }

    public final PrintStream getClientOutput() { return this.clientOutput; }
    
    public final Scanner getClientInput() { return this.clientInput; }

    public final String getUsername() { return this.username; }

    public final Scanner getKeyboardInput() { return this.keyboardInput; }

    public final void setClientSocket(final Socket clientSocket) { this.clientSocket = clientSocket; }

    public final void setClientInput(final Scanner clientInput) { this.clientInput = clientInput; }
    
    public final void setClientOutput(final PrintStream clientOutput) { this.clientOutput = clientOutput; }

    public final void setUsername(final String username) { this.username = username; }

    public void setKeyboardInput(final Scanner keyboardInput) { this.keyboardInput = keyboardInput; }

    public final void initClientOutput() throws IOException {
        try{
            final OutputStream clientOutputStream = this.clientSocket.getOutputStream();
            final PrintStream clientOutput = new PrintStream(clientOutputStream);
            this.setClientOutput(clientOutput);
        } catch (final IOException e) {
            index.handleException(e);
        }
    }

    public final void initClientInput() throws IOException {
        try{
            final InputStream clientInputStream = this.clientSocket.getInputStream();
            final Scanner clientInput = new Scanner(clientInputStream);
            this.setClientInput(clientInput);
        } catch (final IOException e) {
            index.handleException(e);
        }
    }

    public final void joinChat() {
        final String keyWord = index.strJoin;
        this.clientOutput.println(keyWord);
    }

    public final void printResult(final String serverProcess) {
        final String clientProcess = index.clientProcess(this.clientSocket, true);
        final String s = String.format("Client | %s | connected to Server:  | %s |", clientProcess, serverProcess);
        System.out.println(s);
    }

    public final void send(String msg) {
        try {
            final String keyWord = index.strLeave;
            if(msg.equals(keyWord)) {
                this.clientOutput.println(keyWord);
                this.close();
            }
            msg =  index.capitalize(msg);
            final String now = index.getDate();
            final String s = (this.username + ": " + msg + now);
            this.clientOutput.println(s);
        } catch (IOException e) {
            index.handleException(e);
        }
    }

    public final synchronized void close() throws IOException {
        try {
            final String clientProcess = index.clientProcess(this.clientSocket, true);
            System.out.println("Client | " + clientProcess +" | shutting down...");
            this.clientOutput.close();
            this.clientInput.close();
            this.keyboardInput.close();
            this.clientSocket.close();
            final boolean condition = this.clientSocket.isClosed();
            if (condition) {
                System.out.println("Client Closed" + index.getDate());
                System.exit(1);
            }
            else
                System.out.println("Client still running...");
        } catch (final IOException e) {
            index.handleException(e);
        }
    }
  // end methods
}
