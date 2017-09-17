/**
 * 
 */
/**
 * @author Administrator
 *
 */
package hua.world.ssl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class SslServer {

	public static void main(String[] args) throws Exception {
		System.setProperty("javax.net.debug", "ssl,handshake");

		System.setProperty("javax.net.ssl.keyStore", "E:/db/keystore/server.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "123456");
		System.setProperty("javax.net.ssl.trustStore", "E:/db/keystore/server.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "123456");

		SSLServerSocketFactory serverSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		SSLServerSocket serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(9100);
		// 要求客户端身份验证
		serverSocket.setNeedClientAuth(true);

		while (true) {
			SSLSocket socket = (SSLSocket) serverSocket.accept();
			Accepter accepter = new Accepter(socket);
			accepter.service();
		}
	}

	static class Accepter implements Runnable {
		private SSLSocket socket;

		public Accepter(SSLSocket socket) {
			this.socket = socket;
		}

		public void service() {
			Thread thread = new Thread(this);
			thread.start();
		}

		@Override
		public void run() {
			try {
				InputStream inputStream = socket.getInputStream();

				InputStreamReader inputstreamreader = new InputStreamReader(inputStream);
				BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

				String string = null;
				while ((string = bufferedreader.readLine()) != null) {
					System.out.println(string);
					System.out.flush();
				}
			} catch (Exception e) {
				// replace with other code
				e.printStackTrace();
			}
		}
	}
}
