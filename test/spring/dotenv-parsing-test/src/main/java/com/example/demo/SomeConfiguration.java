package com.example.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Data
@Validated
@Component
@ConfigurationProperties(prefix = "app.some")
public class SomeConfiguration {

	private String aKey;

	private NestedGroup nestedGroup = new NestedGroup();

	@Data
	@Validated
	@Component
	public static class NestedGroup {

		private String deepKey;

	}

}