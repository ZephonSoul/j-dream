package com.ldream.ldream_core.coordination;

import com.ldream.ldream_core.components.AbstractComponent;
import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.components.LocalVariable;
import com.ldream.ldream_core.components.Port;

public class DummyComponent extends AbstractComponent implements Component {

	public DummyComponent() {
		super();
		LocalVariable x = new LocalVariable("x",0,this);
		setStore(x);
		Port p1 = new Port("p1");
		setInterface(p1);
	}

}
