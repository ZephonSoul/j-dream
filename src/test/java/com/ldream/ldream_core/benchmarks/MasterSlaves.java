package com.ldream.ldream_core.benchmarks;

import com.dream.core.ExecutionEngine;
import com.dream.core.components.AbstractComponent;
import com.dream.core.components.Component;
import com.dream.core.coordination.*;
import com.dream.core.coordination.constraints.*;
import com.dream.core.coordination.constraints.predicates.*;
import com.dream.core.coordination.operations.Assign;
import com.dream.core.coordination.operations.OperationsSequence;
import com.dream.core.expressions.ReferencedVariable;
import com.dream.core.expressions.SetAdd;
import com.dream.core.expressions.Sum;
import com.dream.core.expressions.values.NumberValue;
import com.dream.core.expressions.values.SetValue;
import com.dream.core.expressions.values.Value;
import com.dream.exec.GreedyStrategy;
import com.dream.exec.output.ConsoleOutput;
import com.dream.exec.output.DummyOutput;
import com.ldream.ldream_core.benchmarks.masterslaves.Master;
import com.ldream.ldream_core.benchmarks.masterslaves.Slave;

public class MasterSlaves extends AbstractComponent implements Component {

	final static Value requiredSlaves = new NumberValue(2);

	public MasterSlaves(int bench_size) {
		super();
		for (int i=0; i<bench_size; i++) {
			addToPool(new Master(),new Slave(),new Slave());
		}

		Declaration 
		forall_masters = new Declaration(
				Quantifier.FORALL,
				new ActualComponentInstance(this),
				new TypeRestriction(Master.class)),
		exists_master = new Declaration(
				Quantifier.EXISTS,
				new ActualComponentInstance(this),
				new TypeRestriction(Master.class)),
		forall_slaves = new Declaration(
				Quantifier.FORALL,
				new ActualComponentInstance(this),
				new TypeRestriction(Slave.class)),
		exists_slave = new Declaration(
				Quantifier.EXISTS,
				new ActualComponentInstance(this),
				new TypeRestriction(Slave.class));

		ComponentInstance m = forall_masters.getVariable(),
				s = exists_slave.getVariable();

		Rule rc1 = new FOILRule(forall_masters, 
				new FOILRule(exists_slave,
						new ConjunctiveTerm(
								new PortReference(m, "connect"),
								new And(
										new PortReference(s, "bind"),
										new LessThan(
												new ReferencedVariable(m, "nSlaves"),
												requiredSlaves
												),
										new Not(
												new InSet(
														new ReferencedVariable(s, "id"),
														new ReferencedVariable(m, "boundSlaves")
														)
												)
										)
								)
						)
				);

		m = exists_master.getVariable();
		s = forall_slaves.getVariable();

		Rule rc2 = new FOILRule(forall_slaves, 
				new FOILRule(exists_master,
						new ConjunctiveTerm(
								new PortReference(s, "bind"), 
								new PortReference(m, "connect"))
						)
				);

		m = forall_masters.getVariable();
		s = forall_slaves.getVariable();

		Rule rc3 = new FOILRule(forall_masters,
				new FOILRule(forall_slaves, 
						new ConjunctiveTerm(
								new PortReference(m, "work"), 
								new And(
										new Equals(
												new ReferencedVariable(m, "nSlaves"),
												requiredSlaves
												),
										new Or(
												new PortReference(s, "serve"),
												new Not(
														new InSet(
																new ReferencedVariable(s, "id"),
																new ReferencedVariable(m, "boundSlaves")
																)
														)
												)
										)
								)
						));

		m = exists_master.getVariable();
		s = forall_slaves.getVariable();

		Rule rc4 = new FOILRule(forall_slaves,
				new FOILRule(exists_master,
						new ConjunctiveTerm(
								new PortReference(s, "serve"),
								new And(
										new PortReference(m, "work"),
										new Equals(
												new ReferencedVariable(s, "master"),
												new ReferencedVariable(m, "id")
												)
										)
								)
						));

		m = exists_master.getVariable();
		s = exists_slave.getVariable();
		
		Rule rd1 = new FOILRule(exists_master,
				new FOILRule(exists_slave,
						new Term(
								new And(
										new PortReference(m, "connect"),
										new PortReference(s, "bind")
										), 
								new OperationsSequence(
										new Assign(
												new ReferencedVariable(m, "boundSlaves"),
												new SetAdd(
														new ReferencedVariable(s, "id"),
														new ReferencedVariable(m, "boundSlaves")
														)
												),
										new Assign(
												new ReferencedVariable(m, "nSlaves"),
												new Sum(
														new ReferencedVariable(m, "nSlaves"), 
														new NumberValue(1))
												),
										new Assign(
												new ReferencedVariable(s, "master"),
												new ReferencedVariable(m, "id")
												)
										)
								)
						));
				
		Rule rd2 = new FOILRule(exists_master,
				new FOILRule(exists_slave,
						new Term(
								new And(
										new PortReference(m, "work"),
										new PortReference(s, "serve"),
										new InSet(
												new ReferencedVariable(s, "id"),
												new ReferencedVariable(m, "boundSlaves")
												)
										),
								new OperationsSequence(
										new Assign(
												new ReferencedVariable(m, "boundSlaves"),
												new SetValue()
												),
										new Assign(
												new ReferencedVariable(m, "nSlaves"),
												new NumberValue(0)
												),
										new Assign(
												new ReferencedVariable(s, "master"),
												new NumberValue(-1)
												)
										)
								)
						)
				);
		
		m = forall_masters.getVariable();
		Declaration dm2 = new Declaration(
				Quantifier.FORALL,
				new ActualComponentInstance(this),
				new TypeRestriction(Master.class));
		ComponentInstance m2 = dm2.getVariable();
		
		Rule ru1 = new FOILRule(forall_masters,
				new FOILRule(dm2,
						new ConjunctiveTerm(
								new And(
										new PortReference(m, "connect"),
										new PortReference(m2, "connect")
										),
								new SameInstance(m,m2)
								)
						));
		ru1 = new Term(new AtMost(new NumberValue(1),new TypeRestriction(Master.class),"connect"));
		
		s = forall_slaves.getVariable();
		Declaration ds2 = new Declaration(
				Quantifier.FORALL,
				new ActualComponentInstance(this),
				new TypeRestriction(Slave.class));
		ComponentInstance s2 = ds2.getVariable();
		
		Rule ru2 = new FOILRule(forall_slaves,
				new FOILRule(ds2,
						new ConjunctiveTerm(
								new And(
										new PortReference(s, "bind"),
										new PortReference(s2, "bind")
										),
								new SameInstance(s,s2)
								)
						));
		ru2 = new Term(new AtMost(new NumberValue(1),new TypeRestriction(Slave.class),"bind"));
		
		Rule globalRule = new AndRule(rc1,rc2,rc3,rc4,ru1,ru2,new OrRule(rd1,rd2));
		
		setRule(globalRule);
		refresh();
	}

	public static void main(String[] args) {
		MasterSlaves c = new MasterSlaves(5);
		
		ExecutionEngine ex = new ExecutionEngine(c,GreedyStrategy.getInstance(),new DummyOutput(),false,20);
		ex.setSnapshotSemantics(true);
		ex.run();
	}
	
}
