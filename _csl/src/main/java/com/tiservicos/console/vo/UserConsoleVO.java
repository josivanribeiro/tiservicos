/* Copyright TI Serviços 2017 */
package com.tiservicos.console.vo;

import java.io.Serializable;
import java.util.List;

/**
 * VO class for UserConsole.
 * 
 * @author josivan@tiservicos.com
 *
 */
public class UserConsoleVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer userConsoleId;
	private String email;
	private String pwd;
	private boolean status;
	private List<UserConsoleProfileVO> userConsoleProfileVOList;
	
	public Integer getUserConsoleId() {
		return userConsoleId;
	}
	public void setUserConsoleId(Integer userConsoleId) {
		this.userConsoleId = userConsoleId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public List<UserConsoleProfileVO> getUserConsoleProfileVOList() {
		return userConsoleProfileVOList;
	}
	public void setUserConsoleProfileVOList(List<UserConsoleProfileVO> userConsoleProfileVOList) {
		this.userConsoleProfileVOList = userConsoleProfileVOList;
	}	
	
}
