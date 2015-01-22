package common;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketHelper {
	// private static Selector selector;
	// private static int maxRunning = 10
	// private static Hashtable<SelectionKey, Runnable> connectEvents = new
	// Hashtable<SelectionKey, Runnable>();
	// private static Hashtable<SelectionKey, Runnable> acceptEvents = new
	// Hashtable<SelectionKey, Runnable>();
	// private static Hashtable<SelectionKey, Runnable> readEvents = new
	// Hashtable<SelectionKey, Runnable>();
	// private static Hashtable<SelectionKey, Runnable> writeEvents = new
	// Hashtable<SelectionKey, Runnable>();
	// private static ExecutorService threadPool;
	private static int timeout = 10000; // 10 seconds(unit ms)

	static {
		// try {
		// selector = Selector.open();
		// } catch (IOException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// threadPool = Executors.newFixedThreadPool(maxRunning);
	}

	public static byte[] SendMsgAsync(InetAddress ipAddress, int port,
			byte[] msg) {

		try {
			byte[] response = null;
			Socket sock = new Socket(ipAddress, port);
			DataOutputStream writer = new DataOutputStream(
					sock.getOutputStream());
			DataInputStream reader = new DataInputStream(sock.getInputStream());

			int len = 0;
			writer.write(msg);
			writer.flush();

			int availableBytes = 0;
			int position = 0;

			int wait = timeout;
			while (wait > 0 && (availableBytes = reader.available()) <= 0) {
				wait -= 10;
				Thread.sleep(10);
			}

			System.out.println("start to read...");
			if (availableBytes > 0) {
				response = new byte[availableBytes];
				int readCount = 0;
				do {
					// if the data from network too large and more than socket
					// read buffer, the available bytes may not correct.
					if (position + reader.available() > response.length) {
						byte[] response2 = new byte[position
								+ reader.available()];
						System.arraycopy(response, 0, response2, 0, position);
						response = response2;
					}

					len = reader.read(response, position, response.length
							- position);

					position += len;

					System.out.println(readCount + ": read " + len
							+ "bytes, avaiolable is " + availableBytes);
					readCount++;

				} while ((availableBytes = reader.available()) > 0);

				System.out.println("read end");
			}
			sock.close();
			return response;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static byte[] SendMsgAsync(String hostName, int port, byte[] msg)
			throws UnknownHostException {
		InetAddress ipAddress = InetAddress.getByName(hostName);
		return SendMsgAsync(ipAddress, port, msg);
	}
}
