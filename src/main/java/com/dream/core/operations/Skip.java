package com.dream.core.operations;

/**
 * @author Alessandro Maggi
 *
 */
public class Skip extends AbstractOperation {
	
	final static int BASE_CODE = 0;
	private static Skip instance;

	public static Skip getInstance() {
		if (instance == null)
			instance = new Skip();
		return instance;
	}

	@Override
	public void evaluate() {}

	@Override
	public void execute() {}
	
	@Override
	public int hashCode() {
		return BASE_CODE;
	}
	
	@Override
	public void clearCache() {}
	
	public boolean equals(Operation op) {
		return (op instanceof Skip);
	}
	
	public String toString() {
		return "skip()";
	}

}
