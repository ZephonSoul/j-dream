package com.ldream.ldream_core.benchmarks.masterslaves;

import com.ldream.ldream_core.components.AbstractComponent;
import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.components.LocalVariable;
import com.ldream.ldream_core.components.Port;
import com.ldream.ldream_core.values.NumberValue;

public class Master extends AbstractComponent implements Component {

	public Master() {
		LocalVariable nSlaves = new LocalVariable("nSlaves",new NumberValue(2));
		setStore(nSlaves);
		setInterface(new Port("connect"), new Port("work"));
		
	}
	
}
