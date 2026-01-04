package org.example.config;

import java.net.InetSocketAddress;
import java.net.Proxy;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OkHttpClientConfig {

  @Value("${proxy.host}")
  private String proxyHost;

  @Value("${proxy.port}")
  private int proxyPort;

  @Value("${proxy.user}")
  private String user;

  @Value("${proxy.password}")
  private String pass;

  @Bean
  public OkHttpClient okHttpClient() {
    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
    return new OkHttpClient()
        .newBuilder()
        .proxy(proxy)
        .proxyAuthenticator(
            (route, response) -> {
              String credential = Credentials.basic(user, pass);
              return response
                  .request()
                  .newBuilder()
                  .header("Proxy-Authorization", credential)
                  .build();
            })
        .build();
  }
}
