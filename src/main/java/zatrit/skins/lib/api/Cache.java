package zatrit.skins.lib.api;

import java.io.IOException;

/**
 * An abstract cache.
 */
@FunctionalInterface
public interface Cache {
    /**
     * Loads bytes from cache if present, else loads using passed function.
     */
    byte[] getOrLoad(String id, LoadFunction load);

    @FunctionalInterface
    interface LoadFunction {
        byte[] load() throws IOException;
    }
}
