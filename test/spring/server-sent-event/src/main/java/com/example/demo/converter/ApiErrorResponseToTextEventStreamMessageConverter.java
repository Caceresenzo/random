package com.example.demo.converter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ApiErrorResponse;

@Component
public class ApiErrorResponseToTextEventStreamMessageConverter implements HttpMessageConverter<ApiErrorResponse> {

	public static final MediaType MEDIA_TYPE = MediaType.TEXT_EVENT_STREAM;
	public static final List<MediaType> SUPPORTED_MEDIA_TYPES = Collections.singletonList(MEDIA_TYPE);

	private final List<HttpMessageConverter<?>> httpMessageConverters;

	public ApiErrorResponseToTextEventStreamMessageConverter(List<HttpMessageConverter<?>> httpMessageConverters) {
		this.httpMessageConverters = httpMessageConverters;
	}

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return false;
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return ApiErrorResponse.class.equals(clazz) && MEDIA_TYPE.equals(mediaType);
	}

	@Override
	public List<MediaType> getSupportedMediaTypes() {
		return SUPPORTED_MEDIA_TYPES;
	}

	@Override
	public ApiErrorResponse read(Class<? extends ApiErrorResponse> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void write(ApiErrorResponse t, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		final var items = SseEmitter.event()
			.name("error:" + t.getHttpStatus().value())
			.data(t)
			.build();

		if (!items.isEmpty()) {
			for (ResponseBodyEmitter.DataWithMediaType item : items) {
				writeItem(item.getData(), item.getMediaType(), outputMessage);
			}

			outputMessage.getBody().flush();
		}
	}

	@SuppressWarnings("unchecked")
	private <T> void writeItem(T data, @Nullable MediaType mediaType, HttpOutputMessage outputMessage) throws IOException {
		for (HttpMessageConverter<?> converter : httpMessageConverters) {
			if (converter instanceof ApiErrorResponseToTextEventStreamMessageConverter) {
				continue;
			}

			if (!converter.canWrite(data.getClass(), mediaType)) {
				continue;
			}

			((HttpMessageConverter<T>) converter).write(
				data,
				mediaType,
				new HeadersIgnoredHttpOutputMessage(outputMessage)
			);

			return;
		}

		throw new IllegalArgumentException("No suitable converter for " + data.getClass());
	}

	/**
	 * Some HTTP Headers have already been set and are now causing issue.
	 * Use a new {@link HttpOutputMessage} instance with mutable {@link HttpHeaders}.
	 */
	public static class HeadersIgnoredHttpOutputMessage implements HttpOutputMessage {

		private final HttpHeaders headers;
		private final OutputStream body;

		public HeadersIgnoredHttpOutputMessage(HttpOutputMessage delegate) throws IOException {
			this.headers = new HttpHeaders();
			this.body = delegate.getBody();
		}

		@Override
		public HttpHeaders getHeaders() {
			return headers;
		}

		@Override
		public OutputStream getBody() throws IOException {
			return body;
		}

	}

}