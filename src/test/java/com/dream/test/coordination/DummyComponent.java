package com.dream.test.coordination;

import com.dream.core.entities.AbstractLightComponent;
import com.dream.core.entities.Port;
import com.dream.core.expressions.values.NumberValue;
import com.dream.core.localstore.LocalVariable;

public class DummyComponent extends AbstractLightComponent {

	public DummyComponent() {
		super();
		LocalVariable x = new LocalVariable("x",new NumberValue(0),this);
		setStore(x);
		Port p1 = new Port("p1");
		setInterface(p1);
	}

}
