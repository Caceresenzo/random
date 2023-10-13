package com.example.custompathvariable.resolver.id;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RoundIdentifierConverter implements Converter<String, RoundIdentifier> {
	
	@Override
	public RoundIdentifier convert(String source) {
		if (source.equals(CurrentRoundIdentifier.WORD)) {
			return CurrentRoundIdentifier.INSTANCE;
		}
		
		return new NumberRoundIdentifier(Long.parseLong(source));
	}
	
}