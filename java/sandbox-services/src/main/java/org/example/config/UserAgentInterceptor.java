package org.example.config;

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class UserAgentInterceptor implements ClientHttpRequestInterceptor {

  private static final String USER_AGENT =
      "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
          + "AppleWebKit/537.36 (KHTML, like Gecko) "
          + "Chrome/120.0.0.0 Safari/537.36";

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

    HttpHeaders headers = request.getHeaders();
    headers.set(HttpHeaders.USER_AGENT, USER_AGENT);

    return execution.execute(request, body);
  }
}
