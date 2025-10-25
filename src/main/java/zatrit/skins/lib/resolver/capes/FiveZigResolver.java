package zatrit.skins.lib.resolver.capes;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Base64;
import java.util.EnumMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import zatrit.skins.lib.BasePlayerTextures;
import zatrit.skins.lib.Config;
import zatrit.skins.lib.TextureType;
import zatrit.skins.lib.api.PlayerTextures;
import zatrit.skins.lib.api.Profile;
import zatrit.skins.lib.api.Resolver;
import zatrit.skins.lib.texture.BytesTexture;

@AllArgsConstructor
public final class FiveZigResolver implements Resolver {
  private static final String FIVEZIG_API = "https://textures.5zigreborn.eu/profile/";
  private final Config config;

  @Override
  public @NotNull PlayerTextures resolve(@NotNull Profile profile) throws IOException {
    val url = FIVEZIG_API + profile.getId();
    @Cleanup val reader = new InputStreamReader(URI.create(url).toURL().openStream());
    val response = this.config.getGson().fromJson(reader, Map.class);

    val textures = new EnumMap<TextureType, BytesTexture>(TextureType.class);
    val textureData = (String) response.get("d");

    if (textureData != null) {
      val decoder = Base64.getDecoder();

      val texture = new BytesTexture(textureData, decoder.decode(textureData), null);

      textures.put(TextureType.CAPE, texture);
    }

    /* Since you can't resolve a list of textures without
    fetching those textures, they may not be cached */
    return new BasePlayerTextures<>(textures, this.config.getLayers());
  }
}
