/* Copyright TI Serviços 2017 */
package com.tiservicos.console.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tiservicos.console.vo.ProfileRoleVO;
import com.tiservicos.console.vo.ProfileVO;
import com.tiservicos.console.vo.RoleVO;

/**
 * DAO class for ProfileRole.
 * 
 * @author Josivan Silva
 *
 */
public class ProfileRoleDAO extends AbstractDAO {

	static Logger logger = Logger.getLogger (ProfileRoleDAO.class.getName());
	
	public ProfileRoleDAO () {
		
	}
	
	/**
	 * Inserts a new profileRole.
	 * 
	 * @param profileRoleVO the profile.
	 * @return the number of affected rows.
	 * @throws DataAccessException
	 */
	public int insert (ProfileRoleVO profileRoleVO) throws DataAccessException {
		int affectedRows = 0;
		StringBuilder sbSql = null;
		sbSql = new StringBuilder();
		sbSql.append ("INSERT INTO PROFILE_ROLE (PROFILE_ID, ROLE_ID) ");
		sbSql.append ("VALUES (" +  profileRoleVO.getProfileVO().getProfileId() + ", ");
		sbSql.append (profileRoleVO.getRoleVO().getRoleId() + ")");
		affectedRows = updateDb (sbSql.toString());
		return affectedRows;
	}
	
	/**
	 * Deletes a profileRole.
	 * 
	 * @param profileVO the profile.
	 * @return the number of affected rows.
	 * @throws DataAccessException
	 */
	public int deleteByProfileId (ProfileVO profileVO) throws DataAccessException {
		String sql       = null;
		int affectedRows = 0;
		sql = "DELETE FROM PROFILE_ROLE WHERE PROFILE_ID = " + profileVO.getProfileId();
		affectedRows = updateDb (sql);
		return affectedRows;
	}
	
	/**
	 * Finds a list of profileRole by profile id.
	 * 
	 * @param profileVO the profile.
	 * @return a list of profileRole.
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<ProfileRoleVO> findByProfileId (ProfileVO profileVO) throws DataAccessException {
		String sql            = null;
		List<Object> rowList  = null;
		List<ProfileRoleVO> profileRoleList = new ArrayList<ProfileRoleVO>();
		sql = "SELECT PR.PROFILE_ID, PR.ROLE_ID, R.NAME "
			+ "FROM PROFILE_ROLE PR "
			+ "INNER JOIN ROLE R ON R.ROLE_ID = PR.ROLE_ID "
			+ "WHERE PR.PROFILE_ID = " + profileVO.getProfileId();
		rowList = selectDb (sql, 3);
		if (!rowList.isEmpty() && rowList.size() > 0) {			
			for (Object columnList : rowList) {
				List<Object> columns = (List<Object>)columnList;
				Integer profileId    = (Integer) columns.get (0);
				Integer roleId       = (Integer) columns.get (1);
				String  roleName     = (String) columns.get (2);
				
				ProfileVO foundProfileVO = new ProfileVO();
				foundProfileVO.setProfileId (profileId);
				
				RoleVO roleVO = new RoleVO();
				roleVO.setRoleId (roleId);
				roleVO.setName (roleName);
								
				ProfileRoleVO profileRoleVO = new ProfileRoleVO();
								
				profileRoleVO.setProfileVO (foundProfileVO);
				profileRoleVO.setRoleVO (roleVO);
				
				profileRoleList.add (profileRoleVO);
			}
		}
		return profileRoleList;
	}
	
	/**
	 * Finds all the roles with the selected, given the profile.
	 * 
	 * @return a list of roles.
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<RoleVO> findAllWithSelected (ProfileVO profileVO) throws DataAccessException {
		String sql            = null;
		List<Object> rowList  = null;
		List<RoleVO> roleList = new ArrayList<RoleVO>();
		sql = "SELECT R.ROLE_ID, R.NAME, "
			+ "(SELECT COUNT(*) FROM PROFILE_ROLE WHERE PROFILE_ID = " + profileVO.getProfileId() + " AND ROLE_ID = R.ROLE_ID) AS SELECTED "
		    + "FROM ROLE R "
			+ "ORDER BY R.ROLE_ID";
		rowList = selectDb (sql, 3);
		if (!rowList.isEmpty() && rowList.size() > 0) {
			for (Object columnList : rowList) {	
							
				List<Object> columns = (List<Object>)columnList;					
				Integer roleId       = (Integer) columns.get (0);
				String name          = (String) columns.get (1);
				Long selected        = (Long) columns.get (2);
				
				RoleVO roleVO = new RoleVO();
				roleVO.setRoleId (roleId);
				roleVO.setName (name);
				boolean isSelected = selected == 1 ? true : false;
				roleVO.setSelected (isSelected);
										
				roleList.add (roleVO);
			}
		}
		return roleList;
	}
	
}
