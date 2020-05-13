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
import com.dream.core.IDFactory;
import com.dream.core.coordination.AndRule;
import com.dream.core.coordination.ConjunctiveTerm;
import com.dream.core.coordination.Declaration;
import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.EntityInstanceRef;
import com.dream.core.coordination.FOILRule;
import com.dream.core.coordination.Interaction;
import com.dream.core.coordination.Quantifier;
import com.dream.core.coordination.Rule;
import com.dream.core.coordination.TypeRestriction;
import com.dream.core.coordination.constraints.And;
import com.dream.core.coordination.constraints.Not;
import com.dream.core.coordination.constraints.Or;
import com.dream.core.coordination.constraints.PortReference;
import com.dream.core.coordination.constraints.predicates.Equals;
import com.dream.core.coordination.constraints.predicates.GreaterThan;
import com.dream.core.coordination.constraints.predicates.LessThan;
import com.dream.core.coordination.constraints.predicates.SameInstance;
import com.dream.core.coordination.maps.MapNodeActual;
import com.dream.core.coordination.maps.MapNodeForEntity;
import com.dream.core.coordination.maps.MapNodeRef;
import com.dream.core.coordination.maps.MapNodeVarEquals;
import com.dream.core.coordination.maps.MapPropertyRef;
import com.dream.core.entities.AbstractMotif;
import com.dream.core.entities.maps.MapNode;
import com.dream.core.entities.maps.predefined.DummyMap;
import com.dream.core.exec.GreedyStrategy;
import com.dream.core.expressions.Difference;
import com.dream.core.expressions.MapSize;
import com.dream.core.expressions.Product;
import com.dream.core.expressions.Sum;
import com.dream.core.expressions.VariableRef;
import com.dream.core.expressions.values.NumberValue;
import com.dream.core.expressions.values.Value;
import com.dream.core.operations.Assign;
import com.dream.core.operations.CreateMapNode;
import com.dream.core.operations.CreateMotifInstance;
import com.dream.core.operations.DeleteInstance;
import com.dream.core.operations.DeleteMapNode;
import com.dream.core.operations.ForLoop;
import com.dream.core.operations.IfThenElse;
import com.dream.core.operations.MigrateMotif;
import com.dream.core.operations.OperationsSequence;
import com.dream.core.output.ConsoleOutput;
import com.dream.core.output.DummyOutput;
import com.dream.test.benchmarks.platooning.Car;
import com.dream.test.benchmarks.platooning.Platoon;
import com.dream.test.benchmarks.platooning.PlatoonOrig;

/**
 * @author Alessandro Maggi
 *
 */
public class Platooning extends AbstractMotif {

	public Value speedUp;
	public Value speedDown;

	/**
	 * @param pool
	 */
	public Platooning(Set<Entity> pool, double joinDistance, double speedDelta) {
		super(null, pool, new DummyMap());
		map.setOwner(this);

		MapNode node = map.getNodes().stream().findFirst().get();
		pool.parallelStream().forEach(e -> map.setEntityMapping(e, node));

		speedUp = new NumberValue(1 + speedDelta);
		speedDown = new NumberValue(1 - speedDelta);

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
		// force p1 != p2
		//allPlatoons2.setInstanceFilter(new Not(new SameInstance(p1,p2)));
		MapNodeRef n = new MapNodeRef();
		// \forall p : Platoon {
		//		\forall c : p.Car {
		//			\exists p' : Platoon{ 
		//				c.initJoin |> p != p' /\ (0 < tail(p').pos - head(p).pos < Delta) /\ head(p').ackJoin
		//					-> addNode(p'); c.speed := head(p').speed; 
		//						@(c).newLeader := head(p').id; @(c).newLoc := mapSize(p')+@(c) }}}}
		Rule join1 = 
				new FOILRule(allPlatoons1, 
						new FOILRule(allCars,
								new FOILRule(allPlatoons2,
										new ConjunctiveTerm(
												new PortReference(c, "initJoin"), 
												new And(
														new Not(new SameInstance(p1,p2)),
														new LessThan(
																new NumberValue(0),
																new Difference(
																		new VariableRef(new MapPropertyRef<>(p2,"tail"),"pos"),
																		new VariableRef(new MapPropertyRef<>(p1,"head"),"pos")
																		),
																new NumberValue(joinDistance)
																),
														new PortReference(new MapPropertyRef<>(p2,"head"),"ackJoin")
														),
												new CreateMapNode(p2, n, 
														new OperationsSequence(
																new Assign(new VariableRef(c,"speed"),
																		new VariableRef(new MapPropertyRef<>(p2,"head"),"speed")),
																new Assign(new VariableRef(new MapNodeForEntity(c),"newLeader"),
																		new VariableRef(new MapPropertyRef<>(p2,"head"),"id")),
																new Assign(new VariableRef(new MapNodeForEntity(c),"newLoc"),
																		new Sum(
																				new MapSize(p2),
																				new VariableRef(new MapNodeForEntity(c),"index")
																				)))
														)
												))));

		allPlatoons1 = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Platoon.class));
		p1 = allPlatoons1.getVariable();
		allPlatoons2 = new Declaration(
				Quantifier.EXISTS,
				scope,
				new TypeRestriction(Platoon.class));
		p2 = allPlatoons2.getVariable();
		//allPlatoons2.setInstanceFilter(new Not(new SameInstance(p1,p2)));
		// \forall p1 : Platoon { \exists p2 : Platoon { 
		//		head(p1).ackJoin |> head(p2).initJoin /\ (0 < tail(p1).pos - head(p2).pos < Delta) } }
		Rule join1b = new FOILRule(allPlatoons1,
				new FOILRule(allPlatoons2,
						new ConjunctiveTerm(
								new PortReference(new MapPropertyRef<>(p1,"head"),"ackJoin"),
								new And(
										new PortReference(new MapPropertyRef<>(p2,"head"),"initJoin"),
										new LessThan(
												new NumberValue(0),
												new Difference(
														new VariableRef(new MapPropertyRef<>(p1,"tail"),"pos"),
														new VariableRef(new MapPropertyRef<>(p2,"head"),"pos")
														),
												new NumberValue(joinDistance)
												)
										)
								)
						));

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
		// force p1 != p2
		//allPlatoons2.setInstanceFilter(new Not(new SameInstance(p1,p2)));
		// \forall p1 : Platoon {
		//		\forall c : Car {
		//			\exists p2 : Platoon {
		//				c.finishJoin |> head(p2).finishJoin /\ ( head(p2).id = @(c).newLeader \/ p1 = p2)
		//				-> IF (p1 != p2) THEN [migrate(c,p2,@(c).newLoc); delete(p1)]
		Rule join2 = 
				new FOILRule(allPlatoons1,
						new FOILRule(allCars,
								new FOILRule(allPlatoons2,
										new ConjunctiveTerm(
												new PortReference(c, "finishJoin"),
												new And(
														new PortReference(new MapPropertyRef<>(p2,"head"),"finishJoin"),
														new Or(
																new Equals(
																		new VariableRef(new MapPropertyRef<>(p2,"head"),"id"),
																		new VariableRef(new MapNodeForEntity(c),"newLeader")
																		),
																new SameInstance(p1,p2)
																)
														),
												new IfThenElse(
														new Not(new SameInstance(p1,p2)),
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
		//			c.initSplit |> TRUE ->	c.speed := c.speed * speedDown;
		//										create(p2 : Platoon, this, @(p1))[ createNode(n,p2)[ migrate(c,p2,n) ];
		//										FOR (i \in [@(c).index+1,mapSize(p)-1]) DO [createNode(p2)]];
		//										deleteNode(@(c))}}
		Rule split1 = 
				new FOILRule(allPlatoons1,
						new FOILRule(allCars,
								new ConjunctiveTerm(
										new PortReference(c,"initSplit"),
										new OperationsSequence(
												new Assign(
														new VariableRef(c,"speed"),
														new Product(new VariableRef(c,"speed"),speedDown)
														),
												new CreateMotifInstance(
														Platoon.class, 
														scope,
														new MapNodeActual(((DummyMap)this.map).getNode()),
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
				Quantifier.EXISTS,
				p1,
				new TypeRestriction(Car.class));
		EntityInstanceRef c2 = allCars2.getVariable();
		// \forall p : Platoon {
		//		\forall c1 : p.Car {
		//			\exists c2 : p.Car {
		//				c1.ackSplit |> c2.initSplit
		//				-> IF (@(c1).index > @(c2).index) THEN [
		//UPD			-> IF (c2.pos > c1.pos) THEN [
		//					@(c1).newLeader := c2.id;
		//					@(c1).newLoc := @(c1).index - @(c2).index;
		//MOD				c1.speed := c1.speed * speedDown]
		//				ELSE [ c1.speed := c1.speed * speedUp ]
		Rule split2 = new FOILRule(allPlatoons1,
				new FOILRule(allCars1,
						new FOILRule(allCars2,
								new ConjunctiveTerm(
										new PortReference(c1,"ackSplit"),
										new PortReference(c2,"initSplit"),
										new IfThenElse(
												new GreaterThan(
														new VariableRef(new MapNodeForEntity(c1),"index"),
														new VariableRef(new MapNodeForEntity(c2),"index")
//														new VariableRef(c2,"pos"),
//														new VariableRef(c1,"pos")
														), 
												new OperationsSequence(
														new Assign(
																new VariableRef(new MapNodeForEntity(c1),"newLeader"),
																new VariableRef(c2,"id")
																),
														new Assign(
																new VariableRef(new MapNodeForEntity(c1),"newLoc"),
																new Difference(
																		new VariableRef(new MapNodeForEntity(c1),"index"),
																		new VariableRef(new MapNodeForEntity(c2),"index")
																		)
																),
														new Assign(
																new VariableRef(c1,"speed"),
																new Product(new VariableRef(c1,"speed"),speedDown)
																)
														),
												new Assign(
														new VariableRef(c1,"speed"),
														new Product(new VariableRef(c1,"speed"),speedUp)
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
		// force p1 != p2
		//allPlatoons2.setInstanceFilter(new Not(new SameInstance(p1,p2)));
		// \forall p1 : Platoon {
		//		\forall c : p.Car {
		//			\exists p2 : Platoon{ 
		//				c.closeSplit |> (head(p2).closeSplit /\ @(c).newLeader=head(p2).id) \/ (@(c).newLeader=-1)
		//				->	IF (@(c).newLeader!=-1) THEN [migrate(c,p2,@(c).newLoc] }}}
		Rule split3 = new FOILRule(allPlatoons1,
				new FOILRule(allCars,
						new FOILRule(allPlatoons2,
								new ConjunctiveTerm(
										new PortReference(c,"closeSplit"),
										new Or(
												new And(
														new Equals(
																new VariableRef(new MapNodeForEntity(c),"newLeader"),
																new VariableRef(new MapPropertyRef<>(p2,"head"),"id")
																//new VariableMapProperty(p2,"head","id")
																),
														new PortReference(new MapPropertyRef<>(p2,"head"),"closeSplit")
														),
												new Equals(
														new VariableRef(new MapNodeForEntity(c),"newLeader"),
														new NumberValue(-1)
														//new VariableRef(c,"id"),
														//new VariableRef(new MapPropertyRef<Instance<Entity>>(p2,"head"),"id")
														//new VariableMapProperty(p2,"head","id")
														)
												),
										//new OperationsSequence(
										//new Assign(
										//new VariableRef(c,"speed"),
										//new Product(new VariableRef(c,"speed"),speedDown)
										//),
										new IfThenElse(
												new Not(
														new Equals(
																new VariableRef(new MapNodeForEntity(c),"newLeader"),
																new NumberValue(-1)
																//new VariableRef(c,"id"),
																//new VariableRef(new MapPropertyRef<Instance<Entity>>(p2,"head"),"id")
																//new VariableMapProperty(p2,"head","id")
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
								//										)
								)));

//		allPlatoons1 = new Declaration(
//				Quantifier.FORALL,
//				scope,
//				new TypeRestriction(Platoon.class));
//		p1 = allPlatoons1.getVariable();
//		// TRUE -> JavaOp(this,this.map.refresh())
//		Rule R_refresh = new FOILRule(allPlatoons1,
//				new Term(
//						new JavaOperation<Entity>(
//								p1, 
//								(context) -> ((AbstractMotif)context).getMap().refresh()
//								)
//						)
//				);

		setRule(new AndRule(join1,join1b,join2,split1,split2,split3));

	}

	private static Platooning generateConfiguration(
			int platoonsNumber, 
			int carsPerPlatoon,
			double avgSpeed,
			double avgSpeedDelta,
			double avgInterCarSpace,
			double avgInterCarSpaceDelta,
			double avgInterPlatoonSpace,
			double avgInterPlatoonSpaceDelta,
			double joinDistance,
			double speedDelta) {

		double globalPos = 0;
		double baseCarSpeed = avgSpeed - avgSpeedDelta/2;
		double baseCarSpace = avgInterCarSpace - avgInterCarSpaceDelta/2;
		double basePlatoonSpace = avgInterPlatoonSpace - avgInterPlatoonSpaceDelta/2;
		Set<Entity> pool = new HashSet<>();
		for (int i=0; i<platoonsNumber; i++) {
			Car[] cars = new Car[carsPerPlatoon];
			for (int j=0; j<carsPerPlatoon; j++) {
				if (j!=0)
					globalPos = globalPos + baseCarSpace + avgInterCarSpaceDelta * Math.random();
				cars[carsPerPlatoon-j-1] = new Car(globalPos,(baseCarSpeed + avgSpeedDelta * Math.random()));
			}
			pool.add(new PlatoonOrig(null,cars));
			globalPos = globalPos + basePlatoonSpace + avgInterPlatoonSpaceDelta * Math.random();
		}
		return new Platooning(pool,joinDistance,speedDelta);
	}



	public static void main(String[] args) {

		// number of platoons
		int[] pN = {2};//6,4,3,2};
		// number of cars per platoon
		int[] cN = {3};//2,3,4,6};
		// average car speed
		double[] avgS = {40,40,40,40,40,40};
		// average speed variance
		double[] avgSVar = {0,0,0,0,0,0};//{10,10,10,10,10,10};
		// average inter-car spacing
		double[] avgCS = {15,15,15,15,15,15};
		// average inter-car spacing variance
		double[] avgCSVar = {0,0,0,0,0,0};////{5,5,5,5,5,5};
		// average inter-platoon spacing
		double[] avgPS = {30,30,30,30,30,30};
		// average inter-platoon spacing variance
		double[] avgPSVar = {0,0,0,0,0,0};//{10,10,10,10,10,10};
		// minimum join distance
		double[] jD = {25,25,25,25,25,25};
		// relative speed variation after splitting
		double[] sD = {0.2,0.2,0.2,0.2,0.2,0.2};
		// number of cycles
		int[] cycles = {50,100,100,100,100,100,100};

		for (int i=0; i<pN.length; i++) {
			IDFactory.getInstance().resetFactory();
			Platooning road = generateConfiguration(pN[i],cN[i],avgS[i],avgSVar[i],avgCS[i],avgCSVar[i],avgPS[i],avgPSVar[i],jD[i],sD[i]);
			String output = String.format("platooning_%d_%d_%.2f_%.2f_%.2f_%.2f_%.2f_%.2f_%.2f_%.2f_%dc.json",
					pN[i],cN[i],avgS[i],avgSVar[i],avgCS[i],avgCSVar[i],avgPS[i],avgPSVar[i],jD[i],sD[i],cycles[i]);
//			System.out.println(Arrays.stream(road.getAllowedInteractions()).map(Interaction::toString).collect(Collectors.joining("\n")));
			System.out.println("\nWorking on: " + output + "\n");
			ExecutionEngine ex = new ExecutionEngine(road,GreedyStrategy.getInstance(),new ConsoleOutput(),false,cycles[i],output);
			ex.setSnapshotSemantics(true);
			ex.run();
		}
	}

}
