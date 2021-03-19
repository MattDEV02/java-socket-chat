package main;

import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
import javax.swing.*;
import utils.*;

/**
 *
 * Description <h1> Simple Chat GUI</h1>
 * @version 1.0 from 14/03/2021
 * @author Matteo Lambertucci
 * @see client
 * @since 1.0
 */

public final class Chat extends JFrame {
    // start attributes
    private final JButton submit = new JButton("SUBMIT");
    private final JTextField input_message = new JTextField("");
    private final JTextArea area_messages = new JTextArea("\n");
    private final JScrollPane area_messagesScrollPane = new JScrollPane(this.area_messages);
    private final JLabel title = new JLabel("Chat-Room");
    private client client = null;
    // end attributes

    public Chat(final client client) {
        // Frame-Init
        super();
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        final int frameWidth = 1200;
        final int frameHeight = 615;
        this.setSize(frameWidth, frameHeight);
        final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        final int x = ((d.width - getSize().width) / 2);
        final int y = ((d.height - getSize().height) / 2);
        this.setLocation(x, y);
        this.setTitle("Java-Chat");
        this.setVisible(true);
        this.setResizable(true);
        final Container cp = getContentPane();
        cp.setLayout(null);
        cp.setBackground(new Color(0xC0C0C0));
        // start components

        this.submit.setBounds(886, 485, 70, 36); // x, y, w, h
        this.submit.setMargin(new Insets(2, 2, 2, 2));
        this.submit.addActionListener(new ActionListener() {
            public final void actionPerformed(final ActionEvent evt) {
                submit_ActionPerformed(evt);
            }
        });
        this.submit.setBackground(Color.CYAN);
        this.submit.setBorder(new javax.swing.border.LineBorder(Color.GREEN, 2, true));
        this.submit.setToolTipText("Send Message");
        this.submit.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 12));
        this.submit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cp.add(this.submit);
        this.input_message.setBounds(239, 485, 655, 36);
        this.input_message.setToolTipText("Enter Message");
        this.input_message.setFont(new Font("Dialog", Font.BOLD, 16));
        this.input_message.addActionListener(new ActionListener() {
            public final void actionPerformed(final ActionEvent evt) {
                input_message_ActionPerformed(evt);
            }
        });
        cp.add(this.input_message);
        this.area_messagesScrollPane.setBounds(237, 105, 720, 332);
        this.area_messages.setEditable(false);
        this.area_messages.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        this.area_messages.setBackground(Color.WHITE);
        this.area_messages.setToolTipText("All messages");
        this.area_messages.setFont(new Font("Verdana", Font.BOLD, 15));
        this.area_messages.setForeground(Color.GREEN);
        cp.add(this.area_messagesScrollPane);
        this.title.setBounds(425, 30, 337, 36) ;
        this.title.setHorizontalAlignment(SwingConstants.CENTER);
        this.title.setFont(new Font("Arial Rounded MT Bold", Font.ITALIC, 25));
        this.title.setForeground(new Color(0xFFC800));
        this.title.setBackground(new Color(0x404040));
        this.title.setOpaque(true);
        cp.add(this.title);
        // end components
        this.client = client;
        final Socket clientSocket = this.client.getClientSocket();
        final String username = this.client.getUsername();
        final String clientProcess = index.clientProcess(clientSocket, true);
        this.title.setText(username + " > " + clientProcess);
        new clientThread(this, clientSocket).start(); // start new Thread
    } // end of public Chat

    // start methods

    public final void output_messages(final String msg) {
        final String s = area_messages.getText();
        StringBuilder text = new StringBuilder(s);
        text.append(index.formatMessage(msg));
        this.area_messages.setText(text.toString());
    }

    public final void submit_message() {
        final String msg = input_message.getText();
        if(msg.length() > 0) {
            this.input_message.setText("");
            this.client.send(msg);
        }
    }

    public final void submit_ActionPerformed(final ActionEvent evt) {
        System.out.println(evt);
        this.submit_message();
    } // end of submit_ActionPerformed

    public final void input_message_ActionPerformed(final ActionEvent evt) {
        System.out.println(evt);
        this.submit_message();

    } // end of input_message_ActionPerformed

    public final static void main(String[] args) {
        final String username = args.length > 0 ? args[0] : utils.index.randomString(5);
        final client client = new client(index.HOST, index.PORT, username);
        new Chat(client);
    } // end of main

    // end methods
} // end of class Chat
