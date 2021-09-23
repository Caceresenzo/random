package app.task;

import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import app.Configuration;
import app.Shared;
import okhttp3.Request;

public class ListingTask implements Runnable {
	
	public static final TypeReference<List<Long>> LIST_OF_LONG = new TypeReference<List<Long>>() {};
	
	public void run() {
		Request request = new Request.Builder()
				.url("https://hacker-news.firebaseio.com/v0/topstories.json")
				.build();
		
		try (InputStream response = Shared.CLIENT.newCall(request).execute().body().byteStream()) {
			List<Long> topStoriesIds = Shared.OBJECT_MAPPER.readValue(response, LIST_OF_LONG);
			
			int index = 0;
			for (long id : topStoriesIds) {
				Shared.STORIES.put(id, "");
				Shared.EXECUTOR_SERVICE.submit(new StoryFetchTask(id));
				
				if (++index == Configuration.TOP_STORIES) {
					break;
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
}