package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Response class
 * Every instantiation for this class represent content that server response client
 * @author Tinayu Wei
 *
 */
public class HttpResponse {

	/*
	 * Some definitions for status-line 
	 * protocol , status code , status description
	 * protocol is fixed, use HTTP/1.1
	 */
	//Status code default 
	private int statusCode = 200;
	//status description default status reason is "Ok"
	private String statusReason = "OK";

	/*
	 * Related definition for response header 
	 */
	//key: header name , value: header value
	Map<String,String> headers = new HashMap<>();

	/*
	 * Response content Definitions
	 */
	//Response content entity file
	private File entity;
	private Socket socket;
	private OutputStream out;
	
	/*
	 * In constructor, Socket as the only args,
	 * so that instantiation of response class can use this socket to transmit Info to corresponding client
	 */
	public HttpResponse(Socket socket) {
		
		
		try {
			this.socket = socket;
			out = socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/*
	 *  By using this method, the content of response class send to client
	 */
	public void flush() {
		
		sendStatusLine();
		sendHeader();
		sendContent();
	}
	
	/*
	 * status-line : protocol , status-code , status-reason;
	 */
	
	private void sendStatusLine() {
		
		try {
			//protocol fixed
			String line = "HTTP/1.1" + " " + statusCode + " " + statusReason;
			out.write(line.getBytes("ISO8859-1"));
			out.write(13);
			out.write(10);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * response header
	 */
	private void sendHeader() {
		
		/*
		 *  One request one response, this is a HTTP basic rules
		 *  
		 * HTTP/1.1 200 OK（CRLF）
		 * Content-Type:text/html(CRLF)
		 * Content-Length:213(CRLF)(CRLF)
		 * 100110101010...
		 * 
		 * HTTP/1.1 200 OK(CRLF)
          Content-Type:text/html(CRLF)
          Content-Length:224586(CRLF)(CRLF)
		 */
		 
		try {
			Set<Entry<String ,String>> header = headers.entrySet();
			
			for(Entry<String,String> head: header) {
				
				String key = head.getKey();
				String value = head.getValue();
				String line = key + ":" + value;
				out.write(line.getBytes("ISO8859-1"));
				out.write(13);
				out.write(10);
				
			}
			
			//when finish response header, Send one more CRLF which ascii code is 13,10
			out.write(13);
			out.write(10);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * response content
	 */
	
	private void sendContent() {
		
		if(entity != null) {
			
			try(FileInputStream fis = new FileInputStream(entity);) {
				
				byte[] data = new byte[1024*10];
				int d = -1;
				while((d = fis.read(data))!= -1) {
					
					out.write(data,0,d);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * Setting will automatically set Content-Type and Content-Length
	 */
	public void setEntity(File entity) {
		this.entity = entity;
		String fileName = entity.getName();
		String [] str = fileName.split("\\.");
		String line = HttpContext.getMimeType(str[str.length-1]);
		this.putHeader("Content-Type", line);
		this.putHeader("Content-Length", entity.length()+"");
	}
	
	public File getEntity() {
		return entity;
	}
	
	
	public void putHeader(String name, String value) {
		
		this.headers.put(name,value);
	}
	
	public String getHeader(String name) {
		return this.headers.get(name);
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusReason() {
		return statusReason;
	}

	public void setStatusReason(String statusReason) {
		this.statusReason = statusReason;
	}
	
	

}
