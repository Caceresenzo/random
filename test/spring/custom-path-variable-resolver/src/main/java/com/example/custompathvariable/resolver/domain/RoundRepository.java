package com.example.custompathvariable.resolver.domain;

import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class RoundRepository {
	
	public Optional<Round> findByNumber(Competition competition, long number) {
//		return Optional.empty();
		return Optional.of(new Round(number, competition));
	}
	
	public Optional<Round> findCurrent(Competition competition) {
//		return Optional.empty();
		return Optional.of(new Round(123, competition));
	}
	
}