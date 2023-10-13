package com.example.custompathvariable.resolver.id;

public record NumberRoundIdentifier(
	long number
) implements RoundIdentifier {}