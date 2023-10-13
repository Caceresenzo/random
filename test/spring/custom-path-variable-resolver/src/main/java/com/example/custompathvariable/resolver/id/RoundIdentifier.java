package com.example.custompathvariable.resolver.id;

public sealed interface RoundIdentifier permits CurrentRoundIdentifier, NumberRoundIdentifier {
	
	public static RoundIdentifier current() {
		return CurrentRoundIdentifier.INSTANCE;
	}
	
	public static RoundIdentifier number(long number) {
		return new NumberRoundIdentifier(number);
	}
	
}