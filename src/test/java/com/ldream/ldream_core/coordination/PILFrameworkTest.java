package com.ldream.ldream_core.coordination;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.components.LocalVariable;
import com.ldream.ldream_core.components.Port;
import com.ldream.ldream_core.coordination.Interaction;
import com.ldream.ldream_core.coordination.interactions.And;
import com.ldream.ldream_core.coordination.interactions.Equals;
import com.ldream.ldream_core.coordination.interactions.Formula;
import com.ldream.ldream_core.coordination.interactions.Or;
import com.ldream.ldream_core.coordination.interactions.PortAtom;
import com.ldream.ldream_core.coordination.interactions.Predicate;
import com.ldream.ldream_core.coordination.interactions.Tautology;
import com.ldream.ldream_core.coordination.operations.Assign;
import com.ldream.ldream_core.coordination.operations.Operation;
import com.ldream.ldream_core.coordination.operations.OperationsSet;
import com.ldream.ldream_core.coordination.operations.Skip;
import com.ldream.ldream_core.expressions.*;

public class PILFrameworkTest {

	Component c,c1,c2;
	Port p1,p2,p3;
	Interaction i1,i2;
	Formula fp1,fp2,fp3;
	Predicate sp1,sp2,sp3;
	LocalVariable lvar1,lvar2;
	Rule ru1,ru2;
	Constant 	n0 = new Constant(0),
				n1 = new Constant(1),
				n2 = new Constant(2),
				n3 = new Constant(3);

	@BeforeEach
	void init() {
		c = new SimpleComponent();
		p1 = new Port("p1",c);
		p2 = new Port("p2",c);
		p3 = new Port("p3",c);
		Port[] i = {p1,p3};
		Port[] ii = {p1};
		i1 = new Interaction(i);
		i2 = new Interaction(ii);
		fp1 = new PortAtom(p1);
		fp2 = new PortAtom(p2);
		fp3 = new PortAtom(p3);
		sp1 = new Equals(n3,n3);
		sp2 = new Equals(n1,n2);
		sp3 = new Equals(
				new Sum(n2,n1),n3,
				new Product(n2,new Constant(1.5),n1)
				);
		lvar1 = new LocalVariable("x1",0);
		lvar2 = new LocalVariable("x2",5);
	}
	
	@Test
	@DisplayName("Check equations")
	void eqnTest() {
		Expression eq = new Sum(n2,n3,n1);
		assertEquals(eq.eval(),6);
	}

	@Test
	@DisplayName("{p1,p3} |= p1")
	void satTest1() {
		assertTrue( fp1.sat(i1) );
	}
	
	@Test
	@DisplayName("{p1,p3} |= not(p2)")
	void satTest2() {
		assertTrue( !fp2.sat(i1) );	
	}
	
	@Test
	@DisplayName("5 = 5")
	void satPredTest1() {
		assertTrue( sp1.sat(i1));
	}
	
	@Test
	@DisplayName("1 != 2")
	void satPredTest2() {
		assertTrue( !sp2.sat(i1));
	}
	
	@Test
	@DisplayName("2+3 = 5 = 2.5*2")
	void satPredTest3() {
		assertTrue( sp3.sat(i1));
	}
	
	@Test
	@DisplayName("{p1,p3} |= (p1 & p3)")
	void satAndTest1() {
		Formula form = new And(fp1,fp3);
		assertTrue( form.sat(i1));
	}
	
	@Test
	@DisplayName("{p1,p3} |= (p1 & 5=5)")
	void satAndTest2() {
		Formula form = new And(fp1,sp1);
		assertTrue( form.sat(i1));
	}
	
	@Test
	@DisplayName("{p1,p3} |= (p1 | p3 | 5=5)")
	void satOrTest1() {
		Formula form = new Or(fp1,new Or(fp3,sp1));
		assertTrue(form.sat(i1));
	}
	
	@Test
	@DisplayName("{} |= True")
	void satTautology() {
		Formula form = Tautology.getInstance();
		assertTrue( form.sat(new Interaction()));
	}
	
	@Test
	@DisplayName("lvar1 := lvar2 + 3")
	void assignmentTest() {
		Operation op = new Assign(new ActualVariable(lvar1), new Sum(new ActualVariable(lvar2),n3));
		op.evaluateParams();
		assertEquals(lvar1.getValue(),0);
		assertEquals(lvar2.getValue(),5);
		op.execute();
		assertEquals(lvar1.getValue(),8);
		assertEquals(lvar2.getValue(),5);
	}
	
	@Test
	@DisplayName("Snapshot semantics: lvar1 := lvar2 + 3; lvar2 := lvar1 * 3")
	void assignmentSequenceTest() {
		Operation op1 = new Assign(new ActualVariable(lvar1), new Sum(new ActualVariable(lvar2),n3));
		Operation op2 = new Assign(new ActualVariable(lvar2), new Product(new ActualVariable(lvar1),n3));
		op1.evaluateParams();
		op2.evaluateParams();
		assertEquals(lvar1.getValue(),0);
		assertEquals(lvar2.getValue(),5);
		op1.execute();
		op2.execute();
		assertEquals(lvar1.getValue(),8);
		assertEquals(lvar2.getValue(),0);
	}
	
	@Test
	@DisplayName("Test rule: {p1,p3} |= (p1 -> assign(...) & p3 -> skip)")
	void RuleTest1() {
		Operation op = new Assign(new ActualVariable(lvar1), new Sum(new ActualVariable(lvar2),n3));
		ru1 = new AndR(new Term(fp1, op),new Term(fp3));
		assertTrue(ru1.sat(i1));
		OperationsSet res = ru1.getOperationsForInteraction(i1);
		System.out.println(res.toString() + "\t" + res.hashCode());
		OperationsSet opSet = new OperationsSet(new Assign(new ActualVariable(lvar1), new Sum(new ActualVariable(lvar2),n3)),Skip.getInstance());
		System.out.println(opSet.toString() + "\t" + opSet.hashCode());
		assertTrue(ru1.getOperationsForInteraction(i1).equals(opSet));
	}
	
	@Test
	@DisplayName("Test rule: {p1} |= (p1 -> assign(...) || p3 -> skip)")
	void RuleTest2() {
		Operation op = new Assign(new ActualVariable(lvar1), new Sum(new ActualVariable(lvar2),n3));
		ru1 = new OrR(new Term(fp1, op),new Term(fp3));
		assertTrue(ru1.sat(i2));
		OperationsSet res = ru1.getOperationsForInteraction(i2);
		System.out.println(res.toString() + "\t" + res.hashCode());
		OperationsSet opSet = new OperationsSet(new Assign(new ActualVariable(lvar1), new Sum(new ActualVariable(lvar2),n3)));
		System.out.println(opSet.toString() + "\t" + opSet.hashCode());
		assertTrue(res.equals(opSet));
	}

}