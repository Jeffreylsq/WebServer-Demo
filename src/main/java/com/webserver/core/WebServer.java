package com.webserver.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * WebServer main class
 * 
 * @author Tianyu Wei
 *
 */
public class WebServer {

	private ServerSocket server;
	private ExecutorService threadPool;

	public WebServer() {

		try {
			server = new ServerSocket(12001);
			threadPool = Executors.newFixedThreadPool(50);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {

		/*
		 * For this time, temporarily not allow one or more user and multiple times
		 * connections
		 */
		try {
			while (true) {

				System.out.println("Waiting for clients connecting");
				Socket socket = server.accept();
				System.out.println("One client connected...");

				// Run Threads and interact with Server;
				ClientHandler handler = new ClientHandler(socket);
				threadPool.execute(handler);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[]args) {

		WebServer server = new WebServer();
		server.start();
	}

}
