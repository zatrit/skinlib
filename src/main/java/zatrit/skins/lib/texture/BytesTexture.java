package zatrit.skins.lib.texture;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.jetbrains.annotations.NotNull;
import zatrit.skins.lib.data.Metadata;

/** A texture wrapping an array of bytes. */
public class BytesTexture extends LazyTexture {
  private final byte[] bytes;

  public BytesTexture(String id, byte @NotNull [] bytes, Metadata metadata) {
    super(id, metadata);
    this.bytes = bytes;
  }

  @Override
  public InputStream getInputStream() {
    return new ByteArrayInputStream(bytes);
  }
}
