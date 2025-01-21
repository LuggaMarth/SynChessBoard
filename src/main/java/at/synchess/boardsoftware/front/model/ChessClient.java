package at.synchess.boardsoftware.front.model;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChessClient {
    private String host;
    private static final int port = 14031;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ChessClient(String host) {
        this.host = host;


        connect();
    }




    public void connect() {
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connected to " + host + ":" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String requestString(String message) {
        if (socket == null || socket.isClosed()) {
            System.out.println("Client is not connected. Please connect first.");
            return null;
        }
        out.println(message);
        out.flush();

        try {
            String response = in.readLine();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> requestList(String message) {
        if (socket == null || socket.isClosed()) {
            System.out.println("Client is not connected. Please connect first.");
            return null;
        }

        out.println(message);
        out.flush();

        try {
            String amount = in.readLine();
            List ret = new ArrayList();
            for (int i = 0; i < Integer.parseInt(amount); i++) {
                ret.add(in.readLine());
            }
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int joinGame(int gameID) {
        String s = requestString("JOIN " + gameID);
        return Integer.parseInt(s);
    }

    public int createGame() {
        String s = requestString("START");
        return Integer.parseInt(s);
    }

    public List<String> getGameList(boolean onlyOpen) {
        return requestList("GAMELIST");
    }


}
