package com.example.demo.subtype;

import lombok.Data;

@Data
public class AwsProviderOptions implements ProviderOptions {

	private String region;

}