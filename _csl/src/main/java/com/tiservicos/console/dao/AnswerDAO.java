/* Copyright TI Serviços 2017 */
package com.tiservicos.console.dao;

import java.util.List;

import org.apache.log4j.Logger;

import com.tiservicos.console.vo.AnswerVO;

/**
 * DAO class for Answer.
 * 
 * @author josivan@tiservicos.com
 *
 */
public class AnswerDAO extends AbstractDAO {

	static Logger logger = Logger.getLogger (AnswerDAO.class.getName());
	
	public AnswerDAO () {
		
	}
	
	/**
	 * Inserts a new answer.
	 * 
	 * @param answerVO the answer.
	 * @return the answerId.
	 * @throws DataAccessException
	 */
	public Long insert (AnswerVO answerVO) throws DataAccessException {
		Long answerId = null;
		StringBuilder sbSql = null;
		sbSql = new StringBuilder();
		sbSql.append ("INSERT INTO ANSWER (ANSWER, BIBLIOGRAPHY_REFERENCES) ");
		sbSql.append ("VALUES ('" +  answerVO.getAnswer() + "', ");
		sbSql.append ("'" + answerVO.getBibliographyReferences() + "')");
		answerId = insertDbWithLongKey (sbSql.toString());
		return answerId;
	}
	
	/**
	 * Updates an existing answer.
	 * 
	 * @param answerVO the answer.
	 * @return the number of affected rows.
	 * @throws DataAccessException
	 */
	public int update (AnswerVO answerVO) throws DataAccessException {
		int affectedRows = 0;
		StringBuilder sbSql = null;
		String whereClause  = null;
		sbSql = new StringBuilder();
		sbSql.append ("UPDATE ANSWER SET ");
		sbSql.append ("ANSWER=");
		sbSql.append ("'" + answerVO.getAnswer() + "', ");
		sbSql.append ("BIBLIOGRAPHY_REFERENCES=");
		sbSql.append ("'" + answerVO.getBibliographyReferences() + "' ");
		whereClause = " WHERE ANSWER_ID = " + answerVO.getAnswerId();
		sbSql.append (whereClause);
		affectedRows = updateDb (sbSql.toString());
		return affectedRows;
	}
	
	/**
	 * Deletes an answer.
	 * 
	 * @param answerVO the answer.
	 * @return the number of affected rows.
	 * @throws DataAccessException
	 */
	public int delete (AnswerVO answerVO) throws DataAccessException {
		String sql       = null;
		int affectedRows = 0;
		sql = "DELETE FROM ANSWER WHERE ANSWER_ID = " + answerVO.getAnswerId();
		affectedRows = updateDb (sql);
		return affectedRows;
	}
	
	/**
	 * Find an answer by its id.
	 * 
	 * @param answerVO the answer.
	 * @return a answer.
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public AnswerVO findById (AnswerVO answerVO) throws DataAccessException {
		AnswerVO foundAnswerVO = null;
		String sql             = null;
		List<Object> rowList   = null;
		sql = "SELECT ANSWER_ID, ANSWER, BIBLIOGRAPHY_REFERENCES FROM ANSWER WHERE ANSWER_ID = " + answerVO.getAnswerId();
		rowList = selectDb (sql, 3);		
		if (!rowList.isEmpty() && rowList.size() > 0) {			
			for (Object columnList : rowList) {				
				List<Object> columns = (List<Object>)columnList;					
				Long answerId  = (Long) columns.get (0);
				String answer  = (String) columns.get (1);
				String bibliographyReferences = (String) columns.get (2);
				
				foundAnswerVO = new AnswerVO();
				foundAnswerVO.setAnswerId(answerId);
				foundAnswerVO.setAnswer(answer);
				foundAnswerVO.setBibliographyReferences(bibliographyReferences);								
			}
		}
		return foundAnswerVO;
	}		
	
}
