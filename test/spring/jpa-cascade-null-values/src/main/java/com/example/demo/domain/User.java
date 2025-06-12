package com.example.demo.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

@Entity
@Table(name = "users")
@Data
@Accessors(chain = true)
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@FieldNameConstants
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@ToString.Include
	private long id;

	@ManyToOne(optional = false)
	private Group group;

	@Column(nullable = false, updatable = false)
	private String name;

	@OneToMany(mappedBy = Pet.Fields.user, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Pet> pets;

	public User(String name) {
		this.name = name;
		this.pets = new ArrayList<>(1);
	}

	public User addPet(Pet pet) {
		getPets().add(pet.setUser(this));

		return this;
	}

}