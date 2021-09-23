package app.task;

import java.io.InputStream;

import app.Configuration;
import app.Shared;
import app.model.Story;
import lombok.AllArgsConstructor;
import okhttp3.Request;

@AllArgsConstructor
public class StoryFetchTask implements Runnable {
	
	private long id;
	
	public void run() {
		Request request = new Request.Builder()
				.url("https://hacker-news.firebaseio.com/v0/item/" + id + ".json")
				.build();
		
		try (InputStream response = Shared.CLIENT.newCall(request).execute().body().byteStream()) {
			Story story = Shared.OBJECT_MAPPER.readValue(response, Story.TYPE);
			
			Shared.STORIES.put(story.getId(), story.getTitle());
			
			if (Configuration.LOG_MODEL) {
				System.out.println(story);
			}
			
			Shared.ACTIVE_COMMENTS.addAndGet(CommentFetchTask.submit(story));
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		
		Shared.ACTIVE_STORIES.decrementAndGet();
	}
	
}