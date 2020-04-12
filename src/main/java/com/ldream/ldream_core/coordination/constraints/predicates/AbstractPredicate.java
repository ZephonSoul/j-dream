package com.ldream.ldream_core.coordination.constraints.predicates;

import com.ldream.ldream_core.coordination.Interaction;

public abstract class AbstractPredicate implements Predicate {
	
	@Override
	public boolean sat(Interaction i) {
		return sat();
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof Predicate)	&& equals((Predicate) o);
	}

}
