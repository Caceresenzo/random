package app.model;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import lombok.Data;

@Data
public class Comment {
	
	public static final TypeReference<Comment> TYPE = new TypeReference<Comment>() {};
	
	private long id;
	private String by;
	private long parent;
	private List<Long> kids;
	private long time;
	private String text;
	private String type;
	private boolean deleted;
	private boolean dead;
	
}