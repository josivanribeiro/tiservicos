/* Copyright TI Serviços 2017 */
package com.tiservicos.console.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tiservicos.console.vo.ProfileVO;
import com.tiservicos.console.vo.UserConsoleProfileVO;
import com.tiservicos.console.vo.UserConsoleVO;


/**
 * DAO class for UserConsoleProfile.
 * 
 * @author josivan@tiservicos.com
 *
 */
public class UserConsoleProfileDAO extends AbstractDAO {

	static Logger logger = Logger.getLogger (UserConsoleProfileDAO.class.getName());
	
	public UserConsoleProfileDAO () {
		
	}
	
	/**
	 * Inserts a new userProfile.
	 * 
	 * @param userProfileVO the profile.
	 * @return the number of affected rows.
	 * @throws DataAccessException
	 */
	public int insert (UserConsoleProfileVO userProfileVO) throws DataAccessException {
		int affectedRows = 0;
		StringBuilder sbSql = null;
		sbSql = new StringBuilder();
		sbSql.append ("INSERT INTO USER_CONSOLE_PROFILE (USER_CONSOLE_ID, PROFILE_ID) ");
		sbSql.append ("VALUES (" +  userProfileVO.getUserConsoleVO().getUserConsoleId() + ", ");
		sbSql.append (userProfileVO.getProfileVO().getProfileId() + ")");
		affectedRows = updateDb (sbSql.toString());
		return affectedRows;
	}
	
	/**
	 * Deletes an userProfile.
	 * 
	 * @param userConsoleVO the userConsole.
	 * @return the number of affected rows.
	 * @throws DataAccessException
	 */
	public int deleteByUserConsoleId (UserConsoleVO userConsoleVO) throws DataAccessException {
		String sql       = null;
		int affectedRows = 0;
		sql = "DELETE FROM USER_CONSOLE_PROFILE WHERE USER_CONSOLE_ID = " + userConsoleVO.getUserConsoleId();
		affectedRows = updateDb (sql);
		return affectedRows;
	}
	
	/**
	 * Finds a list of userProfile by user console id.
	 * 
	 * @param userConsoleVO the userConsole.
	 * @return a list of userProfile.
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<UserConsoleProfileVO> findByUserConsoleId (UserConsoleVO userConsoleVO) throws DataAccessException {
		String sql           = null;
		List<Object> rowList = null;
		List<UserConsoleProfileVO> userProfileList = new ArrayList<UserConsoleProfileVO>();
		sql = "SELECT USER_CONSOLE_ID, PROFILE_ID FROM USER_CONSOLE_PROFILE WHERE USER_CONSOLE_ID = " + userConsoleVO.getUserConsoleId();
		logger.debug(sql);
		rowList = selectDb (sql, 2);
		if (!rowList.isEmpty() && rowList.size() > 0) {			
			for (Object columnList : rowList) {
				List<Object> columns = (List<Object>)columnList;
				Integer userConsoleId    = (Integer) columns.get (0);
				Integer profileId = (Integer) columns.get (1);				
				
				UserConsoleVO foundUserConsoleVO = new UserConsoleVO();
				foundUserConsoleVO.setUserConsoleId (userConsoleId);
				
				ProfileVO profileVO = new ProfileVO();
				profileVO.setProfileId (profileId);
				
				UserConsoleProfileVO userProfileVO = new UserConsoleProfileVO();
								
				userProfileVO.setProfileVO (profileVO);
				userProfileVO.setUserConsoleVO (foundUserConsoleVO);
				
				userProfileList.add (userProfileVO);
			}
		}
		return userProfileList;
	}
	
	/**
	 * Finds all the profiles with the selected, given the user.
	 * 
	 * @return a list of profiles.
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<ProfileVO> findAllWithSelected (UserConsoleVO userConsoleVO) throws DataAccessException {
		String sql            = null;
		List<Object> rowList  = null;
		List<ProfileVO> profileList = new ArrayList<ProfileVO>();
		sql = "SELECT P.PROFILE_ID, P.NAME, "
			+ "(SELECT COUNT(*) FROM USER_CONSOLE_PROFILE WHERE USER_CONSOLE_ID = " + userConsoleVO.getUserConsoleId() + " AND PROFILE_ID = P.PROFILE_ID) AS SELECTED "
		    + "FROM PROFILE P "
			+ "ORDER BY P.PROFILE_ID";
		rowList = selectDb (sql, 3);
		if (!rowList.isEmpty() && rowList.size() > 0) {
			for (Object columnList : rowList) {	
							
				List<Object> columns = (List<Object>)columnList;
				Integer profileId = (Integer) columns.get (0);
				String name       = (String) columns.get (1);
				Long selected     = (Long) columns.get (2);
				
				ProfileVO profileVO = new ProfileVO();
				profileVO.setProfileId (profileId);
				profileVO.setName (name);
				boolean isSelected = selected == 1 ? true : false;
				profileVO.setSelected (isSelected);
				
				profileList.add (profileVO);
			}
		}
		return profileList;
	}
	
}
