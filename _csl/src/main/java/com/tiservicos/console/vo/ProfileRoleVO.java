/* Copyright TI Serviços 2017 */
package com.tiservicos.console.vo;

/**
 * Value Object class for ProfileRole.
 * 
 * @author josivan@tiservicos.com
 *
 */
public class ProfileRoleVO {

	private ProfileVO profileVO;
	private RoleVO roleVO;
	
	public ProfileVO getProfileVO() {
		return profileVO;
	}
	public void setProfileVO(ProfileVO profileVO) {
		this.profileVO = profileVO;
	}
	public RoleVO getRoleVO() {
		return roleVO;
	}
	public void setRoleVO(RoleVO roleVO) {
		this.roleVO = roleVO;
	}
	
}
