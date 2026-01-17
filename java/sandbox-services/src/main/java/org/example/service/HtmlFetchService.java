package org.example.service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.Semaphore;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.example.util.ThrottleUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class HtmlFetchService {
    @Value("${network.use-okhttp:false}")
    private boolean useOkHttp;

    @Value("${network.count-retry-on-503:5}")
    private int countRetryOn503;

    @Value("${network.initial-backoff-millis:300000}")
    private int initialBackoffMillis;

    private final Semaphore rateLimiter = new Semaphore(5);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OkHttpClient okHttpClient;
    private static final Logger logger = LoggerFactory.getLogger(HtmlFetchService.class);

    public HtmlFetchService() {
    }

    public String fetchHtml(String url) {
        for (int attempt = 1; attempt <= countRetryOn503; attempt++) {
            try {
                rateLimiter.acquire();
                if (useOkHttp) {
                    return getHtmlAsStringWithOkHttp(url);
                } else {
                    return getHtmlAsStringWithRestTemplate(url);
                }
            } catch (HttpServerErrorException.ServiceUnavailable ex) {
                backoff(url, initialBackoffMillis, attempt, countRetryOn503);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                rateLimiter.release();
            }
        }
        throw new IllegalStateException("Cannot fetch html from " + url);
    }

    @Nullable
    private String getHtmlAsStringWithRestTemplate(String url) {
        return restTemplate.getForObject(url, String.class);
    }

    @NotNull
    private String getHtmlAsStringWithOkHttp(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        okhttp3.Response response = okHttpClient.newCall(request).execute();
        ResponseBody body = response.body();
        return (body != null) ? body.string() : "";
    }

    private void backoff(String url, long initialBackoffMillis, int attempt, int maxRetries) {
        long backoff = initialBackoffMillis * (1L << (attempt - 1));

        logger.warn(
                "Backing off from {}. Retry {}/{} in {} seconds", url, attempt, maxRetries, backoff / 1000);

        ThrottleUtil.sleep(backoff);
    }
}
