/**
 * 
 */
package com.dream.core.operations;

import java.util.function.Consumer;

import com.dream.core.ActualInstance;
import com.dream.core.Caching;
import com.dream.core.Instance;

/**
 * @author Alessandro Maggi
 *
 */
public class JavaOperation<T> extends AbstractOperation {

	private Instance<T> context;
	private Consumer<T> javaCode;

	public JavaOperation(Instance<T> context, Consumer<T> javaCode) {
		this.context = context;
		this.javaCode = javaCode;
	}

	/**
	 * @return the context
	 */
	public Instance<T> getContext() {
		return context;
	}

	/**
	 * @return the javaCode
	 */
	public Consumer<T> getJavaCode() {
		return javaCode;
	}

	@Override
	public void evaluate() {
		context.evaluate();
	}

	@Override
	public void execute() {
		javaCode.accept(context.getActual());
	}

	@Override
	public boolean equals(Operation op) {
		return (op instanceof JavaOperation<?>) &&
				((JavaOperation<?>) op).getContext().equals(context) &&
				((JavaOperation<?>) op).getJavaCode().equals(javaCode);
	}

	@Override
	public void clearCache() {
		if (context instanceof Caching)
			((Caching)context).clearCache();
	}

	@Override
	public <I> Operation bindInstance(
			Instance<I> reference, 
			Instance<I> actual) {

		if (context instanceof ActualInstance)
			return this;
		else
			return new JavaOperation<T>(bindInstance(context,reference,actual),javaCode);
	}

	public String toString() {
		return String.format("JavaOp(%s,%s)", context.toString(),javaCode.toString());
	}
	
}
