package com.example.demo.domain;

import static jakarta.persistence.CascadeType.DETACH;
import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REFRESH;
import static jakarta.persistence.CascadeType.REMOVE;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

@Entity
@Table(name = "groups")
@Data
@Accessors(chain = true)
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@FieldNameConstants
@NoArgsConstructor
public class Group {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@ToString.Include
	private long id;

	@Column(nullable = false, updatable = false)
	private String name;

	@OneToMany(mappedBy = User.Fields.group, cascade = { PERSIST, MERGE, REMOVE, REFRESH, DETACH }, orphanRemoval = true)
	private List<User> users;

	@Column(nullable = false)
	private int anything;

	public Group(String name) {
		this.name = name;
		this.users = new ArrayList<>(1);
	}

	public Group clearUsers() {
		getUsers().clear();

		return this;
	}

	public Group addUser(User user) {
		getUsers().add(user.setGroup(this));

		return this;
	}

	public Group triggerModifications() {
		anything += 1;
		return this;
	}

}