package network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TcpIpServer implements Runnable {
	ServerSocket serverSocket = null;
	Thread[] threadArray;

	public static void main(String[] args) {
		TcpIpServer server = new TcpIpServer(5);
		server.start();
	}

	public TcpIpServer(int num) {
		try {
			serverSocket = new ServerSocket(7777);
			System.out.println(getTime() + "server is ready.");
			threadArray = new Thread[num];
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		for (int i = 0; i < threadArray.length; i++) {
			threadArray[i] = new Thread(this);
			threadArray[i].start();
		}
	}

	public void run() {
		while (true) {
			try {
				System.out.println(getTime() + "waiting client's connection request.");
				// When client request a connection,
				// Server create new socket for communicating with client's socket
				serverSocket.setSoTimeout(5 * 1000);
				Socket socket = serverSocket.accept();
				System.out.println(getTime() + socket.getInetAddress() + " reqests a connetion");
				System.out.println("getPort(): " + socket.getPort());
				System.out.println("getLocalPort(): " + socket.getLocalPort());

				// get new socket's output stream
				OutputStream out = socket.getOutputStream();
				DataOutputStream dos = new DataOutputStream(out);

				// send data to remote socket
				dos.writeUTF("[Test] test message from server");
				System.out.println(getTime() + "data is sent.");

				// close stream and socket.
				dos.close();
				socket.close();
			} catch (SocketTimeoutException timeout) {
				System.out.println("server is closed cause of no request during the time specified.");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	static String getTime() {
		String name = Thread.currentThread().getName();
		SimpleDateFormat datetime = new SimpleDateFormat("[hh:mm:ss]");
		return datetime.format(new Date())+ name;
	}

}
