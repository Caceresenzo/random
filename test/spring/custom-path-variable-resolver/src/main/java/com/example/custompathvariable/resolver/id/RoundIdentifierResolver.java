package com.example.custompathvariable.resolver.id;

import java.util.Optional;

import com.example.custompathvariable.resolver.domain.Competition;
import com.example.custompathvariable.resolver.domain.Round;

public interface RoundIdentifierResolver {
	
	Optional<Round> find(RoundIdentifier identifier, Competition competition);
	
	Round get(RoundIdentifier identifier, Competition competition);
	
}