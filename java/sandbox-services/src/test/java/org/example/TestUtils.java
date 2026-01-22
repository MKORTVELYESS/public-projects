package org.example;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class TestUtils {

  @NotNull
  public static String getTestFileContent(String pathFromTestResources) throws IOException {
    return new String(
        Objects.requireNonNull(
                TestUtils.class.getClassLoader().getResourceAsStream(pathFromTestResources))
            .readAllBytes(),
        StandardCharsets.UTF_8);
  }
}
