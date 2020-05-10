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
import com.dream.core.coordination.constraints.predicates.GreaterThan;
import com.dream.core.coordination.constraints.predicates.LessThan;
import com.dream.core.coordination.constraints.predicates.SameInstance;
import com.dream.core.coordination.maps.MapNodeForEntity;
import com.dream.core.coordination.maps.MapNodeInstance;
import com.dream.core.coordination.maps.MapNodeRef;
import com.dream.core.coordination.maps.MapNodeVarEquals;
import com.dream.core.entities.AbstractMotif;
import com.dream.core.entities.maps.MapNode;
import com.dream.core.entities.maps.predefined.DummyMap;
import com.dream.core.exec.GreedyStrategy;
import com.dream.core.expressions.AbsValue;
import com.dream.core.expressions.Difference;
import com.dream.core.expressions.MapSize;
import com.dream.core.expressions.PoolSize;
import com.dream.core.expressions.Product;
import com.dream.core.expressions.Sum;
import com.dream.core.expressions.VariableMapProperty;
import com.dream.core.expressions.VariableRef;
import com.dream.core.expressions.values.NumberValue;
import com.dream.core.expressions.values.Value;
import com.dream.core.operations.Assign;
import com.dream.core.operations.CreateInstance;
import com.dream.core.operations.CreateMapNode;
import com.dream.core.operations.DeleteInstance;
import com.dream.core.operations.DeleteMapNode;
import com.dream.core.operations.ForLoop;
import com.dream.core.operations.IfThenElse;
import com.dream.core.operations.MigrateMotif;
import com.dream.core.operations.OperationsSequence;
import com.dream.core.output.ConsoleOutput;
import com.dream.test.benchmarks.platooning.Car;
import com.dream.test.benchmarks.platooning.Platoon;

/**
 * @author alessandro
 *
 */
public class Platooning extends AbstractMotif {

	public static Value speedUp = new NumberValue(1.20);
	public static Value speedDown = new NumberValue(0.80);

	/**
	 * @param pool
	 */
	public Platooning(Set<Entity> pool, double joinDistance) {
		super(null, pool, new DummyMap());
		map.setOwner(this);

		MapNode node = map.getNodes().stream().findFirst().get();
		pool.parallelStream().forEach(e -> map.setEntityMapping(e, node));

		EntityInstanceActual scope = new EntityInstanceActual(this);		

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
		// \forall p : Platoon {
		//		\forall c : p.Car {
		//			\exists p' : Platoon{ 
		//				c.initJoin |> p != p' /\ (tail(p').pos - head(p).pos < Delta) 
		//				-> addNode(p'); c.speed := head(p').speed; 
		//					@(c).newLeader := head(p').id; @(c).newLoc := mapSize(p')+@(c) }}}
		Rule join1 = 
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
		allPlatoons2 = new Declaration(
				Quantifier.EXISTS,
				scope,
				new TypeRestriction(Platoon.class));
		p2 = allPlatoons2.getVariable();
		// \forall p1 : Platoon {
		//		\forall c : Car {
		//			\exists p2 : Platoon {
		//				c.finishJoin |> head(p2).id = @(c).newLeader
		//				-> migrate(c,p2,@(c).newLoc); delete(p1)
		Rule join2 = 
				new FOILRule(allPlatoons1,
						new FOILRule(allCars,
								new FOILRule(allPlatoons2,
										new ConjunctiveTerm(
												new PortReference(c, "finishJoin"),
												new Equals(
														new VariableMapProperty(p2,"head","id"),
														new VariableRef(new MapNodeForEntity(c),"newLeader")
														),
												new OperationsSequence(
														new MigrateMotif(
																c, 
																p2, 
																new MapNodeVarEquals(
																		p2,
																		"index",
																		new VariableRef(
																				new MapNodeForEntity(c), 
																				"newLoc")
																		)
																),
														new DeleteInstance(p1)
														)
												)
										)
								)
						);

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
		p2 = new EntityInstanceRef();
		n = new MapNodeRef();
		VariableRef i = new VariableRef("i");
		// \forall p1 : Platoon {
		//		\forall c: Car {
		//			c.initSplit |> TRUE ->	create(p2 : Platoon, this, @(p1)) [
		//										createNode(n,p2)[ migrate(c,p2,n) ];
		//										FOR (i \in [@(c).index+1,mapSize(p)-1]) DO [createNode(p2)]];
		//									deleteNode(@(c))}}
		Rule split1 = 
				new FOILRule(allPlatoons1,
						new FOILRule(allCars,
								new ConjunctiveTerm(
										new PortReference(c,"initSplit"),
										new OperationsSequence(
												new CreateInstance(
														Platoon.class, 
														scope, 
														p2, 
														new OperationsSequence(
																new CreateMapNode(
																		p2, 
																		n, 
																		new MigrateMotif(
																				c, 
																				p2, 
																				n)),
																new ForLoop(
																		i,
																		new Sum(
																				new NumberValue(1),
																				new VariableRef(new MapNodeForEntity(c),"index")),
																		new MapSize(p1),
																		new CreateMapNode(p2))
																)
														)
												,new DeleteMapNode(new MapNodeForEntity(c))
												)
										)
								)
						);

		allPlatoons1 = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Platoon.class));
		p1 = allPlatoons1.getVariable();
		Declaration allCars1 = new Declaration(
				Quantifier.FORALL,
				p1,
				new TypeRestriction(Car.class));
		EntityInstanceRef c1 = allCars1.getVariable();
		Declaration allCars2 = new Declaration(
				Quantifier.FORALL,
				p1,
				new TypeRestriction(Car.class));
		EntityInstanceRef c2 = allCars2.getVariable();
		// \forall p : Platoon {
		//		\forall c1 : p.Car {
		//			\forall c2 : p.Car {
		//				c1.initSplit |> c2.ackSplit \/ @(c1).index >= @(c2).index
		//				-> IF (@(c1).index <= @(c2).index) THEN [
		//					@(c2).newLeader := c1.id;
		//					@(c2).newLoc := @(c2).index - @(c1).index]
		//				ELSE [ c2.speed := c2.speed * Delta_v ]
		Rule split2 = new FOILRule(allPlatoons1,
				new FOILRule(allCars1,
						new FOILRule(allCars2,
								new ConjunctiveTerm(
										new PortReference(c1,"initSplit"),
										new Or(
												new PortReference(c2,"ackSplit"),
												new Not(
														new LessThan(
																new VariableRef(new MapNodeForEntity(c1),"index"),
																new VariableRef(new MapNodeForEntity(c2),"index")
																))
												),
										new IfThenElse(
												new Not(
														new GreaterThan(
																new VariableRef(new MapNodeForEntity(c1),"index"),
																new VariableRef(new MapNodeForEntity(c2),"index")
																)
														), 
												new OperationsSequence(
														new Assign(
																new VariableRef(new MapNodeForEntity(c2),"newLeader"),
																new VariableRef(c1,"id")
																),
														new Assign(
																new VariableRef(new MapNodeForEntity(c2),"newLoc"),
																new Difference(
																		new VariableRef(new MapNodeForEntity(c2),"index"),
																		new VariableRef(new MapNodeForEntity(c1),"index")
																		)
																)
														),
												new Assign(
														new VariableRef(c2,"speed"),
														new Product(new VariableRef(c2,"speed"),speedUp)
														)
												)
										)
								)
						)
				);

		allPlatoons1 = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Platoon.class));
		p1 = allPlatoons1.getVariable();
		allCars1 = new Declaration(
				Quantifier.FORALL,
				p1,
				new TypeRestriction(Car.class));
		c1 = allCars1.getVariable();
		allCars2 = new Declaration(
				Quantifier.EXISTS,
				p1,
				new TypeRestriction(Car.class));
		c2 = allCars2.getVariable();
		// \forall p : Platoon {
		//		\forall c1 : p.Car {
		//			\exists c2 : p.Car {
		//				c1.ackSplit |> c2.initSplit /\ @(c2).index < @(c1).index
		Rule split2b = new FOILRule(allPlatoons1,
				new FOILRule(allCars1,
						new FOILRule(allCars2,
								new ConjunctiveTerm(
										new PortReference(c1,"ackSplit"),
										new And(
												new PortReference(c2,"initSplit"),
												new LessThan(
														new VariableRef(new MapNodeForEntity(c2),"index"),
														new VariableRef(new MapNodeForEntity(c1),"index")
														)
												)
										)
								)
						)
				);

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
		allPlatoons2 = new Declaration(
				Quantifier.EXISTS,
				scope,
				new TypeRestriction(Platoon.class));
		p2 = allPlatoons2.getVariable();
		// \forall p1 : Platoon {
		//		\forall c : p.Car {
		//			\exists p2 : Platoon{ 
		//				c.closeSplit |> @(c).newLeader=head(p2).id \/ c.id=head(p2).id 
		//				-> c.speed := c.speed * speedDown;
		//					IF (c.id != head(p2).id) THEN [migrate(c,p2,@(c).newLoc] }}}
		Rule split3 = new FOILRule(allPlatoons1,
				new FOILRule(allCars,
						new FOILRule(allPlatoons2,
								new ConjunctiveTerm(
										new PortReference(c,"closeSplit"),
										new Or(
												new Equals(
														new VariableRef(new MapNodeForEntity(c),"newLeader"),
														new VariableMapProperty(p2,"head","id")
														),
												new Equals(
														new VariableRef(c,"id"),
														new VariableMapProperty(p2,"head","id")
														)
												),
										new OperationsSequence(
												new Assign(
														new VariableRef(c,"speed"),
														new Product(new VariableRef(c,"speed"),speedDown)
														),
												new IfThenElse(
														new Not(
																new Equals(
																		new VariableRef(c,"id"),
																		new VariableMapProperty(p2,"head","id")
																		)
																),
														new OperationsSequence(
																new MigrateMotif(
																		c,
																		p2,
																		new MapNodeVarEquals(
																				p2,
																				"index",
																				new VariableRef(new MapNodeForEntity(c),"newLoc")
																				)
																		),
																new DeleteMapNode(new MapNodeForEntity(c))
																)
														)
												)
										))));

		//		allPlatoons1 = new Declaration(
		//				Quantifier.FORALL,
		//				scope,
		//				new TypeRestriction(Platoon.class));
		//		p1 = allPlatoons1.getVariable();
		//		allCars = new Declaration(
		//				Quantifier.FORALL,
		//				p1,
		//				new TypeRestriction(Car.class));
		//		c = allCars.getVariable();
		//		Rule r0 = 
		//				new FOILRule(allPlatoons1,
		//						new FOILRule(allCars,
		//								new Term(new Not(new PortReference(c,"ackSplit")))));
		//										new Or(
		//												new PortReference(c,"initSplit"),
		//												new PortReference(c,"ackSplit"))
		//										))));


		setRule(new AndRule(join1,join2,split1,split2,split2b,split3));

	}


	public static void main(String[] args) {

		//		Car[] cars1 = {new Car(2.0,2.0),new Car(0.0, 1.5)};
		//		AbstractMotif platoon1 = new Platoon(null,cars1);
		//		Car[] cars2 = {new Car(4.0,1.0),new Car(2.5, 1.0)};
		//		AbstractMotif platoon2 = new Platoon(null,cars2);
		Car[] cars3 = {new Car(0.0,2.0),new Car(3.0, 1.0),new Car(5.0, 1.5)};
		AbstractMotif platoon3 = new Platoon(null,cars3);
		Set<Entity> pool = new HashSet<>();
		//		pool.add(platoon1);
		//		pool.add(platoon2);
		pool.add(platoon3);
		Platooning road = new Platooning(pool,1);

		System.out.println(road.getJSONDescriptor());
		System.out.println(road.getExpandedRule().toString());
		System.out.println(road.getExpandedRule().sat(new Interaction()));
		System.out.println(Arrays.stream(road.getAllowedInteractions()).map(Interaction::toString).collect(Collectors.joining("\n")));

		ExecutionEngine ex = new ExecutionEngine(road,GreedyStrategy.getInstance(),new ConsoleOutput(),false,2);
		ex.setSnapshotSemantics(true);
		ex.run();
	}

}
