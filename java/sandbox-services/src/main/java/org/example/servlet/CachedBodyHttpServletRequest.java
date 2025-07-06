package org.example.servlet;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {
  private byte[] cachedBody;

  public static CachedBodyHttpServletRequest fromHttpServletRequest(HttpServletRequest request)
      throws IOException {
    return new CachedBodyHttpServletRequest(request, request.getInputStream().readAllBytes());
  }

  private CachedBodyHttpServletRequest(HttpServletRequest request, byte[] body) {
    super(request);
    cachedBody = body;
  }

  @Override
  public ServletInputStream getInputStream() {
    return new CachedBodyServletInputStream(cachedBody);
  }

  @Override
  public BufferedReader getReader() {
    return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
  }

  private static class CachedBodyServletInputStream extends ServletInputStream {
    private final ByteArrayInputStream buffer;

    public CachedBodyServletInputStream(byte[] body) {
      buffer = new ByteArrayInputStream(body);
    }

    @Override
    public int read() {
      return buffer.read();
    }

    @Override
    public boolean isFinished() {
      return buffer.available() == 0;
    }

    @Override
    public boolean isReady() {
      return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {}
  }
}
