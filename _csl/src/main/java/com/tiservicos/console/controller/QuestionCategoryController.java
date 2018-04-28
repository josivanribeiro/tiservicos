/* Copyright TI Serviços 2017 */
package com.tiservicos.console.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import com.tiservicos.console.services.BusinessException;
import com.tiservicos.console.services.QuestionCategoryService;
import com.tiservicos.console.util.Utils;
import com.tiservicos.console.vo.QuestionCategoryVO;

/**
 * QuestionCategory Controller.
 * 
 * @author josivan@tiservicos.com
 *
 */
@ManagedBean
@ApplicationScoped
public class QuestionCategoryController extends AbstractController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger (QuestionCategoryController.class.getName());
	private QuestionCategoryService questionCategoryService = new QuestionCategoryService();
	private List<QuestionCategoryVO> questionCategoryVOList;
	private Long questionCategoryIdForm;
	private Long questionCategoryFatherIdForm;
	private String nameForm;
	private boolean statusForm = true;
	
	private Map<String,Object> questionCategoryFatherIdMap;
		
	public List<QuestionCategoryVO> getQuestionCategoryVOList() {
		return questionCategoryVOList;
	}

	public void setQuestionCategoryVOList(List<QuestionCategoryVO> questionCategoryVOList) {
		this.questionCategoryVOList = questionCategoryVOList;
	}

	public Long getQuestionCategoryIdForm() {
		return questionCategoryIdForm;
	}

	public void setQuestionCategoryIdForm(Long questionCategoryIdForm) {
		this.questionCategoryIdForm = questionCategoryIdForm;
	}

	public Long getQuestionCategoryFatherIdForm() {
		return questionCategoryFatherIdForm;
	}

	public void setQuestionCategoryFatherIdForm(Long questionCategoryFatherIdForm) {
		this.questionCategoryFatherIdForm = questionCategoryFatherIdForm;
	}

	public String getNameForm() {
		return nameForm;
	}

	public void setNameForm(String nameForm) {
		this.nameForm = nameForm;
	}

	public boolean isStatusForm() {
		return statusForm;
	}

	public void setStatusForm(boolean statusForm) {
		this.statusForm = statusForm;
	}
	
	public Map<String, Object> getQuestionCategoryFatherIdMap() {
		return questionCategoryFatherIdMap;
	}

	public void setQuestionCategoryFatherIdMap(Map<String, Object> questionCategoryFatherIdMap) {
		this.questionCategoryFatherIdMap = questionCategoryFatherIdMap;
	}

	@PostConstruct
    public void init() {
		super.checkReadOnly ("papel_categoria_pergunta_editar");
		logger.debug ("isReadOnly(): " + super.isReadOnly());
		initQuestionCategoryFatherSelect();
    }
	
	/**
	 * Finds all the questionCategories.
	 */
	public void findAll () {
		questionCategoryVOList = new ArrayList<QuestionCategoryVO>();
		try {
			questionCategoryVOList = questionCategoryService.findAll ();
			logger.debug ("questionCategoryVOList.size() [" + questionCategoryVOList.size() + "]");
		} catch (BusinessException e) {
			String error = "An error occurred while find all the questionCategories. " + e.getMessage();
			logger.error (error);
		}
	}
	
	/**
	 * Redirects to add questionCategory page.
	 */
	public String goToAddQuestionCategory() {
	    String toPage = "updateQuestionCategory?faces-redirect=true";
	    logger.debug ("Starting goToAddQuestionCategory method");
	    initQuestionCategoryFatherSelect();
	    return toPage;
	}
	
	/**
	 * Redirects to questionCategories page.
	 */
	public String goToQuestionCategories() {
	    String toPage = "questionCategories?faces-redirect=true";
	    this.findAll();
	    logger.debug ("Starting goToQuestionCategories method");
	    return toPage;
	}
	
	/**
	 * Performs the questionCategory save operation.
	 */
	public void save (ActionEvent actionEvent) {
		if (isValidForm ()) { 
			QuestionCategoryVO questionCategoryVO = new QuestionCategoryVO ();
			questionCategoryVO.setQuestionCategoryId(this.questionCategoryIdForm);
			QuestionCategoryVO questionCategoryFatherVO = new QuestionCategoryVO ();
			questionCategoryFatherVO.setQuestionCategoryId(questionCategoryFatherIdForm);
			questionCategoryVO.setQuestionCategoryFather(questionCategoryFatherVO);
			questionCategoryVO.setName (this.nameForm);
			questionCategoryVO.setStatus(statusForm);
			if (!this.isDuplicatedName (questionCategoryVO)) {
				try {
					if (this.questionCategoryIdForm != null && this.questionCategoryIdForm > 0) {
						int affectedRows = questionCategoryService.update (questionCategoryVO);
						if (affectedRows > 0) {
							super.addInfoMessage ("formUpdateQuestionCategory:messagesUpdateQuestionCategory", " Categoria de pergunta atualizada com sucesso.");
							logger.info ("The questionCategory [" + questionCategoryVO.getName() + "] has been successfully updated.");						
						}
					} else {					
						int affectedRows = questionCategoryService.insert (questionCategoryVO);
						if (affectedRows > 0) {
							super.addInfoMessage ("formUpdateQuestionCategory:messagesUpdateQuestionCategory", " Categoria de pergunta adicionada com sucesso.");
							logger.info ("The questionCategory [" + questionCategoryVO.getName() + "] has been successfully inserted.");
						}
					}
				} catch (BusinessException e) {
					String error = "An error occurred while saving or updating the questionCategory. " + e.getMessage();
					logger.error (error);
				}
				this.resetForm();
			}
		}
	}
		
	/**
	 * Performs the questionCategory delete operation.
	 */
	public void delete (ActionEvent actionEvent) {
		logger.debug ("Starting delete");
		if (this.questionCategoryIdForm != null && this.questionCategoryIdForm > 0) {
			QuestionCategoryVO questionCategoryVO = new QuestionCategoryVO ();
			questionCategoryVO.setQuestionCategoryId (this.questionCategoryIdForm);
			questionCategoryVO.setName (this.nameForm);
			try {
				int affectedRows = questionCategoryService.delete (questionCategoryVO);
				if (affectedRows > 0) {
					super.addInfoMessage ("formUpdateQuestionCategory:messagesUpdateQuestionCategory", " Categoria de pergunta excluída com sucesso.");
					logger.info ("The role [" + questionCategoryVO.getName() + "] has been successfully deleted.");						
				}
			} catch (BusinessException e) {
				String error = "An error occurred while deleting the questionCategory. " + e.getMessage();
				logger.error (error);
			}
			this.resetForm();
		}				
	}	
	
	/**
	 * Loads a questionCategory according with the specified id.
	 */
	public void loadById () {
		logger.debug ("Starting loadById");
		QuestionCategoryVO foundQuestionCategoryVO = null;
		if (this.questionCategoryIdForm != null && this.questionCategoryIdForm > 0) {
			QuestionCategoryVO questionCategoryVO = new QuestionCategoryVO ();
			questionCategoryVO.setQuestionCategoryId(this.questionCategoryIdForm);
	        try {
	        	foundQuestionCategoryVO = this.questionCategoryService.findById (questionCategoryVO);
				this.questionCategoryIdForm = foundQuestionCategoryVO.getQuestionCategoryId();
				this.questionCategoryFatherIdForm = foundQuestionCategoryVO.getQuestionCategoryFather().getQuestionCategoryId(); 
				this.nameForm = foundQuestionCategoryVO.getName();
				
				initQuestionCategoryFatherSelect();
				
				logger.debug ("this.questionCategoryIdForm " + this.questionCategoryIdForm);
				logger.debug ("foundQuestionCategoryVO.getName() " + foundQuestionCategoryVO.getName());
			} catch (BusinessException e) {
				String error = "An error occurred while finding the questionCategory by id. " + e.getMessage();
				logger.error (error);
			}
		}
    }	
	
	/**
	 * Checks if the role is duplicated.
	 * 
	 * @param questionCategoryVO the role containing the name.
	 * @return the result operation.
	 */
	private boolean isDuplicatedName (QuestionCategoryVO questionCategoryVO) {
		boolean isDuplicated = false;
		try {
			int numberDuplicatedName = questionCategoryService.findDuplicatedName (questionCategoryVO);
			isDuplicated = (numberDuplicatedName > 0) ? true : false; 
			if (isDuplicated) {
				super.addWarnMessage ("formUpdateQuestionCategory:messagesUpdateQuestionCategory", " Nome já existente, preencha outro nome.");
				logger.warn ("isDuplicatedName: " + isDuplicated);
			}
		} catch (BusinessException e) {
			String error = "An error occurred while finding a duplicated name. " + e.getMessage();
			logger.error (error);
		}
		return isDuplicated;		
	}
	
	/**
	 * Validates the role insert/update form.
	 * 
	 */
	private boolean isValidForm () {						
		if (!Utils.isNonEmpty (this.nameForm)) {			
			super.addWarnMessage ("formUpdateQuestionCategory:messagesUpdateQuestionCategory", " Preencha o campo nome.");
			return false;			
		}
		return true;
    }
	
	/**
	 * Resets the insert/update form.
	 */
	private void resetForm () {
		this.questionCategoryIdForm       = null;
		this.questionCategoryFatherIdForm = null;
		this.nameForm                     = "";
		this.statusForm                   = true;
		this.questionCategoryFatherIdMap  = null;
	}
	
	/**
	 * Initializes the questionCategoryFather select.
	 */
	private void initQuestionCategoryFatherSelect() {
		logger.debug("\n\nStarting initQuestionCategoryFatherSelect()\n\n");
		List<QuestionCategoryVO> questionCategoryVOList = null;
		questionCategoryFatherIdMap = new LinkedHashMap<String,Object>();
		questionCategoryFatherIdMap.put ("", ""); //label, value
		try {
			questionCategoryVOList = this.questionCategoryService.findAll();			
			if (questionCategoryVOList != null && questionCategoryVOList.size() > 0) {				
				for (QuestionCategoryVO questionCategoryVO : questionCategoryVOList) {
					questionCategoryFatherIdMap.put (questionCategoryVO.getName(), questionCategoryVO.getQuestionCategoryId());
				}
			}
		} catch (BusinessException e) {
			String error = "An error occurred while finding all the question categories. " + e.getMessage();
			logger.error (error);
		}
		logger.debug("\n\nFinishing initQuestionCategoryFatherSelect()\n\n");
	}
	
}
