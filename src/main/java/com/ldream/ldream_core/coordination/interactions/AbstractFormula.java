package com.ldream.ldream_core.coordination.interactions;

public abstract class AbstractFormula implements Formula {
		
	@Override
	public boolean equals(Object o) {
		return (o instanceof Formula) && equals((Formula) o);
	}

	@Override
	public void clearCache() {}
	
}
