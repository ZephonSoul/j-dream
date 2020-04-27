package com.dream.test.benchmarks.masterslaves;

import com.dream.core.coordination.AndRule;
import com.dream.core.coordination.ConjunctiveTerm;
import com.dream.core.coordination.constraints.PortAtom;
import com.dream.core.coordination.constraints.predicates.Equals;
import com.dream.core.entities.AbstractLightComponent;
import com.dream.core.entities.LocalVariable;
import com.dream.core.entities.Port;
import com.dream.core.expressions.VariableActual;
import com.dream.core.expressions.values.NumberValue;
import com.dream.core.operations.Assign;

public class Slave extends AbstractLightComponent {

	public Slave() {
		super();
		// control locations: waiting=0, busy=1
		NumberValue waiting = new NumberValue(0);
		NumberValue busy = new NumberValue(1);
		LocalVariable cLoc = new LocalVariable("cLoc", waiting);
		LocalVariable x = new LocalVariable("master", new NumberValue(-1));
		LocalVariable id = new LocalVariable("id", new NumberValue(getId()));
		setStore(cLoc,id,x);
		Port bind = new Port("bind");
		Port serve = new Port("serve");
		setInterface(bind,serve);

		setRule(
				new AndRule(
						new ConjunctiveTerm(
								new PortAtom(bind),
								new Equals(new VariableActual(cLoc),waiting),
								new Assign(new VariableActual(cLoc),busy)
								),
						new ConjunctiveTerm(
								new PortAtom(serve),
								new Equals(new VariableActual(cLoc),busy),
								new Assign(new VariableActual(cLoc),waiting)
								)
						));

		clearCache();
	}

}
