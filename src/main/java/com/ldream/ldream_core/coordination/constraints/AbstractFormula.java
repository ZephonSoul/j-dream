package com.ldream.ldream_core.coordination.constraints;

public abstract class AbstractFormula implements Formula {
		
	@Override
	public boolean equals(Object o) {
		return (o instanceof Formula) && equals((Formula) o);
	}

	@Override
	public void clearCache() {}
	
}
