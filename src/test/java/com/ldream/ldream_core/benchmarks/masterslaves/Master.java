package com.ldream.ldream_core.benchmarks.masterslaves;

import com.dream.core.components.AbstractComponent;
import com.dream.core.components.Component;
import com.dream.core.components.LocalVariable;
import com.dream.core.components.Port;
import com.dream.core.expressions.values.NumberValue;
import com.dream.core.expressions.values.SetValue;

public class Master extends AbstractComponent implements Component {

	public Master() {
		super();
		LocalVariable x1 = new LocalVariable("nSlaves",new NumberValue(0));
		LocalVariable x2 = new LocalVariable("boundSlaves", new SetValue());
		LocalVariable id = new LocalVariable("id", new NumberValue(getId()));
		setStore(id,x1,x2);
		setInterface(new Port("connect"), new Port("work"));
		refresh();
	}
	
}
