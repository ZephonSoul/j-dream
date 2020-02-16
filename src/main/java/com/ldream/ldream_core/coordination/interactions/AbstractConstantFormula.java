package com.ldream.ldream_core.coordination.interactions;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;

public abstract class AbstractConstantFormula extends AbstractFormula implements Formula {

	@Override
	public Formula bindActualComponent(ComponentInstance componentVariable, ActualComponentInstance actualComponent) {
		return this;
	}
	
	@Override
	public void clearCache() {}

}
