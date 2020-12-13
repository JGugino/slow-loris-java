package org.gugino.loris;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.ArrayList;

public class LorisSender {
	public String lorisID;

	private String TARGET_URI = "";

	private int TOTAL_CLIENTS = 200;

	private String REQUEST_BODY = "";
	
	private ArrayList<ConnectedClient> connectedClients = new ArrayList<>();

	public LorisSender(String _lorisID, String _targetURI, int _totalClients, String _requestBody) throws IOException, InterruptedException {
		this.lorisID = _lorisID;
		this.TARGET_URI = _targetURI;
		this.TOTAL_CLIENTS = _totalClients;
		this.REQUEST_BODY = _requestBody;
	}

	public void connectClients() throws IOException, InterruptedException {
		for (int i = 0; i < TOTAL_CLIENTS; i++) {
			String _clientID = "client_" + i;
			HttpClient httpClient = HttpClient.newHttpClient();
			
			long requestDelay = Long.parseLong(AppStart._delayInput.getText()) * 1000;
			
			connectedClients.add(new ConnectedClient(lorisID, _clientID, httpClient, TARGET_URI, requestDelay, REQUEST_BODY));
			System.out.println("Loris Sender ID: " + lorisID + " - Client Connected - ID: " + _clientID);
		}
	}

	public void startSender() {
		System.out.println("Starting LorisSender ID: " + lorisID);

		for (ConnectedClient _client : connectedClients) {
			if (_client.clientConnection != null) {
				_client.clientThread.start();
			} else System.out.println("Client connection is null! Client ID: " + _client.clientID);
		}
	}
	
	public void stopSenders() {
		System.out.println("Stoping LorisSender ID: " + lorisID);

		for (ConnectedClient _client : connectedClients) {
			if (_client.clientThread.isAlive()) {
				System.out.println("Stopping lorisID: " + lorisID + " - client ID: " + _client.clientID);
				_client.clientRunning = false;
			}
		}
	}
}
