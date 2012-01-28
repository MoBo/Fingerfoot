package bode.moritz.footfinger.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetworkControllerClient {

	private Socket socket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;

	
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
	
	public void close() throws IOException{
		out.close();
		in.close();
		socket.close();
	}

	public boolean isConnected() {
		return socket.isConnected();
	}
	
}