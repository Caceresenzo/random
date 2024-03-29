package uservice.forum.users.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Data
@Accessors(chain = true)
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column
	private String name;
	
	@Column
	private int age;
	
}