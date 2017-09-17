package hua.world.ssl;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SslClient {
	public static void main(String[] args) throws Exception {
		
		System.setProperty("javax.net.debug", "ssl,handshake");

		System.setProperty("javax.net.ssl.keyStore", "E:/db/keystore/server.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "123456");
		System.setProperty("javax.net.ssl.trustStore", "E:/db/keystore/server.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "123456");

		SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket("127.0.0.1", 9100);

		OutputStream outputStream = sslsocket.getOutputStream();
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
		bufferedWriter.write("沉睡的雄狮\n");
		bufferedWriter.flush();

		bufferedWriter.close();
		outputStream.close();
		sslsocket.close();
	}
}
