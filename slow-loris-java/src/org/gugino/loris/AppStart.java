package org.gugino.loris;

import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AppStart implements Runnable {

	public static ArrayList<LorisSender> createdSenders = new ArrayList<>();

	public static JTextField _senderInput;
	public static JTextField _targetInput;
	public static JTextField _clientInput;
	public static JTextField _delayInput;

	private static Thread mainThread;

	private static String requestBody = "";
	
	public static boolean sendersCreated = false;

	public AppStart() {
		mainThread = new Thread(this);
		InputStream _payload = this.getClass().getClassLoader().getResourceAsStream("payload.txt");
		
		Scanner _scanner = new Scanner(_payload);
		
		while (_scanner.hasNextLine()) {
			requestBody += _scanner.nextLine() + System.lineSeparator();
		}
		
		System.out.println("Payload file loaded..");
		
		_scanner.close();
		
		mainThread.start();
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		new AppStart();
	}

	public static void startSenders(String _totalSenders, String _target, String _totalClients)
			throws IOException, InterruptedException {
		if(!sendersCreated) {
			for (int i = 0; i < Integer.parseInt(_totalSenders); i++) {
				String _lorisID = "loris_" + i;
				System.out.println("Created Loris ID: " + _lorisID);
				LorisSender createdSender = new LorisSender(_lorisID, _target, Integer.parseInt(_totalClients),
						requestBody);
				createdSender.connectClients();
				createdSenders.add(createdSender);
			}
			sendersCreated = true;
		}
		
		for(LorisSender _sender : createdSenders) {
			_sender.startSender();
		}
	}

	@Override
	public void run() {
		JFrame _frame = new JFrame("SLOW LORIS - FOR EDUCATIONAL PURPOSES ONLY!");

		Dimension windowDims = new Dimension(400, 325);

		_frame.setResizable(false);

		_frame.setPreferredSize(windowDims);
		_frame.setMaximumSize(windowDims);

		JPanel _jPanel = new JPanel();
		_jPanel.setLayout(new BoxLayout(_jPanel, BoxLayout.Y_AXIS));

		Dimension _inputDims = new Dimension(400, 45);

		_senderInput = new JTextField(20);
		_targetInput = new JTextField(20);
		_clientInput = new JTextField(20);
		_delayInput = new JTextField(20);

		JLabel _senderLabel = new JLabel("Number of Senders: ");
		JLabel _targetLabel = new JLabel("Target URL: ");
		JLabel _clientLabel = new JLabel("Number of Clients: ");
		JLabel _delayLabel = new JLabel("Request Delay (seconds): ");

		_senderInput.setText("1");
		_targetInput.setText("https://gugino.org");
		_clientInput.setText("200");
		_delayInput.setText("10");

		_senderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		_targetLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		_clientLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		_delayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		_senderInput.setAlignmentX(Component.CENTER_ALIGNMENT);
		_targetInput.setAlignmentX(Component.CENTER_ALIGNMENT);
		_clientInput.setAlignmentX(Component.CENTER_ALIGNMENT);
		_delayInput.setAlignmentX(Component.CENTER_ALIGNMENT);

		_senderInput.setMaximumSize(_inputDims);
		_targetInput.setMaximumSize(_inputDims);
		_clientInput.setMaximumSize(_inputDims);
		_delayInput.setMaximumSize(_inputDims);

		JButton _startButton = new JButton("Start Sending");
		JButton _stopButton = new JButton("Stop Sending");
		JButton _clearButton = new JButton("Clear Inputs");

		_startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		_stopButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		_clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		_startButton.setName("start-button");
		_stopButton.setName("stop-button");
		_clearButton.setName("clear-button");

		Dimension _buttonDims = new Dimension(200, 45);

		ButtonActions _actions = new ButtonActions();
		_startButton.addActionListener(_actions);
		_stopButton.addActionListener(_actions);
		_clearButton.addActionListener(_actions);

		_startButton.setMaximumSize(_buttonDims);
		_stopButton.setMaximumSize(_buttonDims);
		_clearButton.setMaximumSize(_buttonDims);

		_jPanel.add(_senderLabel);
		_jPanel.add(_senderInput);

		_jPanel.add(_targetLabel);
		_jPanel.add(_targetInput);

		_jPanel.add(_clientLabel);
		_jPanel.add(_clientInput);

		_jPanel.add(_delayLabel);
		_jPanel.add(_delayInput);

		_jPanel.add(_startButton);
		_jPanel.add(_stopButton);
		_jPanel.add(_clearButton);

		_frame.add(_jPanel);

		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		_frame.setLocationRelativeTo(null);

		_frame.pack();
		_frame.setVisible(true);
	}
}