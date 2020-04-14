package com.dream.test.benchmarks.masterslaves;

import com.dream.core.entities.AbstractLightComponent;
import com.dream.core.entities.LocalVariable;
import com.dream.core.entities.Port;
import com.dream.core.expressions.values.NumberValue;
import com.dream.core.expressions.values.SetValue;

public class Master extends AbstractLightComponent {

	public Master() {
		super();
		LocalVariable x1 = new LocalVariable("nSlaves",new NumberValue(0));
		LocalVariable x2 = new LocalVariable("boundSlaves", new SetValue());
		LocalVariable id = new LocalVariable("id", new NumberValue(getId()));
		setStore(id,x1,x2);
		setInterface(new Port("connect"), new Port("work"));
		clearCache();
	}
	
}
