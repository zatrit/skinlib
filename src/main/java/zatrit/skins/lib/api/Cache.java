package zatrit.skins.lib.api;

import java.io.IOException;
import java.io.InputStream;

/** An abstract cache. */
@FunctionalInterface
public interface Cache {
  /**
   * Creates an {@link InputStream} with cached data if it is present, otherwise loads it with the
   * passed function.
   */
  InputStream getCachedInputStream(String id, LoadFunction load) throws IOException;

  @FunctionalInterface
  interface LoadFunction {
    InputStream getInputStream() throws IOException;
  }
}
