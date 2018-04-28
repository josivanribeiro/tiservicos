/* Copyright TI Serviços 2017 */
package com.tiservicos.console.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tiservicos.console.vo.QuestionCategoryVO;

/**
 * DAO class for Question Category.
 * 
 * @author josivan@tiservicos.com
 *
 */
public class QuestionCategoryDAO extends AbstractDAO {

	static Logger logger = Logger.getLogger (QuestionCategoryDAO.class.getName());
	
	public QuestionCategoryDAO () {
		
	}
	
	/**
	 * Inserts a new question category.
	 * 
	 * @param questionCategoryVO the question category.
	 * @return the questionCategoryId.
	 * @throws DataAccessException
	 */
	public Integer insert (QuestionCategoryVO questionCategoryVO) throws DataAccessException {
		Integer questionCategoryId = 0;
		StringBuilder sbSql = null;
		sbSql = new StringBuilder();
		sbSql.append ("INSERT INTO QUESTION_CATEGORY (QUESTION_CATEGORY_FATHER_ID, NAME, STATUS) ");
		sbSql.append ("VALUES (" +  questionCategoryVO.getQuestionCategoryFather().getQuestionCategoryId() + ", ");
		sbSql.append ("'" + questionCategoryVO.getName() + "', ");
		sbSql.append (questionCategoryVO.isStatus() + ")");
		questionCategoryId = insertDbWithIntegerKey (sbSql.toString());
		return questionCategoryId;
	}
	
	/**
	 * Updates an existing questionCategory.
	 * 
	 * @param questionCategoryVO the questionCategory.
	 * @return the number of affected rows.
	 * @throws DataAccessException
	 */
	public int update (QuestionCategoryVO questionCategoryVO) throws DataAccessException {
		int affectedRows = 0;
		StringBuilder sbSql = null;
		String whereClause  = null;
		sbSql = new StringBuilder();
		sbSql.append ("UPDATE QUESTION_CATEGORY SET ");
		sbSql.append ("QUESTION_CATEGORY_FATHER_ID=");
		sbSql.append (questionCategoryVO.getQuestionCategoryFather().getQuestionCategoryId() + ", ");
		sbSql.append ("NAME=");
		sbSql.append ("'" + questionCategoryVO.getName() + "', ");
		sbSql.append ("STATUS=");
		sbSql.append (questionCategoryVO.isStatus() + " ");
		whereClause = " WHERE QUESTION_CATEGORY_ID = " + questionCategoryVO.getQuestionCategoryId();
		sbSql.append (whereClause);
		affectedRows = updateDb (sbSql.toString());
		return affectedRows;
	}
	
	/**
	 * Deletes a questionCategory.
	 * 
	 * @param questionCategoryVO the questionCategory.
	 * @return the number of affected rows.
	 * @throws DataAccessException
	 */
	public int delete (QuestionCategoryVO questionCategoryVO) throws DataAccessException {
		String sql       = null;
		int affectedRows = 0;
		sql = "DELETE FROM QUESTION_CATEGORY WHERE QUESTION_CATEGORY_ID = " + questionCategoryVO.getQuestionCategoryId();
		affectedRows = updateDb (sql);
		return affectedRows;
	}
	
	/**
	 * Find a questionCategory by its id.
	 * 
	 * @param questionCategoryVO the questionCategory.
	 * @return a questionCategory.
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public QuestionCategoryVO findById (QuestionCategoryVO questionCategoryVO) throws DataAccessException {
		QuestionCategoryVO foundQuestionCategoryVO = null;
		String sql           = null;
		List<Object> rowList = null;
		QuestionCategoryVO questionCategoryFatherVO = null;
		sql = "SELECT QUESTION_CATEGORY_ID, QUESTION_CATEGORY_FATHER_ID, NAME, STATUS FROM QUESTION_CATEGORY WHERE QUESTION_CATEGORY_ID = " + questionCategoryVO.getQuestionCategoryId();
		rowList = selectDb (sql, 4);		
		if (!rowList.isEmpty() && rowList.size() > 0) {			
			for (Object columnList : rowList) {				
				List<Object> columns = (List<Object>)columnList;					
				Long questionCategoryId = (Long) columns.get (0);
				if (columns.get (1) != null) {					
					Long questionCategoryFatherId = (Long) columns.get (1); 
					questionCategoryFatherVO = new QuestionCategoryVO();
					questionCategoryFatherVO.setQuestionCategoryId(questionCategoryFatherId);
				}				
				String name    = (String) columns.get (2);
				Boolean status = (Boolean) columns.get (3);
				
				foundQuestionCategoryVO = new QuestionCategoryVO();
				foundQuestionCategoryVO.setQuestionCategoryId(questionCategoryId);
				if (questionCategoryFatherVO != null) {
					foundQuestionCategoryVO.setQuestionCategoryFather(questionCategoryFatherVO);
				}
				foundQuestionCategoryVO.setName(name);
				foundQuestionCategoryVO.setStatus(status);				
			}
		}
		return foundQuestionCategoryVO;
	}
	
	/**
	 * Finds a duplicated questionCategory name.
	 * 
	 * @param questionCategoryVO the questionCategory.
	 * @return the count of duplicated profile name.
	 * @throws DataAccessException
	 */
	public int findDuplicatedName (QuestionCategoryVO questionCategoryVO) throws DataAccessException {
		String sql = null;
		String whereClause = "";
		int count = 0;
		if (questionCategoryVO.getQuestionCategoryId() != null && questionCategoryVO.getQuestionCategoryId() > 0) {
			whereClause = " AND QUESTION_CATEGORY_ID <> " + questionCategoryVO.getQuestionCategoryId();
		}
		sql = "SELECT COUNT(*) FROM QUESTION_CATEGORY WHERE NAME = '" + questionCategoryVO.getName() + "'" + whereClause;
		count = selectRowCount (sql);
		return count;
	}
	
	/**
	 * Finds all the questionCategories.
	 * 
	 * @return a list of categories.
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<QuestionCategoryVO> findAll () throws DataAccessException {
		String sql            = null;
		List<Object> rowList  = null;
		List<QuestionCategoryVO> questionCategoryVOList = new ArrayList<QuestionCategoryVO>();
				
		sql = "SELECT QC.QUESTION_CATEGORY_ID, "
				+ "(SELECT QCF.NAME "
				+ "FROM QUESTION_CATEGORY QCF " 
				+ "WHERE QCF.QUESTION_CATEGORY_ID = QC.QUESTION_CATEGORY_FATHER_ID) " 
			+ "AS FATHER_NAME, "
		    + "QC.NAME, QC.STATUS "
		    + "FROM QUESTION_CATEGORY QC "
		    + "ORDER BY QC.QUESTION_CATEGORY_ID";		
		
		rowList = selectDb (sql, 4);
		if (!rowList.isEmpty() && rowList.size() > 0) {
			for (Object columnList : rowList) {				
				List<Object> columns = (List<Object>)columnList;				
				QuestionCategoryVO questionCategoryFatherVO = null;
				Long questionCategoryId = (Long) columns.get (0);
				if (columns.get (1) != null) {					
					String questionCategoryFatherName = (String) columns.get (1); 
					questionCategoryFatherVO = new QuestionCategoryVO();
					questionCategoryFatherVO.setName(questionCategoryFatherName);
				}
				String name    = (String) columns.get (2);
				Boolean status = (Boolean) columns.get (3);
				
				QuestionCategoryVO questionCategoryVO = new QuestionCategoryVO();
				questionCategoryVO.setQuestionCategoryId(questionCategoryId);
				if (questionCategoryFatherVO != null) {
					questionCategoryVO.setQuestionCategoryFather(questionCategoryFatherVO);
				}
				questionCategoryVO.setName(name);
				questionCategoryVO.setStatus(status);
										
				questionCategoryVOList.add (questionCategoryVO);
			}
		}
		return questionCategoryVOList;
	}	
	
}
