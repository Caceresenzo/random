package com.example.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@Table
@Data
@ToString(doNotUseGetters = true)
@Accessors(chain = true)
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public long idx;
	
	@Column
	public String name;
	
	public long getId() {
		return idx;
	}
	
	public String getIdAsString() {
		return String.valueOf(idx);
	}
	
}