package controllers;

import com.google.inject.Inject;
import play.mvc.Result;
import play.libs.Json;
import play.mvc.Http;
import repositories.LogRepository;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class IndexController extends BaseController
{
	private final LogRepository logRepository;

	@Inject
	public IndexController
	(
		LogRepository logRepository
	)
	{
		this.logRepository = logRepository;
	}

	private Map<String, List<String>> formatHeadersForRequest(Map<String, List<String>> requestHeaders) {
		Map<String, List<String>> headers = new HashMap<>();
		Set<String> keys = requestHeaders.keySet();
		for(String key: keys)
		{
			List<String> values = requestHeaders.get(key);

			headers.put(key, values);
		}
		return headers;
	}

	private Map formatResponse(String method, Http.Request request, Map<String, List<String>> headers) {
		Object requestPayload = "";
		if(request.body().asJson() != null)
		{
			requestPayload = request.body().asJson();
		}

		return Map.of(
			"method", method,
			"payload", requestPayload,
			"headers", Json.toJson(headers)
		);
	}

	public Result index()
	{
		return ok("INDEX");
	}

	public CompletionStage<Result> get(Http.Request request) throws UnsupportedEncodingException {
		Map<String, List<String>> headers = formatHeadersForRequest(request.getHeaders().asMap());
		this.logRepository.saveRequest(Http.HttpVerbs.GET, request, headers);

		return CompletableFuture.supplyAsync(() -> ok(Json.toJson(formatResponse(Http.HttpVerbs.GET, request, headers))));
	}

	public CompletionStage<Result> post(Http.Request request) throws UnsupportedEncodingException {
		Map<String, List<String>> headers = formatHeadersForRequest(request.getHeaders().asMap());
		this.logRepository.saveRequest(Http.HttpVerbs.POST, request, headers);

		return CompletableFuture.supplyAsync(() -> ok(Json.toJson(formatResponse(Http.HttpVerbs.POST, request, headers))));
	}

    public CompletionStage<Result> clear()
	{
		return CompletableFuture.supplyAsync(() -> {
			this.logRepository.clear();

			return ok("success");
		});
	}
}