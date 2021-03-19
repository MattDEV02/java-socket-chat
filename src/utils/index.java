package utils;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.sound.sampled.*;


/**
 * Description <h1>Global utils Class </h1>
 * @version 1.0 from 14/03/2021
 * @author Matteo Lambertucci
 * @see index
 * @since 1.0
 */

public class index extends Object {

    public static int PORT = 5000;
    public static final InetAddress HOST = getLocalHost();
    public static final String strJoin = "join";
    public static final String strLeave = "leave";
    public static final int limitMin = 1023;
    public static final int limitMax = 65536;

    public static InetAddress getLocalHost() {
        InetAddress HOST = null;
        try {
            HOST = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            handleException(e);
        }
        return HOST;
    }

    public static String handleException(final Exception e) {
        final String ExMsg = e.getMessage();
        e.printStackTrace(System.err);
        System.exit(1);
        return ExMsg;
    }

    public final static String getDate() {
        final long now = System.currentTimeMillis();
        final String date = new Date(now).toString();
        final String result = "  [ " + date + " ]";
        return result;
    }

    public static boolean isValidIPV4(final String IP) {
        final String pattern = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
        final boolean result = IP.matches(pattern);
        return result;
    }

    public static boolean isValidSocketPORT(final int PORT) {
        final boolean result = ((PORT >= limitMin) && (PORT <= limitMax));
        return result;
    }

    public final static boolean isValidSocket(final String HOST , final int PORT) {
        final boolean result = ((isValidIPV4(HOST)) && (isValidSocketPORT(PORT)));
        return result;
    }

    public final static String strRepeat(final String s, final int n) {   // Chat
        int i = 0;
        StringBuilder result = new StringBuilder("");
        for (; (i < n) ; i++)
            result.append(s);
        return result.toString();
    }

    public final static String capitalize(final String s) {
        final String result = (
                s.substring(0, 1)
                        .toUpperCase() + s.substring(1)
        );
        return result;
    }

    public final static String formatMessage(final String msg) {  // Chat
        final String space = strRepeat(" ", 4);
        final String sep = "\n \n";
        final String s = (space + capitalize(msg) + sep);
        return s;
    }

    public final static void handlePORT_ERR() {
        System.err.println("La Porta di Rete deve essere compresa tra " + limitMin + " e " + limitMax+".");
        System.exit(1);
    }

    public final static Random getRandom() {
        final long now = System.currentTimeMillis();
        final Random random = new Random(now);
        return random;
    }

    public final static String randomString(final int length) {
        final String charset = "0123456789abcdefghijklmnopqrstuvwxyz";
        final int range = charset.length();
        final Random random = getRandom();
        StringBuilder result = new StringBuilder("");
        int i = 0;
        for ( ; (i < length); i++) {
            final int randomInt = random.nextInt(range);
            result.append(charset.charAt(randomInt));
        }
        return result.toString();
    }

    public final static String serverProcess(final ServerSocket server) {
        final String server_HOST = HOST.getHostAddress(); // or server.getInetAddress().getHostName();
        final int server_PORT = server.getLocalPort(); // or PORT;
        final String str_serverProcess = (server_HOST + ":" + server_PORT);
        return str_serverProcess;
    }

    public final static String serverProcess(final String HOST, final int PORT) { // overloading
        final String str_serverProcess = (HOST + ":" + PORT);
        return str_serverProcess;
    }

    public final static String clientProcess(final Socket client, final boolean isFromClient) {
        final String client_HOST = client.getInetAddress().getHostAddress();
        final int client_PORT = isFromClient ? client.getLocalPort() : client.getPort();
        final String str_clientProcess = (client_HOST + ":" + client_PORT);
        return str_clientProcess;
    }

    public final static String serverMessage(final Socket clientSocket, final int numClients, final boolean state) {
        final String strState = state ? "joined" : "leaved";
        final String clientProcess = clientProcess(clientSocket, false);
        final String now = getDate();
        final String base = "Client | %s | has %s the Chat ; Actual Clients: %d %s";
        final String msg = String.format(
                base,
                clientProcess,
                strState,
                numClients,
                now
        );
        return msg;
    }

    public final static void serverLog(final Vector<Socket> clients, final boolean state) {
        final int numClients = clients.size();
        final Socket clientSocket = clients.lastElement();
        final String clientProcess = clientProcess(clientSocket, false);
        final String strState = state ? "connected" : "disconnected";
        final String s = String.format("Client %s:  | %s |  ;  Actual Clients: %d", strState, clientProcess, numClients);
        System.out.println(s);
    }

    public final static void serverLog(final Socket clientSocket, final int numClients, final boolean state) { // overloading
        final String clientProcess = clientProcess(clientSocket, false);
        final String strState = state ? "connected" : "disconnected";
        final String s = String.format("Client %s:  | %s |  ;  Actual Clients: %d", strState, clientProcess, numClients);
        System.out.println(s);
    }

   public final static void playSound() {
      try {
         final File audioFile = new File("public/submit.wav");
         final AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
         final Clip audioClip = AudioSystem.getClip();
         audioClip.open(audioStream);
         audioClip.start();
      }
      catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
         handleException(e);
      }
   }

    public final static void handleConstruct_ERR(final String class_name) {
        System.err.println("Operazione di Costruzione dell'Oggetto della Classe | " + class_name + " | Fallita.");
        System.exit(1);
    }

    public final static void handleCopy_Construct_ERR(final String class_name) {
        System.err.println("Operazione di Copia dell'Oggetto della Classe | " + class_name + " | Fallita.");
        System.exit(1);
    }

}