/* Copyright TI Serviços 2017 */
package com.tiservicos.console.dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.tiservicos.console.util.SecurityUtils;
import com.tiservicos.console.vo.UserConsoleVO;

/**
 * DAO class for UserConsole.
 * 
 * @author josivan@tiservicos.com
 *
 */
public class UserConsoleDAO extends AbstractDAO {

	static Logger logger = Logger.getLogger (UserConsoleDAO.class.getName());
	
	public UserConsoleDAO () {
		
	}
	
	/**
	 * Inserts a new userConsoleVO.
	 * 
	 * @param userConsoleVO the userConsole.
	 * @return the number of affected rows.
	 * @throws DataAccessException
	 */
	public int insert (UserConsoleVO userConsoleVO) throws DataAccessException {
		int userConsoleId = 0;
		StringBuilder sbSql = null;
		sbSql = new StringBuilder();
		sbSql.append ("INSERT INTO USER_CONSOLE (EMAIL, PWD, STATUS) ");
		sbSql.append ("VALUES ('" + userConsoleVO.getEmail() + "', ");
		String pwdHash = SecurityUtils.getSHA512Password (userConsoleVO.getPwd(), userConsoleVO.getEmail());
		sbSql.append ("'" + pwdHash + "', ");		
		sbSql.append (userConsoleVO.getStatus() + ") ");		
		logger.debug ("sql: " + sbSql.toString());
		userConsoleId = insertDbWithIntegerKey(sbSql.toString());
		return userConsoleId;
	}
	
	/**
	 * Updates an existing userConsoleVO.
	 * 
	 * @param userConsoleVO the userConsole.
	 * @return the number of affected rows.
	 * @throws DataAccessException
	 */
	public int update (UserConsoleVO userConsoleVO) throws DataAccessException {
		int affectedRows = 0;
		StringBuilder sbSql = null;
		String whereClause  = null;
		sbSql = new StringBuilder();
		sbSql.append ("UPDATE USER_CONSOLE SET ");
		sbSql.append ("EMAIL=");
		sbSql.append ("'" + userConsoleVO.getEmail() + "', ");
		sbSql.append ("PWD=");
		String pwdHash = SecurityUtils.getSHA512Password (userConsoleVO.getPwd(), userConsoleVO.getEmail());
		sbSql.append ("'" + pwdHash + "', ");
		sbSql.append ("STATUS=");
		sbSql.append (userConsoleVO.getStatus() + " ");
		whereClause = "WHERE USER_CONSOLE_ID = " + userConsoleVO.getUserConsoleId();
		sbSql.append (whereClause);
		logger.debug ("sql: " + sbSql.toString());
		affectedRows = updateDb (sbSql.toString());
		return affectedRows;
	}
	
	/**
	 * Deletes a userConsole given the userConsoleId.
	 * 
	 * @param userConsoleVO the userConsole.
	 * @return the number of affected rows.
	 * @throws DataAccessException
	 */
	public int delete (UserConsoleVO userConsoleVO) throws DataAccessException {
		String sql       = null;
		int affectedRows = 0;
		sql = "DELETE FROM USER_CONSOLE WHERE USER_CONSOLE_ID = " + userConsoleVO.getUserConsoleId();
		affectedRows = updateDb (sql);
		return affectedRows;		
	}
	
	/**
	 * Find a userConsole by its id.
	 * 
	 * @param userConsoleVO the userConsole.
	 * @return a userConsole.
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public UserConsoleVO findById (UserConsoleVO userConsoleVO) throws DataAccessException {
		UserConsoleVO foundUserConsoleVO = null;
		String sql                   = null;
		List<Object> rowList         = null;
		sql = "SELECT USER_CONSOLE_ID, EMAIL, STATUS FROM USER_CONSOLE WHERE USER_CONSOLE_ID = " + userConsoleVO.getUserConsoleId();
		rowList = selectDb (sql, 3);
		if (!rowList.isEmpty() && rowList.size() > 0) {
			for (Object columnList : rowList) {
				List<Object> columns  = (List<Object>)columnList;					
				Integer userConsoleId = (Integer) columns.get (0);
				String email          = (String) columns.get (1);
				boolean status        = (Boolean) columns.get (2);
								
				foundUserConsoleVO = new UserConsoleVO();
				foundUserConsoleVO.setUserConsoleId(userConsoleId);
				foundUserConsoleVO.setEmail(email);
				foundUserConsoleVO.setStatus(status);
			}
		}
		return foundUserConsoleVO;
	}
	
	/**
	 * Find all the userConsole.
	 * 
	 * @return a list of userConsole.
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<UserConsoleVO> findAll () throws DataAccessException {
		List<UserConsoleVO> listUserConsoleVO = new ArrayList<UserConsoleVO>();
		String sql                   = null;
		List<Object> rowList         = null;
		sql = "SELECT USER_CONSOLE_ID, EMAIL, STATUS FROM USER_CONSOLE ORDER BY USER_CONSOLE_ID";
		rowList = selectDb (sql, 3);
		if (!rowList.isEmpty() && rowList.size() > 0) {
			for (Object columnList : rowList) {
				List<Object> columns  = (List<Object>)columnList;					
				Integer userConsoleId = (Integer) columns.get (0);
				String email          = (String) columns.get (1);
				boolean status        = (Boolean) columns.get (2);
				
				UserConsoleVO userConsoleVO = new UserConsoleVO();
				userConsoleVO = new UserConsoleVO();
				userConsoleVO.setUserConsoleId(userConsoleId);
				userConsoleVO.setEmail(email);
				userConsoleVO.setStatus(status);
				
				listUserConsoleVO.add(userConsoleVO);
			}
		}
		return listUserConsoleVO;
	}
	
	/**
	 * Performs the userConsole login.
	 * 
	 * @param userConsoleVO the userConsole.
	 * @return the operation result.
	 * @throws DataAccessException
	 */
	public boolean doLogin (UserConsoleVO userConsoleVO) throws DataAccessException {
		logger.debug("Starting doLogin.");
		boolean isValid         = false;
		Connection conn         = null;
		PreparedStatement pstmt = null;
		ResultSet rs            = null;
		String sql              = null;
		try {
			conn = this.getConnection();
			
			logger.debug("conn.isClosed() " + conn.isClosed());
			
			String pwdHash1 = SecurityUtils.getSHA512Password (userConsoleVO.getPwd(), userConsoleVO.getEmail());
			
			logger.debug("pwdHash1 " + pwdHash1 + "\n");
			
			sql = "SELECT PWD FROM USER_CONSOLE WHERE EMAIL = ? AND STATUS = 1";
			
			logger.debug("sql " + sql);
			
			pstmt = conn.prepareStatement (sql);
			
			pstmt.setString (1, userConsoleVO.getEmail());
									
			rs = pstmt.executeQuery();
		    while (rs.next()) {		    	
		    	String pwdHashDb = rs.getString (1);
		    	
		    	logger.debug("pwdHashDb " + pwdHashDb + "\n");
		    			    	
		    	isValid = SecurityUtils.isMessageDigestEqual(pwdHash1, pwdHashDb);
		    }
		    
		    logger.debug ("isValid: " + isValid);
		    
		} catch (SQLException e) {
		    String error = "An error occurred while executing the userConsole login sql statement. " + e.getMessage();
		    logger.error (error);
		    throw new DataAccessException (error);
		} finally {
			this.closeResultSet (rs);
			this.closePreparedStatement (pstmt);
			this.closeConnection (conn);
		}
		logger.debug("Finishing doLogin.");
		return isValid;
	}
	
	/**
	 * Finds an userConsole by email.
	 * 
	 * @param email the email.
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public UserConsoleVO findByEmail (String email) throws DataAccessException {
		UserConsoleVO foundUserConsole = null;
		String sql             = null;
		List<Object> rowList   = null;
		sql = "SELECT USER_CONSOLE_ID, EMAIL FROM USER_CONSOLE WHERE EMAIL = '" + email + "'";
		logger.debug ("sql: " + sql);
		rowList = selectDb (sql, 2);
		logger.debug ("rowList.size(): " + rowList.size());
		if (!rowList.isEmpty() && rowList.size() > 0) {
			for (Object columnList : rowList) {
				List<Object> columns = (List<Object>)columnList;
				
				Integer userConsoleId = (Integer) columns.get (0);
				String newEmail       = (String) columns.get (1);
								
				foundUserConsole = new UserConsoleVO();
				foundUserConsole.setUserConsoleId(userConsoleId);
				foundUserConsole.setEmail(newEmail);				
			}
		}
		return foundUserConsole;
	}
	
	/**
	 * Finds a duplicated email.
	 * 
	 * @param userConsoleVO the userConsole.
	 * @return the count of duplicated email.
	 * @throws DataAccessException
	 */
	public int findDuplicatedEmail (UserConsoleVO userConsoleVO) throws DataAccessException {
		String sql = null;
		String whereClause = "";
		int count = 0;
		if (userConsoleVO.getUserConsoleId() != null && userConsoleVO.getUserConsoleId() > 0) {
			whereClause = " AND USER_CONSOLE_ID <> " + userConsoleVO.getUserConsoleId().toString();
		}
		sql = "SELECT COUNT(*) FROM USER_CONSOLE WHERE EMAIL = '" + userConsoleVO.getEmail() + "'" + whereClause;
		logger.info("sql: " + sql);
		count = selectRowCount (sql);
		return count;
	}
		
}
