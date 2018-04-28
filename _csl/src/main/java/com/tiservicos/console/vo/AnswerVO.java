/* Copyright TI Serviços 2017 */
package com.tiservicos.console.vo;

import java.util.List;

/**
 * Value Object class for Answer.
 * 
 * @author josivan@tiservicos.com
 *
 */
public class AnswerVO {

	private Long answerId;
	private String answer;
	private String bibliographyReferences;
	private List<AttachmentVO> attachmentVOList;
	
	public Long getAnswerId() {
		return answerId;
	}
	public void setAnswerId(Long answerId) {
		this.answerId = answerId;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getBibliographyReferences() {
		return bibliographyReferences;
	}
	public void setBibliographyReferences(String bibliographyReferences) {
		this.bibliographyReferences = bibliographyReferences;
	}
	public List<AttachmentVO> getAttachmentVOList() {
		return attachmentVOList;
	}
	public void setAttachmentVOList(List<AttachmentVO> attachmentVOList) {
		this.attachmentVOList = attachmentVOList;
	}	
	
}
