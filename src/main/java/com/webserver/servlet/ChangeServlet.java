package com.webserver.servlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

public class ChangeServlet extends HttpServlet{

	@Override
	public void service(HttpRequest request, HttpResponse response) {

		 System.out.println("ChangeServlet: " + "start processing service");
		 
		 String username = request.getParameter("username");
		 String oldpass = request.getParameter("oldpass");
		 String newpass = request.getParameter("newpass");
		 boolean flag = false;
		 try(RandomAccessFile raf = new RandomAccessFile("user.dat","rw")){
			 
			 for(int i = 0 ; i < raf.length()/100; i++) {
				 raf.seek(i*100);
				 byte[]data = new byte[32];
				 raf.read(data);
				 String user = new String(data,"utf-8").trim();
				 raf.read(data);
				 raf.read(data);
				 String password = new String(data,"utf-8").trim();
				 if(username.equals(user) && password.equals(oldpass)) {
					 System.out.println("find user and start to change password");
					 raf.seek(i*100 + 64);
					 byte[] data2 = newpass.getBytes("utf-8");
					 data2 = Arrays.copyOf(data2, 32);
					 raf.write(data2);
					 flag = true;
					 break;
				 }else {
					 flag = false;
				 }
			 }
			 
			 if(flag) {
				 response.setEntity(new File("./webapps/myweb/change_success.html"));
			 }else {
				 response.setEntity(new File("./webapps/myweb/change_fail.html"));
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
