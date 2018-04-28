/* Copyright TI Serviços 2017 */
package com.tiservicos.console.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tiservicos.console.vo.AnswerVO;
import com.tiservicos.console.vo.QuestionCategoryVO;
import com.tiservicos.console.vo.QuestionVO;
import com.tiservicos.console.vo.UserConsoleVO;

/**
 * DAO class for Question.
 * 
 * @author josivan@tiservicos.com
 *
 */
public class QuestionDAO extends AbstractDAO {

	static Logger logger = Logger.getLogger (QuestionDAO.class.getName());
	
	public QuestionDAO () {
		
	}
	
	/**
	 * Inserts a new question.
	 * 
	 * @param questionVO the question.
	 * @return the questionId.
	 * @throws DataAccessException
	 */
	public Long insert (QuestionVO questionVO) throws DataAccessException {
		Long questionId = null;
		StringBuilder sbSql = null;
		sbSql = new StringBuilder();
		sbSql.append ("INSERT INTO QUESTION (QUESTION_CATEGORY_ID, USER_CONSOLE_ID, QUESTION, ANSWER_ID) ");
		sbSql.append ("VALUES (" +  questionVO.getQuestionCategoryVO().getQuestionCategoryId() + ", ");
		sbSql.append (questionVO.getUserConsoleVO().getUserConsoleId() + ", ");
		sbSql.append ("'" + questionVO.getQuestion() + "', ");
		sbSql.append (questionVO.getAnswerVO().getAnswerId() + ")");
		questionId = insertDbWithLongKey (sbSql.toString());
		return questionId;
	}
	
	/**
	 * Updates an existing question.
	 * 
	 * @param questionVO the question.
	 * @return the number of affected rows.
	 * @throws DataAccessException
	 */
	public int update (QuestionVO questionVO) throws DataAccessException {
		int affectedRows = 0;
		StringBuilder sbSql = null;
		String whereClause  = null;
		sbSql = new StringBuilder();
		sbSql.append ("UPDATE QUESTION SET ");
		sbSql.append ("QUESTION_CATEGORY_ID=");
		sbSql.append (questionVO.getQuestionCategoryVO().getQuestionCategoryId() + ", ");
		sbSql.append ("USER_CONSOLE_ID=");
		sbSql.append ("'" + questionVO.getUserConsoleVO().getUserConsoleId() + ", ");
		sbSql.append ("QUESTION=");
		sbSql.append ("'" + questionVO.getQuestion() + "', ");
		sbSql.append ("ANSWER_ID=");
		sbSql.append (questionVO.getAnswerVO().getAnswerId() + " ");
		whereClause = " WHERE QUESTION_ID = " + questionVO.getQuestionId();
		sbSql.append (whereClause);
		affectedRows = updateDb (sbSql.toString());
		return affectedRows;
	}
	
	/**
	 * Deletes a question.
	 * 
	 * @param questionVO the question.
	 * @return the number of affected rows.
	 * @throws DataAccessException
	 */
	public int delete (QuestionVO questionVO) throws DataAccessException {
		String sql       = null;
		int affectedRows = 0;
		sql = "DELETE FROM QUESTION WHERE QUESTION_ID = " + questionVO.getQuestionId();
		affectedRows = updateDb (sql);
		return affectedRows;
	}
	
	/**
	 * Finds a question by its id.
	 * 
	 * @param questionVO the question.
	 * @return a question.
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public QuestionVO findById (QuestionVO questionVO) throws DataAccessException {
		QuestionVO foundQuestionVO = null;
		String sql                 = null;
		List<Object> rowList       = null;
		sql = "SELECT QUESTION_ID, QUESTION_CATEGORY_ID, USER_CONSOLE_ID, QUESTION, ANSWER_ID FROM QUESTION WHERE QUESTION_ID = " + questionVO.getQuestionId();
		rowList = selectDb (sql, 5);		
		if (!rowList.isEmpty() && rowList.size() > 0) {			
			for (Object columnList : rowList) {				
				List<Object> columns    = (List<Object>)columnList;					
				Long questionId         = (Long) columns.get (0);
				Long questionCategoryId = (Long) columns.get (1);
				Integer userConsoleId   = (Integer) columns.get (2);
				String question         = (String) columns.get (3);
				Long answerId           = (Long) columns.get (4);
				
				foundQuestionVO = new QuestionVO();
				foundQuestionVO.setQuestionId(questionId);
				
				QuestionCategoryVO questionCategoryVO = new QuestionCategoryVO();
				questionCategoryVO.setQuestionCategoryId(questionCategoryId);
				foundQuestionVO.setQuestionCategoryVO(questionCategoryVO);
				
				UserConsoleVO userConsoleVO = new UserConsoleVO();
				userConsoleVO.setUserConsoleId(userConsoleId);
				foundQuestionVO.setUserConsoleVO(userConsoleVO);
				
				foundQuestionVO.setQuestion(question);
				
				AnswerVO answerVO = new AnswerVO();
				answerVO.setAnswerId(answerId);
				foundQuestionVO.setAnswerVO(answerVO);				
			}
		}
		return foundQuestionVO;
	}
	
	/**
	 * Finds all the questions.
	 * 
	 * @return a list with all questions.
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<QuestionVO> findAll () throws DataAccessException {
		List<QuestionVO> questionVOList = new ArrayList<QuestionVO>();
		String sql                      = null;
		List<Object> rowList            = null;
		sql = "SELECT QUESTION_ID, QUESTION_CATEGORY_ID, USER_CONSOLE_ID, QUESTION, ANSWER_ID "
			+ "FROM QUESTION ORDER BY QUESTION_ID";
		rowList = selectDb (sql, 5);		
		if (!rowList.isEmpty() && rowList.size() > 0) {			
			for (Object columnList : rowList) {				
				List<Object> columns    = (List<Object>)columnList;					
				Long questionId         = (Long) columns.get (0);
				Long questionCategoryId = (Long) columns.get (1);
				Integer userConsoleId   = (Integer) columns.get (2);
				String question         = (String) columns.get (3);
				Long answerId           = (Long) columns.get (4);
				
				QuestionVO foundQuestionVO = new QuestionVO();
				foundQuestionVO.setQuestionId(questionId);
				
				QuestionCategoryVO questionCategoryVO = new QuestionCategoryVO();
				questionCategoryVO.setQuestionCategoryId(questionCategoryId);
				foundQuestionVO.setQuestionCategoryVO(questionCategoryVO);
				
				UserConsoleVO userConsoleVO = new UserConsoleVO();
				userConsoleVO.setUserConsoleId(userConsoleId);
				foundQuestionVO.setUserConsoleVO(userConsoleVO);
				
				foundQuestionVO.setQuestion(question);
				
				AnswerVO answerVO = new AnswerVO();
				answerVO.setAnswerId(answerId);
				foundQuestionVO.setAnswerVO(answerVO);
				
				questionVOList.add(foundQuestionVO);				
			}
		}
		return questionVOList;
	}
	
}
