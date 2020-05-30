package com.dream.test.benchmarks;

import com.dream.ExecutionEngine;
import com.dream.core.Entity;
import com.dream.core.IDFactory;
import com.dream.core.coordination.AndRule;
import com.dream.core.coordination.ConjunctiveTerm;
import com.dream.core.coordination.Declaration;
import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.EntityInstanceRef;
import com.dream.core.coordination.FOILRule;
import com.dream.core.coordination.Quantifier;
import com.dream.core.coordination.Rule;
import com.dream.core.coordination.TypeRestriction;
import com.dream.core.coordination.constraints.And;
import com.dream.core.coordination.constraints.Not;
import com.dream.core.coordination.constraints.Or;
import com.dream.core.coordination.constraints.PortReference;
import com.dream.core.coordination.constraints.predicates.Equals;
import com.dream.core.coordination.constraints.predicates.LessThan;
import com.dream.core.coordination.constraints.predicates.Tautology;
import com.dream.core.coordination.maps.MapNodeForAddress;
import com.dream.core.coordination.maps.MapNodeForEntity;
import com.dream.core.entities.AbstractMotif;
import com.dream.core.entities.CoordinatingEntity;
import com.dream.core.entities.maps.predefined.GridMap;
import com.dream.core.exec.GreedyStrategy;
import com.dream.core.expressions.InstanceIdentifier;
import com.dream.core.expressions.MapNodeAddress;
import com.dream.core.expressions.MapNodeDistance;
import com.dream.core.expressions.Sum;
import com.dream.core.expressions.VariableRef;
import com.dream.core.expressions.values.ArrayValue;
import com.dream.core.operations.Assign;
import com.dream.core.operations.IfThenElse;
import com.dream.core.operations.Move;
import com.dream.core.operations.OperationsSequence;
import com.dream.core.output.*;
import com.dream.test.benchmarks.robotsinteractive.Robot;

/**
 * @author Alessandro Maggi
 *
 */
public class InteractiveRobots extends AbstractMotif {

	public InteractiveRobots(int size, double range) {
		super(new GridMap(size,size));

		int[][] dirs = {{-1,-1},{0,-1},{1,-1},{-1,0},{0,0},{1,0},{-1,1},{0,1},{1,1}};
		int[][] addrs = new int[9][2];
		int step = size/3;
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				addrs[i*3+j][0] = step*j;
				addrs[i*3+j][1] = step*i;
			}
		}
		for (int i=0; i<9; i++) {
			Entity robot = new Robot(this, range, dirs[i][0], dirs[i][1]);
			addToPool(robot);
			setEntityPosition(robot, map.getNodeForAddress(new ArrayValue(addrs[i])));
		}

		EntityInstanceActual scope = new EntityInstanceActual(this);
		// \forall c:Robot { c.tick |> true -> move(c,@(c)+c.dir) }
		Declaration allRobots = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Robot.class));
		EntityInstanceRef c = allRobots.getVariable();
		Rule r1 = new FOILRule(allRobots,
				new ConjunctiveTerm(
						new PortReference(c,"tick"),
						Tautology.getInstance(),
						new Move(
								c,
								new MapNodeForAddress(
										scope,
										new Sum(
												new MapNodeAddress(
														new MapNodeForEntity(c)
														),
												new VariableRef(c,"dir")
												)))));
		// \forall c1:Robot {
		//		\forall c2:Robot { c1.tick |> c2.tick 
		//			-> IF (c1 != c2) /\ (c1.dir != c2.dir) /\ (@(c1)<->@(c2) < c1.range)
		//				/\ (c1.ts < c2.ts \/ (c1.ts = c2.ts /\ c1.id < c2.id))) THEN [
		//					c1.dir := c2.dir; c1.ts := c1.clock] } }
		Declaration allRobots1 = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Robot.class));
		EntityInstanceRef c1 = allRobots1.getVariable();
		Declaration allRobots2 = new Declaration(
				Quantifier.FORALL,
				scope,
				new TypeRestriction(Robot.class));
		EntityInstanceRef c2 = allRobots2.getVariable();
		Rule r2 = new FOILRule(allRobots1,
				new FOILRule(allRobots2,
						new ConjunctiveTerm(
								new PortReference(c1,"tick"),
								new PortReference(c2,"tick"),
								new IfThenElse(
										new And(
												new LessThan(
														new MapNodeDistance(new MapNodeForEntity(c1),new MapNodeForEntity(c2)),
														new VariableRef(c1,"range")
														),
												new Not(new Equals(
														new VariableRef(c1,"dir"),
														new VariableRef(c2,"dir")
														)),
												new Or(
														new LessThan(
																new VariableRef(c1,"ts"),
																new VariableRef(c2,"ts")
																),
														new And(
																new Equals(
																		new VariableRef(c1,"ts"),
																		new VariableRef(c2,"ts")
																		),
																new LessThan(
																		new InstanceIdentifier(c1),
																		new InstanceIdentifier(c2)
																		)
																)
														)
												), 
										new OperationsSequence(
												new Assign(
														new VariableRef(c1,"dir"),
														new VariableRef(c2,"dir")
														),
												new Assign(
														new VariableRef(c1,"ts"),
														new VariableRef(c1,"clock")
														)
												)
										)
								)
						));
		setRule(new AndRule(r1,r2));
	}

	public static void main(String[] args) {
		int[] sizes = {6,9,12,15,18,21,24};
		double[] ranges = {2,3,4,5,6};
		for (int size : sizes) {
			for (double range : ranges) {
				IDFactory.getInstance().resetFactory();
				CoordinatingEntity rootEntity = new InteractiveRobots(size,range);
				ExecutionEngine ex = new ExecutionEngine(
						rootEntity,GreedyStrategy.getInstance(),new DummyOutput(),false,
						30,String.format("interactive_robots_s%d_r%.0f.json",size,range));
				ex.setSnapshotSemantics(true);
				ex.run();
			}
		}
		
	}

}
