package com.dream.test.benchmarks.platooning;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.dream.ExecutionEngine;
import com.dream.core.Entity;
import com.dream.core.coordination.AndRule;
import com.dream.core.coordination.ConjunctiveTerm;
import com.dream.core.coordination.Declaration;
import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.EntityInstanceRef;
import com.dream.core.coordination.FOILRule;
import com.dream.core.coordination.Interaction;
import com.dream.core.coordination.Quantifier;
import com.dream.core.coordination.Rule;
import com.dream.core.coordination.Term;
import com.dream.core.coordination.TypeRestriction;
import com.dream.core.coordination.constraints.And;
import com.dream.core.coordination.constraints.Not;
import com.dream.core.coordination.constraints.Or;
import com.dream.core.coordination.constraints.PortReference;
import com.dream.core.coordination.constraints.predicates.CurrentControlLocation;
import com.dream.core.coordination.constraints.predicates.Equals;
import com.dream.core.coordination.constraints.predicates.GreaterThan;
import com.dream.core.coordination.constraints.predicates.LessThan;
import com.dream.core.coordination.constraints.predicates.SameInstance;
import com.dream.core.coordination.maps.MapNodeForEntity;
import com.dream.core.coordination.maps.MapPropertyRef;
import com.dream.core.entities.AbstractMotif;
import com.dream.core.entities.maps.MapNode;
import com.dream.core.entities.maps.MapProperty;
import com.dream.core.entities.maps.predefined.ArrayMap;
import com.dream.core.entities.maps.predefined.GraphMap;
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
public class Platoon2 extends AbstractMotif {

	/**
	 * @param parent
	 * @param pool
	 */
	public Platoon2(Entity parent, Entity[] initialPool) {
		super(	parent, 
				Arrays.stream(initialPool).collect(Collectors.toSet()), 
				new GraphMap(null));
		map.setOwner(this);
		HashMap<String,MapProperty<?>> properties = new HashMap<>();
		properties.put("head",
				(map) -> {
					NumberValue maxPos = null;
					Entity head = null;
					for (Entity e : map.getOwner().getPool()) {
						NumberValue pos = (NumberValue) ((StoringInstance)e).getVariable("pos").getValue();
						if (maxPos == null || pos.greaterThan(maxPos)) {
							maxPos = pos;
							head = e;
						}
					}
					return head;					
				});
		properties.put("tail",
				(map) -> {
					NumberValue minPos = null;
					Entity tail = null;
					for (Entity e : map.getOwner().getPool()) {
						NumberValue pos = (NumberValue) ((StoringInstance)e).getVariable("pos").getValue();
						if (minPos==null || pos.lessThan(minPos)) {
							minPos = pos;
							tail = e;
						}
					}
					return tail;		
				});
		((GraphMap)map).setProperties(properties);
		pool.stream().forEach(e -> {
			MapNode n = createMapNode();
			setEntityPosition(e, n);
		});


		setRule(newRule(this));
	}

	public Platoon2() {
		this(null,new Entity[0]);
	}

	@Override
	public MapNode createMapNode() {
		MapNode newNode = super.createMapNode();
		newNode.setVariable("newLeader", new NumberValue(-1));
		newNode.setVariable("newLoc", new NumberValue(-1));
		return newNode;
	}

	private static Rule newRule(AbstractMotif current) {
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
		//					poolSize(this)>1 /\ head(this).id != c.id -> 0}
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
												new VariableRef(c,"id")
												)
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

		// \forall c1:Car {
		//		\forall c2:Car { c1.ackJoin |> c2.ackJoin -> 0 }
		Declaration allCars1 = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Car.class));
		var c1 = allCars1.getVariable();
		Declaration allCars2 = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Car.class));
		var c2 = allCars2.getVariable();
		Rule r4 = new FOILRule(allCars1,
				new FOILRule(allCars2,
						new ConjunctiveTerm(
								new PortReference(c1, "ackJoin"),
								new PortReference(c2, "ackJoin")
								)
						)
				);

		// \forall c1:Car {
		//		\forall c2:Car { c1.initJoin |> c2.initJoin -> 0 }
		allCars1 = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Car.class));
		c1 = allCars1.getVariable();
		allCars2 = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Car.class));
		c2 = allCars2.getVariable();
		Rule r5 = new FOILRule(allCars1,
				new FOILRule(allCars2,
						new ConjunctiveTerm(
								new PortReference(c1, "initJoin"),
								new PortReference(c2, "initJoin")
								)
						)
				);

		// \forall c1:Car {
		//		\forall c2:Car { c1.finishJoin |> c2.finishJoin -> 0 }
		allCars1 = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Car.class));
		c1 = allCars1.getVariable();
		allCars2 = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Car.class));
		c2 = allCars2.getVariable();
		Rule r6 = new FOILRule(allCars1,
				new FOILRule(allCars2,
						new ConjunctiveTerm(
								new PortReference(c1, "finishJoin"),
								new PortReference(c2, "finishJoin")
								)
						)
				);

		// \forall c1:Car {
		//		\exists c2:Car { c1.ackSplit |> c2.initSplit -> 0 }
		allCars1 = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Car.class));
		c1 = allCars1.getVariable();
		allCars2 = new Declaration(
				Quantifier.EXISTS,
				scope,
				new TypeRestriction(Car.class));
		c2 = allCars2.getVariable();
		Rule r7 = new FOILRule(allCars1,
				new FOILRule(allCars2,
						new ConjunctiveTerm(
								new PortReference(c1, "ackSplit"),
								new PortReference(c2, "initSplit")
								)
						)
				);

		// \forall c1:Car {
		//		\forall c2:Car { c1.initSplit |> c2.ackSplit \/ c1=c2 -> 0 }
		allCars1 = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Car.class));
		c1 = allCars1.getVariable();
		allCars2 = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Car.class));
		c2 = allCars2.getVariable();
		Rule r8 = new FOILRule(allCars1,
				new FOILRule(allCars2,
						new ConjunctiveTerm(
								new PortReference(c1, "initSplit"),
								new Or(
										new SameInstance(c1,c2),
										new PortReference(c2, "ackSplit")
										)
								)
						)
				);

		// \forall c1:Car {
		//		\forall c2:Car { c1.closeSplit |> c2.closeSplit -> 0 }
		allCars1 = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Car.class));
		c1 = allCars1.getVariable();
		allCars2 = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Car.class));
		c2 = allCars2.getVariable();
		Rule r9 = new FOILRule(allCars1,
				new FOILRule(allCars2,
						new ConjunctiveTerm(
								new PortReference(c1, "closeSplit"),
								new PortReference(c2, "closeSplit")
								)
						)
				);

		return new AndRule(r1,r2,r3,r4,r5,r6,r8);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Car[] cars = {new Car(0.0, 1.5), new Car(2.0,2.0)};//, new Car(5,1)};
		AbstractMotif platoon = new Platoon(null,cars);
		System.out.println(platoon.getJSONDescriptor());
		System.out.println(platoon.getExpandedRule().toString());
		System.out.println(Arrays.stream(platoon.getAllowedInteractions()).map(Interaction::toString).collect(Collectors.joining("\n")));

		//		ExecutionEngine ex = new ExecutionEngine(platoon,GreedyStrategy.getInstance(),new ConsoleOutput(),false,3);
		//		ex.setSnapshotSemantics(true);
		//		ex.run();
	}

}
