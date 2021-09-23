package app.model;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import lombok.Data;

@Data
public class Story {
	
	public static final TypeReference<Story> TYPE = new TypeReference<Story>() {};
	
	private long id;
	private String by;
	private long descendants;
	private List<Long> kids;
	private long score;
	private long time;
	private String title;
	private String type;
	private String url;
	
}