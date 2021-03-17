package utils;

import java.io.*;
import java.util.Scanner;
import java.net.Socket;
import utils.index;


public final class clientThread extends Thread {

    private Socket client = null;

    public clientThread(final Socket client) {
        final String client_HOST = client.getInetAddress().getHostAddress();
        final int client_PORT= client.getPort();
        final boolean cond = utils.index.isValidSocket(client_HOST, client_PORT);
        if(cond)
            this.client = client;
        else {
            System.err.println("Check inserted HOST.");
            utils.index.handlePORT_ERR();
        }
    }

    public clientThread(final clientThread c) {
        if(c != null)
            this.setClient(c.client);
        else {
            final String className = this.getClass().toString();
            utils.index.handleCopy_Construct_ERR(className);
        }
    }

    @Override
    public final void run()  {
        try {
            final InputStream clientInputStream = this.client.getInputStream();
            final Scanner clientInput = new Scanner(clientInputStream);
            do {
                final String msg = clientInput.nextLine();
                System.out.println(msg);
            } while (clientInput.hasNextLine());
            clientInput.close();
        } catch (final IOException e) {
            utils.index.handleException(e);
        }
    }

    @Override
    public final String toString() {
        final String clientStr = utils.index.clientProcess(this.client, true);
        final String s = String.format("Client: %s \nUsername: %s", clientStr);
        return s;
    }

    public final Socket getClient() { return this.client; }

    public final void setClient(final Socket client) { this.client = client; }

}