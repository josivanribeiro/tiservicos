/* Copyright TI Serviços 2017 */
package com.tiservicos.console.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import com.tiservicos.console.util.SecurityUtils;
import com.tiservicos.console.vo.UserConsoleVO;

/**
 * Authorization Security Filter.
 * 
 * @author josivan@tiservicos.com
 *
 */
public class AuthorizationFilter implements Filter {
	
	static Logger logger = Logger.getLogger (AuthorizationFilter.class.getName());
	private List<String> pageAndRoleList = new ArrayList<String>();
		
	@Override
	public void init (FilterConfig filterConfig) throws ServletException {
		this.initPageAndRoleList ();
	}
	
	@Override
	public void doFilter (ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;				
		HttpSession session = request.getSession (false);
		String uri = request.getRequestURI();
		if (this.isValidPage (session, request, uri)) {
			logger.debug("Checking access.");
			if (this.hasAccess (req)) {
				chain.doFilter (req, res);
			} else {
				logger.debug("Has no access.");
				SecurityUtils.logout (req);
				response.sendRedirect (Constants.URL_LOGIN_PAGE);
			}
		} else {
			chain.doFilter (req, res);
		}
	}	
	
	@Override
	public void destroy() {
				
	}
	
	/**
	 * Checks the page that the logged user has access.
	 * 
	 * @param req the request.
	 * @return the success operation.
	 */
	private boolean hasAccess (ServletRequest req) {
		logger.debug ("Starting hasAccess.");
		boolean hasAccess = false;
		HttpServletRequest request = (HttpServletRequest) req;
		String uri = request.getRequestURI();
		outer:
		for (String item : this.pageAndRoleList) {
			
			String[] pageAndRoleArr = item.split ("@");
			String pages = pageAndRoleArr[0];
			String role = pageAndRoleArr[1];
			String[] pageArr = pages.split ("\\|");
			
			for (String page : pageArr) {				
				if (uri.endsWith (page)) {
					logger.debug ("page: " + page);
					logger.debug ("uri: " + uri);
					logger.debug ("role: " + role);
					
					UserConsoleVO loggedUserConsole = getLoggedUserConsole (request);					
					logger.debug ("loggedUserConsole.getEmail(): " + loggedUserConsole.getEmail());
					
					if (SecurityUtils.isUserInRole (role, loggedUserConsole)) {
						hasAccess = true;
						logger.debug ("hasAccess: " + hasAccess);
						break outer;
					} else {
						logger.warn ("The user [" + SecurityUtils.getLoggedUserConsole (request).getEmail() + "] has no access to the page [" + uri + "]");
					}
				}
			}
		}
		logger.debug ("Finishing hasAccess.");
		return hasAccess;
	}
		
	/**
	 * Initializes the page and role list.
	 */
	private void initPageAndRoleList () {
		pageAndRoleList.add ("roles.page|updateRole.page@papel_papel_ler");
		pageAndRoleList.add ("profiles.page|updateProfile.page@papel_perfil_ler");
		pageAndRoleList.add ("usersConsole.page|updateUserConsole.page@papel_usuario_ler");
		pageAndRoleList.add ("questionCategories.page|updateQuestionCategory.page@papel_categoria_pergunta_ler");
	}
	
	/**
	 * Checks if the given page should be checked its access.
	 * 
	 * @param session the http session.
	 * @param request the request.
	 * @param uri the uri.
	 * @return the result operation.
	 */
	private boolean isValidPage (HttpSession session, HttpServletRequest request, String uri) {
		if (session != null
				&& this.getLoggedUserConsole (request) != null
				&& uri.indexOf (Constants.URL_LOGIN_PAGE) == -1
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
	 * Gets logged user console from session.
	 * 
	 * @param request the request.
	 * @return the logged user console.
	 */
	private UserConsoleVO getLoggedUserConsole (HttpServletRequest request) {
		HttpSession session = request.getSession (false);
		if (session.getAttribute("loginController") != null) {
			LoginController loginController = (LoginController) session.getAttribute("loginController");
			if (loginController.getLoggedUserConsole() != null) {
				return loginController.getLoggedUserConsole();
			}
		}
		return null;
	}

}
