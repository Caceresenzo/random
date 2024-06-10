package com.example.demo;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	ApplicationRunner runner(EntityManager entityManager) {
		return new ApplicationRunner() {

			@Override
			@Transactional
			public void run(ApplicationArguments args) throws Exception {
				final var joe = new User()
					.setName("Joe");

				final var clang = new Course()
					.setName("Learn C");

				entityManager.persist(joe);
				entityManager.persist(clang);
				entityManager.flush();

				final var subscription = new Subscription()
					//					.setUser(joe)
					.setUser(joe.getId())
					//					.setCourse(clang)
					.setCourse(clang.getId());

				final var subscriptionViaToOne = new SubscriptionViaToOne()
					.setUser(joe)
					.setCourse(clang);

				entityManager.persist(subscription);
				entityManager.persist(subscriptionViaToOne);
				entityManager.flush();

				entityManager.clear();

				final var primaryKey = new CompositeKeyUsingClass(joe.getId(), clang.getId());
				//				final var primaryKey = new CompositeKeyUsingRecord(joe.getId(), clang.getId());

				final var entity = entityManager.find(Subscription.class, primaryKey);
				System.out.println(entity);

				final var entityViaToOne = entityManager.find(SubscriptionViaToOne.class, primaryKey);
				System.out.println(entityViaToOne);
			}

		};
	}

	@Entity
	@Table
	@Data
	@Accessors(chain = true)
	//	@IdClass(CompositeKeyUsingRecord.class)
	@IdClass(CompositeKeyUsingClass.class)
	public static class Subscription {

		@Id
		private long user;

		@Id
		private long course;

	}

	@Entity
	@Table
	@Data
	@Accessors(chain = true)
	//	@IdClass(CompositeKeyUsingRecord.class)
	@IdClass(CompositeKeyUsingClass.class)
	public static class SubscriptionViaToOne {

		@Id
		@ManyToOne(optional = false)
		private User user;

		@Id
		@ManyToOne(optional = false)
		private Course course;

	}

	@Embeddable
	@Data
	@Accessors(chain = true)
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CompositeKeyUsingClass {

		private long user;
		private long course;

	}

	@Embeddable
	public record CompositeKeyUsingRecord(
		long user,
		long course
	) {}

	@Entity
	@Table
	@Data
	@Accessors(chain = true)
	public static class User {

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private long id;

		@Column(nullable = false)
		private String name;

	}

	@Entity
	@Table
	@Data
	@Accessors(chain = true)
	public static class Course {

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private long id;

		@Column(nullable = false)
		private String name;

	}

}