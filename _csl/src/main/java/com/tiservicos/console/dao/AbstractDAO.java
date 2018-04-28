/* Copyright TI Serviços 2015-2017 */
package com.tiservicos.console.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * Abstract class for all DAO domain objects.
 * 
 * @author josivan@tiservicos.com
 *
 */
public abstract class AbstractDAO {

	static Logger logger = Logger.getLogger (AbstractDAO.class.getName());
	private DataSource dataSource;
	
	public AbstractDAO() {
        
    }
 
    /**
     * Gets a database connection.
     * 
     * @return a connection.
     * @throws DataAccessException
     */
    protected Connection getConnection() throws DataAccessException {
        Connection conn = null;        
        try {
        	Context initialContext = new InitialContext();
            Context webContext = (Context)initialContext.lookup("java:/comp/env");
            dataSource = (DataSource) webContext.lookup("jdbc/tiservicosDb");
        } catch (NamingException e) {
            String error = "An error occurred during the datasource lookup. " + e.getMessage();
			logger.error (error);
			throw new DataAccessException (error);
        }        
        try {
            conn = dataSource.getConnection();
        } catch (SQLException e) {
        	String error = "An error occurred while openning the database connection. " + e.getMessage();
			logger.error (error);
			throw new DataAccessException (error);        	
        }
        return conn;
    }
    
    /**
	 * Executes the given SQL statement.
	 * 
	 * @param sql the sql statement.
	 * @param fieldsNumber the number of fields of the table.
	 * @return a object list.
	 * @throws DataAccessException
	 */
    protected List<Object> selectDb (final String sql, int fieldsNumber) throws DataAccessException {
		Connection conn         = null;
		Statement stmt          = null;
		ResultSet rs            = null;
		List<Object> columnList = null;
		List<Object> rowList    = new ArrayList<Object>();
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			rs   = stmt.executeQuery (sql);
			while (rs.next()) {								
				columnList = new ArrayList<Object>();
				for (int i = 1 ; i <= fieldsNumber; i++) {					
					Object column =  rs.getObject (i);
					columnList.add (column);		
				}
				rowList.add (columnList);
			}		
		} catch (SQLException e) {
		    String error = "An error occurred while executing the sql select. " + e.getMessage();
		    logger.error (error);
		    throw new DataAccessException (error);
		} finally {			
			this.closeResultSet (rs);
			this.closeStatement (stmt);
			this.closeConnection (conn);						
		}
		return rowList;
	}
	
	/**
	 * Executes the given SQL statement, which may be an INSERT, UPDATE, or DELETE statement.
	 * 
	 * @param sql the sql statement.
	 * @return the number of affected rows.
	 * @throws DataAccessException
	 */
    protected int updateDb (final String sql) throws DataAccessException {
		Connection conn  = null;
		Statement stmt   = null;
		int affectedRows = 0;
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			affectedRows = stmt.executeUpdate (sql);			
		} catch (SQLException e) {
		    String error = "An error occurred while executing the sql insert, update or delete statement. " + e.getMessage();
		    logger.error (error);
		    throw new DataAccessException (error);
		} finally {
			this.closeStatement (stmt);
			this.closeConnection (conn);
		}
		return affectedRows;
	}
	
	/**
	 * Executes the given INSERT SQL statement and returns the key as integer.
	 * 
	 * @param sql the sql statement.
	 * @return the key of element inserted.
	 * @throws DataAccessException
	 */
	protected Integer insertDbWithIntegerKey (final String sql) throws DataAccessException {
		Connection conn = null;
		Statement stmt  = null;
		ResultSet rs    = null;
		Integer id      = -1;
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			int affectedRows = stmt.executeUpdate (sql, Statement.RETURN_GENERATED_KEYS);
			if (affectedRows > 0) {
				rs = stmt.getGeneratedKeys();
				if (rs.next()) {
	                id = rs.getInt (1);
	                logger.debug ("\n\nnew integer id [" + id + "]");
	            }
			}			
		} catch (SQLException e) {
		    String error = "An error occurred while executing the sql insert with integer key statement. " + e.getMessage();
		    logger.error (error);
		    throw new DataAccessException (error);
		} finally {
			this.closeResultSet (rs);
			this.closeStatement (stmt);
			this.closeConnection (conn);
		}
		return id;
	}
	
	/**
	 * Executes the given INSERT SQL statement and returns the key as long.
	 * 
	 * @param sql the sql statement.
	 * @return the key of element inserted.
	 * @throws DataAccessException
	 */
	protected Long insertDbWithLongKey (final String sql) throws DataAccessException {
		Connection conn  = null;
		Statement stmt   = null;
		ResultSet rs     = null;
		Long id          = null;
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			int affectedRows = stmt.executeUpdate (sql, Statement.RETURN_GENERATED_KEYS);
			if (affectedRows > 0) {
				rs = stmt.getGeneratedKeys();
				if (rs.next()) {
	                id = rs.getLong (1);
	                logger.debug ("\n\nnew long id [" + id + "]");
	            }
			}			
		} catch (SQLException e) {
		    String error = "An error occurred while executing the sql insert with long key statement. " + e.getMessage();
		    logger.error (error);
		    throw new DataAccessException (error);
		} finally {
			this.closeResultSet (rs);
			this.closeStatement (stmt);
			this.closeConnection (conn);
		}
		return id;
	}
	
	/**
	 * Executes the given SQL statement, which gets the row count of a given table.
	 * 
	 * @param sql the sql statement.
	 * @return the row count.
	 * @throws DataAccessException
	 */
	protected int selectRowCount (final String sql) throws DataAccessException {
		Connection conn = null;
		Statement stmt  = null;
		ResultSet rs    = null;
		int rowCount    = 0;
		try {
			conn = getConnection ();
			stmt = conn.createStatement();
	        rs = stmt.executeQuery (sql);
	        while (rs.next()) {
	        	rowCount = rs.getInt ("COUNT(*)");
			}	        
		} catch (Exception e) {
			String error = "An error occurred while getting the row count. " + e.getMessage();
			logger.error (error);
			throw new DataAccessException (error);
		} finally {
			this.closeResultSet (rs);
			this.closeStatement (stmt);
			this.closeConnection (conn);
		}
		return rowCount;
	}
	
	/**
	 * Tries to close the statement.
	 * 
	 * @param stmt the statement.
	 */
	protected void closeStatement (Statement stmt) {
		try {
	         if (stmt != null) {
	        	 stmt.close(); 
	         }	            
	    } catch (SQLException e) {
	    	logger.error ("An error occurred while closing the statement. " + e.getMessage());
	    }
	}
	
	/**
	 * Tries to close the prepared statement.
	 * 
	 * @param pstmt the prepared statement.
	 */
	protected void closePreparedStatement (PreparedStatement pstmt) {
		try {
	         if (pstmt != null) {
	        	 pstmt.close(); 
	         }	            
	    } catch (SQLException e) {
	    	logger.error ("An error occurred while closing the prepared statement. " + e.getMessage());
	    }
	}
	
	/**
	 * Tries to close the connection.
	 * 
	 * @param conn the connection.
	 */
	protected void closeConnection (Connection conn) {
		try {
	         if (conn != null) {
	        	 conn.close();
	         }
	      }catch (SQLException e){
	         logger.error ("An error occurred while closing the connection. " + e.getMessage());
	    }
	}
	
	/**
	 * Tries to close the resultset.
	 * 
	 * @param rs the resultset.
	 */
	protected void closeResultSet (ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}			
		} catch (SQLException e) {
			logger.error ("An error occurred while closing the resultset. " + e.getMessage());
		}
	}
	
}
