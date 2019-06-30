package com.webserver.servlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

public class LoginServlet extends HttpServlet{

	@Override
	public void service(HttpRequest request, HttpResponse response) {

		System.out.println("LoginServlet: " + "start processing service");
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		System.out.println("Username: " + username + "   password: " + password );
		boolean flag = false;
		try(RandomAccessFile raf = new RandomAccessFile("user.dat","r")){
			for(int i = 0 ; i < raf.length()/100 ; i++) {
				raf.seek(i*100);
				byte[]data = new byte[32];
				raf.read(data);
				String user = new String(data,"utf-8").trim();
				raf.read(data);
				raf.read(data);
				String pass = new String(data,"utf-8").trim();
				if(username.equals(user) && password.equals(pass)) {
					flag = true;
					System.out.println("Login successfully");
					break;
				}else {
					flag = false;
				}
				
			}
			
			if(flag) {
				response.setEntity(new File("./webapps/myweb/login_success.html"));
			}else {
				response.setEntity(new File("./webapps/myweb/login_fail.html"));
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	
	
}
