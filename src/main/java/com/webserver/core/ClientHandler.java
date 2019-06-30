package com.webserver.core;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import com.webserver.http.EmptyRequestException;
import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.servlet.HttpServlet;

public class ClientHandler implements Runnable{

	 private Socket socket;
	 
	 public ClientHandler(Socket socket) {
		 
		 this.socket = socket;
	 }

	public void run() {

		try {
			
			HttpRequest request = new HttpRequest(socket);
			HttpResponse response = new HttpResponse(socket);
			
			//Get path of resource by using path
			String path = request.getRequestURI();
			
			//Get Servlet instantiation by using getServlet();
			HttpServlet servlet = ServerContext.getServlet(path);
			
			if(servlet != null) {
				servlet.service(request, response);
			}else {
				
				File file = new File("./webapps" + path);
				if(file.exists()) {
					System.out.println("Resource exist");
					response.setEntity(file);
				}else {
					System.out.println("Resource does not exist");
					//if doesn't find resource, set 404 page, set statusCode and statusReason 
					response.setEntity(new File("./webapps/root/404.html"));
					response.setStatusCode(404);
					response.setStatusReason("NOT FOUND");
				}
			}
			
			//send response
			response.flush();
		}catch(EmptyRequestException e) {
			System.out.println("Empty request");
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	 
	 
}
