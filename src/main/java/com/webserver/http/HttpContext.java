package com.webserver.http;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


/**
 * HTTP protocol definition
 * This class collect all Content-Type 
 * static type , all other class can use
 * @author Tianyu Wei
 *
 */
public class HttpContext {

	private static final Map<String,String> MIME_MAPPING = new HashMap<>();
	
	static {
		
		initMimeMapping();
		
	}
	
	/*
	 * initialization MiMe
	 */
	public static void initMimeMapping() {
		
		/*
		 * Parse conf/web.xml
		 * Using dom4j to parse web.xml document, and put all Content-Type and values into map MIME_MAPPING
		 */

		  
		  try {
			  SAXReader reader = new SAXReader();
			Document doc = reader.read(new File("conf/web.xml"));
			Element root = doc.getRootElement();
			List<Element> list = root.elements("mime-mapping");
			for(Element e: list) {
				MIME_MAPPING.put(e.elementText("extension"), e.elementText("mime-type"));
			}
			
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*
	 * Get Content-Type
	 */
	
	public static String getMimeType(String ext) {
		
		return MIME_MAPPING.get(ext);
	}
	
	
	/**
	 * Test case
	 * @param args
	 */
	public static void main(String[] args) {
          String str = "hshshs.png";
          String [] data = str.split("\\.");
          str = MIME_MAPPING.get(data[data.length-1]);
          System.out.println(str);
	}
	 

}
