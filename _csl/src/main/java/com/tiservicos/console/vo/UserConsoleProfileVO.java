/* Copyright TI Serviços 2017 */
package com.tiservicos.console.vo;

/**
 * Value Object class for UserConsoleProfile.
 * 
 * @author josivan@tiservicos.com
 *
 */
public class UserConsoleProfileVO {

	private UserConsoleVO userConsoleVO;
	private ProfileVO profileVO;
		
	public UserConsoleVO getUserConsoleVO() {
		return userConsoleVO;
	}
	public void setUserConsoleVO(UserConsoleVO userConsoleVO) {
		this.userConsoleVO = userConsoleVO;
	}
	public ProfileVO getProfileVO() {
		return profileVO;
	}
	public void setProfileVO(ProfileVO profileVO) {
		this.profileVO = profileVO;
	}	
	
}
