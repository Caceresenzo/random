package com.example.demo.composite;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProviderType {

	AWS(AwsProviderOptions.class),
	GCP(GcpProviderOptions.class);

	private static final Map<Class<? extends ProviderOptions>, ProviderType> CLASS_TO_VALUE_MAP = Arrays.stream(values()).collect(Collectors.toMap(ProviderType::getOptionsClass, Function.identity()));

	private final Class<? extends ProviderOptions> optionsClass;

	public static ProviderType valueOf(Class<? extends ProviderOptions> optionsClass) {
		return CLASS_TO_VALUE_MAP.get(optionsClass);
	}

}