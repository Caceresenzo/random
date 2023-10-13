package com.example.custompathvariable.resolver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.custompathvariable.resolver.domain.Competition;
import com.example.custompathvariable.resolver.id.RoundIdentifier;
import com.example.custompathvariable.resolver.id.RoundIdentifierResolver;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
	
	private final RoundIdentifierResolver roundIdentifierResolver;
	
	@GetMapping("/rounds/{identifier}")
	public RoundIdentifier getUserDetails(
		// @PathVariable RoundIdentifier identifier,
		@RequestParam RoundIdentifier identifier
	) {
		final var competition = new Competition("");
		final var round = roundIdentifierResolver.get(identifier, competition);
		
		// return "Received: " + round.toString();
		return identifier;
	}
	
	@GetMapping("/number/{identifier}")
	public long number(
		@PathVariable long identifier
	) {
		return identifier;
	}
	
}