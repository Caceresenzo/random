package com.example.demo.web;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "files")
public class FileRestController {

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Object index(
		@RequestParam(required = false) boolean wait,
		@RequestPart MultipartFile file
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
					System.out.println(file);
					try (final var inputStream = file.getInputStream()) {}

					emitter.send(new UpdateDto("working", x));
					Thread.sleep(Duration.ofMillis(200));
				}

				emitter.send(new UpdateDto("completed", 0));

				emitter.complete();

				//				Thread.ofVirtual().start(new Runnable() {
				//
				//					@SneakyThrows
				//					@Override
				//					public void run() {
				//						Thread.sleep(Duration.ofMillis(1500));
				//						try (final var inputStream = file.getInputStream()) {}
				//					}
				//
				//				});
			}

		});

		emitter.onCompletion(() -> log.info("completed"));
		emitter.onError((error) -> log.error("error: " + error));

		return emitter;
	}

}