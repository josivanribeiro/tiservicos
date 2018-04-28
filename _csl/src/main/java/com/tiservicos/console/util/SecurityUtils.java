package com.tiservicos.console.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.tiservicos.console.controller.LoginController;
import com.tiservicos.console.vo.ProfileRoleVO;
import com.tiservicos.console.vo.UserConsoleProfileVO;
import com.tiservicos.console.vo.UserConsoleVO;

/**
 * Security Utils helper class.
 * 
 * @author Josivan Silva
 *
 */
public class SecurityUtils {
	
	static Logger logger = Logger.getLogger (SecurityUtils.class.getName());
	
	public static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})";
	
	/**
	 * Validates the password.
	 * 
	 * @param pwd the password.
	 * @return
	 */
	public static boolean isValidPwd (String pwd) {
		return pwd.matches (PASSWORD_PATTERN);
	}
	
	/**
	 * Performs the user logout.
	 */
	public static void logout (ServletRequest request) {
		HttpSession session = ((HttpServletRequest) request).getSession (false);
		if (session != null) {
		    session.invalidate();
		}
	}
	
	/**
	 * Gets the logged user console from the session.
	 * 
	 * @param request the request containing the session.
	 * @return
	 */
	public static UserConsoleVO getLoggedUserConsole (ServletRequest request) {
		LoginController loginController = getLoginController (request);
		return loginController.getLoggedUserConsole();
	}
	
	/**
	 * Gets the LoginController instance from session.
	 * 
	 * @param request the request containing the session.
	 * @return a loginController instance.
	 */
	public static LoginController getLoginController (ServletRequest request) {
		LoginController loginController = (LoginController)((HttpServletRequest)request).getSession().getAttribute("loginController");
		return loginController;
	}
	
	/**
	 * Gets a SHA-512 hash password.
	 * 
	 * @param passwordToHash the password to hash.
	 * @param salt the salt.
	 * @return the hash password.
	 */
	public static String getSHA512Password (String passwordToHash, String salt) {
	    String generatedPassword = null;
        MessageDigest md = null;
        byte[] bytes = null;
        if (!Utils.isNonEmpty(passwordToHash) && !Utils.isNonEmpty(salt)) {
        	String error = "The password and salt are empty.";
        	logger.error (error);
        	throw new IllegalArgumentException (error);
        }
        StringBuilder sb = new StringBuilder();
		try {
			md = MessageDigest.getInstance ("SHA-512");
		} catch (NoSuchAlgorithmException e1) {
			logger.error ("An error occurred while get an instance of SHA-512. " + e1.getMessage());
		}        
        try {
			md.update (salt.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e2) {
			logger.error ("An error occurred while encoding salt to UTF-8. " + e2.getMessage());
		}        
		try {
			bytes = md.digest (passwordToHash.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e3) {
			logger.error ("An error occurred while encoding password to UTF-8. " + e3.getMessage());
		}
        for (int i=0; i< bytes.length ;i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        generatedPassword = sb.toString();
        return generatedPassword;
	}
	
	/**
	 * Performs the comparison between two SHA-512 hashes.
	 * 
	 * @param hash1 the hash 1.
	 * @param hash2 the hash 2.
	 * @return
	 */
	public static boolean isMessageDigestEqual(String hash1, String hash2) {
		boolean isValid = false;
		Security.addProvider(new BouncyCastleProvider());
		
		MessageDigest mda = null;
		try {
			mda = MessageDigest.getInstance("SHA-512", "BC");
		} catch (NoSuchAlgorithmException e) {
			logger.error ("An error occurred while get an instance of SHA-512. " + e.getMessage());
		} catch (NoSuchProviderException e) {
			logger.error ("An error occurred while getting the security provider. " + e.getMessage());
		}
		byte [] digesta = mda.digest(hash1.getBytes());

		MessageDigest mdb = null;
		try {
			mdb = MessageDigest.getInstance("SHA-512", "BC");
		} catch (NoSuchAlgorithmException e) {
			logger.error ("An error occurred while get an instance of SHA-512. " + e.getMessage());
		} catch (NoSuchProviderException e) {
			logger.error ("An error occurred while getting the security provider. " + e.getMessage());
		}
		byte [] digestb = mdb.digest(hash2.getBytes());

    	if (MessageDigest.isEqual(digesta, digestb)) {
    		isValid = true;
    	}
    	
    	return isValid;
	}
	
	/**
	 * Checks if the logged user console has the given role.
	 * 
	 * @param name the role name.
	 * @param name the userConsoleVO.
	 * @return the operation result.
	 */
	public static boolean isUserInRole (String name, UserConsoleVO userConsoleVO) {
		boolean isInRole = false;
		if (userConsoleVO != null 
				&& userConsoleVO.getUserConsoleProfileVOList() != null 
				&& userConsoleVO.getUserConsoleProfileVOList().size() > 0) {
			List<UserConsoleProfileVO> userConsoleProfileVOList = userConsoleVO.getUserConsoleProfileVOList();
			if (userConsoleProfileVOList != null && userConsoleProfileVOList.size() > 0) {
				for (UserConsoleProfileVO userConsoleProfileVO : userConsoleProfileVOList) {				
					if (userConsoleProfileVO.getProfileVO() != null
							&& userConsoleProfileVO.getProfileVO().getProfileRoleVOList() != null
							&& userConsoleProfileVO.getProfileVO().getProfileRoleVOList().size() > 0) {					
						for (ProfileRoleVO profileRoleVO : userConsoleProfileVO.getProfileVO().getProfileRoleVOList()) {						
							if (profileRoleVO.getRoleVO().getName().equalsIgnoreCase (name)) {
								isInRole = true;
								break;
							}						
						}	
					}				
				}
			}
		}
		logger.debug ("role name: " + name);
		logger.debug ("isInRole: " + isInRole);
		logger.debug ("userConsoleVO.getEmail(): " + userConsoleVO.getEmail());
		return isInRole;
	}
	
	public static void main (String[] args) throws NoSuchAlgorithmException, NoSuchProviderException {
		/*String result1 = getSHA512Password("@Portoalegre","josivan@tiservicos.com");
		
		String result2 = getSHA512Password("@Portoalegre","josivan@tiservicos.com");
		System.out.println(result1);
		System.out.println(result2);
		
		Security.addProvider(new BouncyCastleProvider());
		
		String data1 = "8c664ab7db2e5801fc22cf8c8ff986800b4602cc6505b1bdcb7e67bf8448b0a2edc82cf443cc4d4a3ea8646cd56f42cede9317a5d65754b4c3c939d855a84ead";
		String data2 = "8c664ab7db2e5801fc22cf8c8ff986800b4602cc6505b1bdcb7e67bf8448b0a2edc82cf443cc4d4a3ea8646cd56f42cede9317a5d65754b4c3c939d855a84ead";
		
		MessageDigest mda = MessageDigest.getInstance("SHA-512", "BC");
		byte [] digesta = mda.digest(result1.getBytes());

		MessageDigest mdb = MessageDigest.getInstance("SHA-512", "BC");
		byte [] digestb = mdb.digest(result2.getBytes());
		
		System.out.println(MessageDigest.isEqual(digesta, digestb));*/
		
	}
	
}
