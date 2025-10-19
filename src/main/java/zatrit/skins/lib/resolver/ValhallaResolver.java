package zatrit.skins.lib.resolver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import zatrit.skins.lib.CachedPlayerTextures;
import zatrit.skins.lib.Config;
import zatrit.skins.lib.api.PlayerTextures;
import zatrit.skins.lib.api.Profile;
import zatrit.skins.lib.api.Resolver;
import zatrit.skins.lib.data.MojangTextures;

/**
 * Valhalla Skin Server Resolver. Read more <a
 * href="https://skins.minelittlepony-mod.com/docs">here</a>.
 */
@AllArgsConstructor
public final class ValhallaResolver implements Resolver {
  private final Config config;
  private final String baseUrl;

  @Override
  public @NotNull PlayerTextures resolve(@NotNull Profile profile) throws IOException {
    val url = this.baseUrl + profile.getId();
    @Cleanup val stream = new URL(url).openStream();

    return new CachedPlayerTextures<>(
        this.config
            .getGson()
            .fromJson(new InputStreamReader(stream), MojangTextures.class)
            .getTextures(),
        this.config.getLayers(),
        this.config.getCache());
  }
}
