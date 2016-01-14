package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class MultichatServer {
	HashMap clients;

	MultichatServer() {
		clients = new HashMap();
		Collections.synchronizedMap(clients);
	}

	public void start() {
		ServerSocket serverSocket = null;
		Socket socket = null;

		try {
			serverSocket = new ServerSocket(7777);
			System.out.println("server is starting.");

			while (true) {
				socket = serverSocket.accept();
				System.out.println("[" + socket.getInetAddress() + ": " + socket.getPort() + "]" + "is connected.");
				ServerReceiver thread = new ServerReceiver(socket);
				thread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void sendToAll(String msg) {
		Iterator allClients = clients.keySet().iterator();

		while (allClients.hasNext()) {
			try {
				DataOutputStream out = (DataOutputStream) clients.get(allClients.next());
				out.writeUTF(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String args[]) {
		new MultichatServer().start();
	}

	class ServerReceiver extends Thread {
		Socket socket;
		DataInputStream in;
		DataOutputStream out;

		ServerReceiver(Socket socket) {
			this.socket = socket;
			try {
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			String name = "";
			try {
				name = in.readUTF();
				sendToAll("@" + name + "is entering.");
				clients.put(name, out);
				System.out.println(clients.size() + "users are here now.");
				while (in != null) {
					sendToAll(in.readUTF());
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				sendToAll("@" + name + "is out.");
				clients.remove(name);
				System.out.println(
						"[" + socket.getInetAddress() + ": " + socket.getPort() + "]" + "terminates connection.");
				System.out.println(clients.size() + "users are here now.");
			}
		}
	}
}
