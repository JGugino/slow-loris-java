package org.gugino.loris;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;

public class ConnectedClient implements Runnable{
	public String clientID;
	
	public String parentID;
	
	public HttpClient clientConnection;

	HttpRequest httpRequest;
	
	private long requestWaitTime = 10000;
	
	public Thread clientThread;
	
	public boolean clientRunning = false;
	
	public ConnectedClient(String _parentID, String _clientID, HttpClient _connection, String _targetURI, long _requestDelay, String _requestBody) {
		this.parentID = _parentID;
		this.clientID = _clientID;
		this.clientConnection = _connection;
		this.requestWaitTime = _requestDelay;
		httpRequest = HttpRequest.newBuilder().POST(BodyPublishers.ofString(_requestBody)).uri(URI.create(_targetURI)).build();
		
		clientThread = new Thread(this);
	}
	
	@Override
	public void run() {
		clientRunning = true;
		while(clientRunning) {
			double _start = System.currentTimeMillis();
			clientConnection.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());
			double _duration = System.currentTimeMillis() - _start;
			System.out.println("Sender ID: " + parentID + ", Client ID: " + clientID
					+ ", Successful Http Request : Response Time: " + _duration + "ms");
			
			try {
				Thread.sleep(requestWaitTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}
	}
}
