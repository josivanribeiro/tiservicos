/* Copyright TI Serviços 2017 */
package com.tiservicos.console.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.tiservicos.console.services.BusinessException;
import com.tiservicos.console.services.ProfileService;
import com.tiservicos.console.services.RoleService;
import com.tiservicos.console.util.Utils;
import com.tiservicos.console.vo.ProfileRoleVO;
import com.tiservicos.console.vo.ProfileVO;
import com.tiservicos.console.vo.RoleVO;

/**
 * Profile Controller.
 * 
 * @author josivan@tiservicos.com
 *
 */
@ManagedBean
@ApplicationScoped
public class ProfileController extends AbstractController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger (ProfileController.class.getName());
	private ProfileService profileService = new ProfileService();
	private RoleService roleService = new RoleService();
	private List<ProfileVO> profileVOList;
	private List<RoleVO> roleVOList;
	private Integer profileIdForm;
	private String nameForm;
		
	public List<ProfileVO> getProfileVOList() {
		return profileVOList;
	}

	public void setProfileVOList(List<ProfileVO> profileVOList) {
		this.profileVOList = profileVOList;
	}
	
	public List<RoleVO> getRoleVOList() {
		return roleVOList;
	}

	public void setRoleVOList(List<RoleVO> roleVOList) {
		this.roleVOList = roleVOList;
	}

	public Integer getProfileIdForm() {
		return profileIdForm;
	}

	public void setProfileIdForm(Integer profileIdForm) {
		this.profileIdForm = profileIdForm;
	}

	public String getNameForm() {
		return nameForm;
	}

	public void setNameForm(String nameForm) {
		this.nameForm = nameForm;
	}

	@PostConstruct
    public void init() {
		super.checkReadOnly ("papel_perfil_editar");
		logger.debug ("isReadOnly(): " + super.isReadOnly());
    }
	
	/**
	 * Finds all the profiles.
	 */
	public void findAll () {
		profileVOList = new ArrayList<ProfileVO>();
		try {
			profileVOList = profileService.findAll ();
			logger.debug ("profileVOList.size() [" + profileVOList.size() + "]");
		} catch (BusinessException e) {
			String error = "An error occurred while find all the roles. " + e.getMessage();
			logger.error (error);
		}
	}
	
	/**
	 * Finds all the roles.
	 */
	public void findAllRole () {
		try {
			roleVOList = roleService.findAll();
		} catch (BusinessException e) {
			String error = "An error occurred while find all the roles. " + e.getMessage();
			logger.error (error);
		}
	}
	
	/**
	 * Finds all the roles with the selected field.
	 */
	public void findAllWithSelected (ProfileVO profileVO) {
		try {
			roleVOList = profileService.findAllWithSelected (profileVO);
			logger.debug ("roleVOList.size() [" + roleVOList.size() + "]");
		} catch (BusinessException e) {
			String error = "An error occurred while find all the roles with selected field. " + e.getMessage();
			logger.error (error);
		}
	}
	
	/**
	 * Redirects to add profile page.
	 */
	public String goToAddProfile() {
	    String toPage = "updateProfile?faces-redirect=true";
	    logger.debug ("Starting goToAddProfile.");
	    return toPage;
	}
	
	/**
	 * Redirects to profiles page.
	 */
	public String goToProfiles() {
	    String toPage = "profiles?faces-redirect=true";
	    this.findAll();
	    logger.debug ("Starting goToProfiles method");
	    return toPage;
	}
	
	/**
	 * Performs the profile save operation.
	 */
	public void save (ActionEvent actionEvent) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest(); 
		String[] roleIdArr = request.getParameterValues ("chkRoleId");
		if (isValidForm ()) { 
			ProfileVO profileVO = new ProfileVO ();
			profileVO.setProfileId (this.profileIdForm);
			profileVO.setName (this.nameForm);
			if (!this.isDuplicatedName (profileVO)) {
				try {					
					List<ProfileRoleVO> profileRoleVO = this.getProfileRoleVOList (roleIdArr);
					profileVO.setProfileRoleVOList (profileRoleVO);					
					if (this.profileIdForm != null && this.profileIdForm > 0) {
						int affectedRows = profileService.update (profileVO);
						if (affectedRows > 0) {
							super.addInfoMessage ("formUpdateProfile:messagesUpdateProfile", " Perfil atualizado com sucesso.");
							logger.info ("The profile [" + profileVO.getName() + "] has been successfully updated.");						
						}
					} else {					
						int affectedRows = profileService.insert (profileVO);
						if (affectedRows > 0) {
							super.addInfoMessage ("formUpdateProfile:messagesUpdateProfile", " Perfil adicionado com sucesso.");
							logger.info ("The profile [" + profileVO.getName() + "] has been successfully inserted.");
						}
					}
				} catch (BusinessException e) {
					String error = "An error occurred while saving or updating the profile. " + e.getMessage();
					logger.error (error);
				}
				this.resetForm();
			}
		}
	}
		
	/**
	 * Performs the profile delete operation.
	 */
	public void delete (ActionEvent actionEvent) {
		logger.debug ("Starting delete");
		if (this.profileIdForm != null && this.profileIdForm > 0) {
			ProfileVO profileVO = new ProfileVO ();
			profileVO.setProfileId (this.profileIdForm);
			profileVO.setName (this.nameForm);
			try {
				int affectedRows = profileService.delete (profileVO);
				if (affectedRows > 0) {
					super.addInfoMessage ("formUpdateProfile:messagesUpdateProfile", " Perfil excluído com sucesso.");
					logger.info ("The profile [" + profileVO.getName() + "] has been successfully deleted.");						
				}
			} catch (BusinessException e) {
				String error = "An error occurred while deleting the profile. " + e.getMessage();
				logger.error (error);
			}
			this.resetForm();
		}				
	}	
	
	/**
	 * Loads a profile according with the specified id.
	 */
	public void loadById () {
		logger.debug ("Starting loadById");
		ProfileVO foundProfileVO = null;
		if (this.profileIdForm != null && this.profileIdForm > 0) {
			ProfileVO profileVO = new ProfileVO ();
	        profileVO.setProfileId (this.profileIdForm);
	        try {
	        	foundProfileVO = this.profileService.findById (profileVO);
				this.profileIdForm = foundProfileVO.getProfileId();
				this.nameForm = foundProfileVO.getName();
				
				this.findAllWithSelected (foundProfileVO);
				
				logger.debug ("this.roleIdForm " + this.profileIdForm);
				logger.debug ("foundProfileVO.getName() " + foundProfileVO.getName());
			} catch (BusinessException e) {
				String error = "An error occurred while finding the profile by id. " + e.getMessage();
				logger.error (error);
			}
		}		
    }	
	
	/**
	 * Checks if the role is duplicated.
	 * 
	 * @param profileVO the role containing the name.
	 * @return the result operation.
	 */
	private boolean isDuplicatedName (ProfileVO profileVO) {
		boolean isDuplicated = false;
		try {
			int numberDuplicatedName = profileService.findDuplicatedName (profileVO);
			isDuplicated = (numberDuplicatedName > 0) ? true : false; 
			if (isDuplicated) {
				super.addWarnMessage ("formUpdateProfile:messagesUpdateProfile", " Nome já existente, preencha outro nome.");
				logger.warn ("isDuplicatedName: " + isDuplicated);
			}
		} catch (BusinessException e) {
			String error = "An error occurred while finding a duplicated name. " + e.getMessage();
			logger.error (error);
		}
		return isDuplicated;		
	}
	
	/**
	 * Gets a list of profileRole, given an array of roleId and the userId.
	 * 
	 * @param roleIdArr the array of roleId
	 * @return a list of profileRole.
	 */
	private List<ProfileRoleVO> getProfileRoleVOList (String[] roleIdArr) {
		List<ProfileRoleVO> profileRoleVOList = new ArrayList<ProfileRoleVO>();
		for (String roleId : roleIdArr) {			
			RoleVO roleVO = new RoleVO();
			roleVO.setRoleId (new Integer(roleId));			
			ProfileRoleVO profileRoleVO = new ProfileRoleVO ();
			profileRoleVO.setRoleVO (roleVO);			
			profileRoleVOList.add (profileRoleVO);
		}		
		return profileRoleVOList;
	}
	
	/**
	 * Validates the profile insert/update form.
	 * 
	 */
	private boolean isValidForm () {
		if (!Utils.isNonEmpty (this.nameForm)) {
			super.addWarnMessage ("formUpdateProfile:messagesUpdateProfile", " Preencha o campo nome.");
			return false;			
		} else if (isEmptyRoles()) {
			super.addWarnMessage ("formUpdateProfile:messagesUpdateProfile", " Selecione pelo menos um Papel.");
			return false;
		}
		return true;
    }
	
	/**
	 * Resets the insert/update form.
	 */
	private void resetForm () {
		this.profileIdForm = null;
		this.nameForm      = "";
		this.roleVOList    = null;
	}
	
	/**
	 * Checks if at least one role is selected.
	 * 
	 * @return the operation result.
	 */
	private boolean isEmptyRoles() {
		boolean isEmpty = true;
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest(); 
		String[] roleIdArr = request.getParameterValues ("chkRoleId");
		if (roleIdArr != null && roleIdArr.length > 0) {
			isEmpty = false;
		}	
		return isEmpty;
	}
	
}
