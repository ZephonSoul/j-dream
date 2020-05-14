/**
 * 
 */
package com.dream.test.benchmarks.platooning2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.dream.core.Entity;
import com.dream.core.coordination.Term;
import com.dream.core.coordination.constraints.And;
import com.dream.core.coordination.constraints.PortAtom;
import com.dream.core.coordination.constraints.predicates.LessThan;
import com.dream.core.entities.AbstractComponent;
import com.dream.core.entities.Port;
import com.dream.core.entities.behavior.ControlLocation;
import com.dream.core.entities.behavior.LTS;
import com.dream.core.entities.behavior.Transition;
import com.dream.core.expressions.RandomNumber;
import com.dream.core.expressions.values.NumberValue;
import com.dream.core.localstore.LocalVariable;
import com.dream.core.localstore.VarStore;

/**
 * @author Alessandro Maggi
 *
 */
public class Car extends AbstractComponent {

	/**
	 * @param parent
	 */
	public Car(Entity parent, double position, double speed, double splitProb) {
		super(parent);

		setInterface(newInterface());

		setStore(newStore(getId(),position,speed,splitProb));

		setBehavior(newBehavior(getInterface(),getStore()));
	}

	public Car(double position, double speed, double splitProb) {
		this(null,position,speed,splitProb);
	}

	private static Port[] newInterface() {
		Port[] ports = {
				new Port("initSplit"), 
				new Port("ackSplit"), 
				new Port("closeSplit"), 
				new Port("join")};
		return ports;
	}

	private static VarStore newStore(int id, double position, double speed, double splitProb) {
		return new VarStore(
				new LocalVariable("id", new NumberValue(id)),
				new LocalVariable("pos", new NumberValue(position)),
				new LocalVariable("speed", new NumberValue(speed)),
				new LocalVariable("followCar", new NumberValue(0)),
				new LocalVariable("splitProb", new NumberValue(splitProb)));
	}

	private static LTS newBehavior(Map<String, Port> cInterface, VarStore store) {
		Map<ControlLocation,Set<Transition>> transitions = new HashMap<>();
		ControlLocation currentControlLocation = new ControlLocation("cruising");

		ControlLocation c1,c2;

		c1 = currentControlLocation;
		c2 = new ControlLocation("splitting");
		transitions.put(c1, new HashSet<>());
		transitions.get(c1).add(new Transition(c1, 
				new Term(
						new And(
								new LessThan(
										new RandomNumber(),
										store.getLocalVariable("splitProb").getValue()
										),
								new PortAtom(cInterface.get("initSplit"))
								)), c2));
		transitions.get(c1).add(new Transition(c1, new Term(new PortAtom(cInterface.get("ackSplit"))), c2));
		transitions.put(c2, new HashSet<>());
		transitions.get(c2).add(new Transition(c2, new Term(new PortAtom(cInterface.get("closeSplit"))), c1));
		transitions.get(c1).add(new Transition(c1, new Term(new PortAtom(cInterface.get("join"))), c1));

		return new LTS(transitions,currentControlLocation);
	}

}
