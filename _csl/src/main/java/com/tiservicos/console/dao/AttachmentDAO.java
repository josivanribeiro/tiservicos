/* Copyright TI Serviços 2017 */
package com.tiservicos.console.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.tiservicos.console.vo.AnswerVO;
import com.tiservicos.console.vo.AttachmentVO;

import br.com.nw51.common.vo.ClassVO;

/**
 * DAO class for Attachment.
 * 
 * @author josivan@tiservicos.com
 *
 */
public class AttachmentDAO extends AbstractDAO {

	static Logger logger = Logger.getLogger (AttachmentDAO.class.getName());
	
	public AttachmentDAO () {
		
	}
	
	/**
	 * Inserts a new attachment.
	 * 
	 * @param attachmentVO the attachment.
	 * @return the number of affected rows.
	 * @throws DataAccessException
	 */
	public int insert (AttachmentVO attachmentVO) throws DataAccessException {
		Connection conn         = null;
		PreparedStatement pstmt = null;
		String sql              = null;
		int affectedRows = 0;
		try {
			conn = this.getConnection();
			sql = "INSERT INTO ATTACHMENT (ANSWER_ID, NAME, TYPE, SIZE, FILE) VALUES(?,?,?,?,?)";
			pstmt = conn.prepareStatement (sql);
			pstmt.setLong (1, attachmentVO.getAnswerVO().getAnswerId());
			pstmt.setString (2, attachmentVO.getName());
			pstmt.setString (3, attachmentVO.getType());
			pstmt.setString (4, attachmentVO.getSize());
			pstmt.setBinaryStream (5, attachmentVO.getFile());
			affectedRows = pstmt.executeUpdate();			
			logger.debug ("affectedRows: " + affectedRows);
		} catch (SQLException e) {
		    String error = "An error occurred while executing the insert attachment sql statement. " + e.getMessage();
		    logger.error (error);
		    throw new DataAccessException (error);
		} finally {
			this.closePreparedStatement (pstmt);
			this.closeConnection (conn);
		}
		return affectedRows;
	}
	
	/**
	 * Deletes an attachment.
	 * 
	 * @param attachmentVO the attachment.
	 * @return the number of affected rows.
	 * @throws DataAccessException
	 */
	public int delete (AttachmentVO attachmentVO) throws DataAccessException {
		String sql       = null;
		int affectedRows = 0;
		sql = "DELETE FROM ATTACHMENT WHERE ATTACHMENT_ID = " + attachmentVO.getAttachmentId();
		affectedRows = updateDb (sql);
		return affectedRows;
	}
	
	/**
	 * Deletes attachments by answer id.
	 * 
	 * @param answerVO the answer.
	 * @return the number of affected rows.
	 * @throws DataAccessException
	 */
	public int deleteByAnswer (AnswerVO answerVO) throws DataAccessException {
		String sql       = null;
		int affectedRows = 0;
		sql = "DELETE FROM ATTACHMENT WHERE ANSWER_ID = " + answerVO.getAnswerId();
		affectedRows = updateDb (sql);
		return affectedRows;
	}
	
	/**
	 * Find an Attachment by its id.
	 * 
	 * @param attachmentVO the attachment.
	 * @return an attachment.
	 * @throws DataAccessException
	 */
	public AttachmentVO findById (AttachmentVO attachmentVO) throws DataAccessException {
		AttachmentVO foundAttachmentVO = null;
		Connection conn                = null;
		PreparedStatement pstmt        = null;
		ResultSet rs                   = null;
		String sql                     = null;
		try {
			conn = super.getConnection();
			sql = "SELECT ATTACHMENT_ID, ANSWER_ID, NAME, TYPE, SIZE, FILE FROM ATTACHMENT WHERE ATTACHMENT_ID = " + attachmentVO.getAttachmentId();
			pstmt = conn.prepareStatement (sql);
			rs = pstmt.executeQuery();
		    while (rs.next()) {		    	
		    	Long attachmentId = rs.getLong (1);
				Long answerId     = rs.getLong (2);
				String  name      = rs.getString (3);
				String  type      = rs.getString (4);
				String  size      = rs.getString (5);
				InputStream file  = rs.getBinaryStream (6);
						        
				foundAttachmentVO = new AttachmentVO();
				foundAttachmentVO.setAttachmentId (attachmentId);
				AnswerVO answerVO = new AnswerVO ();
				answerVO.setAnswerId(answerId);
				foundAttachmentVO.setAnswerVO(answerVO);
				foundAttachmentVO.setName (name);
				foundAttachmentVO.setType (type);
				foundAttachmentVO.setSize (size);
				foundAttachmentVO.setFile (file);				
		    }
		    
		} catch (SQLException e) {
		    String error = "An error occurred while executing the sql select statement. " + e.getMessage();
		    logger.error (error);
		    throw new DataAccessException (error);
		} finally {
			this.closeResultSet (rs);
			this.closePreparedStatement (pstmt);
			this.closeConnection (conn);
		}
		return foundAttachmentVO;
	}
	
	/**
	 * Finds attachments by answer.
	 * 
	 * @return a list of attachments.
	 * @throws DataAccessException
	 */
	public List<AttachmentVO> findAttachmentsByAnswer (AnswerVO answerVO) throws DataAccessException {
		List<AttachmentVO> attachmentVOList = new ArrayList<AttachmentVO>();
		Connection conn                     = null;
		PreparedStatement pstmt             = null;
		ResultSet rs                        = null;
		String sql                          = null;
		try {
			conn = super.getConnection();
			sql = "SELECT ATTACHMENT_ID, ANSWER_ID, NAME, TYPE, SIZE, FILE FROM ATTACHMENT WHERE ANSWER_ID = ?";
			pstmt = conn.prepareStatement (sql);
			pstmt.setLong (1, answerVO.getAnswerId());
			rs = pstmt.executeQuery();
		    while (rs.next()) {
		    	Long attachmentId = rs.getLong (1);
				Long answerId     = rs.getLong (2);
				String  name      = rs.getString (3);
				String  type      = rs.getString (4);
				String  size      = rs.getString (5);
				InputStream file  = rs.getBinaryStream (6);
						        
				AttachmentVO attachmentVO = new AttachmentVO();
				attachmentVO.setAttachmentId (attachmentId);
				AnswerVO newAnswerVO = new AnswerVO ();
				newAnswerVO.setAnswerId (answerId);
				attachmentVO.setAnswerVO (newAnswerVO);
				attachmentVO.setName (name);
				attachmentVO.setType (type);
				attachmentVO.setSize (size);
				attachmentVO.setFile (file);
								
				attachmentVOList.add (attachmentVO);
		    }
		    
		} catch (SQLException e) {
		    String error = "An error occurred while finding attachments by answer. " + e.getMessage();
		    logger.error (error);
		    throw new DataAccessException (error);
		} finally {
			this.closeResultSet (rs);
			this.closePreparedStatement (pstmt);
			this.closeConnection (conn);
		}
		return attachmentVOList;
	}
	
}
