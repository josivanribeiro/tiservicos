/* Copyright TI Serviços 2017 */
package com.tiservicos.console.services;

/**
 * Root of the hierarchy of business exceptions.
 * 
 * @author josivan@tiservicos.com
 *
 */
public class BusinessException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for BusinessException.
	 * 
	 * @param message the detail message.
	 */
	public BusinessException (String message) {
		super (message);
	}

	/**
	 * Constructor for BusinessException.
	 * 
	 * @param message the detail message.
	 * @param cause the root cause.
	 */
	public BusinessException (String message, Throwable cause) {
		super (message, cause);
	}
}

