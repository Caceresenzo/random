package com.example.demo.subtype;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
	@JsonSubTypes.Type(value = AwsProviderOptions.class, name = "AWS"),
	@JsonSubTypes.Type(value = GcpProviderOptions.class, name = "GCP"),
})
public interface ProviderOptions {}