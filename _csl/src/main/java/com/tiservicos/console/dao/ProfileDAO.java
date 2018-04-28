/* Copyright TI Serviços 2017 */
package com.tiservicos.console.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tiservicos.console.vo.ProfileVO;

/**
 * DAO class for Profile.
 * 
 * @author josivan@tiservicos.com
 *
 */
public class ProfileDAO extends AbstractDAO {

	static Logger logger = Logger.getLogger (ProfileDAO.class.getName());
	
	public ProfileDAO () {
		
	}
	
	/**
	 * Inserts a new profile.
	 * 
	 * @param profileVO the profile.
	 * @return the number of affected rows.
	 * @throws DataAccessException
	 */
	public Integer insert (ProfileVO profileVO) throws DataAccessException {
		Integer profileId = 0;
		StringBuilder sbSql = null;
		sbSql = new StringBuilder();
		sbSql.append ("INSERT INTO PROFILE (PROFILE_ID, NAME) ");
		sbSql.append ("VALUES (" +  profileVO.getProfileId() + ", ");
		sbSql.append ("'" + profileVO.getName() + "')");
		profileId = insertDbWithIntegerKey (sbSql.toString());
		return profileId;
	}
	
	/**
	 * Updates an existing profile.
	 * 
	 * @param profileVO the profile.
	 * @return the number of affected rows.
	 * @throws DataAccessException
	 */
	public int update (ProfileVO profileVO) throws DataAccessException {
		int affectedRows = 0;
		StringBuilder sbSql = null;
		String whereClause  = null;
		sbSql = new StringBuilder();
		sbSql.append ("UPDATE PROFILE SET ");
		sbSql.append ("NAME=");
		sbSql.append ("'" + profileVO.getName() + "' ");
		whereClause = " WHERE PROFILE_ID = " + profileVO.getProfileId();
		sbSql.append (whereClause);
		affectedRows = updateDb (sbSql.toString());
		return affectedRows;
	}
	
	/**
	 * Deletes a profile.
	 * 
	 * @param profileVO the profile.
	 * @return the number of affected rows.
	 * @throws DataAccessException
	 */
	public int delete (ProfileVO profileVO) throws DataAccessException {
		String sql       = null;
		int affectedRows = 0;
		sql = "DELETE FROM PROFILE WHERE PROFILE_ID = " + profileVO.getProfileId();
		affectedRows = updateDb (sql);
		return affectedRows;
	}
	
	/**
	 * Find a profile by its id.
	 * 
	 * @param profileVO the profile.
	 * @return a profile.
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public ProfileVO findById (ProfileVO profileVO) throws DataAccessException {
		ProfileVO foundProfileVO = null;
		String sql         = null;
		List<Object> rowList = null;
		sql = "SELECT PROFILE_ID, NAME FROM PROFILE WHERE PROFILE_ID = " + profileVO.getProfileId();
		rowList = selectDb (sql, 2);		
		if (!rowList.isEmpty() && rowList.size() > 0) {			
			for (Object columnList : rowList) {				
				List<Object> columns = (List<Object>)columnList;					
				Integer profileId = (Integer) columns.get (0);
				String name    = (String) columns.get (1);
				
				foundProfileVO = new ProfileVO();
				foundProfileVO.setProfileId (profileId);
				foundProfileVO.setName (name);
			}
		}
		return foundProfileVO;
	}
	
	/**
	 * Finds a duplicated profile name.
	 * 
	 * @param profileVO the profile.
	 * @return the count of duplicated profile name.
	 * @throws DataAccessException
	 */
	public int findDuplicatedName (ProfileVO profileVO) throws DataAccessException {
		String sql = null;
		String whereClause = "";
		int count = 0;
		if (profileVO.getProfileId() != null && profileVO.getProfileId() > 0) {
			whereClause = " AND PROFILE_ID <> " + profileVO.getProfileId();
		}
		sql = "SELECT COUNT(*) FROM PROFILE WHERE NAME = '" + profileVO.getName() + "'" + whereClause;
		count = selectRowCount (sql);
		return count;
	}
	
	/**
	 * Finds all the profiles.
	 * 
	 * @return a list of profiles.
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<ProfileVO> findAll () throws DataAccessException {
		String sql            = null;
		List<Object> rowList  = null;
		List<ProfileVO> profileList = new ArrayList<ProfileVO>();
		sql = "SELECT PROFILE_ID, NAME FROM PROFILE ORDER BY PROFILE_ID";				
		rowList = selectDb (sql, 2);
		if (!rowList.isEmpty() && rowList.size() > 0) {
			for (Object columnList : rowList) {	
							
				List<Object> columns = (List<Object>)columnList;			
				Integer profileId    = (Integer) columns.get (0);
				String name          = (String) columns.get (1);
				
				ProfileVO profileVO = new ProfileVO();
				profileVO.setProfileId (profileId);
				profileVO.setName (name);
										
				profileList.add (profileVO);
			}
		}
		return profileList;
	}	
	
}
