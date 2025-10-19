package zatrit.skins.lib.resolver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.util.Base64;
import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import zatrit.skins.lib.CachedPlayerTextures;
import zatrit.skins.lib.Config;
import zatrit.skins.lib.api.PlayerTextures;
import zatrit.skins.lib.api.Profile;
import zatrit.skins.lib.api.Resolver;
import zatrit.skins.lib.data.MojangTextures;

/**
 * An implementation of the GeyserMC skin API based on <a
 * href="https://github.com/onebeastchris/customplayerheads">CustomPlayerHeads</a>.
 */
@AllArgsConstructor
public final class GeyserResolver implements Resolver {
  private static final String GEYSER_XUID_API = "https://api.geysermc.org/v2/xbox/xuid/";
  private static final String GEYSER_SKIN_API = "https://api.geysermc.org/v2/skin/";
  private final Config config;
  private final Collection<String> floodgatePrefixes;

  @Override
  public boolean requiresUuid() {
    return false;
  }

  @Override
  public @NotNull PlayerTextures resolve(@NotNull Profile profile)
      throws IOException, NoSuchElementException {
    String name = profile.getName();

    @Nullable String prefix = null;
    for (String floodgatePrefix : floodgatePrefixes) {
      if (name.startsWith(floodgatePrefix)) {
        prefix = floodgatePrefix;
        break;
      }
    }

    HttpClient client;

    if (prefix == null) {
      throw new NoSuchElementException();
    }

    name = name.substring(prefix.length());

    val xuidUrl = URI.create(GEYSER_XUID_API + name).toURL();
    val xuid =
        (Long)
            config
                .getGson()
                .fromJson(new InputStreamReader(xuidUrl.openStream()), Map.class)
                .get("xuid");

    val skinUrl = URI.create(GEYSER_SKIN_API + xuid).toURL();
    /* value contains literally the same data as properties[0] in the
    Mojang API response, so it can be decoded in the same way */
    val response =
        (String)
            config
                .getGson()
                .fromJson(new InputStreamReader(skinUrl.openStream()), Map.class)
                .get("value");

    val decoder = Base64.getDecoder();
    val textureData = decoder.decode(response);
    val reader = new InputStreamReader(new ByteArrayInputStream(textureData));

    return new CachedPlayerTextures<>(
        config.getGson().fromJson(reader, MojangTextures.class).getTextures(),
        this.config.getLayers(),
        this.config.getCache());
  }
}
