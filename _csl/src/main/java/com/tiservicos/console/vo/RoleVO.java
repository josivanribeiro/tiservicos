/* Copyright TI Serviços 2017 */
package com.tiservicos.console.vo;

/**
 * Value Object class for Role.
 * 
 * @author josivan@tiservicos.com
 *
 */
public class RoleVO {

	private Integer roleId;
	private String name;
	private boolean selected;
		
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}	
}
