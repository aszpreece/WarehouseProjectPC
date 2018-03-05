package com.whpc.bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class RobotConnection implements Runnable {

	private DataInputStream input;
	private DataOutputStream output;
	private final NXTInfo m_nxt;

	public RobotConnection(NXTInfo nxt) {
		m_nxt = nxt;
	}

	public boolean connect(NXTComm comm) throws NXTCommException {
		if (comm.open(m_nxt)) {

			input = new DataInputStream(comm.getInputStream());
			output = new DataOutputStream(comm.getOutputStream());
		}
		return isConnected();
	}

	public boolean isConnected() {
		return output != null;
	}

	@Override
	public void run() {

		try {
			while (true) {
				output.writeInt(1);
				output.flush();

				int answer = input.readInt();
				System.out.println(m_nxt.name + " returned " + answer);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {

			NXTInfo[] nxts = {

					new NXTInfo(NXTCommFactory.BLUETOOTH, "Anakin",
							"0016531B5A19")};

			ArrayList<RobotConnection> connections = new ArrayList<>(
					nxts.length);

			for (NXTInfo nxt : nxts) {
				connections.add(new RobotConnection(nxt));
			}

			for (RobotConnection connection : connections) {
				NXTComm nxtComm = NXTCommFactory
						.createNXTComm(NXTCommFactory.BLUETOOTH);
				connection.connect(nxtComm);
			}

			ArrayList<Thread> threads = new ArrayList<>(nxts.length);

			for (RobotConnection connection : connections) {
				threads.add(new Thread(connection));
			}

			for (Thread thread : threads) {
				thread.start();
			}

			for (Thread thread : threads) {
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} catch (NXTCommException e) {
			e.printStackTrace();
		}

	}
}