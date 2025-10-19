package zatrit.skins.lib;

import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import zatrit.skins.lib.api.Cache;
import zatrit.skins.lib.api.Layer;
import zatrit.skins.lib.api.PlayerTextures;
import zatrit.skins.lib.api.Texture;
import zatrit.skins.lib.data.TypedTexture;
import zatrit.skins.lib.texture.LazyTexture;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

/**
 * Implementation of {@link PlayerTextures} with caching support with {@link Cache}.
 *
 * @param <T> texture type.
 * @see PlayerTextures
 */
public class CachedPlayerTextures<T extends Texture>
    extends BasePlayerTextures<T> {
    private final @Nullable Cache cache;

    public CachedPlayerTextures(
        @NotNull Map<TextureType, T> map,
        @NotNull Collection<Layer<TypedTexture>> layers,
        @Nullable Cache cache) {
        super(map, layers);
        this.cache = cache;
    }

    @Override
    protected Texture wrapTexture(@NotNull T sourceTexture) {
        val texture = super.wrapTexture(sourceTexture);

        // If there is no cache, it returns the texture in its original form.
        if (this.cache == null) {
            return texture;
        }

        return new LazyTexture(texture.getId(), texture.getMetadata()) {
            @Override
            public InputStream getInputStream() {
                return cache.getCachedInputStream(texture.getId(), texture);
            }
        };
    }
}
