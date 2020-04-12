package com.ldream.ldream_core.benchmarks.masterslaves;

import com.ldream.ldream_core.components.AbstractComponent;
import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.components.LocalVariable;
import com.ldream.ldream_core.components.Port;
import com.ldream.ldream_core.coordination.AndRule;
import com.ldream.ldream_core.coordination.ConjunctiveTerm;
import com.ldream.ldream_core.coordination.constraints.PortAtom;
import com.ldream.ldream_core.coordination.constraints.predicates.Equals;
import com.ldream.ldream_core.coordination.operations.Assign;
import com.ldream.ldream_core.expressions.ActualVariable;
import com.ldream.ldream_core.expressions.values.NumberValue;

public class Slave extends AbstractComponent implements Component {

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
								new Equals(new ActualVariable(cLoc),waiting),
								new Assign(new ActualVariable(cLoc),busy)
								),
						new ConjunctiveTerm(
								new PortAtom(serve),
								new Equals(new ActualVariable(cLoc),busy),
								new Assign(new ActualVariable(cLoc),waiting)
								)
						));

		refresh();
	}

}
