/* Copyright TI Serviços 2017 */
package com.tiservicos.console.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

import com.tiservicos.console.services.BusinessException;
import com.tiservicos.console.services.ProfileService;
import com.tiservicos.console.services.UserConsoleService;
import com.tiservicos.console.util.Constants;
import com.tiservicos.console.util.SecurityUtils;
import com.tiservicos.console.util.Utils;
import com.tiservicos.console.vo.UserConsoleProfileVO;
import com.tiservicos.console.vo.UserConsoleVO;


/**
 * Login Controller.
 * 
 * @author josivan@josivansilva.com
 *
 */
@ManagedBean
@SessionScoped
public class LoginController extends AbstractController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger (LoginController.class.getName());
	private UserConsoleService userConsoleService = new UserConsoleService ();
	private ProfileService profileService = new ProfileService();
	private UserConsoleVO loggedUserConsole;
	private String emailForm;
	private String pwdForm;
	private boolean isLogged = false;
			
		
	public UserConsoleVO getLoggedUserConsole() {
		return loggedUserConsole;
	}
	public void setLoggedUserConsole(UserConsoleVO loggedUserConsole) {
		this.loggedUserConsole = loggedUserConsole;
	}
	public String getEmailForm() {
		return emailForm;
	}
	public void setEmailForm(String emailForm) {
		this.emailForm = emailForm;
	}
	public String getPwdForm() {
		return pwdForm;
	}
	public void setPwdForm(String pwdForm) {
		this.pwdForm = pwdForm;
	}
	public void setLogged(boolean isLogged) {
		this.isLogged = isLogged;
	}
	public boolean isLogged() {
		return isLogged;
	}
	
	@PostConstruct
    public void init() {
		
    }	
	
	public void doLogin (ActionEvent actionEvent) {
		RequestContext context = RequestContext.getCurrentInstance();
		if (isValidLoginForm()) {
			
			UserConsoleVO userConsoleLogin = new UserConsoleVO();
			userConsoleLogin.setEmail(emailForm);
			userConsoleLogin.setPwd(pwdForm);
			
			try {
				if (this.userConsoleService.doLogin(userConsoleLogin)) {
					
					UserConsoleVO userConsoleVO = userConsoleService.findByEmail(emailForm);
					
					//getting the user console profiles
					if (userConsoleVO != null && userConsoleVO.getUserConsoleId() > 0) {
						List<UserConsoleProfileVO> userConsoleProfileVOList = profileService.findProfilesByUserConsole(userConsoleVO);
						//setting the found userConsoleProfiles to the userConsole
						if (userConsoleProfileVOList != null && userConsoleProfileVOList.size() > 0) {
							userConsoleVO.setUserConsoleProfileVOList (userConsoleProfileVOList);
						}
					}
					
					FacesContext
					.getCurrentInstance()
						.getExternalContext()
							.getSessionMap().put (Constants.USER_CONSOLE_LOGGED_USER, userConsoleVO);
				
					//redirectToPage = Constants.USERS_CONSOLE_PAGE;
					isLogged = true;
					this.loggedUserConsole = userConsoleVO;
					
				} else {					
					super.addErrorMessage ("formLogin:messagesLogin", " Nome de usuário ou senha inválidos.");
				}		
				resetForm();
			} catch (BusinessException e) {
				String error = "An error occurred while performing the userConsole login. " + e.getMessage();
				logger.error (error);
			}		
			
		}
		String redirectToPage = this.getPageBasedInRole ();
		
		logger.debug("redirectToPage " + redirectToPage);
		
		context.addCallbackParam ("isLogged", isLogged);
		context.addCallbackParam ("redirectToPage", redirectToPage);
	}
	
	/**
	 * Performs the student logout.
	 */
	public String logout() {
		logger.debug ("Starting logout.");
		String toPage = "login?faces-redirect=true";
		this.isLogged = false;
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		SecurityUtils.logout (request);
		logger.debug ("Finishing logout.");
		return toPage;
	}
	
	/**
	 * Validates the userConsole login form.
	 * 
	 */
	private boolean isValidLoginForm () {
		if (!Utils.isNonEmpty (this.emailForm)
				|| !Utils.isNonEmpty (this.pwdForm)) {
			super.addWarnMessage ("formLogin:messagesLogin", " Preencha todos os campos.");
			resetForm();
			return false;
		} else if (!Utils.isValidEmail(this.emailForm)) {
			super.addWarnMessage ("formLogin:messagesLogin", " Email inválido.");
			resetForm();
			return false;
		}
		return true;
    }	
	
	/**
	 * Resets the login form.
	 */
	private void resetForm () {
		this.emailForm = "";
		this.pwdForm   = "";
	}
	
	/**
	 * Gets the page to redirect based in the user role.
	 * 
	 * @return the page to redirect.
	 */
	private String getPageBasedInRole () {
		String page = null;
		if (super.isUserInRole (Constants.ROLE_USER_CONSOLE_READ)) {
			page = Constants.USERS_CONSOLE_PAGE;
		} else if (super.isUserInRole (Constants.ROLE_QUESTION_CATEGORY_READ)) {
			page = Constants.QUESTION_CATEGORIES_PAGE;
		}
		return page;
	}
	
}
