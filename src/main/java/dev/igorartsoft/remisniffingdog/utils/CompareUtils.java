package dev.igorartsoft.remisniffingdog.utils;

/**
 * @author Igor Artimenko
 * Contains common utilities
 */
public class CompareUtils {
	
	/**
	 * Efficient implementation of comparing if string is empty.
	 * It does not create new objects like trim method.
	 * Use it if you don't want to introduce new dependencies like Apache commons.
	 * 
	 * Java 11 may have out-of-the box similar solution. I did not have time to investigate. 
	 * 
	 * @param strToCompare
	 * @return
	 */
	public static boolean isEmpty( String strToCompare ) {
		
		int len;
		if ( strToCompare == null || (len = strToCompare.length()) == 0 ) return true;
		for (int i = 0; i < len; i++) {
		  if (!Character.isWhitespace(strToCompare.charAt(i))) return false;
		}
		return true;
		
	}
}
