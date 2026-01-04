package org.example.config;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  static {
    // ESSENTIAL: Remove "Basic" from the disabled tunneling schemes
    System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
  }

  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();

    restTemplate.setInterceptors(List.of(new UserAgentInterceptor()));

    return restTemplate;
  }

  @Bean
  public RestTemplate revolvingIpRestTemplate() {
    String proxyHost = "ad96022999189510.zqz.na.pyproxy.io";
    int proxyPort = 16666;
    String user = "javaproxyuser2-zone-resi";
    String pass = "emb3rt3l3n";

    // 1. Set the global Authenticator for the proxy credentials
    Authenticator.setDefault(
        new Authenticator() {
          @Override
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user, pass.toCharArray());
          }
        });

    // 2. Create the Proxy object
    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));

    // 3. Configure the RequestFactory to use the proxy
    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
    requestFactory.setProxy(proxy);

    return new RestTemplate(requestFactory);
  }
}
