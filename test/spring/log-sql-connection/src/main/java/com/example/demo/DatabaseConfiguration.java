package com.example.demo;

import java.util.Optional;
import java.util.function.Supplier;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

@Configuration
public class DatabaseConfiguration {

	@Bean
	ConnectionLogger connectionLogger() {
		return new ConnectionLogger(new Supplier<String>() {

			@Override
			public String get() {
				return getCurrentPath()
					.or(this::getThreadName)
					.orElse("(unknown)");
			}

			public Optional<String> getCurrentPath() {
				final var attributes = RequestContextHolder.getRequestAttributes();

				if (attributes instanceof ServletRequestAttributes servletRequestAttributes) {
					return Optional.ofNullable(servletRequestAttributes.getRequest().getRequestURI());
				}

				return Optional.empty();
			}

			public Optional<String> getThreadName() {
				final var thread = Thread.currentThread();

				return Optional.of(thread.getName());
			}

		});
	}

	@Bean
	DataSource originalDataSource(DataSourceProperties properties) {
		return properties.initializeDataSourceBuilder().build();
	}

	@Primary
	@Bean
	DataSource dataSource(
		DataSource originalDataSource,
		ConnectionLogger connectionLogger
	) {
		return ProxyDataSourceBuilder.create(originalDataSource)
			.listener(connectionLogger)
			.build();
	}

}