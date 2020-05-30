package com.dream.test.benchmarks.robotsinteractive;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.dream.core.Entity;
import com.dream.core.coordination.Interaction;
import com.dream.core.coordination.Term;
import com.dream.core.coordination.constraints.PortAtom;
import com.dream.core.entities.AbstractComponent;
import com.dream.core.entities.AbstractInteractingEntity;
import com.dream.core.entities.Port;
import com.dream.core.entities.behavior.ControlLocation;
import com.dream.core.entities.behavior.LTS;
import com.dream.core.entities.behavior.Transition;
import com.dream.core.expressions.Sum;
import com.dream.core.expressions.VariableActual;
import com.dream.core.expressions.values.ArrayValue;
import com.dream.core.expressions.values.NumberValue;
import com.dream.core.localstore.LocalVariable;
import com.dream.core.localstore.VarStore;
import com.dream.core.operations.Assign;
import com.dream.core.operations.Operation;

public class Robot extends AbstractComponent {

	public Robot(Entity parent, double range, int dirx, int diry) {
		super(parent);
		
		setInterface(
				new Port("tick",this)
				);
		
		setStore(
				new VarStore(
						new LocalVariable("clock",new NumberValue(0)),
						new LocalVariable("range", new NumberValue(range)),
						new LocalVariable("ts", new NumberValue(0)),
						new LocalVariable("dir", new ArrayValue(dirx,diry))
						)
				);
		
		setBehavior(newBehavior(getInterface(),getStore()));
		
	}
	
	private static LTS newBehavior(Map<String, Port> cInterface, VarStore store) {
		Map<ControlLocation,Set<Transition>> transitions = new HashMap<>();
		ControlLocation currentControlLocation = new ControlLocation("s0");

		transitions.put(currentControlLocation, new HashSet<>());
		transitions.get(currentControlLocation).add(new Transition(currentControlLocation, 
				new Term(
						new PortAtom(cInterface.get("tick")),
						new Assign(
								new VariableActual(store.getLocalVariable("clock")),
								new Sum(new VariableActual(store.getLocalVariable("clock")),new NumberValue(1)))
						), currentControlLocation));

		return new LTS(transitions,currentControlLocation);
	}
	
	public static void main(String[] args) {
		AbstractInteractingEntity r = new Robot(null, 2, 1, 1);
		Interaction i = r.getAllowedInteraction();
		System.out.println(i.toString());
		Operation op = r.getOperationsForInteraction(i);
		System.out.println(op.toString());
		System.out.println(r.getJSONDescriptor().toString());
		op.execute();
		System.out.println(r.getJSONDescriptor().toString());
		r.clearCache();
		i = r.getAllowedInteraction();
		System.out.println(i.toString());
		op = r.getOperationsForInteraction(i);
		System.out.println(op.toString());
	}

}
