package com.ldream.ldream_core.shared;

public class Messages {

	public static String incompatibleValueMessage(String sourceType, String requestedType, String value) {
		return String.format("Cannot convert %s \"%s\" to %s: incompatible value types", sourceType, requestedType, value);
	}
	
	public static String unsupportedOperation(String operandType, String operation) {
		return String.format("Cannot perform %s on value type %s", operation, operandType);
	}

	public static String invalidLocalVariableName(String varName) {
		return String.format("Invalid local variable name %s: no such local variable", varName);
	}
	
	public static String invalidPortName(String portName,String ownerName) {
		return String.format("Invalid port name %s: no such port for component %s", portName, ownerName);
	}
	
	public static String noAdmissibleInteractionMessage(String scope) {
		return String.format("No admissible interactions found for component %s", scope);
	}
}
