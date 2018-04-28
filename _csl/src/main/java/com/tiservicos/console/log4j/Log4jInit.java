/* Copyright TI Serviços 2017 */
package com.tiservicos.console.log4j;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Log4j initialization class.
 * 
 * @author josivan@josivansilva.com
 *
 */
public class Log4jInit extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String LOG4J_FILE = "_csl_log4j.properties";
	static Logger logger = Logger.getLogger (Log4jInit.class.getName());
	
	public void init() {
		String prefix =  getServletContext().getRealPath("/");
		prefix = prefix.replace("_csl/", "config/");
	    PropertyConfigurator.configure (prefix + LOG4J_FILE);
	    logger.info ("Log4j successfully initialized.");
	}

	public void doGet (HttpServletRequest req, HttpServletResponse res) {	
	}
	
}
