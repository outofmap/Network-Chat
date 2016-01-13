package network;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class TcpIpClient {

	public static void main(String[] args) {

		try {
			String serverIp = "10.73.42.124";
			System.out.println("connecting to Server. Server IP: " + serverIp);
			// create socket and request
			Socket socket = new Socket(serverIp, 7777);

			InputStream in = socket.getInputStream();
			DataInputStream dis = new DataInputStream(in);

			System.out.println("message received :" + dis.readUTF());
			System.out.println("close connection.");

			dis.close();
			socket.close();
			System.out.println("connection is closed.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
