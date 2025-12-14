package com.example.demo.composite;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.metamodel.spi.ValueAccess;
import org.hibernate.usertype.CompositeUserType;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class ProviderOptionsCompositeType implements CompositeUserType<ProviderOptions> {

	private final ObjectMapper objectMapper;

	@Override
	@SneakyThrows
	public Object getPropertyValue(ProviderOptions component, int property) throws HibernateException {
		switch (property) {
			case 0:
				return ProviderType.valueOf(component.getClass());
			case 1:
				return objectMapper.writeValueAsString(component);
			default:
				throw new IllegalArgumentException("Unknown property: " + property);
		}
	}

	@Override
	@SneakyThrows
	public ProviderOptions instantiate(ValueAccess values, SessionFactoryImplementor sessionFactory) {
		// Hibernate sort fields alphabetically...
		final var type = values.getValue(1, ProviderType.class);
		final var json = values.getValue(0, String.class);

		if (type == null) {
			return null;
		}

		final var optionsClass = type.getOptionsClass();
		return objectMapper.readValue(json, optionsClass);
	}

	@Override
	public Class<?> embeddable() {
		return ProviderOptionsEmbeddable.class;
	}

	@Override
	public Class<ProviderOptions> returnedClass() {
		return ProviderOptions.class;
	}

	@Override
	public boolean equals(ProviderOptions x, ProviderOptions y) {
		return x.equals(y);
	}

	@Override
	public int hashCode(ProviderOptions x) {
		return x.hashCode();
	}

	@Override
	@SneakyThrows
	public ProviderOptions deepCopy(ProviderOptions value) {
		final var buffer = objectMapper.writeValueAsString(value);
		return objectMapper.readValue(buffer, value.getClass());
	}

	@Override
	public boolean isMutable() {
		return true;
	}

	@Override
	public Serializable disassemble(ProviderOptions value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ProviderOptions assemble(Serializable cached, Object owner) {
		throw new UnsupportedOperationException();
	}

	@Override
	@SneakyThrows
	public ProviderOptions replace(ProviderOptions detached, ProviderOptions managed, Object owner) {
		return objectMapper.updateValue(detached, managed);
	}

}