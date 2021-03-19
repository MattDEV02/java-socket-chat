package utils;

import java.io.*;
import java.util.Scanner;
import java.net.Socket;
import main.*;


/**
 * Description <h1>Thread structure of Client Class </h1>
 * @version 1.0 from 14/03/2021
 * @author Matteo Lambertucci
 * @see client
 * @since 1.0
 */

public final class clientThread implements Runnable {

    private Chat chat = null;
    private Scanner clientInput = null;

    public clientThread(final Chat chat, final Scanner clientInput) {
        if((chat != null) && (clientInput != null)) {
            this.chat = chat;
            this.clientInput = clientInput;
        } else {
            final String className = this.getClass().toString();
            index.handleConstruct_ERR(className);
        }
    }

    public clientThread(final clientThread c) {
        if(c != null) {
            this.setChat(c.chat);
            this.setClientInput(c.clientInput);
        } else {
            final String className = this.getClass().toString();
            utils.index.handleCopy_Construct_ERR(className);
        }
    }

    @Override
    public final void run()  {
        do {
            final String msg = this.clientInput.nextLine();
            this.chat.output_messages(msg);
        } while (clientInput.hasNextLine());
        this.clientInput.close();
    }

    @Override
    public final String toString() {
        final String clientInputStr =  this.clientInput.toString();
        final String s = String.format("Client: %s \nUsername: %s", clientInputStr);
        return s;
    }

    public final Chat getChat() { return this.chat; }

    public final Scanner getClientInput() { return this.clientInput; }

    public final void setChat(final Chat chat) { this.chat = chat; }

    public final void setClientInput(final Scanner clientInput) { this.clientInput = clientInput; }

}