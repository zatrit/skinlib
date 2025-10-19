package zatrit.skins.lib.texture;

import com.google.common.io.ByteStreams;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.Getter;
import lombok.val;
import zatrit.skins.lib.api.Texture;
import zatrit.skins.lib.data.Metadata;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * A texture that loads its content at a given {@link URL}.
 */
@AllArgsConstructor
public class URLTexture implements Texture {
    private final String url;
    private final @Getter Metadata metadata;

    @Override
    public String getId() {
        return this.url;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new URL(this.url).openStream();
    }
}