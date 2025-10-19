package zatrit.skins.lib.resolver;

import static net.andreinc.aleph.AlephFormatter.str;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import zatrit.skins.lib.BasePlayerTextures;
import zatrit.skins.lib.Config;
import zatrit.skins.lib.TextureType;
import zatrit.skins.lib.api.PlayerTextures;
import zatrit.skins.lib.api.Profile;
import zatrit.skins.lib.api.Resolver;
import zatrit.skins.lib.api.Texture;
import zatrit.skins.lib.texture.BytesTexture;
import zatrit.skins.lib.util.IOUtil;

@AllArgsConstructor
public final class DirectResolver implements Resolver {
  private final Config config;
  private final String baseUrl;
  private final Collection<TextureType> types;

  @Override
  public boolean requiresUuid() {
    return this.baseUrl.contains("{id}") || this.baseUrl.contains("{shortId}");
  }

  @Override
  public @NotNull PlayerTextures resolve(@NotNull Profile profile) throws IOException {
    val textures = new EnumMap<TextureType, Texture>(TextureType.class);
    val replaces = new HashMap<String, Object>(3, 1);
    replaces.put("id", profile.getId());
    replaces.put("name", profile.getName());
    replaces.put("shortId", profile.getShortId());

    types.parallelStream().forEach(type -> textures.put(type, downloadTexture(replaces, type)));

    /* Since you can't check for the existence/change of a
    texture without fetching that texture, it should not be cached. */
    return new BasePlayerTextures<>(textures, this.config.getLayers());
  }

  @SneakyThrows
  private @Nullable Texture downloadTexture(HashMap<String, Object> replaces, TextureType type) {
    val url = new URL(str(this.baseUrl, replaces).arg("type", type).fmt());
    val content = IOUtil.download(url);
    if (content != null) {
      return new BytesTexture(url.toString(), content, null);
    }
    return null;
  }
}
