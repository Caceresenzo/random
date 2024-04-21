package com.example.demo.web;

import java.time.Duration;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.demo.exception.SomethingFatalHappenException;
import com.example.demo.exception.SomethingHappenException;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "hello")
public class HelloRestController {

	@GetMapping
	public Object index(
		@RequestParam(required = false) boolean wait,
		@RequestParam(required = false) boolean fatal
	) {
		if (wait) {
			return new UpdateDto("ok", 1);
		}

		final var emitter = new SseEmitter();

		Thread.ofVirtual().start(new Runnable() {

			@SneakyThrows
			@Override
			public void run() {
				emitter.send(new UpdateDto("starting", 0));

				for (var x = 0; x < 3; ++x) {
					emitter.send(new UpdateDto("working", x));
					Thread.sleep(Duration.ofMillis(200));
				}

				emitter.send(new UpdateDto("completed", 0));

				emitter.completeWithError(
					fatal
						? new SomethingFatalHappenException()
						: new SomethingHappenException()
				);
			}

		});

		emitter.onCompletion(() -> log.info("completed"));
		emitter.onError((error) -> log.error("error: " + error));

		return emitter;
	}

}