/* Copyright TI Serviços 2017 */
package com.tiservicos.console.filter;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.tiservicos.console.controller.LoginController;
import com.tiservicos.console.util.Constants;

/**
 * Authentication Security Filter.
 * 
 * @author josivan@tiservicos.com
 *
 */
public class AuthenticationFilter implements Filter {
	
	static Logger logger = Logger.getLogger (AuthenticationFilter.class.getName());
		
	@Override
	public void init (FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter (ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
	    HttpServletResponse response = (HttpServletResponse) res;
	    String uri = request.getRequestURI();
	    HttpSession session = request.getSession (false);
	    if (isUnauthorizedAccess (uri, request, session)) {
	    	response.sendRedirect (Constants.URL_LOGIN_PAGE);
	    } else {
	    	chain.doFilter (req, res);
	    }	    
	}	

	@Override
	public void destroy() {
		
	}
	
	/**
	 * Checks if the given access is unauthorized or not.
	 * 
	 * @param uri the uri.
	 * @param request the request.
	 * @param session the session.
	 * @return the operation result.
	 */
	private boolean isUnauthorizedAccess (String uri, HttpServletRequest request, HttpSession session) {
		boolean success = false;
		if (this.isValidURI (uri) 
				&& isInvalidAppURI(uri) 
				&& !this.isLoggedUser (request, session)) {
	    	logger.debug ("uri: " + uri);
	    	logger.warn ("user is not authenticated.");
			if (session != null) {
				session.invalidate();
			}
			success = true;			
	    }
		return success;
	}
	
	/**
	 * Checks if the URI is valid or not.
	 * 
	 * @param uri the URI.
	 * @return
	 */
	private boolean isValidURI (String uri) {
		if (uri.indexOf (Constants.URL_LOGIN_PAGE) == -1
	    		&& uri.indexOf ("/css/") == -1
	    		&& uri.indexOf ("/img/") == -1
	    		&& uri.indexOf ("/js/") == -1
	    		&& uri.indexOf ("/scss/") == -1
	    		&& uri.indexOf ("/vendor/") == -1
	    		&& uri.indexOf ("/javax.faces.resource/") == -1
	    		&& uri.indexOf ("RES_NOT_FOUND") == -1) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if the URI app is valid or not.
	 * 
	 * @param uri the uri.
	 * @return
	 */
	private boolean isInvalidAppURI (String uri) {
		if (uri.indexOf ("usersConsole.page") == -1
				&& uri.indexOf ("updateUserConsole") == -1) {
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if the user is logged or not.
	 * 
	 * @param request the request.
	 * @return the operation result.
	 */
	private boolean isLoggedUser (HttpServletRequest request, HttpSession session) {
		boolean isLogged = false;
		if (session != null				
				&& session.getAttribute("loginController") != null) {
			LoginController loginController = (LoginController) session.getAttribute("loginController");
			if (loginController.isLogged()) {
				isLogged = true;
			}
		}
		return isLogged;
	}

}
