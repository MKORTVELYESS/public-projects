package org.example;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@EntityListeners(LogEntityListener.class)
public class HttpRequestLog {

  public HttpRequestLog() {}

  @Override
  public String toString() {
    return "HttpRequestLog{"
        + "id="
        + id
        + ", method='"
        + method
        + '\''
        + ", path='"
        + path
        + '\''
        + ", queryParams='"
        + queryParams
        + '\''
        + ", headers='"
        + headers
        + '\''
        + ", body='"
        + body
        + '\''
        + ", remoteIp='"
        + remoteIp
        + '\''
        + ", timestamp="
        + timestamp
        + '}';
  }

  public HttpRequestLog(
      Long id,
      String method,
      String path,
      String queryParams,
      String headers,
      String body,
      String remoteIp,
      OffsetDateTime timestamp) {
    setId(id);
    setMethod(method);
    setPath(path);
    setQueryParams(queryParams);
    setHeaders(headers);
    setBody(body);
    setRemoteIp(remoteIp);
    setTimestamp(timestamp);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    HttpRequestLog that = (HttpRequestLog) o;
    return Objects.equals(id, that.id)
        && Objects.equals(method, that.method)
        && Objects.equals(path, that.path)
        && Objects.equals(queryParams, that.queryParams)
        && Objects.equals(headers, that.headers)
        && Objects.equals(body, that.body)
        && Objects.equals(remoteIp, that.remoteIp)
        && Objects.equals(timestamp, that.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, method, path, queryParams, headers, body, remoteIp, timestamp);
  }

  public HttpRequestLog(
      String method,
      String path,
      String queryParams,
      String headers,
      String body,
      String remoteIp) {
    setMethod(method);
    setPath(path);
    setQueryParams(queryParams);
    setHeaders(headers);
    setBody(body);
    setRemoteIp(remoteIp);
    setTimestamp(timestamp);
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String method;
  private String path;
  private String queryParams;

  @Lob
  @Column(columnDefinition = "text")
  private String headers;

  @Lob
  @Column(columnDefinition = "text")
  private String body;

  private String remoteIp;
  private OffsetDateTime timestamp;

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getQueryParams() {
    return queryParams;
  }

  public void setQueryParams(String queryParams) {
    this.queryParams = queryParams;
  }

  public String getHeaders() {
    return headers;
  }

  public void setHeaders(String headers) {
    this.headers = headers;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getRemoteIp() {
    return remoteIp;
  }

  public void setRemoteIp(String remoteIp) {
    this.remoteIp = remoteIp;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }
}
