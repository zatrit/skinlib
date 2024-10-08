package zatrit.skins.lib.resolver;

import lombok.RequiredArgsConstructor;
import lombok.val;
import zatrit.skins.lib.CachedPlayerTextures;
import zatrit.skins.lib.Config;
import zatrit.skins.lib.TextureType;
import zatrit.skins.lib.api.PlayerTextures;
import zatrit.skins.lib.api.Profile;
import zatrit.skins.lib.api.Resolver;
import zatrit.skins.lib.texture.URLTexture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
public abstract class CapesListResolver implements Resolver {
    protected final Config config;
    protected @Nullable Map<String, String> owners;

    protected abstract Map<String, String> fetchList() throws IOException;

    protected abstract String getUrl(String capeName);

    @Override
    public synchronized void reset() {
        this.owners = null;
    }

    protected @Nullable String getCapeName(@NotNull Profile profile) {
        return Objects.requireNonNull(this.owners).get(profile.getShortId());
    }

    @Override
    public @NotNull PlayerTextures resolve(@NotNull Profile profile)
        throws Exception {
        synchronized (this) {
            if (this.owners == null) {
                this.owners = this.fetchList();
            }
        }

        val capeName = getCapeName(profile);
        val textures = new EnumMap<TextureType, URLTexture>(TextureType.class);

        if (capeName != null) {
            textures.put(
                TextureType.CAPE,
                new URLTexture(this.getUrl(capeName), null)
            );
        }

        return new CachedPlayerTextures<>(
            textures,
            this.config.getLayers(),
            this.config.getCache()
        );
    }
}
