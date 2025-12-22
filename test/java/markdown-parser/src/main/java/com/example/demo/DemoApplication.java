package com.example.demo;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.Nullable;

import com.example.demo.DemoApplication.Slice.SliceNativeConfiguration;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.slugify.Slugify;
import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.ast.Paragraph;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TableBlock;
import com.vladsch.flexmark.ext.tables.TableBody;
import com.vladsch.flexmark.ext.tables.TableHead;
import com.vladsch.flexmark.ext.tables.TableRow;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Data
	@Accessors(chain = true)
	public static class Slice {

		private String name;
		private String displayName;
		private Type type;
		@JsonRawValue
		private String nativeConfiguration;
		private long order;

		@Getter
		@Accessors(fluent = true)
		public enum Type {

			CONTENT(ContentNativeConfiguration::new),
			KEY_METRICS(KeyMetricsNativeConfiguration::new),
			TIMELINE(TimelineNativeConfiguration::new),
			TEAM(TeamNativeConfiguration::new);

			private final String kebabName;
			private final Supplier<SliceNativeConfiguration> nativeConfigurationSupplier;

			private Type(Supplier<SliceNativeConfiguration> nativeConfigurationSupplier) {
				this.kebabName = name().toLowerCase().replace("_", "-");
				this.nativeConfigurationSupplier = nativeConfigurationSupplier;
			}

			public SliceNativeConfiguration newNativeConfiguration() {
				return nativeConfigurationSupplier.get();
			}

			public static Type fromKebabName(String kebabName) {
				for (final var type : values()) {
					if (type.kebabName.equals(kebabName)) {
						return type;
					}
				}

				return CONTENT;
			}

		}

		public interface SliceNativeConfiguration {

			public void accept(Node node);

		}

		@Data
		public static class ContentNativeConfiguration implements SliceNativeConfiguration {

			private String content;

			@Override
			public void accept(Node node) {
				appendContent(node.getChildChars().toString());
			}

			public ContentNativeConfiguration appendContent(String content) {
				if (this.content == null) {
					this.content = content;
				} else {
					this.content += "\n" + content;
				}

				return this;
			}

		}

		@Data
		public static class KeyMetricsNativeConfiguration implements SliceNativeConfiguration {

			private List<Metric> metrics = new ArrayList<>();

			@Override
			public void accept(Node node) {
				if (!(node instanceof TableBlock table)) {
					return;
				}

				final var tableContent = TableContent.extract(table);

				for (final var row : tableContent) {
					final var displayName = row.get("Name");
					final var displayValue = row.get("Value");

					if (displayName == null || displayValue == null) {
						continue;
					}

					addMetric(new Metric(displayName, displayValue));
				}
			}

			public KeyMetricsNativeConfiguration addMetric(Metric metric) {
				this.metrics.add(metric);
				return this;
			}

			public static record Metric(
				String displayName,
				String displayValue
			) {}

		}

		@Data
		public static class TimelineNativeConfiguration implements SliceNativeConfiguration {

			private List<Event> events = new ArrayList<>();

			@Override
			public void accept(Node node) {
				if (!(node instanceof TableBlock table)) {
					return;
				}

				final var tableContent = TableContent.extract(table);

				for (final var row : tableContent) {
					final var date = row.get("Date", LocalDate::parse);
					final var description = row.get("Description");

					if (date == null || description == null) {
						continue;
					}

					addMetric(new Event(
						date,
						description,
						row.get("Description")
					));
				}
			}

			public TimelineNativeConfiguration addMetric(Event event) {
				this.events.add(event);
				return this;
			}

			public static record Event(
				LocalDate date,
				String markdown,
				@JsonInclude(NON_NULL) String link
			) {}

		}

		@Data
		public static class TeamNativeConfiguration implements SliceNativeConfiguration {

			private List<TeamMember> members = new ArrayList<>();

			@Override
			public void accept(Node node) {
				if (!(node instanceof TableBlock table)) {
					return;
				}

				final var tableContent = TableContent.extract(table);

				for (final var row : tableContent) {
					final var fullName = row.get("Name");
					final var descriptionMarkdown = row.get("Biography");

					if (fullName == null || descriptionMarkdown == null) {
						continue;
					}

					addMember(new TeamMember(
						fullName,
						row.get("Avatar Image"),
						descriptionMarkdown,
						row.get("Twitter"),
						row.get("LinkedIn"),
						row.get("Website")
					));
				}
			}

			public TeamNativeConfiguration addMember(TeamMember member) {
				this.members.add(member);
				return this;
			}

			public static record TeamMember(
				String fullName,
				@JsonInclude(NON_NULL) String avatarImageUrl,
				String descriptionMarkdown,
				@JsonInclude(NON_NULL) String twitterUrl,
				@JsonInclude(NON_NULL) String linkedinUrl,
				@JsonInclude(NON_NULL) String websiteUrl
			) {}

		}

	}

	@Bean
	Slugify slugify() {
		return Slugify.builder()
			.lowerCase(true)
			.underscoreSeparator(false)
			.customReplacement("_", "-")
			.build();
	}

	@Bean
	ObjectWriter prettyObjectWritter() {
		return new ObjectMapper()
			.registerModule(new JavaTimeModule())
			.writerWithDefaultPrettyPrinter();
	}

	@Bean
	ApplicationRunner applicationRunner(Slugify slugify, ObjectWriter prettyObjectWritter) {
		MutableDataSet options = new MutableDataSet();

		options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));
		options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

		Parser parser = Parser.builder(options).build();
		HtmlRenderer renderer = HtmlRenderer.builder(options).build();

		return (args) -> {
			// You can re-use parser and renderer instances
			Node document = parser.parse("""
# Overview

Lorem ipsum dolor sit amet, consectetur adipiscing elit.

# Title
@type=key-metrics

| Name | Value |
| --- | --- |
| Wins | +1000 |
| Loss | -1000 |

# Description

Lorem ipsum dolor sit amet, consectetur adipiscing elit.

## Hello

Lorem ipsum dolor sit amet, consectetur adipiscing elit.

# Roadmap
@type=timeline

| Date | Description | Link |
| --- | --- | --- |
| 2025-01-01 | Lorem ipsum dolor sit amet, consectetur adipiscing elit. | https://x.com/a |
| 2025-01-01 | Lorem ipsum dolor sit amet, consectetur adipiscing elit. | https://x.com/a |
| 2025-01-01 | Lorem ipsum dolor sit amet, consectetur adipiscing elit. | https://x.com/a |

# Team
@type=team

| Name | Biography | Twitter | LinkedIn |
| --- | --- | --- | --- |
| Enzo | *Lorem ipsum dolor sit amet*, consectetur adipiscing elit. <br /> Hello | https://x.com/a | |
| Enzo | Lorem ipsum dolor sit amet, consectetur adipiscing elit. | https://x.com/a | |
| Enzo | Lorem ipsum dolor sit amet, consectetur adipiscing elit. | https://x.com/a | x.com |
| Enzo | Lorem ipsum dolor sit amet, consectetur adipiscing elit. | https://x.com/a | |
				""");

			final var slices = new ArrayList<Slice>();

			final var iterator = document.getChildIterator();

			Slice currentSlice = null;
			SliceNativeConfiguration currentNativeConfiguration = null;

			while (iterator.hasNext()) {
				var node = iterator.next();

				if (node instanceof Heading heading && heading.getLevel() == 1) {
					if (currentSlice != null) {
						currentSlice.setNativeConfiguration(prettyObjectWritter.writeValueAsString(currentNativeConfiguration));
						slices.add(currentSlice);
					}

					final var displayName = heading.getText().toString();
					final var name = slugify.slugify(displayName);

					currentSlice = new Slice()
						.setName(name)
						.setDisplayName(displayName)
						.setOrder((slices.size() + 1) * 10);

					final var properties = new HashMap<String, String>();
					while ((node = iterator.peek()) != null && node instanceof Paragraph paragraph && paragraph.getContentChars().startsWith("@")) {
						final var content = paragraph.getContentChars().toString();

						final var parts = content.split("=", 2);
						final var key = parts[0].substring(1).trim();
						final var value = parts.length == 2 ? parts[1].trim() : null;

						properties.put(key, value);
						iterator.next();
					}

					final var type = Slice.Type.fromKebabName(properties.get("type"));

					currentSlice.setType(type);
					currentNativeConfiguration = type.newNativeConfiguration();
					continue;
				}

				if (currentSlice != null) {
					currentNativeConfiguration.accept(node);
				}
			}

			if (currentSlice != null) {
				currentSlice.setNativeConfiguration(prettyObjectWritter.writeValueAsString(currentNativeConfiguration));
				slices.add(currentSlice);
			}

			System.out.println(prettyObjectWritter.writeValueAsString(slices));
		};
	}

	public record TableContent(
		List<String> names,
		List<List<String>> values
	) implements Iterable<TableLine> {

		public int columnIndex(String name) {
			return names.indexOf(name);
		}

		@Override
		public Iterator<TableLine> iterator() {
			final var iterator = values.iterator();

			return new Iterator<TableLine>() {

				@Override
				public boolean hasNext() {
					return iterator.hasNext();
				}

				@Override
				public TableLine next() {
					return new TableLine(names, iterator.next());
				}

			};
		}

		public static TableContent extract(TableBlock table) {
			final var tableHead = table.getFirstChildAny(TableHead.class);
			final var headRow = tableHead.getFirstChildAny(TableRow.class);

			final var columnNames = new ArrayList<String>();
			for (final var cellNode : headRow.getChildren()) {
				columnNames.add(cellNode.getExactChildChars().trim().toString());
			}

			final var tableBody = table.getFirstChildAny(TableBody.class);

			final var columnValues = new ArrayList<List<String>>();
			for (final var tableRow : tableBody.getChildren()) {
				final var values = new ArrayList<String>();
				columnValues.add(values);

				for (final var cellNode : tableRow.getChildren()) {
					values.add(cellNode.getExactChildChars().trim().toString());
				}
			}

			return new TableContent(columnNames, columnValues);
		}

	}

	public record TableLine(
		List<String> columns,
		List<String> values
	) {

		public @Nullable String get(String name) {
			final var columnIndex = columns.indexOf(name);
			if (columnIndex == -1) {
				return null;
			}

			final var value = values.get(columnIndex);
			if (value.isBlank()) {
				return null;
			}

			return value;
		}

		public @Nullable <T> T get(String name, Function<String, T> mapper) {
			final var value = get(name);

			if (value == null) {
				return null;
			}

			return mapper.apply(value);
		}

	}

}