package com.tiservicos.console.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Locale;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

/**
 * Utils helper class.
 * 
 * @author Josivan Silva
 *
 */
public class Utils {
	
	static Logger logger = Logger.getLogger (Utils.class.getName());

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	private static final String NUMERATION_PATTERN = "^[0-9]+(\\.[0-9]+)*$";
	
	private static final long K = 1024;
	private static final long M = K * K;
	private static final long G = M * K;
	private static final long T = G * K;
	
	/**
	 * Gets the config folder path.
	 * 
	 * @return the config path.
	 */
	public static String getConfigPath () {
		String path = null;
		if (System.getProperty (Constants.DEV_CONFIG_PATH) != null) {
			path = getDevConfigPath ();
		} else {
			path = getDefaultConfigPath ();
		}
		return path;
	}
	
	/**
	 * Gets the default config folder path.
	 * 
	 * @return the default config path.
	 */
	private static String getDefaultConfigPath () {
		String path = System.getProperty ("user.dir");
		path += "/webapps/config/";
		return path;
	}
	
	/**
	 * Gets the dev config folder path.
	 * 
	 * @return the dev config path.
	 */
	private static String getDevConfigPath() {
		String path = System.getProperty (Constants.DEV_CONFIG_PATH) + "/";
		return path;
	}	
	
	/**
	 * Checks if a given string is not empty.
	 * 
	 * @param str the string to be checked.
	 * @return a boolean indicating the result.
	 */
	public static boolean isNonEmpty (String str) {
		boolean isNonEmpty = false;
		if (str != null && str.length() > 0) {
			isNonEmpty = true;
		}
		return isNonEmpty;
	}
	
	/**
	 * Validates a given email.
	 * 
	 * @param email the email
	 * @return a boolean indicating the result.
	 */
	public static boolean isValidEmail (String email) {
		return email.matches (EMAIL_PATTERN);
	}
	
	/**
	 * Gets the email without @domain.aaa
	 * @param email the email.
	 * @return the formatted email.
	 */
	public static String getFormattedEmail  (String email) {
		String formattedEmail = null;
		String[] tokens = email.split ("@");
		if (tokens != null && tokens.length > 0) {
			formattedEmail = tokens[0];
		}
		return formattedEmail;
	}
		
	/**
	 * Validates a date interval.
	 * 
	 * @param startDate the start date.
	 * @param endDate the end date.
	 * @return the operation result.
	 */
	public static boolean isValidDateInterval (String startDate, String endDate) {
		boolean isValid = false;
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = new SimpleDateFormat ("dd/MM/yyyy").parse (startDate);
		} catch (ParseException e) {
			logger.error ("An error occurred while parsing start date. " + e.getMessage());
			return isValid;
		}
		try {
			date2 = new SimpleDateFormat ("dd/MM/yyyy").parse (endDate);
		} catch (ParseException e) {
			logger.error ("An error occurred while parsing end date. " + e.getMessage());
			return isValid;
		}		
		int result = date1.compareTo (date2);
		if (result <= 0) {
			isValid = true;
		}		
		return isValid;
	}
	
	/**
	 * Checks if a datetime interval is lower or equal the given hours.
	 * 
	 * @param startDatetime the start datetime.
	 * @param endDatetime the end datetime.
	 * @param hours the hours.
	 * @return the success operation.
	 */
	public static boolean isValidIntervalInHours (Date startDatetime, Date endDatetime, int hours) {
		boolean success = true;
		//in milliseconds
		long diff = endDatetime.getTime() - startDatetime.getTime();
		long diffMinutes = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000) % 24;
		long diffDays = diff / (24 * 60 * 60 * 1000);		
		if (diffDays >= 1) {
			logger.debug ("diffDays: " + diffDays);
			success = false;
		} else if (diffHours > hours) {
			logger.debug ("diffHours: " + diffHours);
			success = false;
		} else if (diffHours == hours && diffMinutes >= 1) {
			logger.debug ("diffMinutes: " + diffMinutes);
			success = false;
		}
		logger.debug ("success: " + success);
		return success;
	}
	
	/**
	 * Checks if a datetime is equal or higher than the minimum given hours.
	 * 
	 * @param startDatetime the start datetime.
	 * @param endDatetime the end datetime.
	 * @param hours the hours.
	 * @return the success operation.
	 */
	public static boolean isMinimumIntervalInHours (Date startDatetime, Date endDatetime, int hours) {
		boolean success = true;
		//in milliseconds
		long diff = endDatetime.getTime() - startDatetime.getTime();
		long diffHours = diff / (60 * 60 * 1000) % 24;
		long diffDays = diff / (24 * 60 * 60 * 1000);		
		if (diffDays == 0 && diffHours < hours) {
			logger.debug ("diffDays: " + diffDays);
			logger.debug ("diffHours: " + diffHours);
			success = false;
		}
		logger.debug ("success: " + success);
		return success;
	}
	
	/**
	 * Removes the CPF mask.
	 * 
	 * @param cpf the CPF.
	 * @return the CPF without mask.
	 */
	public static String removeCpfMask (String cpf) {
		String newCpf = cpf.replaceAll ("\\.", "").replace ("-", "");
		return newCpf;
	}	
	
	/**
	 * Validates a CPF.
	 * 
	 * @param cpf the CPF.
	 * @return the operation result.
	 */
	public static boolean isCPF (String CPF) {
		if (CPF.equals("00000000000") 
				|| CPF.equals("11111111111") 
				|| CPF.equals("22222222222") 
				|| CPF.equals("33333333333") 
				|| CPF.equals("44444444444") 
				|| CPF.equals("55555555555") 
				|| CPF.equals("66666666666") 
				|| CPF.equals("77777777777") 
				|| CPF.equals("88888888888") 
				|| CPF.equals("99999999999") 
				|| CPF.length() != 11) {
			return false;
		}	       

		char dig10, dig11;
		int sm, i, r, num, peso;
		
		try {
		      // Calculo do 1o. Digito Verificador
		      sm = 0;
		      peso = 10;
		      for (i = 0; i < 9; i++) {              
				// converte o i-esimo caractere do CPF em um numero:
				// por exemplo, transforma o caractere '0' no inteiro 0         
				// (48 eh a posicao de '0' na tabela ASCII)         
		        num = (int)(CPF.charAt(i) - 48); 
		        sm = sm + (num * peso);
		        peso = peso - 1;
		      }

		      r = 11 - (sm % 11);
		      if (r == 10 || r == 11) {
		    	  dig10 = '0'; 
		      } else {
		    	  dig10 = (char)(r + 48); // converte no respectivo caractere numerico  
		      }

		      // Calculo do 2o. Digito Verificador
		      sm = 0;
		      peso = 11;
		      for ( i = 0; i < 10; i++) {
		        num = (int)(CPF.charAt(i) - 48);
		        sm = sm + (num * peso);
		        peso = peso - 1;
		      }

		      r = 11 - (sm % 11);
		      if (r == 10 || r == 11) {
		    	  dig11 = '0'; 
		      } else {
		    	  dig11 = (char)(r + 48);  
		      }

		      // Verifica se os digitos calculados conferem com os digitos informados.
		      if (dig10 == CPF.charAt(9) && dig11 == CPF.charAt (10)) {
		    	  return true; 
		      } else {
		    	  return false;
		      }
		      
		} catch (InputMismatchException e) {
		        return false;
		}
		
	}
	
	/**
	 * Formats a double value to current currency.
	 * 
	 * @param amount the amount.
	 * @return the formatted amount.
	 */
	public static String formatDoubleToReal (Double amount) {
		Locale ptBr = new Locale ("pt", "BR");
		return NumberFormat.getCurrencyInstance (ptBr).format (amount);
	}
	
	/**
	 * Formats a byte value to other representations (e.g., KB, MB, etc.).
	 * 
	 * @param value the value in bytes.
	 * @return the converted value.
	 */
	public static String convertToStringRepresentation (final long value) {
	    final long[] dividers = new long[] { T, G, M, K, 1 };
	    final String[] units = new String[] { "TB", "GB", "MB", "KB", "B" };
	    if(value < 1) {
	    	throw new IllegalArgumentException("Invalid file size: " + value);
	    }	        
	    String result = null;
	    for(int i = 0; i < dividers.length; i++) {
	        final long divider = dividers[i];
	        if(value >= divider){
	            result = format (value, divider, units[i]);
	            break;
	        }
	    }
	    return result;
	}

	/**
	 * Gets the current date.
	 * 
	 * @return the current date.
	 */
	public static java.sql.Date getCurrentDate() {
	    java.util.Date today = new java.util.Date();
	    return new java.sql.Date (today.getTime());
	}
		
	/**
	 * Checks if the numeration is valid or not.
	 * 
	 * @param numeration the numeration.
	 * @return the operation result.
	 */
	public static boolean isValidNumeration (String numeration) {
		if (numeration == null || numeration.length() == 0) {
			return false;
		} else {
			return numeration.matches (NUMERATION_PATTERN);	
		}
	}
	
	/**
	 * Checks if the numeration is within the allowed limit. 
	 * 
	 * @param numeration the numeration.
	 * @return the operation result.
	 */
	public static boolean isNumerationWithinLimit (String numeration) {
		if (numeration == null || numeration.length() == 0) {
			return false;
		} else {
			String[] tokens = numeration.split ("\\.");
			if (tokens.length == 1
					|| tokens.length == 2
					|| tokens.length == 3
					|| tokens.length == 4) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Formats the byte value according with its unit.
	 */
	private static String format (final long value, final long divider, final String unit) {
	    final double result = divider > 1 ? (double) value / (double) divider : (double) value;
	    return new DecimalFormat("#,##0.#").format(result) + " " + unit;
	}
	
	/**
	 * Checks is a given phone is valid or not.
	 * 
	 * <ul>Valid phone numbers:
	 *  <li>96-3242-2738</li>
	 *	<li>(96) 3242-2738</li>
	 *  <li>(96) 3242 2738</li>
	 *	<li>(96) 32422738</li>
	 *	<li>96 3242 2738</li>
	 *  <li>96 3242-2738</li>
	 *  <li>96 32422738</li>
	 * </ul>
	 * 
	 * @param phone the phone number.
	 * @return the operation result.
	 */
	public static boolean isValidPhone (String phone) {
		boolean isValid = false;
		if (phone.matches ("\\d{9}")
				|| phone.matches ("\\d{2}[-\\s]\\d{4}[-\\s]?\\d{4}")
				|| phone.matches ("\\(\\d{2}\\) \\d{4}[-\\s]?\\d{4}")) {
			isValid = true;
		}
		return isValid;
	}
	
	/**
	 * Checks is a given mobile number is valid or not.
	 * 
	 * <ul>Valid mobile numbers:
	 *  <li>51983204050</li>
	 *  <li>5183204050</li>
	 *  <li>51-58320-4050</li>
	 *  <li>51-8320-4050</li>
	 *  <li>51 58320 4050</li>
	 *  <li>(51)-58320-4050</li>
	 *  <li>(51) 8320 4050</li>  
	 *  <li>(51) 83204050</li>
	 * </ul>
	 *  
	 * @param mobile the mobile number.
	 * @return the operation result.
	 */
	public static boolean isValidMobile (String mobile) {
		boolean isValid = false;
		if (mobile.matches("\\d{10,11}")
				|| mobile.matches("\\d{2}[-\\s]\\d{4,5}[-\\s]?\\d{4}")
				|| mobile.matches("\\(\\d{2}\\)[-\\s]\\d{4,5}[-\\s]?\\d{4}")) {
			isValid = true;
		}	
		return isValid;
	}
	
	/**
	 * Checks is a given CEP is valid or not.
	 * 
	 * <p>Example: 92030-030</p> 
	 * 
	 * @param cep the CEP.
	 * @return the operation result.
	 */
	public static boolean isValidCep (String cep) {
		boolean isValid = false;
		if (cep.matches("[0-9]{5}-[0-9]{3}")) {
			isValid = true;
		}
		return isValid;
	}
	
	/**
	 * Checks is a String is number.
	 * 
	 * @param number the number.
	 * @return the operation result.
	 */
	public static boolean isNumber (String number) {
		boolean isValid = false;
		if (number.matches("\\d+")) {
			isValid = true;
		}
		return isValid;
	}
	
	/**
	 * Resizes an image.
	 * 
	 * @param imageInputStream
	 */
	public static InputStream resizeImageWithHint(InputStream imageInputStream){
		BufferedImage originalImage = null;
		try {
			originalImage = ImageIO.read(imageInputStream);
		} catch (IOException e) {
			logger.debug(e.getMessage());
			e.printStackTrace();			
		}		
		if (originalImage != null) {
			int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
			BufferedImage resizedImage = new BufferedImage(200, 200, type);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(originalImage, 0, 0, 200, 200, null);
			g.dispose();
			g.setComposite(AlphaComposite.Src);

			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
			RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.setRenderingHint(RenderingHints.KEY_RENDERING,
			RenderingHints.VALUE_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);

			return convertBufferedImageToInputStream(resizedImage);
			
		} else {
			return null;
		}				 
	 }
	
	 /**
	  * Converts BufferedImage to InputStream.
	 * @param image
	 * @return
	 */
	public static InputStream convertBufferedImageToInputStream (BufferedImage image) {
		 
		 ByteArrayOutputStream os = new ByteArrayOutputStream();
		 try {
			ImageIO.write(image, "jpg", os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		 InputStream is = new ByteArrayInputStream(os.toByteArray());
		 return is;
	 }
	
	/**
	 * Gets the CPF as number.
	 * 
	 * @param cpf the cpf.
	 * @return the cpf as number.
	 */
	public static String getCPFAsNumber (String cpf) {
		String result = null;
		result = cpf.replaceAll ("\\.", "");
		result = result.replace("-", "");
		return result;
	}
	
	public static void main (String[] args) {
		String cpf = "003.939.708-41";		
		System.out.println ("resultado: " + getCPFAsNumber (cpf));
	}
	
}
