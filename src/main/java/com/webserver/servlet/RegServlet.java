package com.webserver.servlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * handler register service 
 * @author Tianyu Wei
 *
 */
public class RegServlet extends HttpServlet{

	@Override
	public void service(HttpRequest request, HttpResponse response) {

		System.out.println("RegServlet: start processing service");
		
		/*
		 * 1.Obtain register INFO by using request
		 * 2.Save register INFO into user.dat (treat this file as database)
		 * 3.response register successfully page or error page
		 */
		String username = request.getParameter("username");
		String nickname = request.getParameter("nickname");
		int age = (request.getParameter("age")!= null)?Integer.parseInt(request.getParameter("age")):(0);
		String password = request.getParameter("password");
		System.out.println(username + " : " + nickname + " : " + age + " : " + password );
		
		if(username == null) {
			response.setEntity(new File("./webapps/root/loginError.html"));
			return;
		}
		
		/*
		 * Save User INFO into user.dat, every document occupy 100 bytes
		 * username password, nickname take 32bytes;
		 * age take 4 bytes;
		 */
		
		try {
			RandomAccessFile raf = new RandomAccessFile("user.dat","rw");
			raf.seek(raf.length());
			byte[]data = username.getBytes("utf-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);
			
			data = nickname.getBytes("utf-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);
			
			data = password.getBytes("utf-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);
			
			raf.writeInt(age);
			raf.close();
			
			//set login successfully page
			response.setEntity(new File("./webapps/myweb/reg_success.html"));
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		System.out.println("RegServlet: register finish");
		
	}

}
