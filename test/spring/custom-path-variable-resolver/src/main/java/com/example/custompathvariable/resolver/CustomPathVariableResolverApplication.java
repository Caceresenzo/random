package com.example.custompathvariable.resolver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SuppressWarnings("unchecked")
@SpringBootApplication
public class CustomPathVariableResolverApplication {
	
//	static {
////		AbstractRequestService
//		SpringDocUtils.getConfig()
////		.replaceParameterObjectWithClass(source, target)
//			.replaceWithSchema(RoundIdentifier.class, new Schema<>()
//				.oneOf(Arrays.asList(
//					new Schema<>()
//						.type("number")
//						.format("int64"),
//					new Schema<>()
//						.type("string")
//						._enum(Arrays.asList("@current"))
//				))
//			);
//	}
	
	public static void main(String[] args) {
		SpringApplication.run(CustomPathVariableResolverApplication.class, args);
	}
	
}