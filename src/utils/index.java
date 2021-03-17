package utils;

import java.net.*;
import java.util.*;


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
        String result = "";
        for (; (i < n) ; i++)
            result += s;  // StringBuilder
        return result;
    }

    public final static String capitalize(final String s) {
        final String result = (
                s.substring(0, 1)
                        .toUpperCase() + s.substring(1)
        );
        return result;
    }

    public final static String formatMessage(final String msg) {  // Chat
        final String space = strRepeat(" ", 7);
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
        final String charset = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        final int range = charset.length();
        final Random random = getRandom();
        String result = "";
        int i = 0;
        for ( ; (i < length); i++) {
            final int randomInt = random.nextInt(range);
            result += (charset.charAt(randomInt));
        }
        return result;
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

    public final static String serverMessage(final Vector<Socket> clients, final boolean state) {
        final String strState = state ? "joined" : "leaved";
        final int numClients = clients.size();
        final Socket clientSocket = clients.lastElement();
        final String clientProcess = clientProcess(clientSocket, false);
        final String now = getDate();
        final String msg = String.format("Client  | %s | has %s the Chat%s  ; Actual Clients: %d", clientProcess, strState, now, numClients);
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

    public final static void handleConstruct_ERR(final String class_name) {
        System.err.println("Operazione di Costruzione dell'Oggetto della Classe | " + class_name + " | Fallita.");
        System.exit(1);
    }

    public final static void handleCopy_Construct_ERR(final String class_name) {
        System.err.println("Operazione di Copia dell'Oggetto della Classe | " + class_name + " | Fallita.");
        System.exit(1);
    }

}