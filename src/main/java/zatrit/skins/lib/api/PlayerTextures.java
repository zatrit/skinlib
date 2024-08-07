package zatrit.skins.lib.api;

import zatrit.skins.lib.TextureType;
import zatrit.skins.lib.data.TypedTexture;
import org.jetbrains.annotations.Nullable;

/**
 * Player-specific textures container.
 */
public interface PlayerTextures {
    /**
     * @return true, if texture is present.
     */
    boolean hasTexture(TextureType type);

    /**
     * @return texture of specified type if
     * present (check via {@link #hasTexture}).
     */
    @Nullable TypedTexture getTexture(TextureType type);
}
