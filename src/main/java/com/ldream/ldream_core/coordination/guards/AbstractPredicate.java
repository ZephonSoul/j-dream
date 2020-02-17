package com.ldream.ldream_core.coordination.guards;

public abstract class AbstractPredicate implements Predicate {

	@Override
	public boolean equals(Object o) {
		return (o instanceof Predicate)	&& equals((Predicate) o);
	}

}
