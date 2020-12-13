package org.gugino.loris;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;

public class ButtonActions implements ActionListener{

	private boolean isStarted = false;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(((JButton)e.getSource()).getName()) {
		case "start-button":
			System.out.println(isStarted);
			if(!isStarted) {
				isStarted = true;
				String _targetURI = AppStart._targetInput.getText();
				String _totalSenders = AppStart._senderInput.getText();
				String _totalClients = AppStart._clientInput.getText();
				
				if(_targetURI.isEmpty() || _targetURI.equals("https://")) {
					System.out.println("You must enter a vaild target!");
					return;
				}else{
					System.out.println("Starting " + _totalSenders + " sender(s)... Targeting: " + _targetURI + " with " + _totalClients + " clients");
					try {
						AppStart.startSenders(_totalSenders, _targetURI, _totalClients);
					} catch (IOException | InterruptedException e1) {
						e1.printStackTrace();
					}				
				}	
			}
			break;
		case "stop-button":
			for (LorisSender sender : AppStart.createdSenders) {
				sender.stopSenders();
			}
			 isStarted = false;
			break;
		case "clear-button":
			AppStart._clientInput.setText("200");
			AppStart._senderInput.setText("1");
			AppStart._targetInput.setText("https://");
			AppStart._delayInput.setText("10");
			
			AppStart.createdSenders.clear();
			AppStart.sendersCreated = false;
			
			System.out.println("Inputs cleared...");
			break;
		}
	}
}
