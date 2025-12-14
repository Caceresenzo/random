package com.example.demo.subtype;

import lombok.Data;

@Data
public class GcpProviderOptions implements ProviderOptions {

	private String projectId;

}