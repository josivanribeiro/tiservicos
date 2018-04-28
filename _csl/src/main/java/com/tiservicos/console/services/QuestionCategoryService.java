/* Copyright TI Serviços 2017 */
package com.tiservicos.console.services;

import java.util.List;

import org.apache.log4j.Logger;

import com.tiservicos.console.dao.DataAccessException;
import com.tiservicos.console.dao.QuestionCategoryDAO;
import com.tiservicos.console.vo.QuestionCategoryVO;

/**
 * Business Service class for Question Category.
 * 
 * @author josivan@tiservicos.com
 *
 */
public class QuestionCategoryService {

	static Logger logger = Logger.getLogger (QuestionCategoryService.class.getName());
	private QuestionCategoryDAO questionCategoryDAO = new QuestionCategoryDAO();
			
	public QuestionCategoryService() {
		
	}
	
	/**
	 * Inserts a new questionCategory.
	 * 
	 * @param questionCategoryVO the questionCategory.
	 * @return the questionCategory id.
	 * @throws BusinessException
	 */
	public Integer insert (QuestionCategoryVO questionCategoryVO) throws BusinessException {
		Integer questionCategoryId = 0;
		try {
			questionCategoryId = questionCategoryDAO.insert (questionCategoryVO);
			logger.debug ("new questionCategoryId [" + questionCategoryId + "]");			
		} catch (DataAccessException e) {
			String errorMessage = "A business exception error occurred while inserting the questionCategory. " + e.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e.getCause());
		}
		return questionCategoryId;
	}
	
	/**
	 * Updates a questionCategory.
	 * 
	 * @param questionCategoryVO the questionCategory.
	 * @return the number of affected rows.
	 * @throws BusinessException
	 */
	public int update (QuestionCategoryVO questionCategoryVO) throws BusinessException {
		int affectedRows = 0;
		try {
			affectedRows = questionCategoryDAO.update (questionCategoryVO);
			logger.debug ("affectedRows [" + affectedRows + "]");						
		} catch (DataAccessException e) {
			String errorMessage = "A business exception error occurred while updating the questionCategory. " + e.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e.getCause());
		}
		return affectedRows;
	}
	
	/**
	 * Deletes a profile.
	 * 
	 * @param ProfileVO the profile.
	 * @return the number of affected rows.
	 * @throws BusinessException
	 */
	public int delete (QuestionCategoryVO questionCategoryVO) throws BusinessException {
		int affectedRows = 0;
		try {
			affectedRows = questionCategoryDAO.delete (questionCategoryVO);
			logger.info ("affectedRows [" + affectedRows + "]");
		} catch (DataAccessException e) {
			String errorMessage = "A business exception error occurred while deleting the questionCategory. " + e.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e.getCause());
		}
		return affectedRows;
	}
	
	/**
	 * Finds a questionCategory by its id.
	 * 
	 * @param QuestionCategoryVO the questionCategory.
	 * @return the found questionCategory.
	 * @throws BusinessException
	 */
	public QuestionCategoryVO findById (QuestionCategoryVO questionCategoryVO) throws BusinessException {
		QuestionCategoryVO foundQuestionCategoryVO = null;
		try {
			foundQuestionCategoryVO = questionCategoryDAO.findById (questionCategoryVO);			
		} catch (DataAccessException e) {
			String errorMessage = "A business exception error occurred while finding the questionCategory by id. " + e.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e.getCause());
		}
		return foundQuestionCategoryVO;
	}
	
	/**
	 * Finds a duplicated questionCategory name.
	 * 
	 * @param questionCategoryVO the questionCategory.
	 * @return the count of duplicated questionCategory name.
	 * @throws BusinessException
	 */
	public int findDuplicatedName (QuestionCategoryVO questionCategoryVO) throws BusinessException {
		int count = 0;
		try {
			count = questionCategoryDAO.findDuplicatedName (questionCategoryVO);
			logger.debug ("count [" + count + "]");
		} catch (DataAccessException e) {
			String errorMessage = "A business exception error occurred while finding the a duplicated name. " + e.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e.getCause());
		}
		return count;
	}
	
	/**
	 * Finds all the questionCategories.
	 * 
	 * @return a list of questionCategories.
	 * @throws BusinessException
	 */
	public List<QuestionCategoryVO> findAll () throws BusinessException {
		List<QuestionCategoryVO> questionCategoryVOList = null;
		try {
			questionCategoryVOList = questionCategoryDAO.findAll();
		} catch (DataAccessException e) {
			String errorMessage = "A business exception error occurred while finding the all the questionCategories. " + e.getMessage();
			logger.error (errorMessage);
			throw new BusinessException (errorMessage, e.getCause());
		}
		return questionCategoryVOList;
	}		
	
}
