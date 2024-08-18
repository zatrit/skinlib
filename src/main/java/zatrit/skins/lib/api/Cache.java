package zatrit.skins.lib.api;

import java.io.IOException;

/**
 * An abstract cache.
 */
public interface Cache {
    /**
     * Loads bytes from cache if present, else loads using passed function.
     */
    byte[] getOrLoad(String id, LoadFunction load);

    interface LoadFunction {
        byte[] load() throws IOException;
    }
}
