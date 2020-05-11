package com.dream.test.benchmarks.platooning;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

import com.dream.ExecutionEngine;
import com.dream.core.Entity;
import com.dream.core.coordination.AndRule;
import com.dream.core.coordination.ConjunctiveTerm;
import com.dream.core.coordination.Declaration;
import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.EntityInstanceRef;
import com.dream.core.coordination.FOILRule;
import com.dream.core.coordination.Quantifier;
import com.dream.core.coordination.Rule;
import com.dream.core.coordination.Term;
import com.dream.core.coordination.TypeRestriction;
import com.dream.core.coordination.constraints.And;
import com.dream.core.coordination.constraints.Not;
import com.dream.core.coordination.constraints.PortReference;
import com.dream.core.coordination.constraints.predicates.CurrentControlLocation;
import com.dream.core.coordination.constraints.predicates.Equals;
import com.dream.core.coordination.constraints.predicates.GreaterThan;
import com.dream.core.coordination.constraints.predicates.LessThan;
import com.dream.core.coordination.maps.MapPropertyRef;
import com.dream.core.entities.AbstractMotif;
import com.dream.core.entities.maps.MapNode;
import com.dream.core.entities.maps.predefined.ArrayMap;
import com.dream.core.exec.GreedyStrategy;
import com.dream.core.expressions.PoolSize;
import com.dream.core.expressions.RandomNumber;
import com.dream.core.expressions.Sum;
import com.dream.core.expressions.VariableRef;
import com.dream.core.expressions.values.NumberValue;
import com.dream.core.operations.Assign;
import com.dream.core.output.ConsoleOutput;
import com.dream.core.localstore.StoringInstance;

/**
 * @author Alessandro Maggi
 *
 */
public class Platoon extends AbstractMotif {

	public static NumberValue splitProb = new NumberValue(0.2);
	
	/**
	 * @param parent
	 * @param pool
	 */
	public Platoon(Entity parent, Entity[] initialPool) {
		super(	parent, 
				Arrays.stream(initialPool).collect(Collectors.toSet()), 
				new ArrayMap(
						initialPool.length,
						Comparator.comparing(
								e -> ((NumberValue) 
										((StoringInstance)
												e).getVariable("pos").getValue()).getRawValue().doubleValue()
								)
						)
				);
		map.setOwner(this);
		for (MapNode n : map.getNodes()) {
			n.getStore().setVarValue("newLeader", new NumberValue(-1));
			n.getStore().setVarValue("newLoc", new NumberValue(-1));
		}

		for (int i=0; i<initialPool.length; i++)
			setEntityPosition(initialPool[i], ((ArrayMap)map).getNodeAtIndex(i));


		setRule(newRule(this,splitProb));
	}

	public Platoon() {
		this(null,new Entity[0]);
	}

	@Override
	public MapNode createMapNode() {
		MapNode newNode = super.createMapNode();
		newNode.getStore().setVarValue("newLeader", new NumberValue(-1));
		newNode.getStore().setVarValue("newLoc", new NumberValue(-1));
		return newNode;
	}

	private static Rule newRule(AbstractMotif current, NumberValue splitProb) {
		EntityInstanceActual scope = new EntityInstanceActual(current);
		// \forall c:Car {true -> c.pos := c.pos + c.speed \times \varDelta_t}
		Declaration allCars = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Car.class));
		EntityInstanceRef c = allCars.getVariable();
		Rule r1 = new FOILRule(allCars,
				new Term(
						new Assign(
								new VariableRef(c, "pos"),
								new Sum(
										new VariableRef(c, "pos"),
										new VariableRef(c, "speed")
										)
								)
						)
				);
		// \forall c:Car {c.initSplit |> 
		//					poolSize(this)>1 /\ head(this).id != c.id /\ Rand(1) > 0.7 -> 0}
		allCars = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Car.class));
		c = allCars.getVariable();
		Rule r2 = new FOILRule(allCars,
				new ConjunctiveTerm(
						new PortReference(c, "initSplit"),
						new And(
								new GreaterThan(new PoolSize(scope),new NumberValue(1)),
								new Not(
										new Equals(
												new VariableRef(new MapPropertyRef<>(scope,"head"),"id"),
//												new VariableMapProperty(scope,"head","id"),
												new VariableRef(c,"id")
												)
										),
								new LessThan(
										new RandomNumber(),
										splitProb
										)
								)
						)
				);
		// \forall c:Car {c.speed != head(this).speed /\ cloc(c,cruising) |> true 
		//					-> c.speed := leader(this).speed}
		allCars = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Car.class));
		c = allCars.getVariable();
		Rule r3 = new FOILRule(allCars,
				new ConjunctiveTerm(
						new And(
								new Not(
										new Equals(
												new VariableRef(c, "speed"),
												new VariableRef(new MapPropertyRef<>(scope,"head"),"speed")
												//new VariableMapProperty(scope,"head","speed"))
												)
										),
								new CurrentControlLocation(c,"cruising")
								),
						new Assign(
								new VariableRef(c, "speed"),
								new VariableRef(new MapPropertyRef<>(scope,"head"),"speed")
								//new VariableMapProperty(scope,"head","speed")
								)
						)
				);

		return new AndRule(r1,r2,r3);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Car[] cars = {new Car(0.0, 1.5), new Car(2.0,2.0)};//, new Car(5,1)};
		AbstractMotif platoon = new Platoon(null,cars);
		System.out.println(platoon.getJSONDescriptor());
		System.out.println(platoon.getExpandedRule().toString());

		ExecutionEngine ex = new ExecutionEngine(platoon,GreedyStrategy.getInstance(),new ConsoleOutput(),false,3);
		ex.setSnapshotSemantics(true);
		ex.run();
	}

}
