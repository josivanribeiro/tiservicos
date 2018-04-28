/* Copyright TI Serviços 2017 */
package com.tiservicos.console.vo;

/**
 * Value Object class for Question Category.
 * 
 * @author josivan@tiservicos.com
 *
 */
public class QuestionVO {

	private Long questionId;
	private QuestionCategoryVO questionCategoryVO;
	private UserConsoleVO userConsoleVO;
	private String question;
	private AnswerVO answerVO;
	
	public Long getQuestionId() {
		return questionId;
	}
	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}
	public QuestionCategoryVO getQuestionCategoryVO() {
		return questionCategoryVO;
	}
	public void setQuestionCategoryVO(QuestionCategoryVO questionCategoryVO) {
		this.questionCategoryVO = questionCategoryVO;
	}
	public UserConsoleVO getUserConsoleVO() {
		return userConsoleVO;
	}
	public void setUserConsoleVO(UserConsoleVO userConsoleVO) {
		this.userConsoleVO = userConsoleVO;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public AnswerVO getAnswerVO() {
		return answerVO;
	}
	public void setAnswerVO(AnswerVO answerVO) {
		this.answerVO = answerVO;
	}
	
}
