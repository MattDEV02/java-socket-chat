package utils.thread;

import java.util.Scanner;
import main.*;
import utils.index;
import utils.net.*
        ;

/**
 * Description <h1>Thread structure of Client Class </h1>
 * @version 1.0 from 14/03/2021
 * @author Matteo Lambertucci
 * @see client
 * @since 1.0
 */

public final class clientThread extends Object implements Runnable {

    private Chat chat = null;
    private Scanner clientInput = null;
    private db db = null;

    public clientThread(final Chat chat, final Scanner clientInput, final db db) {
        if((chat != null) && (clientInput != null)) {
            this.chat = chat;
            this.clientInput = clientInput;
            this.db = db;
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
            index.playSound();
            db.insert(msg);
        } while (clientInput.hasNextLine());
        this.clientInput.close();
    }

    @Override
    public final String toString() {
        final String clientInputStr =  this.clientInput.toString();
        final String s = String.format("ClientInput: %s", clientInputStr);
        return s;
    }

    public final Chat getChat() { return this.chat; }

    public final Scanner getClientInput() { return this.clientInput; }

    public final void setChat(final Chat chat) { this.chat = chat; }

    public final void setClientInput(final Scanner clientInput) { this.clientInput = clientInput; }

}