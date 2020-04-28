package com.dream.core.coordination.constraints;

/**
 * @author Alessandro Maggi
 *
 */
public abstract class AbstractFormula implements Formula {
		
	@Override
	public boolean equals(Object o) {
		return (o instanceof Formula) && equals((Formula) o);
	}

	@Override
	public void clearCache() {}
	
	@Override
	public boolean sat() {
		throw new RuntimeSatisfactionCheckException(this);
	}
	
}
