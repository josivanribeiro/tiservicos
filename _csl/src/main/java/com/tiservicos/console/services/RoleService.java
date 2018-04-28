/* Copyright TI Serviços 2017 */
package com.tiservicos.console.services;

import java.util.List;

import org.apache.log4j.Logger;

import com.tiservicos.console.dao.DataAccessException;
import com.tiservicos.console.dao.RoleDAO;
import com.tiservicos.console.vo.RoleVO;

/**
 * Business Service class for Role.
 * 
 * @author josivan@tiservicos.com
 *
 */
public class RoleService {

	static Logger logger = Logger.getLogger (RoleService.class.getName());	
	private RoleDAO roleDAO = new RoleDAO();
		
	public RoleService() {
		
	}
	
	/**
	 * Inserts a new role.
	 * 
	 * @param roleVO the role.
	 * @return the number of affected rows.
	 * @throws BusinessException
	 */
	public int insert (RoleVO roleVO) throws BusinessException {
		int affectedRows = 0;
		try {
			affectedRows = roleDAO.insert (roleVO);
			logger.debug ("affectedRows [" + affectedRows + "]");
		} catch (DataAccessException e) {
			String errorMessage = "A business exception error occurred while inserting the role. " + e.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e.getCause());
		}
		return affectedRows;
	}
	
	/**
	 * Updates a role.
	 * 
	 * @param roleVO the role.
	 * @return the number of affected rows.
	 * @throws BusinessException
	 */
	public int update (RoleVO RoleVO) throws BusinessException {
		int affectedRows = 0;
		try {
			affectedRows = roleDAO.update (RoleVO);
			logger.debug ("affectedRows [" + affectedRows + "]");
		} catch (DataAccessException e) {
			String errorMessage = "A business exception error occurred while updating the role. " + e.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e.getCause());
		}
		return affectedRows;
	}
	
	/**
	 * Deletes a role.
	 * 
	 * @param RoleVO the role.
	 * @return the number of affected rows.
	 * @throws BusinessException
	 */
	public int delete (RoleVO RoleVO) throws BusinessException {
		int affectedRows = 0;
		try {
			affectedRows = roleDAO.delete (RoleVO);
			logger.debug ("affectedRows [" + affectedRows + "]");
		} catch (DataAccessException e) {
			String errorMessage = "A business exception error occurred while deleting the role. " + e.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e.getCause());
		}
		return affectedRows;
	}
	
	/**
	 * Finds a role by its id.
	 * 
	 * @param RoleVO the role.
	 * @return the found role.
	 * @throws BusinessException
	 */
	public RoleVO findById (RoleVO roleVO) throws BusinessException {
		RoleVO foundRoleVO = null;
		try {
			foundRoleVO = roleDAO.findById (roleVO);
		} catch (DataAccessException e) {
			String errorMessage = "A business exception error occurred while finding the role by id. " + e.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e.getCause());
		}
		return foundRoleVO;
	}
	
	/**
	 * Finds a duplicated role name.
	 * 
	 * @param roleVO the role.
	 * @return the count of duplicated role name.
	 * @throws BusinessException
	 */
	public int findDuplicatedName (RoleVO roleVO) throws BusinessException {
		int count = 0;
		try {
			count = roleDAO.findDuplicatedName (roleVO);
			logger.debug ("count [" + count + "]");
		} catch (DataAccessException e) {
			String errorMessage = "A business exception error occurred while finding the a duplicated name. " + e.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e.getCause());
		}
		return count;
	}
	
	/**
	 * Finds all the roles.
	 * 
	 * @return a list of roles.
	 * @throws BusinessException
	 */
	public List<RoleVO> findAll () throws BusinessException {
		List<RoleVO> roleVOList = null;
		try {
			roleVOList = roleDAO.findAll();
		} catch (DataAccessException e) {
			String errorMessage = "A business exception error occurred while finding the all the roles. " + e.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e.getCause());
		}
		return roleVOList;
	}
	
	/**
	 * Gets the row count of roles.
	 * 
	 * @return the row count.
	 * @throws BusinessException
	 */
	public int rowCount () throws BusinessException {
		int count = 0;
		try {
			count = roleDAO.rowCount();
		} catch (DataAccessException e) {
			String errorMessage = "A business exception error occurred while getting the row count of roles. " + e.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e.getCause());
		}
		return count;
	}
	
}
