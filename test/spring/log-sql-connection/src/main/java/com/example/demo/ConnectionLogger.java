package com.example.demo;

import java.util.function.Supplier;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ttddyy.dsproxy.listener.MethodExecutionContext;
import net.ttddyy.dsproxy.listener.lifecycle.JdbcLifecycleEventListenerAdapter;

@Slf4j
@RequiredArgsConstructor
public class ConnectionLogger extends JdbcLifecycleEventListenerAdapter {

	private final Supplier<String> environmentSupplier;

	@Override
	public void beforeGetConnection(MethodExecutionContext executionContext) {
		log.info("beforeGetConnection - connectionId={} environment={}", executionContext.getConnectionInfo().getConnectionId(), environmentSupplier.get());
	}

	@Override
	public void beforeClose(MethodExecutionContext executionContext) {
		log.info("beforeClose - connectionId={} environment={}", executionContext.getConnectionInfo().getConnectionId(), environmentSupplier.get());
	}

}