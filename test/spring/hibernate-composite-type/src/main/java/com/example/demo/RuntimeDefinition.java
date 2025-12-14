package com.example.demo;

import org.hibernate.annotations.CompositeType;
import org.hibernate.annotations.JdbcTypeCode;

import com.example.demo.composite.ProviderOptionsCompositeType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "runtime_definitions")
@Data
public class RuntimeDefinition {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false)
	private String name;

	@CompositeType(ProviderOptionsCompositeType.class)
	private com.example.demo.composite.ProviderOptions providerOptions;

	@JdbcTypeCode(value = org.hibernate.type.SqlTypes.JSON)
	private com.example.demo.subtype.ProviderOptions provider;

}