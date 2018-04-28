/* Copyright TI Serviços 2017 */
package com.tiservicos.console.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tiservicos.console.dao.DataAccessException;
import com.tiservicos.console.dao.UserConsoleDAO;
import com.tiservicos.console.dao.UserConsoleProfileDAO;
import com.tiservicos.console.vo.ProfileVO;
import com.tiservicos.console.vo.UserConsoleProfileVO;
import com.tiservicos.console.vo.UserConsoleVO;


/**
 * Service class for UserConsole.
 * 
 * @author josivan@tiservicos.com
 *
 */
public class UserConsoleService {

	static Logger logger = Logger.getLogger (UserConsoleService.class.getName());	
	private UserConsoleDAO userConsoleDAO = new UserConsoleDAO();
	private UserConsoleProfileDAO userConsoleProfileDAO = new UserConsoleProfileDAO();
	
	public UserConsoleService() {
		
    }
	
	public Integer insert (UserConsoleVO userConsoleVO) throws BusinessException {
		Integer userConsoleId = 0;
		try {
			userConsoleId = userConsoleDAO.insert (userConsoleVO);
			logger.debug ("new userConsoleId [" + userConsoleId + "]");
			if (userConsoleId != null && userConsoleId > 0) {				
				for (UserConsoleProfileVO userConsoleProfileVO : userConsoleVO.getUserConsoleProfileVOList()) {					
					UserConsoleVO newUserConsoleVO = new UserConsoleVO();
					newUserConsoleVO.setUserConsoleId (userConsoleId);
					userConsoleProfileVO.setUserConsoleVO (newUserConsoleVO);
					int affectedRows = userConsoleProfileDAO.insert (userConsoleProfileVO);
					if (affectedRows > 0) {
						logger.debug ("profile id [" + userConsoleProfileVO.getProfileVO().getProfileId() + "] inserted successfully.");
					}
				}			
			}			
		} catch (DataAccessException e) {
			String errorMessage = "A business exception error occurred while inserting the user console or userConsoleProfile. " + e.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e.getCause());
		}
		return userConsoleId;
	}
	
	/**
	 * Updates an user.
	 * 
	 * @param userConsoleVO the userConsole.
	 * @return the number of affected rows.
	 * @throws BusinessException
	 */
	public int update (UserConsoleVO userConsoleVO) throws BusinessException {
		int affectedRows = 0;
		try {
			affectedRows = userConsoleDAO.update (userConsoleVO);
			logger.debug ("affectedRows [" + affectedRows + "]");			
			if (affectedRows > 0) {				
				int deletedRows = userConsoleProfileDAO.deleteByUserConsoleId (userConsoleVO);
				logger.debug ("userConsoleProfileDeletedRows [" + deletedRows + "]");				
				for (UserConsoleProfileVO userConsoleProfileVO : userConsoleVO.getUserConsoleProfileVOList()) {
					userConsoleProfileVO.setUserConsoleVO (userConsoleVO);
					userConsoleProfileDAO.insert (userConsoleProfileVO);
					logger.debug ("inserted userConsoleProfile with userConsoleId "
							+ userConsoleProfileVO.getUserConsoleVO().getUserConsoleId() + " and profileId " + userConsoleProfileVO.getProfileVO().getProfileId());					
				}				
			}			
		} catch (DataAccessException e) {
			String errorMessage = "A business exception error occurred while updating the userConsole with profile. " + e.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e.getCause());
		}
		return affectedRows;
	}
	
	/**
	 * Finds an userConsole by its id.
	 * 
	 * @param userConsoleVO the userConsole.
	 * @return the found userConsole.
	 * @throws BusinessException
	 */
	public UserConsoleVO findById (UserConsoleVO userConsoleVO) throws BusinessException {
		UserConsoleVO foundUserConsoleVO = null;
		try {
			foundUserConsoleVO = userConsoleDAO.findById (userConsoleVO);			
			List<UserConsoleProfileVO> userConsoleProfileVOList = userConsoleProfileDAO.findByUserConsoleId (userConsoleVO);
			
			logger.debug("userConsoleProfileVOList.size() " + userConsoleProfileVOList.size());
			
			if (userConsoleProfileVOList != null && userConsoleProfileVOList.size() > 0) {
				foundUserConsoleVO.setUserConsoleProfileVOList (userConsoleProfileVOList);
			}			
		} catch (DataAccessException e) {
			String errorMessage = "A business exception error occurred while finding the user console by id. " + e.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e.getCause());
		}
		return foundUserConsoleVO;
	}
	
	/**
	 * Deletes the UserConsole.
	 * 
	 * @param userConsoleVO the userConsole.
	 * @return the number of affected rows.
	 * @throws BusinessException
	 */
	public int delete (UserConsoleVO userConsoleVO) throws BusinessException {
		int affectedRows = 0;
		try {
			affectedRows = userConsoleDAO.delete (userConsoleVO);			
			logger.debug ("affectedRows [" + affectedRows + "]");
		} catch (DataAccessException e) {
			String errorMessage = "A business exception error occurred while deleting the user console. " + e.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e.getCause());
		}
		return affectedRows;
	}
	
	/**
	 * Finds all the userConsole.
	 * 
	 * @return a userConsole list.
	 * @throws BusinessException
	 */
	public List<UserConsoleVO> findAll () throws BusinessException {
		List<UserConsoleVO> userConsoleVOList = new ArrayList<UserConsoleVO>();
		try {
			userConsoleVOList = userConsoleDAO.findAll();
		} catch (DataAccessException e) {
			String errorMessage = "A business exception error occurred while finding all the user console. " + e.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e.getCause());
		}
		return userConsoleVOList;
	}
	
	/**
	 * Finds all the profiles with the selected, given the userConsole.
	 * 
	 * @return a list of profiles.
	 * @throws BusinessException
	 */
	public List<ProfileVO> findAllWithSelected (UserConsoleVO userConsoleVO) throws BusinessException {
		List<ProfileVO> profileVOList = null;
		try {
			profileVOList = userConsoleProfileDAO.findAllWithSelected (userConsoleVO);
		} catch (DataAccessException e) {
			String errorMessage = "A business exception error occurred while finding the all profiles with selected field. " + e.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e.getCause());
		}
		return profileVOList;
	}
	
	/**
	 * Finds a duplicated email.
	 * 
	 * @param userConsoleVO the userConsole.
	 * @return the count of duplicated email.
	 * @throws BusinessException
	 */
	public int findDuplicatedEmail (UserConsoleVO userConsoleVO) throws BusinessException {
		int count = 0;
		try {
			count = userConsoleDAO.findDuplicatedEmail (userConsoleVO);
			logger.debug ("count [" + count + "]");
		} catch (DataAccessException e) {
			String errorMessage = "A business exception error occurred while finding the a duplicated email. " + e.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e.getCause());
		}
		return count;
	}
	
	/**
	 * Performs the userConsole login.
	 * 
	 * @param userConsoleVO the userConsole.
	 * @return a the result of the login operation.
	 * @throws BusinessException
	 */
	public boolean doLogin (UserConsoleVO userConsoleVO) throws BusinessException {
		boolean isLogged = false;
		try {
			isLogged = userConsoleDAO.doLogin (userConsoleVO);			
		} catch (DataAccessException e) {
			String errorMessage = "A business exception error occurred while performing the userConsole login. " + e.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e.getCause());
		}
		return isLogged;
	}
	
	/**
	 * Finds userConsole by email.
	 * 
	 * @param email the email.
	 * @return
	 * @throws BusinessException
	 */
	public UserConsoleVO findByEmail (String email) throws BusinessException {
		UserConsoleVO userConsoleVO = null;
		try {
			userConsoleVO = userConsoleDAO.findByEmail(email);
		} catch (DataAccessException e) {
			String errorMessage = "A business exception error occurred while finding the userConsole by email. " + e.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e.getCause());
		}
		return userConsoleVO;
	}
		
}
