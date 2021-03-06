/**
 * 
 */
package com.dream.test.benchmarks;

import java.util.HashSet;
import java.util.Set;

import com.dream.ExecutionEngine;
import com.dream.core.Entity;
import com.dream.core.IDFactory;
import com.dream.core.Instance;
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
import com.dream.core.coordination.constraints.Or;
import com.dream.core.coordination.constraints.PortReference;
import com.dream.core.coordination.constraints.predicates.CurrentControlLocation;
import com.dream.core.coordination.constraints.predicates.Equals;
import com.dream.core.coordination.constraints.predicates.GreaterThan;
import com.dream.core.coordination.constraints.predicates.LessThan;
import com.dream.core.coordination.constraints.predicates.SameInstance;
import com.dream.core.coordination.maps.MapNodeActual;
import com.dream.core.coordination.maps.MapPropertyRef;
import com.dream.core.entities.AbstractMotif;
import com.dream.core.entities.maps.MapNode;
import com.dream.core.entities.maps.predefined.DummyMap;
import com.dream.core.exec.GreedyStrategy;
import com.dream.core.expressions.Difference;
import com.dream.core.expressions.InstanceIdentifier;
import com.dream.core.expressions.PoolSize;
import com.dream.core.expressions.Product;
import com.dream.core.expressions.Sum;
import com.dream.core.expressions.VariableRef;
import com.dream.core.expressions.values.NumberValue;
import com.dream.core.expressions.values.Value;
import com.dream.core.operations.Assign;
import com.dream.core.operations.CreateMotifInstance;
import com.dream.core.operations.DeleteInstance;
import com.dream.core.operations.IfThenElse;
import com.dream.core.operations.MigrateMotif;
import com.dream.core.operations.OperationsSequence;
import com.dream.core.output.ConsoleOutput;
import com.dream.core.output.DummyOutput;
import com.dream.test.benchmarks.platooning2.Car;
import com.dream.test.benchmarks.platooning2.Platoon;

/**
 * @author Alessandro Maggi
 *
 */
public class Platooning2 extends AbstractMotif {

	public Value speedUp;
	public Value speedDown;

	/**
	 * @param pool
	 */
	public Platooning2(Set<Entity> pool, double joinDistance, double speedDelta) {
		super(null, pool, new DummyMap());
		map.setOwner(this);

		MapNode node = map.getNodes().stream().findFirst().get();
		pool.parallelStream().forEach(e -> map.setEntityMapping(e, node));

		speedUp = new NumberValue(1 + speedDelta);
		speedDown = new NumberValue(1 - speedDelta);
		NumberValue joinDist = new NumberValue(joinDistance);

		EntityInstanceActual scope = new EntityInstanceActual(this);		

		Declaration platoons1 = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Platoon.class));
		EntityInstanceRef p1 = platoons1.getVariable();
		platoons1.setInstanceFilter(new GreaterThan(new PoolSize(p1),new NumberValue(0)));
		Declaration cars = new Declaration(
				Quantifier.FORALL,
				p1,
				new TypeRestriction(Car.class));
		EntityInstanceRef c = cars.getVariable();
		Declaration platoons2 = new Declaration(
				Quantifier.EXISTS,
				scope,
				new TypeRestriction(Platoon.class));
		EntityInstanceRef p2 = platoons2.getVariable();
		platoons2.setInstanceFilter(new GreaterThan(new PoolSize(p2),new NumberValue(0)));
		Instance<Entity> lead_p1 = new MapPropertyRef<>(p1,"leader");
		Instance<Entity> lead_p2 = new MapPropertyRef<>(p2,"leader");
		Instance<Entity> tail_p1 = new MapPropertyRef<>(p1,"tail");
		Instance<Entity> tail_p2 = new MapPropertyRef<>(p2,"tail");
		// \forall p : Platoon [poolSize(p1)>0] {
		//		\forall c : p.Car {
		//			\exists p2 : Platoon [poolSize(p2)>0] { 
		//				c.join |> p != p' 
		//				/\ ( (0 < (tail(p2).pos + tail(p2).speed) - (leader(p1).pos + leader(p1).speed ) < joinDist) 
		//					\/ (0 < (tail(p1).pos + tail(p1).speed) - (leader(p2).pos + leader(p2).speed ) < joinDist) ) 
		//				/\ leader(p2).join
		//					-> IF (lead(p1).pos < lead(p2).pos) [ c.speed := tail(p2).speed; migrate(c,p2,root(p2)) ] }}}}
		Rule join1 = 
				new FOILRule(platoons1, 
						new FOILRule(cars,
								new FOILRule(platoons2,
										new ConjunctiveTerm(
												new PortReference(c, "join"), 
												new And(
														new Not(new SameInstance(p1,p2)),
														new Or(
																new LessThan(
																		new NumberValue(0),
																		new Difference(
																				new Sum(
																						new VariableRef(tail_p2,"pos"),
																						new VariableRef(tail_p2,"speed")
																						),
																				new Sum(
																						new VariableRef(lead_p1,"pos"),
																						new VariableRef(lead_p1,"speed")
																						)
																				),
																		joinDist
																		),
																new LessThan(
																		new NumberValue(0),
																		new Difference(
																				new Sum(
																						new VariableRef(tail_p1,"pos"),
																						new VariableRef(tail_p1,"speed")
																						),
																				new Sum(
																						new VariableRef(lead_p2,"pos"),
																						new VariableRef(lead_p2,"speed")
																						)
																				),
																		joinDist
																		)
																),
														new PortReference(lead_p2,"join")
														),
												new IfThenElse(
														new LessThan(
																new VariableRef(lead_p1,"pos"),
																new VariableRef(lead_p2,"pos")
																), 
														new OperationsSequence(
																new Assign(new VariableRef(c,"speed"),
																		new VariableRef(tail_p2,"speed")),
																new MigrateMotif(c,p2,new MapPropertyRef<>(p2,"root"))
																)
														)	
												)
										)
								)
						);

		platoons1 = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Platoon.class));
		p1 = platoons1.getVariable();
		platoons1.setInstanceFilter(new GreaterThan(new PoolSize(p1),new NumberValue(0)));
		platoons2 = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Platoon.class));
		p2 = platoons2.getVariable();
		platoons2.setInstanceFilter(new GreaterThan(new PoolSize(p2),new NumberValue(0)));
		// \forall p1 : Platoon[PoolSize(p1)>0] { \forall p2 : Platoon[PoolSize(p2)>0] { 
		//		(0 < tail(p1).pos+speed - head(p2).pos+speed < Delta) /\ lead(p1).cruising /\ lead(p2).cruising |> 
		//		p1 = p2 \/ ( leader(p2).join /\ leader(p1).join)  } }
		lead_p1 = new MapPropertyRef<>(p1,"leader");
		lead_p2 = new MapPropertyRef<>(p2,"leader");
		tail_p1 = new MapPropertyRef<>(p1,"tail");
		tail_p2 = new MapPropertyRef<>(p2,"tail");
		Rule join2 = new FOILRule(platoons1,
				new FOILRule(platoons2,
						new ConjunctiveTerm(
								new And(
										new CurrentControlLocation(lead_p1, "cruising"),
										new CurrentControlLocation(lead_p2, "cruising"),
										new LessThan(
												new NumberValue(0),
												new Difference(
														new Sum(
																new VariableRef(tail_p1,"pos"),
																new VariableRef(tail_p1,"speed")
																),
														new Sum(
																new VariableRef(lead_p2,"pos"),
																new VariableRef(lead_p2,"speed")
																)
														),
												joinDist
												)
										),
								new Or(
										new SameInstance(p1,p2),
										new And(
												new PortReference(lead_p1,"join"),
												new PortReference(lead_p2,"join")
												)
										)
								)
						));

		platoons1 = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Platoon.class));
		p1 = platoons1.getVariable();
		platoons1.setInstanceFilter(new GreaterThan(new PoolSize(p1),new NumberValue(0)));
		cars = new Declaration(
				Quantifier.FORALL,
				p1,
				new TypeRestriction(Car.class));
		c = cars.getVariable();
		p2 = new EntityInstanceRef();
		// \forall p1 : Platoon {
		//		\forall c: Car {
		//			c.initSplit |> TRUE ->	c.speed := c.speed * speedDown;
		//									c.followCar := c;
		//									p2 = create(Platoon, this, @(p1))[
		//										migrate(c,p2,root(p2)) ]}}
		Rule split1 = 
				new FOILRule(platoons1,
						new FOILRule(cars,
								new ConjunctiveTerm(
										new PortReference(c,"initSplit"),
										new OperationsSequence(
												new Assign(
														new VariableRef(c,"speed"),
														new Product(new VariableRef(c,"speed"),speedDown)
														),
												new Assign(
														new VariableRef(c,"followCar"),
														new InstanceIdentifier(c)),
												new CreateMotifInstance(
														Platoon.class, 
														scope,
														new MapNodeActual(((DummyMap)this.map).getNode()),
														p2, 
														new MigrateMotif(
																c,
																p2, 
																new MapPropertyRef<>(p2,"root"))
														)
												)
										)
								)
						);

		platoons1 = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Platoon.class));
		p1 = platoons1.getVariable();
		platoons1.setInstanceFilter(new GreaterThan(new PoolSize(p1),new NumberValue(0)));
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
		//				-> IF (c1.pos < c2.pos) THEN [
		//					c1.followCar := c2;
		//					c1.speed := c1.speed * speedDown]
		//				ELSE [ 
		//					c1.followCar := c1;
		//					c1.speed := c1.speed * speedUp ]
		Rule split2 = new FOILRule(platoons1,
				new FOILRule(allCars1,
						new FOILRule(allCars2,
								new ConjunctiveTerm(
										new PortReference(c1,"ackSplit"),
										new PortReference(c2,"initSplit"),
										new IfThenElse(
												new LessThan(
														new VariableRef(c1,"pos"),
														new VariableRef(c2,"pos")
														), 
												new OperationsSequence(
														new Assign(
																new VariableRef(c1,"followCar"),
																new InstanceIdentifier(c2)
																),
														new Assign(
																new VariableRef(c1,"speed"),
																new Product(new VariableRef(c1,"speed"),speedDown)
																)
														),
												new OperationsSequence(
														new Assign(
																new VariableRef(c1,"followCar"),
																new InstanceIdentifier(c1)
																),
														new Assign(
																new VariableRef(c1,"speed"),
																new Product(new VariableRef(c1,"speed"),speedUp)
																)
														)
												)
										)
								)
						)
				);


		platoons1 = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Platoon.class));
		p1 = platoons1.getVariable();
		platoons1.setInstanceFilter(new GreaterThan(new PoolSize(p1),new NumberValue(0)));
		cars = new Declaration(
				Quantifier.FORALL,
				p1,
				new TypeRestriction(Car.class));
		c = cars.getVariable();
		platoons2 = new Declaration(
				Quantifier.EXISTS,
				scope,
				new TypeRestriction(Platoon.class));
		p2 = platoons2.getVariable();
		platoons2.setInstanceFilter(new GreaterThan(new PoolSize(p2),new NumberValue(0)));
		// \forall p1 : Platoon {
		//		\forall c : p.Car {
		//			\exists p2 : Platoon{ 
		//				c.closeSplit |> (leader(p2).closeSplit /\ c.followCar = leader(p2)) \/ c.followCar = c
		//				->	IF (c.followCar != c) THEN [migrate(c,p2,root(p2)];
		//					c.followCar := 0 }}}
		Rule split3 = new FOILRule(platoons1,
				new FOILRule(cars,
						new FOILRule(platoons2,
								new ConjunctiveTerm(
										new PortReference(c,"closeSplit"),
										new Or(
												new Equals(
														new VariableRef(c,"followCar"),
														new InstanceIdentifier(c)
														),
												new And(
														new Equals(
																new VariableRef(c,"followCar"),
																new InstanceIdentifier(new MapPropertyRef<>(p2,"leader"))
																),
														new PortReference(new MapPropertyRef<>(p2,"leader"),"closeSplit")
														)
												),
										new OperationsSequence(
												new IfThenElse(
														new Not(
																new Equals(
																		new VariableRef(c,"followCar"),
																		new InstanceIdentifier(c)
																		)
																),
														new MigrateMotif(
																c,
																p2,
																new MapPropertyRef<>(p2,"root")
																)
														),
												new Assign(
														new VariableRef(c,"followCar"),
														new NumberValue(0))
												)
										)

								)
						)
				);

		platoons1 = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Platoon.class));
		p1 = platoons1.getVariable();
		platoons1.setInstanceFilter(new Equals(new PoolSize(p1),new NumberValue(0)));
		// clean-up
		// \forall p:Platoon[PoolSize(p)=0] { TRUE -> delete(p) }
		Rule cleanup = new FOILRule(platoons1,
				new Term(
						new DeleteInstance(p1)));

		setRule(new AndRule(join1,join2,split1,split2,split3,cleanup));

	}

	private static Platooning2 generateConfiguration(
			int platoonsNumber, 
			int carsPerPlatoon,
			double avgSpeed,
			double avgSpeedDelta,
			double avgInterCarSpace,
			double avgInterCarSpaceDelta,
			double avgInterPlatoonSpace,
			double avgInterPlatoonSpaceDelta,
			double joinDistance,
			double speedDelta,
			double splitProb) {

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
				cars[carsPerPlatoon-j-1] = new Car(globalPos,(baseCarSpeed + avgSpeedDelta * Math.random()),splitProb);
			}
			pool.add(new Platoon(null,cars));
			globalPos = globalPos + basePlatoonSpace + avgInterPlatoonSpaceDelta * Math.random();
		}
		return new Platooning2(pool,joinDistance,speedDelta);
	}



	public static void main(String[] args) {

		int runs = 4;
		// number of platoons
		int[] pN = {6,4,3,2};
		// number of cars per platoon
		int[] cN = {2,3,4,6};
		// average car speed
		double[] avgS = {40};
		// average speed variance
		double[] avgSVar = {10};//{10,10,10,10,10,10};
		// average inter-car spacing
		double[] avgCS = {15};
		// average inter-car spacing variance
		double[] avgCSVar = {5};////{5,5,5,5,5,5};
		// average inter-platoon spacing
		double[] avgPS = {30};
		// average inter-platoon spacing variance
		double[] avgPSVar = {10};//{10,10,10,10,10,10};
		// minimum join distance
		double[] jD = {25};
		// relative speed variation after splitting
		double[] sD = {0.2};
		// splitting probability
		double[] sP = {0.2};
		// number of cycles
		int[] cycles = {100};

		for (int i=0; i<runs; i++) {
			IDFactory.getInstance().resetFactory();
			Platooning2 road = generateConfiguration(
					pN[i%pN.length],
					cN[i%cN.length],
					avgS[i%avgS.length],
					avgSVar[i%avgSVar.length],
					avgCS[i%avgCS.length],
					avgCSVar[i%avgCSVar.length],
					avgPS[i%avgPS.length],
					avgPSVar[i%avgPSVar.length],
					jD[i%jD.length],
					sD[i%sD.length],
					sP[i%sP.length]);
			String output = String.format(
					"platooning_%d_%d_%.2f_%.2f_%.2f_%.2f_%.2f_%.2f_%.2f_%.2f_%.2f_%dc.json",
					pN[i%pN.length],
					cN[i%cN.length],
					avgS[i%avgS.length],
					avgSVar[i%avgSVar.length],
					avgCS[i%avgCS.length],
					avgCSVar[i%avgCSVar.length],
					avgPS[i%avgPS.length],
					avgPSVar[i%avgPSVar.length],
					jD[i%jD.length],
					sD[i%sD.length],
					sP[i%sP.length],
					cycles[i%cycles.length]);

			System.out.println("\nWorking on: " + output + "\n");
			ExecutionEngine ex = new ExecutionEngine(
					road,GreedyStrategy.getInstance(),new DummyOutput(),false,
					cycles[i%cycles.length],output);
			ex.setSnapshotSemantics(true);
			ex.run();
		}
	}

}
