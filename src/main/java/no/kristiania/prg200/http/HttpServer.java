package no.kristiania.prg200.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {
	private int port;
	private int actualPort;
	
	public HttpServer(int port) throws IOException {
		this.port = port;
		start();
	}
	
	public void start() throws IOException {
		ServerSocket serverSocket = new ServerSocket(port);
		this.actualPort = serverSocket.getLocalPort();
		new Thread(() -> serverThread(serverSocket)).start();
	}

	public void serverThread(ServerSocket serverSocket) {
		while (true) {
			try {
				Socket clientSocket = serverSocket.accept();
				InputStream input = clientSocket.getInputStream();
				OutputStream output = clientSocket.getOutputStream();

				String line = readNextLine(input).split(" ")[1];


				int questionPos = line.indexOf("?");
				line = line.substring(questionPos + 1);

				Map<String, String> paramMap = new HashMap<>();

				if(line.contains("?")) {
					for(String parameter : line.split("&")) {
						int equalPos = parameter.indexOf("=");
						String paramName = URLDecoder.decode((parameter.substring(0, equalPos)), "UTF-8");
						String paramValue = URLDecoder.decode((parameter.substring(equalPos + 1)), "UTF-8");

						paramMap.put(paramName, paramValue);
					}
				}

				String statusCode = paramMap.get("status");
				if (statusCode == null) {
					statusCode = "200";
				}

				String body = paramMap.get("body");
				if (body == null) {
					body = "Hello world";
				}

				String location = paramMap.get("Location");

				while (!line.isEmpty()) {
					System.out.println(line);
					line = readNextLine(input);
				}



				output.write(("HTTP/1.1 " + statusCode + " Ok\r\n").getBytes());
				output.write("X-Server-Name: Kristiania Web Server\r\n".getBytes());
				output.write("Connection: close\r\n".getBytes());
				if (location != null) {
					output.write(("Location: " + location + "\r\n").getBytes());
				}
				output.write("Content-Type: text/html; charset=utf-8\r\n".getBytes());
				output.write(("Content-Length: " + body.length() + "\r\n").getBytes());
				output.write("\r\n".getBytes());
				output.write(body.getBytes());
				output.flush();
		    }
			
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String readNextLine(InputStream input) throws IOException {
		StringBuilder nextLine = new StringBuilder();
		int c;
		while ((c = input.read()) != -1) {
			if (c == '\r') {
				input.read();
				break;
			}
			
			nextLine.append((char) c);
		}
		
		return nextLine.toString();
		
	}
	
	public int getPort() {
		return actualPort;
	}


	public static void main(String[] args) throws IOException {
		HttpServer server = new HttpServer(10080);
	}

}