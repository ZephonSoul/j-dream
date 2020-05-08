/**
 * 
 */
package com.dream.test.benchmarks;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
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
import com.dream.core.coordination.constraints.predicates.Equals;
import com.dream.core.coordination.constraints.predicates.LessThan;
import com.dream.core.coordination.constraints.predicates.SameInstance;
import com.dream.core.coordination.maps.MapNodeForEntity;
import com.dream.core.coordination.maps.MapNodeInstance;
import com.dream.core.coordination.maps.MapNodeRef;
import com.dream.core.entities.AbstractMotif;
import com.dream.core.entities.maps.MapNode;
import com.dream.core.entities.maps.predefined.DummyMap;
import com.dream.core.exec.GreedyStrategy;
import com.dream.core.expressions.AbsValue;
import com.dream.core.expressions.Difference;
import com.dream.core.expressions.PoolSize;
import com.dream.core.expressions.Sum;
import com.dream.core.expressions.VariableMapProperty;
import com.dream.core.expressions.VariableRef;
import com.dream.core.expressions.values.NumberValue;
import com.dream.core.operations.Assign;
import com.dream.core.operations.CreateMapNode;
import com.dream.core.operations.OperationsSequence;
import com.dream.core.output.ConsoleOutput;
import com.dream.test.benchmarks.platooning.Car;
import com.dream.test.benchmarks.platooning.Platoon;

/**
 * @author alessandro
 *
 */
public class Platooning extends AbstractMotif {

	/**
	 * @param pool
	 */
	public Platooning(Set<Entity> pool, double joinDistance) {
		super(null, pool, new DummyMap());
		map.setOwner(this);

		MapNode node = map.getNodes().stream().findFirst().get();
		pool.parallelStream().forEach(e -> map.setEntityMapping(e, node));

		EntityInstanceActual scope = new EntityInstanceActual(this);		
		// \forall p : Platoon {
		//		\forall c : p.Car {
		//			\exists p' : Platoon{ 
		//				c.initJoin |> p != p' /\ (tail(p').pos - head(p).pos < Delta) 
		//				-> addNode(p'); c.speed := head(p').speed; 
		//					@(c).newLeader := head(p').id; @(c).newLoc := mapSize(p')+@(c) }}}
		Declaration allPlatoons1 = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Platoon.class));
		EntityInstanceRef p1 = allPlatoons1.getVariable();
		Declaration allCars = new Declaration(
				Quantifier.FORALL,
				p1,
				new TypeRestriction(Car.class));
		EntityInstanceRef c = allCars.getVariable();
		Declaration allPlatoons2 = new Declaration(
				Quantifier.EXISTS,
				scope,
				new TypeRestriction(Platoon.class));
		EntityInstanceRef p2 = allPlatoons2.getVariable();
		MapNodeRef n = new MapNodeRef();
		Rule r1 = 
				new FOILRule(allPlatoons1, 
						new FOILRule(allCars,
								new FOILRule(allPlatoons2,
										new ConjunctiveTerm(
												new PortReference(c, "initJoin"), 
												new And(
														new Not(new SameInstance(p1,p2)),
														new LessThan(
																new AbsValue(
																		new Difference(
																				new VariableMapProperty(p2,"tail","pos"),
																				new VariableMapProperty(p1,"head","pos")
																				)
																		),
																new NumberValue(joinDistance)
																)
														),
												new CreateMapNode(p2, n, 
														new OperationsSequence(
																new Assign(new VariableRef(c,"speed"),
																		new VariableMapProperty(p2,"head","speed")),
																new Assign(new VariableRef(new MapNodeForEntity(c),"newLeader"),
																		new VariableMapProperty(p2,"head","id")),
																new Assign(new VariableRef(new MapNodeForEntity(c),"newLoc"),
																		new Sum(
																				new PoolSize(p2),//TODO: replace with MapSize
																				new VariableRef(new MapNodeForEntity(c),"index")
																				)))
														)
												))));

		allPlatoons1 = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Platoon.class));
		p1 = allPlatoons1.getVariable();
		allCars = new Declaration(
				Quantifier.FORALL,
				p1,
				new TypeRestriction(Car.class));
		c = allCars.getVariable();
		Rule r2 = 
				new FOILRule(allPlatoons1,
						new FOILRule(allCars,
								new Term(new Not(
										new Or(
												new PortReference(c,"initSplit"),
												new PortReference(c,"ackSplit"))
										))));
		setRule(new AndRule(r1,r2));
		
	}


	public static void main(String[] args) {
		Car[] cars1 = {new Car(2.0,2.0),new Car(0.0, 1.5)};
		AbstractMotif platoon1 = new Platoon(null,cars1);
		System.out.println(platoon1.getMapProperty("tail").get().toString());
		Car[] cars2 = {new Car(4.0,1.0),new Car(2.5, 1.0)};
		AbstractMotif platoon2 = new Platoon(null,cars2);
		Set<Entity> pool = new HashSet<>();
		pool.add(platoon1);
		pool.add(platoon2);
		Platooning road = new Platooning(pool,1);

		System.out.println(road.getJSONDescriptor());
		System.out.println(road.getExpandedRule().toString());
		//		System.out.println(road.getExpandedRule().sat(new Interaction()));
		//		System.out.println(Arrays.stream(road.getAllowedInteractions()).map(Interaction::toString).collect(Collectors.joining("\n")));


		ExecutionEngine ex = new ExecutionEngine(road,GreedyStrategy.getInstance(),new ConsoleOutput(),false,2);
		ex.setSnapshotSemantics(true);
		ex.run();
	}

}
