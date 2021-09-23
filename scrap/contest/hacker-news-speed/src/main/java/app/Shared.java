package app;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;

/* not really like doing something like this, but i was short on time */
public class Shared {
	
	public static ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 8);
	public static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	public static OkHttpClient CLIENT = new OkHttpClient();
	
	public static AtomicInteger ACTIVE_STORIES = new AtomicInteger(Configuration.TOP_STORIES);
	public static AtomicInteger ACTIVE_COMMENTS = new AtomicInteger();
	public static AtomicInteger TOTAL_COMMENT = new AtomicInteger();
	
	public static Map<Long, String> STORIES = new LinkedHashMap<>();
	public static Map<String, AtomicInteger> PARTICIPATIONS = new ConcurrentHashMap<>();
	
}