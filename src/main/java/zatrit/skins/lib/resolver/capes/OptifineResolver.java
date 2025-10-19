package zatrit.skins.lib.resolver.capes;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import zatrit.skins.lib.BasePlayerTextures;
import zatrit.skins.lib.Config;
import zatrit.skins.lib.TextureType;
import zatrit.skins.lib.api.PlayerTextures;
import zatrit.skins.lib.api.Profile;
import zatrit.skins.lib.api.Resolver;
import zatrit.skins.lib.texture.BytesTexture;
import zatrit.skins.lib.util.IOUtil;

/**
 * <a href="https://optifine.readthedocs.io/capes.html">Optifine API</a> resolver for OpenMCSkins.
 *
 * <p>Does not cache skins, because connecting to API already loads textures.
 */
@AllArgsConstructor
public final class OptifineResolver implements Resolver {
  private final Config config;
  private final String baseUrl;

  @Override
  public boolean requiresUuid() {
    return false;
  }

  @Override
  public @NotNull PlayerTextures resolve(@NotNull Profile profile)
      throws IOException, NullPointerException {
    val url = URI.create(this.baseUrl + "/capes/" + profile.getName() + ".png").toURL();
    val texture =
        new BytesTexture(url.toString(), Objects.requireNonNull(IOUtil.download(url)), null);

    /* Since you can't check for the existence/change of a
    texture without fetching that texture, it should not be cached. */
    return new BasePlayerTextures<>(
        Collections.singletonMap(TextureType.CAPE, texture), this.config.getLayers());
  }
}
