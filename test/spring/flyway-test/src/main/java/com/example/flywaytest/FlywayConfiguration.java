package com.example.flywaytest;

import org.flywaydb.core.api.exception.FlywayValidateException;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.extern.slf4j.Slf4j;

@Configuration(proxyBeanMethods = false)
@Slf4j
class FlywayConfiguration {
	
	@Bean
	@Profile("dev")
	FlywayMigrationStrategy repairFlyway() {
		return flyway -> {
			try {
				flyway.migrate();
			} catch (FlywayValidateException exception) {
				log.warn("Cannot migrate, trying to repair and retry", exception);
				
				flyway.repair();
				flyway.migrate();
			}
		};
	}
	
}