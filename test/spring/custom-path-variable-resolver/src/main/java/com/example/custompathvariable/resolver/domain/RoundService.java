package com.example.custompathvariable.resolver.domain;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.custompathvariable.resolver.id.CurrentRoundIdentifier;
import com.example.custompathvariable.resolver.id.NumberRoundIdentifier;
import com.example.custompathvariable.resolver.id.RoundIdentifier;
import com.example.custompathvariable.resolver.id.RoundIdentifierResolver;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoundService implements RoundIdentifierResolver {

	private final RoundRepository repository;
	
	@Override
	public Optional<Round> find(RoundIdentifier identifier, Competition competition) {
		if (identifier instanceof CurrentRoundIdentifier) {
			return repository.findCurrent(competition);
		} else if (identifier instanceof NumberRoundIdentifier numberIdentifier) {
			return repository.findByNumber(competition, numberIdentifier.number());
		} else {
			throw new IllegalArgumentException("unsupported " + identifier);
		}
	}

	@Override
	public Round get(RoundIdentifier identifier, Competition competition) {
		final var round = find(identifier, competition);
		
		if (round.isPresent()) {
			return round.get();
		}

		if (identifier instanceof CurrentRoundIdentifier) {
			throw new RuntimeException("current round not found");
		} else if (identifier instanceof NumberRoundIdentifier numberIdentifier) {
			throw new RuntimeException("round not found with number " + numberIdentifier.number());
		} else {
			throw new IllegalArgumentException("unsupported " + identifier);
		}
	}
	
}