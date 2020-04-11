package com.ldream.ldream_core.coordination;

import com.dream.core.components.AbstractComponent;
import com.dream.core.components.Component;
import com.dream.core.components.LocalVariable;
import com.dream.core.components.Port;
import com.dream.core.expressions.values.NumberValue;

public class DummyComponent extends AbstractComponent implements Component {

	public DummyComponent() {
		super();
		LocalVariable x = new LocalVariable("x",new NumberValue(0),this);
		setStore(x);
		Port p1 = new Port("p1");
		setInterface(p1);
	}

}
