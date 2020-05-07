package com.dream.test.entities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.dream.core.Entity;
import com.dream.core.coordination.Interaction;
import com.dream.core.coordination.Term;
import com.dream.core.coordination.constraints.PortAtom;
import com.dream.core.entities.AbstractComponent;
import com.dream.core.entities.InteractingEntity;
import com.dream.core.entities.Port;
import com.dream.core.entities.behavior.ControlLocation;
import com.dream.core.entities.behavior.LTS;
import com.dream.core.entities.behavior.Transition;
import com.dream.core.expressions.VariableActual;
import com.dream.core.expressions.values.NumberValue;
import com.dream.core.localstore.LocalVariable;
import com.dream.core.localstore.VarStore;
import com.dream.core.operations.Assign;

public class DComp extends AbstractComponent implements InteractingEntity {

	public DComp(Entity parent) {
		super(parent);

		setStore(new VarStore(this,new LocalVariable("x",new NumberValue(2))));

		Port[] ports = {new Port("p"),new Port("q")};
		HashMap<String,Port> iface = new HashMap<>();
		for (Port p : ports)
			iface.put(p.getName(), p);
		setInterface(iface);

		// Two-state automaton with transitions
		ControlLocation a = new ControlLocation("a"),
				b = new ControlLocation("b");
		Transition t_ab = new Transition(
				a,
				new Term(
						new PortAtom(cInterface.get("p")),
						new Assign(new VariableActual(store.getLocalVariable("x")), new NumberValue(10))),
				b),
				t_ba = new Transition(
						b,
						new Term(
								new PortAtom(cInterface.get("q")),
								new Assign(new VariableActual(store.getLocalVariable("x")), new NumberValue(0))),
						a);
		HashMap<ControlLocation,Set<Transition>> transitions = new HashMap<>();
		transitions.put(a, new HashSet<Transition>());
		transitions.get(a).add(t_ab);
		transitions.put(b, new HashSet<Transition>());
		transitions.get(b).add(t_ba);
		
		setBehavior(new LTS(transitions,a));
	}
	
	public DComp() {
		this(null);
	}

	public static void main(String[] args) {
		InteractingEntity comp = new DComp();
		System.out.println(comp.getJSONDescriptor().toString());
		
		System.out.println(comp.getAllowedInteraction().toString());
		Interaction[] is = comp.getAllowedInteractions();
		System.out.println(Arrays.stream(is).map(Interaction::toString).collect(Collectors.joining(",")));
//		System.out.println(comp.getOperationsForInteraction(is[0]));
//		comp.getOperationsForInteraction(is[0]).executeOperations(false);
//		System.out.println(comp.getJSONDescriptor().toString());
//		System.out.println(comp.getAllowedInteraction().toString());
	}
	
}
