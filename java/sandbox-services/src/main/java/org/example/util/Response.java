package org.example.util;

import java.util.concurrent.CompletableFuture;

public class Response {
  public final CompletableFuture<String> body;

  public Response(CompletableFuture<String> body) {
    this.body = body;
  }

  public CompletableFuture<String> getBody() {
    return body;
  }
}
