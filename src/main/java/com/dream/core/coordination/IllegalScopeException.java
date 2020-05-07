/**
 * 
 */
package com.dream.core.coordination;

import com.dream.core.Entity;
import com.dream.core.Instance;

/**
 * @author Alessandro Maggi
 *
 */
public class IllegalScopeException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public IllegalScopeException(Entity scope) {
		super(getMessage(scope));
	}
	
	public IllegalScopeException(Entity scope, String token) {
		super(getMessage(scope,token));
	}
	
	public IllegalScopeException(Instance<?> actual, String token) {
		super(getMessage(actual,token));
	}

	public static String getMessage(Entity scope) {
		return String.format(
				"Illegal entity for declaration scoping: %s", 
				scope.toString());
	}
	
	public static String getMessage(Entity scope, String token) {
		return String.format(
				"Illegal entity scope %s for element %s", 
				scope.toString(),
				token);
	}
	
	public static String getMessage(Instance<?> scope, String token) {
		return String.format(
				"Illegal scope %s for element %s", 
				scope.toString(),
				token);
	}

}
