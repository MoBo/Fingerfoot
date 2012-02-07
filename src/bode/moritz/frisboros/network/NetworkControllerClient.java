package bode.moritz.frisboros.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetworkControllerClient {

	private static NetworkControllerClient instance;
	private Socket socket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;

	private NetworkControllerClient() {

	}

	public void connectSocket(String ip, int port) throws UnknownHostException,
			IOException {
		socket = new Socket(ip, port);
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	public String transmitAndGetResponse(String message) throws IOException {
		out.println(message);
		return in.readLine();
	}

	public void close() throws IOException {
		if (socket != null) {
			out.close();
			in.close();
			socket.close();
		}

	}

	public boolean isConnected() {
		return socket.isConnected();
	}

	public static NetworkControllerClient getInstance() {
		if (instance == null) {
			instance = new NetworkControllerClient();
		}
		return instance;
	}

}