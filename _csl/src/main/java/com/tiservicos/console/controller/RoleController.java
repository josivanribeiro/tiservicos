/* Copyright TI Serviços 2017 */
package com.tiservicos.console.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import com.tiservicos.console.services.BusinessException;
import com.tiservicos.console.services.RoleService;
import com.tiservicos.console.util.Utils;
import com.tiservicos.console.vo.RoleVO;

/**
 * Role Controller.
 * 
 * @author josivan@tiservicos.com
 *
 */
@ManagedBean
@ApplicationScoped
public class RoleController extends AbstractController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger (RoleController.class.getName());
	private RoleService roleService = new RoleService();
	private List<RoleVO> roleVOList;
	private Integer roleIdForm;
	private String nameForm;
		
	public List<RoleVO> getRoleVOList() {
		return roleVOList;
	}

	public void setRoleVOList(List<RoleVO> roleVOList) {
		this.roleVOList = roleVOList;
	}

	public Integer getRoleIdForm() {
		return roleIdForm;
	}

	public void setRoleIdForm(Integer roleIdForm) {
		this.roleIdForm = roleIdForm;
	}

	public String getNameForm() {
		return nameForm;
	}

	public void setNameForm(String nameForm) {
		this.nameForm = nameForm;
	}

	@PostConstruct
    public void init() {
		super.checkReadOnly ("papel_papel_editar");
		logger.debug ("isReadOnly(): " + super.isReadOnly());
    }
	
	/**
	 * Finds all the roles.
	 */
	public void findAll () {
		roleVOList = new ArrayList<RoleVO>();
		try {
			roleVOList = roleService.findAll ();
			logger.debug ("roleVOList.size() [" + roleVOList.size() + "]");
		} catch (BusinessException e) {
			String error = "An error occurred while find all the roles. " + e.getMessage();
			logger.error (error);
		}
	}
	
	/**
	 * Redirects to add role page.
	 */
	public String goToAddRole() {
	    String toPage = "updateRole?faces-redirect=true";
	    logger.debug ("Starting goToAddRole method");
	    return toPage;
	}
	
	/**
	 * Redirects to roles page.
	 */
	public String goToRoles() {
	    String toPage = "roles?faces-redirect=true";
	    this.findAll();
	    logger.debug ("Starting goToRoles method");
	    return toPage;
	}
	
	/**
	 * Performs the role save operation.
	 */
	public void save (ActionEvent actionEvent) {
		if (isValidForm ()) { 
			RoleVO roleVO = new RoleVO ();
			roleVO.setRoleId (this.roleIdForm);
			roleVO.setName (this.nameForm);
			if (!this.isDuplicatedName (roleVO)) {
				try {
					if (this.roleIdForm != null && this.roleIdForm > 0) {
						int affectedRows = roleService.update (roleVO);
						if (affectedRows > 0) {
							super.addInfoMessage ("formUpdateRole:messagesUpdateRole", " Papel atualizado com sucesso.");
							logger.info ("The role [" + roleVO.getName() + "] has been successfully updated.");						
						}
					} else {					
						int affectedRows = roleService.insert (roleVO);
						if (affectedRows > 0) {
							super.addInfoMessage ("formUpdateRole:messagesUpdateRole", " Papel adicionado com sucesso.");
							logger.info ("The role [" + roleVO.getName() + "] has been successfully inserted.");
						}
					}
				} catch (BusinessException e) {
					String error = "An error occurred while saving or updating the role. " + e.getMessage();
					logger.error (error);
				}
				this.resetForm();
			}
		}
	}
		
	/**
	 * Performs the role delete operation.
	 */
	public void delete (ActionEvent actionEvent) {
		logger.debug ("Starting delete");
		if (this.roleIdForm != null && this.roleIdForm > 0) {
			RoleVO roleVO = new RoleVO ();
			roleVO.setRoleId (this.roleIdForm);
			roleVO.setName (this.nameForm);
			try {
				int affectedRows = roleService.delete (roleVO);
				if (affectedRows > 0) {
					super.addInfoMessage ("formUpdateRole:messagesUpdateRole", " Papel excluído com sucesso.");
					logger.info ("The role [" + roleVO.getName() + "] has been successfully deleted.");						
				}
			} catch (BusinessException e) {
				String error = "An error occurred while deleting the role. " + e.getMessage();
				logger.error (error);
			}
			this.resetForm();
		}				
	}	
	
	/**
	 * Loads a role according with the specified id.
	 */
	public void loadById () {
		logger.debug ("Starting loadById");
		RoleVO foundRoleVO = null;
		if (this.roleIdForm != null && this.roleIdForm > 0) {
			RoleVO roleVO = new RoleVO ();
	        roleVO.setRoleId (this.roleIdForm);
	        try {
	        	foundRoleVO = this.roleService.findById (roleVO);
				this.roleIdForm = foundRoleVO.getRoleId();
				this.nameForm = foundRoleVO.getName();
				logger.debug ("this.roleIdForm " + this.roleIdForm);
				logger.debug ("foundRoleVO.getName() " + foundRoleVO.getName());
			} catch (BusinessException e) {
				String error = "An error occurred while finding the role by id. " + e.getMessage();
				logger.error (error);
			}
		}
    }	
	
	/**
	 * Checks if the role is duplicated.
	 * 
	 * @param roleVO the role containing the name.
	 * @return the result operation.
	 */
	private boolean isDuplicatedName (RoleVO roleVO) {
		boolean isDuplicated = false;
		try {
			int numberDuplicatedName = roleService.findDuplicatedName (roleVO);
			isDuplicated = (numberDuplicatedName > 0) ? true : false; 
			if (isDuplicated) {
				super.addWarnMessage ("formUpdateRole:messagesUpdateRole", " Nome já existente, preencha outro nome.");
				logger.warn ("isDuplicatedName: " + isDuplicated);
			}
		} catch (BusinessException e) {
			String error = "An error occurred while finding a duplicated name. " + e.getMessage();
			logger.error (error);
		}
		return isDuplicated;		
	}
	
	/**
	 * Validates the role insert/update form.
	 * 
	 */
	private boolean isValidForm () {						
		if (!Utils.isNonEmpty (this.nameForm)) {			
			super.addWarnMessage ("formUpdateRole:messagesUpdateRole", " Preencha o campo nome.");
			return false;			
		}
		return true;
    }
	
	/**
	 * Resets the insert/update form.
	 */
	private void resetForm () {
		this.roleIdForm = null;
		this.nameForm   = "";				
	}
	
}
