/* Copyright TI Serviços 2017 */
package com.tiservicos.console.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tiservicos.console.vo.RoleVO;

/**
 * DAO class for Role.
 * 
 * @author josivan@tiservicos.com
 *
 */
public class RoleDAO extends AbstractDAO {

	static Logger logger = Logger.getLogger (RoleDAO.class.getName());
	
	public RoleDAO () {
		
	}
	
	/**
	 * Inserts a new role.
	 * 
	 * @param roleVO the role.
	 * @return the number of affected rows.
	 * @throws DataAccessException
	 */
	public int insert (RoleVO roleVO) throws DataAccessException {
		int affectedRows = 0;
		StringBuilder sbSql = null;
		sbSql = new StringBuilder();
		sbSql.append ("INSERT INTO ROLE (ROLE_ID, NAME) ");
		sbSql.append ("VALUES (" +  roleVO.getRoleId() + ", ");
		sbSql.append ("'" + roleVO.getName() + "')");
		affectedRows = updateDb (sbSql.toString());
		return affectedRows;
	}
	
	/**
	 * Updates an existing role.
	 * 
	 * @param roleVO the role.
	 * @return the number of affected rows.
	 * @throws DataAccessException
	 */
	public int update (RoleVO roleVO) throws DataAccessException {
		int affectedRows = 0;
		StringBuilder sbSql = null;
		String whereClause  = null;
		sbSql = new StringBuilder();
		sbSql.append ("UPDATE ROLE SET ");
		sbSql.append ("NAME=");
		sbSql.append ("'" + roleVO.getName() + "' ");
		whereClause = " WHERE ROLE_ID = " + roleVO.getRoleId();
		sbSql.append (whereClause);
		affectedRows = updateDb (sbSql.toString());
		return affectedRows;
	}
	
	/**
	 * Deletes a role.
	 * 
	 * @param roleVO the role.
	 * @return the number of affected rows.
	 * @throws DataAccessException
	 */
	public int delete (RoleVO roleVO) throws DataAccessException {
		String sql       = null;
		int affectedRows = 0;
		sql = "DELETE FROM ROLE WHERE ROLE_ID = " + roleVO.getRoleId();
		affectedRows = updateDb (sql);
		return affectedRows;
	}
	
	/**
	 * Find a role by its id.
	 * 
	 * @param roleVO the role.
	 * @return a role.
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public RoleVO findById (RoleVO roleVO) throws DataAccessException {
		RoleVO foundRoleVO = null;
		String sql         = null;
		List<Object> rowList = null;
		sql = "SELECT ROLE_ID, NAME FROM ROLE WHERE ROLE_ID = " + roleVO.getRoleId();
		rowList = selectDb (sql, 2);		
		if (!rowList.isEmpty() && rowList.size() > 0) {			
			for (Object columnList : rowList) {				
				List<Object> columns = (List<Object>)columnList;					
				Integer roleId = (Integer) columns.get (0);
				String name    = (String) columns.get (1);
				
				foundRoleVO = new RoleVO();
				foundRoleVO.setRoleId (roleId);
				foundRoleVO.setName (name);
			}
		}
		return foundRoleVO;
	}
	
	/**
	 * Finds a duplicated role name.
	 * 
	 * @param roleVO the role.
	 * @return the count of duplicated role name.
	 * @throws DataAccessException
	 */
	public int findDuplicatedName (RoleVO roleVO) throws DataAccessException {
		String sql = null;
		String whereClause = "";
		int count = 0;
		if (roleVO.getRoleId() != null && roleVO.getRoleId() > 0) {
			whereClause = " AND ROLE_ID <> " + roleVO.getRoleId();
		}
		sql = "SELECT COUNT(*) FROM ROLE WHERE NAME = '" + roleVO.getName() + "'" + whereClause;
		count = selectRowCount (sql);
		return count;
	}
	
	/**
	 * Finds all the roles.
	 * 
	 * @return a list of roles.
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<RoleVO> findAll () throws DataAccessException {
		String sql            = null;
		List<Object> rowList  = null;
		List<RoleVO> roleList = new ArrayList<RoleVO>();
		sql = "SELECT ROLE_ID, NAME FROM ROLE ORDER BY ROLE_ID";				
		rowList = selectDb (sql, 2);
		if (!rowList.isEmpty() && rowList.size() > 0) {
			for (Object columnList : rowList) {	
							
				List<Object> columns = (List<Object>)columnList;					
				Integer roleId       = (Integer) columns.get (0);
				String name          = (String) columns.get (1);
				
				RoleVO roleVO = new RoleVO();
				roleVO.setRoleId (roleId);
				roleVO.setName (name);
										
				roleList.add (roleVO);
			}
		}
		return roleList;
	}
	
	/**
	 * Gets the row count of roles.
	 * 
	 * @return the row count.
	 * @throws DataAccessException
	 */
	public int rowCount () throws DataAccessException {
		int count = 0;
		String sql = null;
		sql = "SELECT COUNT(*) FROM ROLE";
		logger.debug ("sql: " + sql);
		count = selectRowCount (sql);
		return count;
	}
	
}
