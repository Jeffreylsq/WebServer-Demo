package com.webserver.core;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.webserver.servlet.HttpServlet;

/**
 * Server configuration INFO
 * @author Tianyu Wei
 *
 */
public class ServerContext {

	/*
	 * HttpServlet is abstract, other servlet extends HttpServlet
	 * this is upcasting  
	 */
	private static final Map<String,HttpServlet> SERVLET_MAPPING = new HashMap<>();
	
	static {
		initServletMapping();
	}
	
	public static void initServletMapping() {
		
		/**
		 * reload conf/servlets.xml 
		 * get all elements from root tag(servlets) 
		 * set path attribute as key
		 * take className out, and base on reflect theory,
		 * corresponding Servlet instantiation as value
		 * save instantiation in SERVLET_MAPPING
		 */
		try {
		SAXReader reader = new SAXReader();
		Document doc = reader.read(new File("conf/servlets.xml"));
		Element root = doc.getRootElement();
		List<Element> list = root.elements("servlet");
		for(Element e: list) {
			String key = e.attributeValue("path");
			String value = e.attributeValue("className");
			System.out.println(key + " : " + value);
			Class cls = Class.forName(value);
			HttpServlet servlet = (HttpServlet)cls.newInstance();
		    SERVLET_MAPPING.put(key,servlet);
		}
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static HttpServlet getServlet(String path) {
		return SERVLET_MAPPING.get(path);
	}
	
    public static void main(String[] args) {
		HttpServlet servlet = getServlet("/myweb/reg");
	    System.out.println(servlet);
    }
}
