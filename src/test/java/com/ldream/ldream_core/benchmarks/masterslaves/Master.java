package com.ldream.ldream_core.benchmarks.masterslaves;

import com.ldream.ldream_core.components.AbstractComponent;
import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.components.LocalVariable;
import com.ldream.ldream_core.components.Port;
import com.ldream.ldream_core.values.NumberValue;
import com.ldream.ldream_core.values.SetValue;

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
