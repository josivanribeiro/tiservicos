/* Copyright TI Serviços 2017 */
package com.tiservicos.console.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.tiservicos.console.services.BusinessException;
import com.tiservicos.console.services.ProfileService;
import com.tiservicos.console.services.UserConsoleService;
import com.tiservicos.console.util.SecurityUtils;
import com.tiservicos.console.util.Utils;
import com.tiservicos.console.vo.ProfileVO;
import com.tiservicos.console.vo.UserConsoleProfileVO;
import com.tiservicos.console.vo.UserConsoleVO;

/**
 * User Console Controller.
 * 
 * @author josivan@josivansilva.com
 *
 */
@ManagedBean
@SessionScoped
public class UserConsoleController extends AbstractController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger (UserConsoleController.class.getName());
	private UserConsoleService userConsoleService = new UserConsoleService();
	private ProfileService profileService = new ProfileService();
	private List<UserConsoleVO> userConsoleVOList;
	private List<ProfileVO> profileVOList;
	private Integer userConsoleIdForm;
	private String emailForm;
	private String pwdForm;
	private boolean statusForm = true;
	
	private List<SelectItem> profileList;
	private String[] profileIdArr;
	
	public List<UserConsoleVO> getUserConsoleVOList() {
		return userConsoleVOList;
	}

	public void setUserConsoleVOList(List<UserConsoleVO> userConsoleVOList) {
		this.userConsoleVOList = userConsoleVOList;
	}
	
	public List<ProfileVO> getProfileVOList() {
		return profileVOList;
	}

	public void setProfileVOList(List<ProfileVO> profileVOList) {
		this.profileVOList = profileVOList;
	}

	public Integer getUserConsoleIdForm() {
		return userConsoleIdForm;
	}

	public void setUserConsoleIdForm(Integer userConsoleIdForm) {
		this.userConsoleIdForm = userConsoleIdForm;
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

	public boolean isStatusForm() {
		return statusForm;
	}

	public void setStatusForm(boolean statusForm) {
		this.statusForm = statusForm;
	}
	
	public List<SelectItem> getProfileList() {
		return profileList;
	}

	public void setProfileList(List<SelectItem> profileList) {
		this.profileList = profileList;
	}
	
	public String[] getProfileIdArr() {
		return profileIdArr;
	}

	public void setProfileIdArr(String[] profileIdArr) {
		this.profileIdArr = profileIdArr;
	}

	@PostConstruct
    public void init() {
		super.checkReadOnly ("papel_usuario_editar");
		logger.debug ("isReadOnly(): " + super.isReadOnly());
		this.findAll();
    }
	
	/**
	 * Finds all the users console.
	 */
	public void findAll () {
		try {
			userConsoleVOList = userConsoleService.findAll();
			logger.info ("userConsoleVOList.size() [" + userConsoleVOList.size() + "]");
		} catch (BusinessException e) {
			String error = "An error occurred while find all the users console. " + e.getMessage();
			logger.error (error);
		}				
	}
	
	/**
	 * Finds all the profiles.
	 */
	public void findAllProfile () {
		try {
			profileVOList = profileService.findAll();
			logger.debug ("profileVOList.size() [" + profileVOList.size() + "]");
		} catch (BusinessException e) {
			String error = "An error occurred while find all the profiles. " + e.getMessage();
			logger.error (error);
		}		
	}
	
	/**
	 * Gets the profiles selectitem values.
	 * 
	 * @return
	 */
	public List<SelectItem> getProfileValue() {
		List<SelectItem> profileList = new ArrayList<SelectItem>();
		if (profileVOList != null && profileVOList.size() >0) {
			for (ProfileVO profileVO : profileVOList) {
				SelectItem item = new SelectItem(profileVO.getProfileId(), profileVO.getName());
				profileList.add(item);
			}
		}		
		return profileList;
	}
	
	/**
	 * Redirects to add user console page.
	 */
	public String goToAddUserConsole() {
	    String toPage = "updateUserConsole?faces-redirect=true";
	    logger.debug ("goToAddUserConsole");
	    this.resetForm();
	    this.findAllProfile();
	    return toPage;
	}
	
	/**
	 * Redirects to users console page.
	 */
	public String goToUsersConsole() {
	    String toPage = "usersConsole?faces-redirect=true";
	    this.findAll();
	    logger.debug ("goToUsersConsole");
	    return toPage;
	}
	
	/**
	 * Performs the user save operation.
	 */
	public void save (ActionEvent actionEvent) {
		//HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest(); 
		//String[] profileIdArr = request.getParameterValues ("chkProfileId"); 
		if (isValidForm ()) {
			UserConsoleVO userConsoleVO = new UserConsoleVO ();
			userConsoleVO.setUserConsoleId (this.userConsoleIdForm);
			userConsoleVO.setEmail (this.emailForm);
			userConsoleVO.setPwd (this.pwdForm);
			userConsoleVO.setStatus (this.statusForm);
			if (!this.isDuplicatedEmail (userConsoleVO)) {
				try {
					List<UserConsoleProfileVO> userConsoleProfileVOList = this.getUserConsoleProfileVOList (profileIdArr);
					userConsoleVO.setUserConsoleProfileVOList (userConsoleProfileVOList);
					if (this.userConsoleIdForm != null && this.userConsoleIdForm > 0) {						
						int affectedRows = userConsoleService.update (userConsoleVO);
						if (affectedRows > 0) {							
							super.addInfoMessage ("formUpdateUserConsole:messagesUpdateUserConsole", " Usuário atualizado com sucesso.");
							logger.info ("The userConsole [" + userConsoleVO.getEmail() + "] has been successfully updated.");						
						}
					} else {					
						int affectedRows = userConsoleService.insert (userConsoleVO);
						if (affectedRows > 0) {							
							super.addInfoMessage ("formUpdateUserConsole:messagesUpdateUserConsole", " Usuário adicionado com sucesso.");
							logger.info ("The userConsole [" + userConsoleVO.getEmail() + "] has been successfully inserted.");
						}
					}					
				} catch (BusinessException e) {
					String error = "An error occurred while saving or updating the userConsole. " + e.getMessage();
					logger.error (error);
				}
				this.resetForm ();
			}
		}
	}
	
	/**
	 * Performs the user console delete operation.
	 */
	public void delete (ActionEvent actionEvent) {
		if (this.userConsoleIdForm != null && this.userConsoleIdForm > 0) {
			UserConsoleVO userConsoleVO = new UserConsoleVO ();
			userConsoleVO.setUserConsoleId (this.userConsoleIdForm);
			userConsoleVO.setEmail (this.emailForm);
			try {
				int affectedRows = userConsoleService.delete (userConsoleVO);
				if (affectedRows > 0) {
					super.addInfoMessage ("formUpdateUserConsole:messagesUpdateUserConsole", " Usuário excluído com sucesso.");
					logger.info ("The user console [" + userConsoleVO.getEmail() + "] has been successfully deleted.");						
				}
			} catch (BusinessException e) {
				String error = "An error occurred while deleting the user console. " + e.getMessage();
				logger.error (error);
			}		
		}
		this.resetForm();		
	}
	
	/**
	 * Loads an user console according with the specified id.
	 */
	public void loadById () {
		logger.debug ("Starting loadById.");
		UserConsoleVO foundUserConsoleVO = null;						
		if (this.userConsoleIdForm != null && this.userConsoleIdForm > 0) {
			UserConsoleVO userConsoleVO = new UserConsoleVO ();
			userConsoleVO.setUserConsoleId (this.userConsoleIdForm);
	        try {
	        	foundUserConsoleVO = this.userConsoleService.findById (userConsoleVO);
				this.userConsoleIdForm = foundUserConsoleVO.getUserConsoleId();
				this.emailForm = foundUserConsoleVO.getEmail();
				this.setStatusForm (foundUserConsoleVO.getStatus());
				
				this.findAllProfileWithSelected (foundUserConsoleVO);
				
				logger.debug ("foundUserConsoleVO.getEmail() " + foundUserConsoleVO.getEmail());
			} catch (BusinessException e) {
				String error = "An error occurred while finding the user console by id. " + e.getMessage();
				logger.error (error);
			}
		}		
    }
	
	/**
	 * Finds all the profiles with the selected field.
	 */
	private void findAllProfileWithSelected (UserConsoleVO userConsoleVO) {
		try {
			profileVOList = userConsoleService.findAllWithSelected (userConsoleVO);
			logger.debug ("profileVOList.size() [" + profileVOList.size() + "]");
			
			List<String> profileList = new ArrayList<String>();
			
			for (ProfileVO profileVO : profileVOList) {
				if (profileVO.isSelected()) {
					profileList.add(profileVO.getProfileId().toString());
				}				
			}
			
			profileIdArr = new String[profileList.size()];
			profileIdArr = profileList.toArray(profileIdArr);			
			
		} catch (BusinessException e) {
			String error = "An error occurred while find all the profiles with selected field. " + e.getMessage();
			logger.error (error);
		}
	}
	
	/**
	 * Gets a list of userConsoleProfile, given an array of profileId.
	 * 
	 * @param profileIdArr the array of profileId
	 * @return a list of userConsoleProfile.
	 */
	private List<UserConsoleProfileVO>getUserConsoleProfileVOList (String[] profileIdArr) {
		List<UserConsoleProfileVO> userProfileVOList = new ArrayList<UserConsoleProfileVO>();
		for (String profileId : profileIdArr) {			
			ProfileVO profileVO = new ProfileVO();
			profileVO.setProfileId (new Integer (profileId));						
			UserConsoleProfileVO userConsoleProfileVO = new UserConsoleProfileVO ();			
			userConsoleProfileVO.setProfileVO (profileVO);						
			userProfileVOList.add (userConsoleProfileVO);
		}		
		return userProfileVOList;
	}
	
	/**
	 * Checks if the email is duplicated.
	 * 
	 * @param userConsoleVO the userConsole containing the email.
	 * @return the result operation.
	 */
	private boolean isDuplicatedEmail (UserConsoleVO userConsoleVO) {
		boolean isDuplicated = false;
		try {
			int numberDuplicatedEmail = userConsoleService.findDuplicatedEmail (userConsoleVO);
			isDuplicated = (numberDuplicatedEmail > 0) ? true : false; 
			if (isDuplicated) {
				super.addWarnMessage ("formUpdateUser:messagesUpdateUser", " Email já existente, preencha outro email.");
				logger.warn ("isDuplicatedEmail: " + isDuplicated);
			}
		} catch (BusinessException e) {
			String error = "An error occurred while finding a duplicated email. " + e.getMessage();
			logger.error (error);
		}
		return isDuplicated;
	}
	
	/**
	 * Validates the user console insert/update form.
	 * 
	 */
	private boolean isValidForm () {						
		if (!Utils.isNonEmpty (this.emailForm)
				|| !Utils.isNonEmpty (this.pwdForm)) {
			super.addWarnMessage ("formUpdateUserConsole:messagesUpdateUserConsole", " Preencha todos os campos.");
			return false;			
		} else if (!Utils.isValidEmail (this.emailForm)) {
			super.addWarnMessage ("formUpdateUserConsole:messagesUpdateUserConsole", " Preencha o Email corretamente.");
			return false;
		} else if (!SecurityUtils.isValidPwd (this.pwdForm)) {
			super.addWarnMessage ("formUpdateUserConsole:messagesUpdateUserConsole", " A Senha deve conter pelo menos um número, uma letra minúscula, "
								 + "uma letra maiúscula e um símbolo (@#$%). Mínimo 10 e máximo de 20 caracteres.");
			return false;
		}  else if (isEmptyProfiles()) {
			super.addWarnMessage ("formUpdateUserConsole:messagesUpdateUserConsole", " Selecione pelo menos um Perfil.");
			return false;
		}
		return true;
    }
	
	/**
	 * Checks if at least one profile is selected.
	 * 
	 * @return the operation result.
	 */
	private boolean isEmptyProfiles() {
		boolean isEmpty = true;
		if (profileIdArr != null && profileIdArr.length > 0) {
			isEmpty = false;
		}	
		return isEmpty;
	}
	
	/**
	 * Resets the insert/update form.
	 */
	private void resetForm () {
		this.userConsoleIdForm = null;
		this.emailForm         = "";
		this.pwdForm           = "";
		this.statusForm        = true;
		//this.profileVOList     = null;
		this.profileIdArr      = null;		
	}
	
}
