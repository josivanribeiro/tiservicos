/* Copyright TI Serviços 2017 */
package com.tiservicos.console.vo;

import java.io.InputStream;

/**
 * Value Object class for Attachment.
 * 
 * @author josivan@tiservicos.com
 *
 */
public class AttachmentVO {

	private Long attachmentId;
	private AnswerVO answerVO;
	private String name;
	private String type;
	private String size;
	private InputStream file;
	
	public Long getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(Long attachmentId) {
		this.attachmentId = attachmentId;
	}
	public AnswerVO getAnswerVO() {
		return answerVO;
	}
	public void setAnswerVO(AnswerVO answerVO) {
		this.answerVO = answerVO;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public InputStream getFile() {
		return file;
	}
	public void setFile(InputStream file) {
		this.file = file;
	}		
	
}
