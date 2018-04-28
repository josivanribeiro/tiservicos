/* Copyright TI Serviços 2017 */
package com.tiservicos.console.vo;

import java.util.List;

/**
 * Value Object class for Question Category.
 * 
 * @author josivan@tiservicos.com
 *
 */
public class QuestionCategoryVO {

	private Long questionCategoryId;
	private QuestionCategoryVO questionCategoryFather;
	private String name;
	private List<QuestionCategoryVO> questionCategoryVOList;
	private boolean status;
	
	public Long getQuestionCategoryId() {
		return questionCategoryId;
	}
	public void setQuestionCategoryId(Long questionCategoryId) {
		this.questionCategoryId = questionCategoryId;
	}
	public QuestionCategoryVO getQuestionCategoryFather() {
		return questionCategoryFather;
	}
	public void setQuestionCategoryFather(QuestionCategoryVO questionCategoryFather) {
		this.questionCategoryFather = questionCategoryFather;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<QuestionCategoryVO> getQuestionCategoryVOList() {
		return questionCategoryVOList;
	}
	public void setQuestionCategoryVOList(List<QuestionCategoryVO> questionCategoryVOList) {
		this.questionCategoryVOList = questionCategoryVOList;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}	
	
}
