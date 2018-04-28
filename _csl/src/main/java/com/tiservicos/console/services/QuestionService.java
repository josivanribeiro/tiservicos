/* Copyright TI Serviços 2017 */
package com.tiservicos.console.services;

import java.util.List;

import org.apache.log4j.Logger;

import com.tiservicos.console.dao.AnswerDAO;
import com.tiservicos.console.dao.AttachmentDAO;
import com.tiservicos.console.dao.DataAccessException;
import com.tiservicos.console.dao.QuestionDAO;
import com.tiservicos.console.vo.AnswerVO;
import com.tiservicos.console.vo.AttachmentVO;
import com.tiservicos.console.vo.QuestionCategoryVO;
import com.tiservicos.console.vo.QuestionVO;

/**
 * Business Service class for Question.
 * 
 * @author josivan@tiservicos.com
 *
 */
public class QuestionService {

	static Logger logger = Logger.getLogger (QuestionService.class.getName());
	private QuestionDAO questionDAO = new QuestionDAO();
	private AnswerDAO answerDAO = new AnswerDAO();
	private AttachmentDAO attachmentDAO = new AttachmentDAO();
			
	public QuestionService() {
		
	}
	
	/**
	 * Inserts a new question.
	 * 
	 * @param questionVO the question.
	 * @return the question id.
	 * @throws BusinessException
	 */
	public Long insert (QuestionVO questionVO) throws BusinessException {
		Long questionId = null;
		Long answerId   = null;
		
		try {
			answerId = answerDAO.insert (questionVO.getAnswerVO());
			logger.debug ("new answerId [" + answerId + "]");			
		} catch (DataAccessException e) {
			String errorMessage = "A business exception error occurred while inserting the answer. " + e.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e.getCause());
		}
		
		if (answerId != null && answerId > 0) {
						
			try {
				for (AttachmentVO attachmentVO : questionVO.getAnswerVO().getAttachmentVOList()) {
					AnswerVO answerVO = new AnswerVO();
					answerVO.setAnswerId(answerId);
					attachmentVO.setAnswerVO(answerVO);
					attachmentDAO.insert(attachmentVO);
				}
			} catch (DataAccessException e) {
				String errorMessage = "A business exception error occurred while inserting the attachment. " + e.getMessage();
				logger.error (errorMessage);
				throw new BusinessException (errorMessage, e.getCause());
			}			
			
			try {
				questionId = questionDAO.insert (questionVO);
				logger.debug ("new questionId [" + questionId + "]");			
			} catch (DataAccessException e) {
				String errorMessage = "A business exception error occurred while inserting the question. " + e.getMessage();
				logger.error (errorMessage);
				throw new BusinessException (errorMessage, e.getCause());
			}			
		}		
		
		return questionId;
	}
	
	/**
	 * Updates a question.
	 * 
	 * @param questionVO the question.
	 * @return the number of affected rows.
	 * @throws BusinessException
	 */
	public int update (QuestionVO questionVO) throws BusinessException {
		int answerAffectedRows = 0;
		int questionAffectedRows = 0;
		
		try {
			attachmentDAO.deleteByAnswer (questionVO.getAnswerVO());
		} catch (DataAccessException e1) {
			String errorMessage = "A business exception error occurred while deleting the attachments. " + e1.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e1.getCause());
		}
		
		try {
			for (AttachmentVO attachmentVO : questionVO.getAnswerVO().getAttachmentVOList()) {
				attachmentDAO.insert(attachmentVO);
			}
		} catch (DataAccessException e1) {
			String errorMessage = "A business exception error occurred while deleting the attachments. " + e1.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e1.getCause());
		}		
		
		try {
			answerAffectedRows = answerDAO.update (questionVO.getAnswerVO());
			logger.debug ("answerAffectedRows [" + answerAffectedRows + "]");						
		} catch (DataAccessException e) {
			String errorMessage = "A business exception error occurred while updating the answer. " + e.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e.getCause());
		}
		
		try {
			questionAffectedRows = questionDAO.update (questionVO);
			logger.debug ("questionAffectedRows [" + questionAffectedRows + "]");						
		} catch (DataAccessException e) {
			String errorMessage = "A business exception error occurred while updating the question. " + e.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e.getCause());
		}
		return questionAffectedRows;
	}
	
	/**
	 * Deletes a question.
	 * 
	 * @param questionVO the question.
	 * @return the number of affected rows.
	 * @throws BusinessException
	 */
	public int delete (QuestionVO questionVO) throws BusinessException {
		int affectedRows = 0;
		try {
			affectedRows = questionDAO.delete (questionVO);
			logger.info ("affectedRows [" + affectedRows + "]");
		} catch (DataAccessException e) {
			String errorMessage = "A business exception error occurred while deleting the question/answer. " + e.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e.getCause());
		}
		return affectedRows;
	}
	
	/**
	 * Finds a question by its id.
	 * 
	 * @param QuestionVO the question.
	 * @return the found question.
	 * @throws BusinessException
	 */
	public QuestionVO findById (QuestionVO questionVO) throws BusinessException {
		QuestionVO foundQuestionVO = null;
		AnswerVO foundAnswerVO     = null;
		try {
			foundQuestionVO = questionDAO.findById (questionVO);			
		} catch (DataAccessException e) {
			String errorMessage = "A business exception error occurred while finding the question by id. " + e.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e.getCause());
		}		
		if (foundQuestionVO != null && foundQuestionVO.getAnswerVO() != null) {
			try {
				foundAnswerVO = answerDAO.findById (questionVO.getAnswerVO());
				foundQuestionVO.setAnswerVO(foundAnswerVO);				
			} catch (DataAccessException e) {
				String errorMessage = "A business exception error occurred while finding the answer by id. " + e.getMessage();
				logger.error (errorMessage);
				throw new BusinessException (errorMessage, e.getCause());
			}
		}		
		return foundQuestionVO;
	}
	
	/**
	 * Finds all the questions.
	 * 
	 * @return a list of questions.
	 * @throws BusinessException
	 */
	public List<QuestionVO> findAll () throws BusinessException {
		List<QuestionVO> questionVOList = null;
		try {
			questionVOList = questionDAO.findAll();
		} catch (DataAccessException e) {
			String errorMessage = "A business exception error occurred while finding the all the questions. " + e.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e.getCause());
		}
		return questionVOList;
	}		
	
}
