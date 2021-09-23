package app;

import java.time.Duration;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import app.task.ListingTask;

public class Bootstrap {
	
	public static void main(String[] args) throws InterruptedException {
		long startTime = System.nanoTime();
		
		Shared.EXECUTOR_SERVICE.submit(new ListingTask());
		while (!Shared.EXECUTOR_SERVICE.awaitTermination(200, TimeUnit.MILLISECONDS)) {
			int stories = Shared.ACTIVE_STORIES.intValue();
			int comments = Shared.ACTIVE_COMMENTS.intValue();
			
			System.out.println(String.format("stories %2d comments %4d", stories, comments));
			
			if ((stories | comments) == 0) {
				Shared.EXECUTOR_SERVICE.shutdown();
			}
		}
		
		printTopStories();
		printTopUsers();
		
		long endTime = System.nanoTime();
		System.out.println(Duration.ofNanos(endTime - startTime));
	}
	
	private static void printTopStories() {
		System.out.println(String.format("Top %s stories...", Configuration.TOP_STORIES));
		
		int index = 0;
		for (String title : Shared.STORIES.values()) {
			System.out.println(String.format("   %2d - %s", ++index, title));
		}
	}
	
	private static void printTopUsers() {
		final int limit = 10;
		System.out.println(String.format("Top %s users by comment... (total comment analyzed: %d)", limit, Shared.TOTAL_COMMENT.intValue()));
		
		Shared.PARTICIPATIONS.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByValue(Comparator.comparing(AtomicInteger::intValue).reversed()))
				.limit(10)
				.forEach((entry) -> {
					String user = entry.getKey();
					int commentCount = entry.getValue().intValue();
					
					System.out.println(String.format(" %4d comments - %s", commentCount, user));
				});
	}
	
}