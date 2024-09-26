package zatrit.skins.lib.api;

import org.jetbrains.annotations.Nullable;
import zatrit.skins.lib.data.Metadata;

import java.io.IOException;
import java.io.InputStream;

/**
 * An abstract texture that can be converted to a {@link Byte} array.
 */
public interface Texture extends Cache.LoadFunction {
    /**
     * Texture name used during caching.
     */
    String getId();

    @Nullable Metadata getMetadata();

    /**
     * May contain I/O operations, as usage implies execution in
     * a parallel game thread so that the game does not freeze.
     *
     * @return the {@link InputStream} containing texture data.
     */
    @Override
    InputStream getInputStream() throws IOException;
}
