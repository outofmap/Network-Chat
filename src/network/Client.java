package network;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

public class Client {

	public static void main(String[] args) {
		String serverIp = "10.73.42.124";
		Socket socket;
		try {
			socket = new Socket(serverIp,7777);
			System.out.println("connecting to Server");
			Sender sender = new Sender(socket);
			Receiver receiver = new Receiver(socket);
			
			sender.start();
			receiver.start();
		} catch (ConnectException ce){
			ce.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}

}
