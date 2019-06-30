package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Request Class
 * Every instantiation for this class represent Http's request content
 * One request include three part: method, header , content
 * 
 * @author Tianyu Wei
 *
 */
public class HttpRequest {

	 /*
	  * Some definitions for request line;
	  */
	
	//request method 
	private String method;
	//resource URL
	private String url;
	//protocol edition 
	private String protocol;
	//header INFO
	Map<String, String> headers = new HashMap<>();
	//url Request part (? left-part)
	private String requestURI;
	//url parameter part(? right-part)
	private String queryString;
	//Every pair parameter key:parameter name , value: value
	Map<String,String> parameters = new HashMap<>();
	
	private Socket socket;
	private InputStream in;
	
	public HttpRequest(Socket socket) throws EmptyRequestException{
		
		
		try {
			this.socket = socket;
			this.in = socket.getInputStream();
			parseRequestLine();
			parseHeaders();
			parseContent();
		} catch(EmptyRequestException e) {
			throw e;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * parse request line
	 */
	private void parseRequestLine() throws EmptyRequestException{
		
		System.out.println("Start parse request line");
		
		try {
			
			/*
			 * receive first line from socket by using inputStream
			 * after we receive it split request-line by space;
			 * Get method url protocol
			 * 
			 */
			
			String line = readLine();
			System.out.println("Request-Line: " + line);
			if("".equals(line)) {
				throw new EmptyRequestException();
			}
			String []arr = line.split("\\s");
			this.method = arr[0];
			this.url = arr[1];
			parseURL();
			this.protocol = arr[2];
			
			System.out.println("method: " + method);
			System.out.println("url: " + url);
			System.out.println("protocol: " + protocol);
			
		}catch(EmptyRequestException e) {
		     throw e;
		}catch(Exception e) {
		
			e.printStackTrace();
		}
		System.out.println("HttpRequest: parse finish");
	}
	
	
	/**
	 * There are two cases
	 * 
	 * URL have cases:
	 * 
	 * case 1: no parameter,  /myweb/index.html, set this kind of url in requestURI()
	 * 
	 * case 2: have parameter, /myweb/reg?username=xx&password=20...
	 * for this case 2 , we separate url by using ? 
	 *  set first part in requestURI
	 *  separate part 2 by using =, which are parameter name and value;
	 *  put them into headers;
	 */
	private void parseURL() {
		
		
		System.out.println("continue to parse URL");
		if(!url.contains("?")) {
			this.requestURI = url;
		}else {
			
			String [] data = url.split("[?]");
			requestURI = data[0];
			if(data.length > 1) {
				queryString = data[1];
				
				try {
					this.queryString = URLDecoder.decode(queryString, "utf-8");
					
					
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			data = queryString.split("[&]");
			
			for(int i = 0 ; i < data.length; i++) {
				
				String [] data2 = data[i].split("[=]");
				if(data2.length > 1) {
					parameters.put(data2[0], data2[1]);
				}else {
					parameters.put(data2[0],null);
				}
			}
			
			
		}
		System.out.println("requestURI: " + requestURI);
		System.out.println("queryString: " + queryString);
		System.out.println("parameter: " + parameters);
		System.out.println("HttpRequest: Finish parse url");
		
		
	}
	
	/**
	 * parseRequestLine method already read request-line by using inputStream
	 * and when we use parseHeader method, inputStream read header part
	 * We read end of headers until content is empty. Because there are two CRLF in the end
	 */
	
	private void parseHeaders() {
		System.out.println("HttpReuqest :Parse header ");
		while(true) {
			
			try {
				String line = readLine();
				if("".equals(line)) {
					break;
				}
				String[] data = line.split(": ");
				headers.put(data[0],data[1]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		System.out.println(headers);
		System.out.println("HttpRequest: finish parse header ");
		
	}
	
	
	/*
	 * parse content
	 */
	
	private void parseContent() {
		
	}
	
	
	
	
	//By using inputStream, read String from client, every String ended by CRLF(CR in ascii code(13)(LF in ascii code (10)))
	private String readLine() throws IOException {
		
		StringBuilder builder = new StringBuilder();
		//c1:last time read char , c2: this time read char
		int c1 = -1, c2 = -1;
		while((c2 = in.read())!= -1) {
			
			if((c1 == 13) && (c2 == 10)) {
				break;
			}
			
			builder.append((char)c2);
			c1 = c2;
		}
		
		return builder.toString().trim();
		
	}

	public String getMethod() {
		return method;
	}

	public String getUrl() {
		return url;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getParameter(String name) {
		return this.parameters.get(name);
	}

	public String getRequestURI() {
		return requestURI;
	}

	public String getQueryString() {
		return queryString;
	}
	
}
