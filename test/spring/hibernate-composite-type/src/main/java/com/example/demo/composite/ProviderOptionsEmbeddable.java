package com.example.demo.composite;

import org.hibernate.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProviderOptionsEmbeddable {

	@Column(name = "provider_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private ProviderType type;

	@Column(name = "provider_options", nullable = false, length = Length.LONG)
	private String options;

}