package app.task;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import app.Configuration;
import app.Shared;
import app.model.Comment;
import app.model.Story;
import lombok.AllArgsConstructor;
import okhttp3.Request;

@AllArgsConstructor
public class CommentFetchTask implements Runnable {
	
	private long id;
	
	public void run() {
		Request request = new Request.Builder()
				.url("https://hacker-news.firebaseio.com/v0/item/" + id + ".json")
				.build();
		
		try (InputStream response = Shared.CLIENT.newCall(request).execute().body().byteStream()) {
			Comment comment = Shared.OBJECT_MAPPER.readValue(response, Comment.TYPE);
			
			if (comment == null) {
				System.err.println(String.format("%d is null", id));
			}
			
			if (Configuration.LOG_MODEL) {
				System.out.println(comment);
			}
			
			if (comment.getBy() != null) {
				Shared.PARTICIPATIONS.compute(comment.getBy(), CommentFetchTask::increment);
			}
			
			int added = submit(comment);
			
			Shared.ACTIVE_COMMENTS.addAndGet(added);
			Shared.TOTAL_COMMENT.addAndGet(added);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		
		Shared.ACTIVE_COMMENTS.decrementAndGet();
	}
	
	public static int submit(Comment comment) {
		return submit(comment.getKids());
	}
	
	public static int submit(Story story) {
		return submit(story.getKids());
	}
	
	private static int submit(List<Long> ids) {
		if (ids == null) {
			return 0;
		}
		
		ids.stream()
				.map(CommentFetchTask::new)
				.forEach(Shared.EXECUTOR_SERVICE::submit);
		
		return ids.size();
	}
	
	private static AtomicInteger increment(String key, AtomicInteger atomic) {
		if (atomic == null) {
			return new AtomicInteger(1);
		}
		
		atomic.incrementAndGet();
		
		return atomic;
	}
	
}