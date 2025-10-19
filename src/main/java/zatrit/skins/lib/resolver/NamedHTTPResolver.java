package zatrit.skins.lib.resolver;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.EnumMap;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import zatrit.skins.lib.CachedPlayerTextures;
import zatrit.skins.lib.Config;
import zatrit.skins.lib.TextureType;
import zatrit.skins.lib.api.PlayerTextures;
import zatrit.skins.lib.api.Profile;
import zatrit.skins.lib.api.Resolver;
import zatrit.skins.lib.texture.URLTexture;

/**
 * <a href="https://docs.ely.by/en/skins-system.html">ely.by API</a> implementation for OpenMCSKins.
 * Works for some other APIs.
 */
@AllArgsConstructor
public final class NamedHTTPResolver implements Resolver {
  private final Config config;
  private final String baseUrl;

  /** Doesn't require UUID, because resolves by name. {@inheritDoc} */
  @Override
  public boolean requiresUuid() {
    return false;
  }

  @Override
  public @NotNull PlayerTextures resolve(@NotNull Profile profile) throws IOException {
    val url = URI.create(this.baseUrl + profile.getName()).toURL();
    @Cleanup val reader = new InputStreamReader(url.openStream());

    // Type for EnumMap<TextureType, URLTexture>
    val type =
        TypeToken.getParameterized(EnumMap.class, TextureType.class, URLTexture.class).getType();

    return new CachedPlayerTextures<>(
        this.config.getGson().fromJson(reader, type),
        this.config.getLayers(),
        this.config.getCache());
  }
}
