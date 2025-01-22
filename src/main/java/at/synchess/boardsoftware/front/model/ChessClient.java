package at.synchess.boardsoftware.front.model;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import at.synchess.boardsoftware.front.controller.GameController;
import org.eclipse.paho.client.mqttv3.*;

public class ChessClient {
    private String host;
    private static final int port = 14031;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private MqttClient mqttClient;
    private String myID;

    public ChessClient(String host)  throws IOException,MqttException{
        this.host = host;
        myID = "HAHAHA";
        connect();
    }




    public void connect() throws IOException, MqttException{
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connected to " + host + ":" + port);

            mqttClient = new MqttClient("tcp://localhost:3110", myID);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            mqttClient.connect(options);


    }

    public void subscribeToGame(int gameid, GameController gameController) throws MqttException{

            mqttClient.subscribe("" + gameid, (topic, mqttMessage) -> {
                gameController.onMessageReceived(mqttMessage.toString());
            });

    }

    public String requestString(String message) throws IOException {
        if (socket == null || socket.isClosed()) {
            System.out.println("Client is not connected. Please connect first.");
            return null;
        }
        out.println(message);
        out.flush();


            String response = in.readLine();
            return response;
    }

    public List<String> requestList(String message) throws IOException {
        if (socket == null || socket.isClosed()) {
            System.out.println("Client is not connected. Please connect first.");
            return null;
        }

        out.println(message);
        out.flush();


            String amount = in.readLine();
            List ret = new ArrayList();
            for (int i = 0; i < Integer.parseInt(amount); i++) {
                ret.add(in.readLine());
            }
            return ret;


    }

    public int joinGame(int gameID) throws IOException{
        String s = requestString("JOIN " + gameID);
        return Integer.parseInt(s);
    }

    public int createGame() throws IOException {
        String s = requestString("START");
        return Integer.parseInt(s);
    }

    public List<String> getGameList(boolean onlyOpen) throws IOException {
        return requestList("GAMELIST");
    }

    public void close() throws IOException, MqttException {
        socket.close();
        mqttClient.close();
    }


}
