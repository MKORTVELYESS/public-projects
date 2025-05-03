package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public interface HttpAware {
    default CompletableFuture
            <HttpResponse<String>> sendGetRequestTo(String url) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .build();

        CompletableFuture
                <HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
}
